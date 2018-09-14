package com.qyy.jyshop.member.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.dao.MemberPointDao;
import com.qyy.jyshop.dao.MemberPointExtDao;
import com.qyy.jyshop.dao.MemberWithdrawInfoDao;
import com.qyy.jyshop.dao.PointDrawLogDao;
import com.qyy.jyshop.dao.PointParamDao;
import com.qyy.jyshop.dao.ProfitParamDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.member.service.RedPointService;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.MemberPoint;
import com.qyy.jyshop.model.MemberPointExt;
import com.qyy.jyshop.model.MemberWithdrawInfo;
import com.qyy.jyshop.model.PointDrawLog;
import com.qyy.jyshop.model.ProfitParam;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.TimestampUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class RedPointServiceImpl extends AbstratService<MemberPoint> implements RedPointService {

	@Autowired
	private MemberDao memberDao;
	@Autowired
	private MemberPointDao memberPointDao;
	@Autowired
	private MemberPointExtDao memberPointExtDao;
	@Autowired
	private MemberWithdrawInfoDao memberWithdrawInfoDao;
	@Autowired
	private PointParamDao pointParamDao;
	@Autowired
	private ProfitParamDao profitParamDao;
	@Autowired
	private PointDrawLogDao pointDrawLogDao;

	/**
	 * 红积分提现明细
	 */
	@Override
	public PageAjax<Map<String, Object>> getMemberRedPointDrawList(String token,int status, Integer pageNo, Integer pageSize) {
		Member member = this.getMember(token);
		if (member == null) {
			throw new AppErrorException("请先登录！");
		}
		ParamData params = this.getParamData(pageNo, pageSize);
		params.put("memberId", member.getMemberId());
		params.put("status", status);
		return this.pageQuery("MemberPointExtDao.getMemberRedPointDrawList", params);
	}

	/**
	 * 红积分获取已确认明细
	 */
	@Override
	public List<Map<String, Object>> getMemberRedPointGainList(String token) {
		Member member = this.getMember(token);
		if (member == null) {
			throw new AppErrorException("请先登录！");
		}
		return memberPointDao.getMemberRedPointGainList(member.getMemberId());
	}

	/**
	 * 红积分获取待确认明细
	 */
	@Override
	public List<Map<String, Object>> getMemberRedPointGainUnconfirmedList(String token) {
		Member member = this.getMember(token);
		if (member == null) {
			throw new AppErrorException("请先登录！");
		}
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		ProfitParam profitParam=this.profitParamDao.selectAll().get(0);
		BigDecimal totalScale=profitParam.getTax().add(profitParam.getManagement());
		totalScale=totalScale.add(profitParam.getOperation());
		totalScale=totalScale.add(profitParam.getGain());
		totalScale=totalScale.add(profitParam.getPresenter());
        
        if(totalScale.compareTo(new BigDecimal(100))<0){
        	List<Map<String,Object>> redPointUnconfirmedList= this.memberPointDao.selectRedPointUnconfirmedList(member.getMemberId());
        	
        	Boolean firstProfitFlag;
        	Map<String,Object> tmpMap = new HashMap<String, Object>();
        	for(Map map : redPointUnconfirmedList){
        		Map<String,Object> returnMap = new HashMap<String, Object>();
        		firstProfitFlag = false;
        		Integer orderType = Integer.parseInt((String)map.get("orderType"));
        		String type = (String) map.get("type");
        		BigDecimal profit = (BigDecimal) map.get("profit");
        		if(profit.compareTo(BigDecimal.ZERO)<=0){
    				continue;
    			}
        		if(type != null && type.equals("firstProfit")){
//        			BigDecimal firstProfit = (BigDecimal) map.get("profit");
//        			
//        			if(firstProfit.compareTo(BigDecimal.ZERO)<=0){
//        				continue;
//        			}
        			firstProfitFlag = true;
        			//select a.type,a.point,a.create_time as createTime,b.ORDER_AMOUNT as orderAmount,a.order_sn as orderSn,a.point_status as pointStaus,b.PAYMENT_TIME as paymentTime
        			//计算自购首次分润订单对应的分润
            		BigDecimal profitAmount=profit.subtract(profit.multiply(totalScale).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
            		BigDecimal divide = profitAmount.multiply(profitParam.getProfitMemberFirst()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
            		returnMap.put("point", divide);
        		}else if(type != null && type.equals("secProfit")){
        			/*BigDecimal secProfit = (BigDecimal) map.get("profit");
        			if(secProfit.compareTo(BigDecimal.ZERO)<=0){
        				continue;
        			}*/
        			//计算自购分润
        			BigDecimal profitAmount=profit.subtract(profit.multiply(totalScale).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
        			BigDecimal divide = profitAmount.multiply(profitParam.getProfitMemberSec()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
        			returnMap.put("point", divide);
        		}else if(type != null && type.equals("subProfit")){
        			/*BigDecimal subProfit = (BigDecimal) map.get("profit");
        			if(subProfit.compareTo(BigDecimal.ZERO)<=0){
        				continue;
        			}*/
        			//计算下级待确认分润
        			BigDecimal profitAmount=profit.subtract(profit.multiply(totalScale).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
                	BigDecimal divide = profitAmount.multiply(profitParam.getProfitShopownerSub()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
                	returnMap.put("point", divide);
        		}
        		returnMap.put("type", 0);
        		returnMap.put("createTime", map.get("paymentTime"));
        		returnMap.put("orderAmount", map.get("orderAmount"));
        		returnMap.put("orderSn", map.get("orderSn"));
        		returnMap.put("pointStatus", 0);
        		returnMap.put("paymentTime", map.get("paymentTime"));
        		if(!firstProfitFlag){
        			returnList.add(returnMap);
        		}else{
        			if(tmpMap == null || orderType == 1)
        				tmpMap = returnMap;//若同时有普通订单和礼包订单，只取礼包订单作首购分润订单
        		}
        	}
        	if(MapUtils.isNotEmpty(tmpMap)){
        		returnList.add(tmpMap);
        	}
        }
		return returnList;
	}
	/**
	 * 红积分提现
	 */
	@Override
	@Transactional
	public Map<String, Object> redPointDraw(String token, Integer drawPoint,Integer style, MemberPointExt memberPointExt) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Member member = this.getMember(token);
		if (member == null) {
			throw new AppErrorException("请先登录！");
		}
		BigDecimal withdrawDepositMin = pointParamDao.selectAll().get(0).getWithdrawDepositMin();
		if(drawPoint - withdrawDepositMin.intValue() < 0) {
			returnMap.put("code", 1);
			returnMap.put("msg", "提现低于提现最低门槛，提现最低为:"+withdrawDepositMin+"积分");
			return returnMap;
		}
		//取银行卡/支付宝/微信信息
		Example example = new Example(MemberWithdrawInfo.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("memberId", member.getMemberId());
		createCriteria.andEqualTo("drawType", style);
		List<MemberWithdrawInfo> memberWithdrawInfoList = memberWithdrawInfoDao.selectByExample(example);
		if(CollectionUtils.isEmpty(memberWithdrawInfoList)) {
			returnMap.put("code", 2);
			returnMap.put("msg", "无积分提取方式信息");
			return returnMap;
		}
		MemberWithdrawInfo memberWithdrawInfo = memberWithdrawInfoList.get(0);
		Member memberNow = memberDao.findMemerById(member.getMemberId());
		BigDecimal redPointOld = memberNow.getRedPoint();
		synchronized (memberNow) {
			//积分小于等于0 或者 当前积分小于提现积分，返回错误“积分不足”
		    
		    System.out.println("memberNow.getRedPoint():"+memberNow.getRedPoint());
		    System.out.println("drawPoint:"+drawPoint);
		    System.out.println(memberNow.getRedPoint().compareTo(BigDecimal.ZERO));
		    System.out.println(memberNow.getRedPoint().compareTo(new BigDecimal(drawPoint)));
			if(memberNow.getRedPoint().compareTo(BigDecimal.ZERO)<=0 || memberNow.getRedPoint().compareTo(new BigDecimal(drawPoint)) <=0){
				System.out.println("积分不足...");
			    returnMap.put("code", 3);
				returnMap.put("msg", "积分不足");
				return returnMap;
			}
			memberNow.setRedPoint(memberNow.getRedPoint().subtract(new BigDecimal(drawPoint)));
			memberDao.updateByPrimaryKeySelective(memberNow);// 修改用户红积分

			redisDao.saveObject(token, memberNow, 3600 * 24 * 30);// 重新设置缓存里的用户信息

			// 生成一条提现记录
			MemberPoint memberPoint = new MemberPoint();
			memberPoint.setOrderId(0L);
			memberPoint.setOrderSn("");
			memberPoint.setType(2);// type=2 提现
			memberPoint.setPoint(new BigDecimal(drawPoint));
			memberPoint.setPointType(2); // pointtype=2 红积分
			memberPoint.setGetType(0);
			memberPoint.setPointStatus(0);// pointStatus=0 代付款
			memberPoint.setCreateTime(TimestampUtil.getNowTime());
			memberPoint.setMemberId(member.getMemberId());
			this.insert(memberPoint);

			// 生成一条提现申请
			MemberPointExt memberPointExtNew = new MemberPointExt();
			BeanUtils.copyProperties(memberPointExt, memberPointExtNew);
			memberPointExtNew.setMpId(memberPoint.getId());
			memberPointExtNew.setMemberId(member.getMemberId());
			memberPointExtNew.setBillNo(String.valueOf(System.currentTimeMillis()));
			memberPointExtNew.setStatus(0);
			memberPointExtNew.setStyle(style);	//提现方式
			memberPointExtNew.setAmount(new BigDecimal(drawPoint));	//提现金额
			memberPointExtNew.setCreateTime(TimestampUtil.getNowTime());
			memberPointExtNew.setAccountName(memberWithdrawInfo.getAccountName());
			memberPointExtNew.setAccountNo(memberWithdrawInfo.getAccountNo());
			memberPointExtNew.setOpenBank(memberWithdrawInfo.getOpenBank());
			memberPointExtNew.setOpenBankCity(memberWithdrawInfo.getOpenBankCity());
			memberPointExtNew.setBranchName(memberWithdrawInfo.getBranchName());
			memberPointExtDao.insert(memberPointExtNew);
			
			//插入日志
			PointDrawLog pointDrawLog = new PointDrawLog();
			pointDrawLog.setBillNo(memberPointExtNew.getBillNo());
			pointDrawLog.setCreateTime(TimestampUtil.getNowTime());
			pointDrawLog.setOpId(member.getMemberId());
			pointDrawLog.setOpUser(member.getNickname());
			pointDrawLog.setPointType(2); // pointType=2 红积分
			pointDrawLog.setPoint(new BigDecimal(drawPoint));
			pointDrawLog.setPointBefore(redPointOld);
	        pointDrawLog.setPointAfter(redPointOld.subtract(memberPoint.getPoint()));
			pointDrawLog.setMessage("用户【"+member.getMemberId()+":"+member.getNickname()+"】发起提现申请");
            pointDrawLogDao.insert(pointDrawLog );
		}
		return null;
	}

	@Override
	public Map<String, Object> getMemberRedPointInfo(String token) {
		Map<String,Object> resultMap = new HashedMap();
		Member member = this.getMember(token);
		if (member == null) {
			throw new AppErrorException("请先登录！");
		}
		member = memberDao.selectByPrimaryKey(member.getMemberId());
		
		BigDecimal redPointToBeConfirmed = new BigDecimal(0);
		
		ProfitParam profitParam=this.profitParamDao.selectAll().get(0);
		BigDecimal totalScale=profitParam.getTax().add(profitParam.getManagement());
		totalScale=totalScale.add(profitParam.getOperation());
		totalScale=totalScale.add(profitParam.getGain());
		totalScale=totalScale.add(profitParam.getPresenter());
        
        if(totalScale.compareTo(new BigDecimal(100))<0){
        	List<Map<String,Object>> redPointUnconfirmedList= this.memberPointDao.selectRedPointUnconfirmedList(member.getMemberId());
        	BigDecimal tmpPoint = BigDecimal.ZERO;
        	for(Map map : redPointUnconfirmedList){
        		//Map<String,Object> returnMap = new HashMap<String, Object>();
        		String type = (String) map.get("type");
        		Integer orderType = Integer.parseInt((String)map.get("orderType"));
        		BigDecimal profit = (BigDecimal) map.get("profit");
        		if(profit.compareTo(BigDecimal.ZERO)<=0 && orderType != 1){
    				continue;
    			}
        		Boolean flag = false;
        		if(type != null && type.equals("firstProfit")){
        			//BigDecimal firstProfit = (BigDecimal) map.get("profit");
        			/*if(firstProfit.compareTo(BigDecimal.ZERO)<=0){
        				continue;
        			}*/
        			//计算自购首次分润订单对应的分润
            		BigDecimal profitAmount=profit.subtract(profit.multiply(totalScale).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
            		BigDecimal divide = profitAmount.multiply(profitParam.getProfitMemberFirst()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
            		//redPointToBeConfirmed = redPointToBeConfirmed.add(divide);
            		//tmpPoint = tmpPoint.add(divide);
            		if(flag){
            		    continue;
            		}
            		if(orderType == 1 ){
            			tmpPoint = BigDecimal.ZERO.add(divide);
            			System.out.println("订单礼包首次分润");
            			System.out.println(divide);
            			flag = true;
            		}
            		if(orderType == 0 && divide.compareTo(BigDecimal.ZERO) > 0){
            		    System.out.println("普通礼包首次分润");
            		    tmpPoint = BigDecimal.ZERO.add(divide);
            		    System.out.println(divide);
            		}
        		}else if(type != null && type.equals("secProfit")){
        			//BigDecimal secProfit = (BigDecimal) map.get("profit");
        			//计算自购分润
        			BigDecimal profitAmount=profit.subtract(profit.multiply(totalScale).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
        			BigDecimal divide = profitAmount.multiply(profitParam.getProfitMemberSec()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
        			redPointToBeConfirmed = redPointToBeConfirmed.add(divide);
        		}else if(type != null && type.equals("subProfit")){
        			//BigDecimal subProfit = (BigDecimal) map.get("profit");
        			//计算下级待确认分润
        			BigDecimal profitAmount=profit.subtract(profit.multiply(totalScale).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
                	BigDecimal divide = profitAmount.multiply(profitParam.getProfitShopownerSub()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
                	redPointToBeConfirmed = redPointToBeConfirmed.add(divide);
        		}
        	}
        	redPointToBeConfirmed = redPointToBeConfirmed.add(tmpPoint);
        }
		
//		List<Map<String,Object>> selectRedPointUnconfirmed = this.memberPointDao.selectRedPointUnconfirmed(member.getMemberId());
//		BigDecimal firstProfit = new BigDecimal(0);
//		BigDecimal secProfit = new BigDecimal(0);
//		BigDecimal subProfit = new BigDecimal(0);
//		for (Map<String, Object> map : selectRedPointUnconfirmed) {
//			String type = (String) map.get("type");
//			if(type.equals("firstProfit")) {
//				firstProfit = (BigDecimal)map.get("profit");
//			}else if(type.equals("secProfit")) {
//				secProfit = (BigDecimal)map.get("profit");
//			}else if(type.equals("subProfit")) {
//				subProfit = (BigDecimal)map.get("profit");
//			}
//		}
//		ProfitParam profitParam=this.profitParamDao.selectAll().get(0);
//		BigDecimal totalScale=profitParam.getTax().add(profitParam.getManagement());
//		totalScale=totalScale.add(profitParam.getOperation());
//		totalScale=totalScale.add(profitParam.getGain());
//		totalScale=totalScale.add(profitParam.getPresenter());
//        
//        if(totalScale.compareTo(new BigDecimal(100))<0){
//        	if(member.getIsProfit() == 0) {
//    			//计算自购首次分润订单对应的分润
//        		BigDecimal profitAmount=firstProfit.subtract(firstProfit.multiply(totalScale).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
//        		BigDecimal divide = profitAmount.multiply(profitParam.getProfitMemberFirst()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
//        		redPointToBeConfirmed = redPointToBeConfirmed.add(divide);
//    		}else {
//    			//计算自购分润
//    			BigDecimal profitAmount=secProfit.subtract(secProfit.multiply(totalScale).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
//    			redPointToBeConfirmed = redPointToBeConfirmed.add(profitAmount.multiply(profitParam.getProfitMemberSec()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN));
//    		}
//    		//计算下级待确认分润
//        	BigDecimal profitAmount=subProfit.subtract(subProfit.multiply(totalScale).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
//        	redPointToBeConfirmed = redPointToBeConfirmed.add(profitAmount.multiply(profitParam.getProfitShopownerSub()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN));
//        }
        
		//查询提现中和已提现积分
		Object redPointAlreadyPresente = null;
		Object redPonitWithdrawals = null;
		Object redPonitIncome = null;
		List<Map<String,Object>> memberRedInfoList = this.memberPointDao.getMemberRedPointInfo(member.getMemberId());
		for (Map<String, Object> map : memberRedInfoList) {
			int status = Integer.parseInt((String) map.get("status"));
			if(status==0 || status ==1){	
				redPonitWithdrawals = map.get("pointSum");	//提现中
			}else if(status == 2 || status == 3){
				redPointAlreadyPresente = map.get("pointSum");			//已提现
			}else if(status == 100){
				redPonitIncome = map.get("pointSum");			//已提现
			}
		}
		//查询可提现红积分
		resultMap.put("redPoint", member.getRedPoint());
		//已提现积分
		resultMap.put("redPointAlreadyPresente", redPointAlreadyPresente==null?0:redPointAlreadyPresente);
		//提现中积分
		resultMap.put("redPonitWithdrawals", redPonitWithdrawals==null?0:redPonitWithdrawals);
		//redPointToBeConfirmed
		resultMap.put("redPointToBeConfirmed", redPointToBeConfirmed);
		//累计收入积分
		resultMap.put("RedPonitIncome", redPonitIncome);
		return resultMap;
	}
        

}
