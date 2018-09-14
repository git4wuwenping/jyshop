package com.qyy.jyshop.admin.coupon.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.admin.coupon.service.CouponService;
import com.qyy.jyshop.dao.CouponDao;
import com.qyy.jyshop.dao.CouponRelDao;
import com.qyy.jyshop.dao.MemberCouponDao;
import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.Coupon;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.MemberCoupon;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.TimestampUtil;

@Service
public class CouponServiceImpl extends AbstratService<Coupon> implements CouponService {
    
    @Autowired
    private CouponDao couponDao;
    @Autowired
    private CouponRelDao couponRelDao;
    @Autowired
    private MemberCouponDao memberCouponDao;
    @Autowired
    private MemberDao memberDao;

    @Override
    public PageAjax<Map<String, Object>> pageCouponAjax(PageAjax<Map<String, Object>> page) {
        ParamData params=this.getParamData(page.getPageNo(), page.getPageSize());
        return this.pageQuery("CouponDao.selectCouponList", params);
    }

    @Override
    @Transactional
    public String editCoupon(Coupon coupon) {
        if(coupon.getUseType() == 1){//使用类型： 0按日期 1按领取日期有效天数
            coupon.setUseBeginTime(null);
            coupon.setUseEndTime(null);
        }else{
            coupon.setEffectiveDate(null);
        }
        
        //0-线上发放1-线下发放2-直接到账3-注册赠送 
        if(coupon.getGrantType() == 2 || coupon.getGrantType() == 3){
            coupon.setLimitGet(0);
            coupon.setGrantNum(0);
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cpnSn", coupon.getCpnSn());
        params.put("cpnType", coupon.getCpnType());
        this.couponRelDao.delRel(params);
        this.updateByID(coupon);
        return null;
    }

    @Override
    @Transactional
    public String addCoupon(Coupon coupon) {
        long time = new Date().getTime();
        Timestamp nowTime = new Timestamp(time);
        
        coupon.setCreateTime(nowTime);
        if(coupon.getUseType() == 1){//使用类型： 0按日期 1按领取日期有效天数
            coupon.setUseBeginTime(null);
            coupon.setUseEndTime(null);
        }else{
            coupon.setEffectiveDate(null);
        }
        
        //0-线上发放1-线下发放2-直接到账3-注册赠送 
        if(coupon.getGrantType() == 2 || coupon.getGrantType() == 3){
            coupon.setLimitGet(0);
            coupon.setGrantNum(0);
        }
        
//      Timestamp useEndTime = new Timestamp(time + 86400 * coupon.getEffectiveDate());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cpnSn", coupon.getCpnSn());
        params.put("cpnType", coupon.getCpnType());
        this.couponRelDao.delRel(params);
        this.insert(coupon);
        return null;
    }

    @Override
    @Transactional
    public String delCoupon(Long cpnId) {
        
        MemberCoupon record = new MemberCoupon();
        record.setCpnId(cpnId);
        List<MemberCoupon> select = memberCouponDao.select(record);
        if(select.size() > 0){
            return "该优惠券已经发放，不能删除！";
        }
        Map<String, Object> params = new HashMap<String, Object>();
        Coupon queryByID = this.queryByID(cpnId);
        params.put("cpnSn", queryByID.getCpnSn());
        this.deleteByID(cpnId);
        couponRelDao.delRel(params);
        return null;
    }

    @Override
    public Map<String, Object> selectCouponInfoByCpnId(Long cpnId) {
        return this.couponDao.selectCouponInfoByCpnId(cpnId);
    }

    /**
     * 
     * 优惠券发放 - 线上发放/线下发放/注册赠送
     * @author jiangbin
     * @created 2018年4月17日 上午9:24:55
     * @param cpnId 优惠券ID
     * @param grantType 发放类型 0-线上发放1-线下发放2-直接到账3-注册赠送
     * @param grantCount 发放数量
     * @return
     * @see com.qyy.jyshop.admin.coupon.service.CouponService#grantCoupon(java.lang.Long, java.lang.Integer, java.lang.Integer)
     */
    @Override
    @Transactional
    public String grantCoupon(Long cpnId, Integer grantType, Integer grantCount) {
        if(grantType == 0 || grantType == 1 || grantType == 3 ){//发放类型：0-线上发放1-线下发放2-直接到账3-注册赠送
            synchronized (cpnId) {
                this.couponDao.updateGrantNumByCpnId(cpnId,grantCount); 
            }
        }
        return null;
    }

    /**
     * 
     * 直接到账-优惠券发放
     * @author jiangbin
     * @created 2018年4月17日 上午9:23:04
     * @param cpnId 优惠券ID
     * @param memberIds 会员ID数组
     * @param count 每个会员发放张数
     * @param gType 发放对象 0-全站会员 1-指定会员
     * @return
     */
    @Override
    @Transactional
    public String grantCoupon(Long cpnId, String memberIds, Integer count, Integer gType) {
        List<Long> memberIdList = new ArrayList<Long>();
        List<Member> memberList = new ArrayList<Member>();
        Coupon coupon = this.queryByID(cpnId);
        if(gType == 0){
            memberList = memberDao.selectAll();
            if(memberList.size() == 0){
                return "没有选择会员";
            }
            
            List<MemberCoupon> memberCouponList = new ArrayList<MemberCoupon>();
            synchronized (cpnId) {
                for(Member member:memberList){
                    for(int i=0;i<count;i++){
                        MemberCoupon memberCoupon = new MemberCoupon();
                        BeanUtils.copyProperties(coupon, memberCoupon);
                        memberCoupon.setSn(UUID.randomUUID().toString());
                        memberCoupon.setGetTime(null);
                        memberCoupon.setIsGet(0);
                        memberCoupon.setIsUse(0);
                        memberCoupon.setMemberId(member.getMemberId());
                        memberCoupon.setUseTime(null);
                        memberCoupon.setCreateTime(TimestampUtil.getNowTime());
                        memberCouponList.add(memberCoupon);
                    }
                }
                this.memberCouponDao.batchInsert(memberCouponList);
                this.couponDao.updateGrantNumByCpnId(cpnId,memberList.size()*count); 
            }
            
        }else{
            if(StringUtils.isBlank(memberIds)){
                return "没有选择会员";
            }
            Arrays.asList(memberIds.split(",")).forEach(e->{
                memberIdList.add(Long.parseLong(e));
            });
            List<MemberCoupon> memberCouponList = new ArrayList<MemberCoupon>();
            synchronized (cpnId) {
                for(Long memberId:memberIdList){
                    for(int i=0;i<count;i++){
                        MemberCoupon memberCoupon = new MemberCoupon();
                        BeanUtils.copyProperties(coupon, memberCoupon);
                        memberCoupon.setSn(UUID.randomUUID().toString());
                        memberCoupon.setGetTime(null);
                        memberCoupon.setIsGet(0);
                        memberCoupon.setIsUse(0);
                        memberCoupon.setMemberId(memberId);
                        memberCoupon.setUseTime(null);
                        memberCoupon.setCreateTime(TimestampUtil.getNowTime());
                        memberCouponList.add(memberCoupon);
                    }
                }
                this.memberCouponDao.batchInsert(memberCouponList);
                this.couponDao.updateGrantNumByCpnId(cpnId,memberIdList.size()*count); 
            }
        }
        return null;
    }

    @Override
    public Coupon selectOne(Long cpnId) {
        return this.queryByID(cpnId);
    }

}
