package com.qyy.jyshop.admin.profit.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.admin.profit.service.ProfitService;
import com.qyy.jyshop.dao.GiftOrderDao;
import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.dao.MemberPointDao;
import com.qyy.jyshop.dao.MemberProfitDao;
import com.qyy.jyshop.dao.OrderDao;
import com.qyy.jyshop.dao.OrderItemsDao;
import com.qyy.jyshop.dao.PointLogDao;
import com.qyy.jyshop.dao.ProfitLogDao;
import com.qyy.jyshop.dao.ProfitParamDao;
import com.qyy.jyshop.model.MemberPoint;
import com.qyy.jyshop.model.OrderItems;
import com.qyy.jyshop.model.PointLog;
import com.qyy.jyshop.model.ProfitParam;
import com.qyy.jyshop.platform.job.ProfitJob;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;

@Service
public class ProfitServiceImpl implements ProfitService{
	private static final Integer GETTYPE_FIRSTPROFIT = 0;
	private static final Integer GETTYPE_SUCPROFIT = 1;
	private static final Integer GETTYPE_SUBPROFIT = 2;

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private GiftOrderDao giftOrderDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private MemberPointDao memberPointDao;
    @Autowired
    private MemberProfitDao memberProfitDao;
    @Autowired
    private PointLogDao pointLogDao;
    @Autowired
    private ProfitLogDao profitLogDao;
    @Autowired
    private ProfitParamDao profitParamDao;
    @Autowired
    private OrderItemsDao orderItemsDao;
    
    @Override
    @Transactional
    public void doOrderFirstProfit(Map<String,Object> orderMap,ProfitParam profitParam,Integer orderType) {
        //分润 
        this.memberDao.updateProfitMember(Long.valueOf(orderMap.get("memberId").toString()), 
        		(Timestamp)orderMap.get("paymentTime"));
        //对第一个订单进行分润
        this.memberProfit(profitParam,orderMap,true,orderType);
        //修改订单为己分润
        if(orderType == ProfitJob.ORDER_COMMON){
	        this.orderDao.updateOfProfitStatus(Long.valueOf(orderMap.get("orderId").toString()),
	                Integer.valueOf(DictionaryUtil.getDataValueByName("profit_status", "profit_yes")));
        }else if(orderType == ProfitJob.ORDER_GIFT){
        	this.giftOrderDao.updateOfProfitStatus(Long.valueOf(orderMap.get("orderId").toString()),
	                Integer.valueOf(DictionaryUtil.getDataValueByName("profit_status", "profit_yes")));
        }
    }
    
    @Override
    @Transactional
    public void doOrderProfit(Map<String,Object> orderMap,ProfitParam profitParam,Integer orderType){
        //对订单进行分润
        this.memberProfit(profitParam,orderMap,false,orderType);
        //修改订单为己分润
        if(orderType == ProfitJob.ORDER_COMMON){
	        this.orderDao.updateOfProfitStatus(Long.valueOf(orderMap.get("orderId").toString()),
	                Integer.valueOf(DictionaryUtil.getDataValueByName("profit_status", "profit_yes")));
        }else{
        	this.giftOrderDao.updateOfProfitStatus(Long.valueOf(orderMap.get("orderId").toString()),
	                Integer.valueOf(DictionaryUtil.getDataValueByName("profit_status", "profit_yes")));
        }
    }
    
    private void memberProfit(ProfitParam profitParam,Map<String,Object> orderMap,boolean isFirst,Integer orderType){
        
        BigDecimal total=profitParam.getTax().add(profitParam.getManagement());
        total=total.add(profitParam.getOperation());
        total=total.add(profitParam.getGain());
        total=total.add(profitParam.getPresenter());
        
        if(total.compareTo(new BigDecimal(100))<0){
            
            BigDecimal goodsCost=new BigDecimal(0);
            BigDecimal goodsPrice=new BigDecimal(0);
            List<OrderItems> orderItemsList=this.orderItemsDao.selectByOrderId(Long.valueOf(orderMap.get("orderId").toString()));
            
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
//              //分润金额(毛利润2)
                profitAmount=profitAmount.subtract(profitAmount.multiply(total).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
//                //分润
//                MemberProfit memberProfit=new MemberProfit();
//                memberProfit.setMemberId(Long.valueOf(orderMap.get("memberId").toString()));
//                memberProfit.setOrderId(Long.valueOf(orderMap.get("orderId").toString()));
//                memberProfit.setOrderSn(orderMap.get("orderSn").toString());
//                memberProfit.setProfitAmount(profitAmount);
//                if(isFirst)
//                    memberProfit.setRatio(profitParam.getProfitMemberFirst().divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
//                else
//                    memberProfit.setRatio(profitParam.getProfitMemberSec().divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
//                memberProfit.setAmount(profitAmount.multiply(memberProfit.getRatio()).setScale(2, BigDecimal.ROUND_DOWN));
//                memberProfit.setCreateTime(new Timestamp(System.currentTimeMillis()));
//                memberProfit.setStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("examine_status", "success")));
//                this.memberProfitDao.insertSelective(memberProfit);
                //添加分润日志
//                ProfitLog profitLog=new ProfitLog();
//                profitLog.setMemberId(memberProfit.getMemberId());
//                profitLog.setOrderId(memberProfit.getOrderId());
//                profitLog.setOrderSn(memberProfit.getOrderSn());
//                profitLog.setOrderAmount(new BigDecimal(orderMap.get("orderAmount").toString()));
//                profitLog.setGoodsAmount(new BigDecimal(orderMap.get("goodsAmount").toString()));
//                profitLog.setGoodsCost(goodsCost);
//                profitLog.setGoodsPrice(goodsPrice);
//                profitLog.setProfitAmount(profitAmount);
//                profitLog.setOneProfitRatio(total.divide(new BigDecimal(100), 4,BigDecimal.ROUND_DOWN));
//                profitLog.setTwoProfitRatio(new BigDecimal(1).subtract(profitLog.getOneProfitRatio()));
//                profitLog.setTax(profitParam.getTax());
//                profitLog.setManagement(profitParam.getManagement());
//                profitLog.setOperation(profitParam.getOperation());
//                profitLog.setGain(profitParam.getGain());
//                profitLog.setServiceCenter(profitParam.getServiceCenter());
//                profitLog.setReferee(profitParam.getReferee());
//                profitLog.setOperator(profitParam.getOperator());
//                profitLog.setProfitMemberFirst(profitParam.getProfitMemberFirst());
//                profitLog.setProfitMemberSec(profitParam.getProfitMemberSec());
//                profitLog.setProfitShopownerFirst(profitParam.getProfitShopownerFirst());
//                profitLog.setProfitShopownerSec(profitParam.getProfitShopownerSec());
//                profitLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
//                profitLogDao.insertSelective(profitLog);
                
                //将分润金额添加到用户预存款中
//                this.memberDao.updateMemberAdvanceOfAdd(memberProfit.getMemberId(), memberProfit.getAmount());
                
                if(isFirst)
                	profitAmount=profitAmount.multiply(profitParam.getProfitMemberFirst()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN);
                    //profitAmount=profitParam.getProfitMemberFirst().divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN);
                else
                	profitAmount=profitAmount.multiply(profitParam.getProfitMemberSec()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN);
                    //profitAmount=profitParam.getProfitMemberSec().divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN);
                //积分
                List<MemberPoint>memberPointList=new ArrayList<MemberPoint>();
                
                memberPointList.add(this.getMemberPoint(orderMap,profitAmount,Integer.valueOf(DictionaryUtil.getDataValueByName("point_type", "cloud_point")),orderType,isFirst?GETTYPE_FIRSTPROFIT:GETTYPE_SUCPROFIT));
                memberPointList.add(this.getMemberPoint(orderMap,profitAmount,Integer.valueOf(DictionaryUtil.getDataValueByName("point_type", "yellow_point")),orderType,isFirst?GETTYPE_FIRSTPROFIT:GETTYPE_SUCPROFIT));
                memberPointList.add(this.getMemberPoint(orderMap,profitAmount,Integer.valueOf(DictionaryUtil.getDataValueByName("point_type", "red_point")),orderType,isFirst?GETTYPE_FIRSTPROFIT:GETTYPE_SUCPROFIT));
            
                this.memberPointDao.batchInsert(memberPointList);
                PointLog pointLog=new PointLog();
                pointLog.setMemberId(Long.valueOf(orderMap.get("memberId").toString()));
                pointLog.setOrderId(Long.valueOf(orderMap.get("orderId").toString()));
                pointLog.setOrderSn(orderMap.get("orderSn").toString());
                pointLog.setCloudPoint(profitAmount);
                pointLog.setYellowPoint(profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN));
                pointLog.setRedPoint(profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN));
                pointLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
                this.pointLogDao.insertSelective(pointLog);
                
                BigDecimal payMoney =BigDecimal.ZERO;
                try{
                	payMoney = new BigDecimal(orderMap.get("payMoney").toString());
                }catch (Exception e) {}
                
                this.memberDao.updateMemberPointOfAdd(Long.valueOf(orderMap.get("memberId").toString()), 
                		payMoney, 
                        profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN), 
                        profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN));
            }
        }
    }
    
    private MemberPoint getMemberPoint(Map<String, Object>orderMap,BigDecimal profitAmount,Integer pointType,Integer orderType,Integer getType){
        
        MemberPoint memberPoint =new MemberPoint();
        memberPoint.setMemberId(Long.valueOf(orderMap.get("memberId").toString()));
        memberPoint.setOrderId(Long.valueOf(orderMap.get("orderId").toString()));
        memberPoint.setOrderSn(orderMap.get("orderSn").toString());
        
        BigDecimal payMoney =BigDecimal.ZERO;
        try{
        	payMoney = new BigDecimal(orderMap.get("payMoney").toString());
        }catch (Exception e) {}
        
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
        memberPoint.setPointStatus(1);
        memberPoint.setOrderType(orderType);
        memberPoint.setGetType(getType);
        return memberPoint;
    }

	@Override
	@Transactional
	public void doShopownerSubProfitOrder(Map<String, Object> orderMap, ProfitParam profitParam,Integer orderType) {
		//对订单进行上级分润
        this.subProfitOrder(profitParam,orderMap,orderType);
        //修改订单为上级己分润
        this.orderDao.updateSubProfitStatus(Long.valueOf(orderMap.get("orderId").toString()),
                Integer.valueOf(DictionaryUtil.getDataValueByName("profit_status", "profit_yes")));
	}
	
	
	private void subProfitOrder(ProfitParam profitParam,Map<String,Object> orderMap,Integer orderType){
        
        BigDecimal total=profitParam.getTax().add(profitParam.getManagement());
        total=total.add(profitParam.getOperation());
        total=total.add(profitParam.getGain());
        total=total.add(profitParam.getPresenter());
        
        if(total.compareTo(new BigDecimal(100))<0){
            
            BigDecimal goodsCost=new BigDecimal(0);
            BigDecimal goodsPrice=new BigDecimal(0);
            List<OrderItems> orderItemsList=this.orderItemsDao.selectByOrderId(Long.valueOf(orderMap.get("orderId").toString()));
            
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
                //以系统毛利润扣除第一部分相应角色的分润后为基础进行计算。
                profitAmount=profitAmount.subtract(profitAmount.multiply(total).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
                profitAmount=profitAmount.multiply(profitParam.getProfitShopownerSub()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN);
                //积分
                List<MemberPoint>memberPointList=new ArrayList<MemberPoint>();
                //上级获得积分
                orderMap.put("memberId", orderMap.get("parentId"));
                memberPointList.add(this.getMemberPoint(orderMap,profitAmount,Integer.valueOf(DictionaryUtil.getDataValueByName("point_type", "cloud_point")),orderType,GETTYPE_SUBPROFIT));
                memberPointList.add(this.getMemberPoint(orderMap,profitAmount,Integer.valueOf(DictionaryUtil.getDataValueByName("point_type", "yellow_point")),orderType,GETTYPE_SUBPROFIT));
                memberPointList.add(this.getMemberPoint(orderMap,profitAmount,Integer.valueOf(DictionaryUtil.getDataValueByName("point_type", "red_point")),orderType,GETTYPE_SUBPROFIT));
            
                this.memberPointDao.batchInsert(memberPointList);
                PointLog pointLog=new PointLog();
                pointLog.setMemberId(Long.valueOf(orderMap.get("parentId").toString()));
                pointLog.setOrderId(Long.valueOf(orderMap.get("orderId").toString()));
                pointLog.setOrderSn(orderMap.get("orderSn").toString());
                pointLog.setCloudPoint(profitAmount);
                pointLog.setYellowPoint(profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN));
                pointLog.setRedPoint(profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN));
                pointLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
                this.pointLogDao.insertSelective(pointLog);
                
                BigDecimal payMoney =BigDecimal.ZERO;
                try{
                	payMoney = new BigDecimal(orderMap.get("payMoney").toString());
                }catch (Exception e) {}
                
                this.memberDao.updateMemberPointOfAdd(Long.valueOf(orderMap.get("parentId").toString()), 
                		payMoney, 
                        profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN), 
                        profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN));
            }
        }
    }

}
