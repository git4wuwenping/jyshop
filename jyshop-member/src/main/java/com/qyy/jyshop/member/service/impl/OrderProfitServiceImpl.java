package com.qyy.jyshop.member.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.dao.MemberPointDao;
import com.qyy.jyshop.dao.OrderItemsDao;
import com.qyy.jyshop.dao.PointLogDao;
import com.qyy.jyshop.dao.ProfitParamDao;
import com.qyy.jyshop.member.service.OrderProfitService;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.MemberPoint;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.model.OrderItems;
import com.qyy.jyshop.model.PayOrder;
import com.qyy.jyshop.model.PointLog;
import com.qyy.jyshop.model.ProfitParam;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;
@Service
public class OrderProfitServiceImpl extends AbstratService<PayOrder> implements OrderProfitService {
	
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private ProfitParamDao profitParamDao;
	@Autowired
	private OrderItemsDao orderItemsDao;
	@Autowired
	private MemberPointDao memberPointDao;
	@Autowired
	private PointLogDao pointLogDao;

	@Override
	public String orderPayProfit(Order order) {
		ProfitParam profitParam=this.profitParamDao.selectAll().get(0);
		BigDecimal totalScale=profitParam.getTax().add(profitParam.getManagement());
		totalScale=totalScale.add(profitParam.getOperation());
		totalScale=totalScale.add(profitParam.getGain());
		totalScale=totalScale.add(profitParam.getPresenter());
		if(totalScale.compareTo(new BigDecimal(100))<0){
			BigDecimal goodsCost=new BigDecimal(0);
            BigDecimal goodsPrice=new BigDecimal(0);
            List<OrderItems> orderItemsList=this.orderItemsDao.selectByOrderId(order.getOrderId());
            if(orderItemsList!=null && orderItemsList.size()>0){
                for (OrderItems orderItems : orderItemsList) {
                    //只有销售价大于成本价的才进行分润
                    if(!StringUtil.isEmpty(orderItems.getCost()) &&
                            orderItems.getPrice().compareTo(orderItems.getCost())>0){
                        goodsCost=goodsCost.add(orderItems.getCost().multiply(new BigDecimal(orderItems.getBuyCount())));
                        goodsPrice=goodsPrice.add(orderItems.getPrice().multiply(new BigDecimal(orderItems.getBuyCount())));
                    }
                }
            }
            //如果销售总金额大于成本总金额,则进行分润
            if(goodsPrice.compareTo(goodsCost)>0){
            	BigDecimal profitAmount=goodsPrice.subtract(goodsCost);
                profitAmount=profitAmount.subtract(profitAmount.multiply(totalScale).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
				Long memberId = order.getMemberId();
				Member member = memberDao.selectByPrimaryKey(memberId);
				Integer isProfit = member.getIsProfit();
				if(isProfit <= 0) {
					//判断此订单是否可成为首购订单
					if(order.getOrderAmount().intValue() >= 200) {
		                profitAmount=profitAmount.multiply(profitParam.getProfitMemberFirst()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN);
		                //积分
		                List<MemberPoint>memberPointList=new ArrayList<MemberPoint>();
		                
		                memberPointList.add(this.getMemberPoint(order,profitAmount,Integer.valueOf(DictionaryUtil.getDataValueByName("point_type", "cloud_point"))));
		                memberPointList.add(this.getMemberPoint(order,profitAmount,Integer.valueOf(DictionaryUtil.getDataValueByName("point_type", "yellow_point"))));
		                memberPointList.add(this.getMemberPoint(order,profitAmount,Integer.valueOf(DictionaryUtil.getDataValueByName("point_type", "red_point"))));
		            
		                this.memberPointDao.batchInsert(memberPointList);
		                this.pointLogDao.insertSelective(this.getPointLog(order,profitAmount));
			        }
				}else {
					//自购分润
					profitAmount=profitAmount.multiply(profitParam.getProfitMemberSec()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN);
	                //积分
	                List<MemberPoint>memberPointList=new ArrayList<MemberPoint>();
	                
	                memberPointList.add(this.getMemberPoint(order,profitAmount,Integer.valueOf(DictionaryUtil.getDataValueByName("point_type", "cloud_point"))));
	                memberPointList.add(this.getMemberPoint(order,profitAmount,Integer.valueOf(DictionaryUtil.getDataValueByName("point_type", "yellow_point"))));
	                memberPointList.add(this.getMemberPoint(order,profitAmount,Integer.valueOf(DictionaryUtil.getDataValueByName("point_type", "red_point"))));
	            
	                this.memberPointDao.batchInsert(memberPointList);
	                this.pointLogDao.insertSelective(this.getPointLog(order,profitAmount));
				}
				//下级分润
				Integer parentId = member.getParentId();
				if(parentId > 0) {
					profitAmount=profitAmount.multiply(profitParam.getProfitShopownerSub()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN);
	                //积分
	                List<MemberPoint>memberPointList=new ArrayList<MemberPoint>();
	                
	                memberPointList.add(this.getMemberPoint(order,profitAmount,Integer.valueOf(DictionaryUtil.getDataValueByName("point_type", "cloud_point"))));
	                memberPointList.add(this.getMemberPoint(order,profitAmount,Integer.valueOf(DictionaryUtil.getDataValueByName("point_type", "yellow_point"))));
	                memberPointList.add(this.getMemberPoint(order,profitAmount,Integer.valueOf(DictionaryUtil.getDataValueByName("point_type", "red_point"))));
	            
	                this.memberPointDao.batchInsert(memberPointList);
	                this.pointLogDao.insertSelective(this.getPointLog(order,profitAmount));
				}
				//其他分润
            }
		}
		return null;
	}
	
	private PointLog getPointLog(Order order, BigDecimal profitAmount) {
		PointLog pointLog= new PointLog();
        pointLog.setMemberId(order.getMemberId());
        pointLog.setOrderId(order.getOrderId());
        pointLog.setOrderSn(order.getOrderSn());
        pointLog.setCloudPoint(order.getPayMoney());
        pointLog.setYellowPoint(profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN));
        pointLog.setRedPoint(profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN));
        pointLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return pointLog;
	}

	private MemberPoint getMemberPoint(Order order,BigDecimal profitAmount,Integer pointType){
        MemberPoint memberPoint =new MemberPoint();
        memberPoint.setMemberId(order.getMemberId());
        memberPoint.setOrderId(order.getOrderId());
        memberPoint.setOrderSn(order.getOrderSn());
        BigDecimal payMoney =  order.getPayMoney();
        if(pointType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("point_type", "cloud_point")))){
            memberPoint.setPoint(payMoney);
        }else if(pointType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("point_type", "yellow_point")))){
            memberPoint.setPoint(profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN));
        }else if(pointType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("point_type", "red_point")))){
            memberPoint.setPoint(profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN));
        }
        memberPoint.setPointType(pointType);
        memberPoint.setType(Integer.valueOf(DictionaryUtil.getDataValueByName("use_type", "gain")));
        memberPoint.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return memberPoint;
    }	

}
