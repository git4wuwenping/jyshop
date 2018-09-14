package com.qyy.jyshop.admin.point.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.admin.point.service.MemberPointExtService;
import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.dao.MemberPointDao;
import com.qyy.jyshop.dao.MemberPointExtDao;
import com.qyy.jyshop.dao.PointDrawLogDao;
import com.qyy.jyshop.dao.SpecDao;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.MemberPoint;
import com.qyy.jyshop.model.MemberPointExt;
import com.qyy.jyshop.model.PointDrawLog;
import com.qyy.jyshop.model.Spec;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.redis.RedisDao;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.TimestampUtil;
import com.qyy.jyshop.vo.MemberPointExtVo;

@Service
public class MemberPointExtServiceImpl extends AbstratService<MemberPointExt> implements MemberPointExtService {
	
	@Autowired
	private MemberPointDao memberPointDao;
	@Autowired
	private MemberPointExtDao memberPointExtDao;
	@Autowired
	private SpecDao specDao;
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private RedisDao RedisDao;
	@Autowired
	private PointDrawLogDao pointDrawLogDao;

	@Override
	public PageAjax<Map<String, Object>> pageRedPointDrawApplyAjax(PageAjax<Map<String, Object>> page,
			MemberPointExtVo memberPointExtVo) {
		ParamData params = this.getParamData(page.getPageNo(), page.getPageSize());
		if (memberPointExtVo.getConditionType() == 1) {
			memberPointExtVo.setBillNo(memberPointExtVo.getConditionVal());
		} else if (memberPointExtVo.getConditionType() == 2) {
			memberPointExtVo.setMemberNo(memberPointExtVo.getConditionVal());
		} else {
			memberPointExtVo.setPhone(memberPointExtVo.getConditionVal());
		}

		params.put("memberPointExtVo", memberPointExtVo);
		return this.pageQuery("MemberPointExtDao.selectRedPointDrawApplyList", params);
	}

	@Override
	public MemberPointExt getMemberPointExtById(Long id) {
		return this.queryByID(id);
	}

	/**
	 * 打款
	 */
	@Override
	@Transactional
	public String remitRedPointDrawApply(Long id) {
		MemberPointExt memberPointExt = this.queryByID(id);
		if(memberPointExt == null || memberPointExt.getStatus() != 1) { //status = 1 待打款
			return "状态不正确";
		}
		MemberPoint memberPoint = memberPointDao.selectByPrimaryKey(memberPointExt.getMpId());
		if(memberPoint == null || memberPoint.getPointStatus() !=1) {
			return "状态不正确";
		}
		memberPointExt.setStatus(2);
		memberPointExt.setRemitTime(TimestampUtil.getNowTime());
		this.update(memberPointExt);
		
		memberPoint.setPointStatus(2);
		memberPointDao.updateByPrimaryKey(memberPoint);
		
		Member member = memberDao.selectByPrimaryKey(memberPointExt);
		
		//记录日志
        PointDrawLog pointDrawLog = new PointDrawLog();
        pointDrawLog.setBillNo(memberPointExt.getBillNo());
        pointDrawLog.setCreateTime(TimestampUtil.getNowTime());
        pointDrawLog.setOpId(this.getLoginUser().getId().longValue());
        pointDrawLog.setOpUser(this.getLoginUser().getUsername());
        pointDrawLog.setPointType(2); // pointType=2 红积分
        pointDrawLog.setPoint(memberPoint.getPoint());
        pointDrawLog.setPointBefore(member.getRedPoint());
        pointDrawLog.setPointAfter(member.getRedPoint());
        pointDrawLog.setMessage("【"+this.getLoginUser().getUsername()+"】向用户【"+memberPointExt.getMemberId()+"】的提现申请进行了打款");
        pointDrawLogDao.insert(pointDrawLog);
        
		return null;
	}

	/**
	 * 审核
	 */
	@Override
	@Transactional
	public String auditRedPointDrawApply(MemberPointExt memberPointExt) {

		MemberPointExt memberPointExtUpdate = this.queryByID(memberPointExt.getId());
		if(memberPointExtUpdate == null || memberPointExtUpdate.getStatus() != 0) { //status = 1 待打款
			return "状态不正确";
		}
		MemberPoint memberPoint = memberPointDao.selectByPrimaryKey(memberPointExtUpdate.getMpId());
		if(memberPoint == null || memberPoint.getPointStatus() !=0) {
			return "状态不正确";
		}
		memberPointExtUpdate.setStatus(memberPointExt.getStatus());
		memberPointExtUpdate.setMark(memberPointExt.getMark());
		memberPointExtUpdate.setAuditTime(TimestampUtil.getNowTime());
		this.update(memberPointExtUpdate);
		
		Member memberNow = memberDao.findMemerById(memberPointExtUpdate.getMemberId());
		BigDecimal redPointBefore = memberNow.getRedPoint();
		this.memberPointExtDao.updateByPrimaryKeySelective(memberPointExtUpdate);
		
		//审核不通过,已拒绝，返还积分给用户
		if(memberPointExt.getStatus()==4) {
			BigDecimal drawPoint = memberPointDao.selectByPrimaryKey(memberPointExtUpdate.getMpId()).getPoint();
			memberNow.setRedPoint(memberNow.getRedPoint().add(drawPoint));//修改用户红积分
			memberDao.updateByPrimaryKeySelective(memberNow);
			//Redis缓存处理
			Object token = this.redisDao.getObject(memberNow.getUname());
			if(token != null) {
				this.redisDao.saveObject((String)token, memberNow,3600*24*30);
			}
		}
		
		memberPoint.setPointStatus(memberPointExt.getStatus()); //pointStatus 积分消费状态：0-待确认，1-已确认 | 积分提现状态 0-待审核 1-待打款 2-已打款 3-已到账 4-已拒绝
		memberPointDao.updateByPrimaryKey(memberPoint);
		
		//记录日志
		PointDrawLog pointDrawLog = new PointDrawLog();
        pointDrawLog.setBillNo(memberPointExt.getBillNo());
        pointDrawLog.setCreateTime(TimestampUtil.getNowTime());
        pointDrawLog.setOpId(this.getLoginUser().getId().longValue());
        pointDrawLog.setOpUser(this.getLoginUser().getUsername());
        pointDrawLog.setPointType(2); // pointType=2 红积分
        pointDrawLog.setPoint(memberPoint.getPoint());
        
        if(memberPointExt.getStatus()==1){
            pointDrawLog.setMessage("【"+this.getLoginUser().getUsername()+"】审核通过用户【ID:"+memberPointExt.getMemberId()+"】的提现申请");
            pointDrawLog.setPointBefore(redPointBefore);
            pointDrawLog.setPointAfter(redPointBefore);
        }else if(memberPointExt.getStatus()==4){
            pointDrawLog.setMessage("【"+this.getLoginUser().getUsername()+"】审核拒绝用户【ID:"+memberPointExt.getMemberId()+"】的提现申请");
            pointDrawLog.setPointBefore(redPointBefore);
            pointDrawLog.setPointAfter(memberNow.getRedPoint());
        }
        pointDrawLogDao.insert(pointDrawLog);
		
		return null;
	}

}
