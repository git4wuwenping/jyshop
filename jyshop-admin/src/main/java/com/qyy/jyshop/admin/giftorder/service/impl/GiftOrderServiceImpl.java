package com.qyy.jyshop.admin.giftorder.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.giftorder.service.GiftOrderService;
import com.qyy.jyshop.dao.GiftDeliveryDao;
import com.qyy.jyshop.dao.GiftOrderDao;
import com.qyy.jyshop.dao.GiftOrderItemsDao;
import com.qyy.jyshop.dao.GiftOrderLogDao;
import com.qyy.jyshop.dao.LogiDao;
import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.dao.MemberPointDao;
import com.qyy.jyshop.dao.ProfitParamDao;
import com.qyy.jyshop.model.GiftDelivery;
import com.qyy.jyshop.model.GiftOrder;
import com.qyy.jyshop.model.GiftOrderItems;
import com.qyy.jyshop.model.GiftOrderLog;
import com.qyy.jyshop.model.Logi;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.model.OrderLog;
import com.qyy.jyshop.model.ProfitParam;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;
import com.qyy.jyshop.util.TimestampUtil;

@Service
public class GiftOrderServiceImpl extends AbstratService<GiftOrder> implements GiftOrderService{
    
    public static Logger logger = LoggerFactory.getLogger(GiftOrderServiceImpl.class);

    @Autowired
    private GiftOrderDao giftOrderDao;
    @Autowired
    private GiftOrderItemsDao giftOrderItemsDao;
    @Autowired
    private GiftOrderLogDao giftOrderLogDao;
    @Autowired
    private LogiDao logiDao;
    @Autowired
    private GiftDeliveryDao giftDeliveryDao;
    @Autowired
    private MemberPointDao memberPointDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private ProfitParamDao profitParamDao;
    
    @ServiceLog("查询订单详情")
    public GiftOrder queryOrder(Long orderId){
        return this.queryByID(orderId);
    }
    
    @Override
    @ServiceLog("查询已支付待分配订单")
    public List<Long> queryPayOrderId(){
        return this.giftOrderDao.selectPayOrderId();
    }
    
    @Override
    @ServiceLog("获取订单列表(分页)")
    public PageAjax<Map<String,Object>> pageOrder(PageAjax<Map<String,Object>> page) {
        
        ParamData pd = this.getParamData(page.getPageNo(),page.getPageSize());
        return this.pageQuery("GiftOrderDao.selectOrderByParams", pd);

    }
    
    
    @Override
    @Transactional
    public void doAutomaticReceipt(){
        
        this.giftOrderDao.updateOfReceipt(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_rog")), 
                7, 
                Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_complete")));
    }
    
    @Override
    @Transactional
    public void doAutomaticComplete(){
        
        this.giftOrderDao.updateOfReceipt(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_ship")), 
                7, 
                Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_rog")));
    }
    
    @Override
    @Transactional
    @ServiceLog("订单发货")
    public String doOrderDelivery(Map<String,Object> params){
        
        if(StringUtil.isEmpty(params.get("logiNo")))
            return "获取物流单号不能为空....";
                    
        GiftDelivery delivery=new GiftDelivery();
        
        Logi logi=logiDao.selectByPrimaryKey(Integer.valueOf(params.get("logiId").toString()));
        if(logi!=null){
            delivery.setLogiId(logi.getLogiId());
            delivery.setLogiCode(logi.getLogiCode());
            delivery.setLogiName(logi.getLogiName());
        }else{
            return "获取物流公司信息失败....";
        }
            
        GiftOrder order=this.giftOrderDao.selectByPrimaryKey(Long.valueOf(params.get("orderId").toString()));
        if(order!=null){

            if(!StringUtil.isEmpty(order.getComId()) && !(
                    order.getComId().equals(0) ||
                    order.getComId().equals(this.getLoginUser().getComId()))){
                return "你没你权限对订单进行发货操作...";
            }
            
            if(!order.getOrderStatus().equals(
                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_allocation")))){
                return "订单状态错误,可能已进行过发货动作...";
            }
            
            /**
             * 如果是父订单,则直接修改订单状态与货运状态为己发货
             * 如果是子订单,则对所有子订单发货情况进行判断,如果己全部发货,则修改父订单为己发货,否则修改为部分发货
             */
            Integer orderStatus=Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_ship"));
            Integer shipStatus=Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_yes"));
            
            //修改父订单状态
            GiftOrder updateCOrder=new GiftOrder();
            updateCOrder.setOrderId(order.getOrderId());
            updateCOrder.setOrderStatus(orderStatus);
            updateCOrder.setShipStatus(shipStatus);
            updateCOrder.setDeliverTime(new Timestamp(System.currentTimeMillis()));
            this.update(updateCOrder);
            
            if(!order.getParentId().equals(0L)){//如果不是父订单,则对子订单的发货状态进行判断
                
                Example example = new Example(GiftOrder.class);
                Criteria criteria = example.createCriteria();
                criteria.andEqualTo("parentId", order.getParentId());
                criteria.andEqualTo("orderStatus", DictionaryUtil.getDataValueByName("order_status", "order_allocation"));
                criteria.andEqualTo("shipStatus", DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_yes"));

                //如果子订单都己发货完成,则修改主订单状态为己发货状态,否则修改为部分发货
                if(this.giftOrderDao.selectCountByExample(example)>0){
                    orderStatus=Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_partial_shiped"));
                    shipStatus=Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_partial_shiped"));
                }
                
                //修改父订单状态
                GiftOrder updateOrder=new GiftOrder();
                updateOrder.setOrderId(order.getParentId());
                updateOrder.setOrderStatus(orderStatus);
                updateOrder.setShipStatus(shipStatus);
                updateOrder.setDeliverTime(new Timestamp(System.currentTimeMillis()));
                this.update(updateOrder);
            }
            
            delivery.setOrderId(order.getOrderId());
            delivery.setOrderSn(order.getOrderSn());
            delivery.setMemberId(order.getMemberId());
            delivery.setLogiNo(params.get("logiNo").toString());
            delivery.setOpId(Long.valueOf(this.getLoginUser().getId()));
            delivery.setOpUser(this.getLoginUser().getUsername());
            delivery.setCreationTime(new Timestamp(System.currentTimeMillis()));
            delivery.setType(Integer.valueOf(DictionaryUtil.getDataValueByName("delivery_type", "delivery")));
            this.giftDeliveryDao.insertSelective(delivery);

            //添加日志
            GiftOrderLog orderLog=new GiftOrderLog();
            orderLog.setOrderId(order.getOrderId());
            orderLog.setOrderSn(order.getOrderSn());
            orderLog.setOpId(Long.valueOf(this.getLoginUser().getId()));
            orderLog.setOpUser(this.getLoginUser().getUsername());
            orderLog.setMessage(this.getLoginUser().getUsername()+"对订单"+order.getOrderSn()+"进行了发货操作,发货订单ID:"+delivery.getOrderId());
            orderLog.setUserType(Integer.valueOf(DictionaryUtil.getDataValueByName("user_type", "administrator")));
            orderLog.setCreationTime(new Timestamp(System.currentTimeMillis()));
            this.giftOrderLogDao.insertSelective(orderLog);
        }else{
            return "获取发货订单信息失败...";
        }
        return null;
    }
    
    @Override
    @Transactional
    @ServiceLog("订单作废")
    public String doCancleOrder(Long orderId){
        
        GiftOrder order=this.queryByID(orderId);
        if(order==null)
            return "获取订单信息失败...";
        if(this.getLoginUserComId().equals(0) || this.getLoginUserComId().equals(order.getComId())){
            
            this.giftOrderDao.updateOrderStatus(orderId, 
                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_returnlation")),
                    null, null);
            //添加日志
            GiftOrderLog orderLog=new GiftOrderLog();
            orderLog.setOrderId(order.getOrderId());
            orderLog.setOrderSn(order.getOrderSn());
            orderLog.setOpId(Long.valueOf(this.getLoginUser().getId()));
            orderLog.setOpUser(this.getLoginUser().getUsername());
            orderLog.setMessage(this.getLoginUser().getUsername()+"对订单"+order.getOrderSn()+"进行了作废操作,作废订单ID:"+order.getOrderId());
            orderLog.setUserType(Integer.valueOf(DictionaryUtil.getDataValueByName("user_type", "administrator")));
            orderLog.setCreationTime(new Timestamp(System.currentTimeMillis()));
            this.giftOrderLogDao.insertSelective(orderLog);
        }else{
            return "你没有权限操作该订单...";
        }
   
        return null;
    }

	@Override
	public Map<String, Object> getOrderCountByMemberId(Long memberId) {
		return this.giftOrderDao.getOrderCountByMemberId(memberId);
	}
	
	@Override
	public List<Map<String, Object>> queryGiftOrderProfit(GiftOrder order, Member member) {
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		if(order.getOrderStatus() == Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_complete"))){
			//返回已分润的结果
			returnList = memberPointDao.getOrderProfit(order.getOrderId());
		}else if(order.getOrderStatus() >= 0 && order.getOrderStatus() != 8){
			//预分润
			ProfitParam profitParam = profitParamDao.selectAll().get(0);
			BigDecimal total=profitParam.getTax().add(profitParam.getManagement());
	        total=total.add(profitParam.getOperation());
	        total=total.add(profitParam.getGain());
	        total=total.add(profitParam.getPresenter());
	        
	        if(total.compareTo(new BigDecimal(100))<0){
	        	BigDecimal goodsCost=new BigDecimal(0);//成本总金额
	            BigDecimal goodsPrice=new BigDecimal(0);//销售总金额
	            List<GiftOrderItems> giftOrderItemsList=this.giftOrderItemsDao.selectByOrderId(order.getOrderId());
	            if(giftOrderItemsList!=null && giftOrderItemsList.size()>0){
	                for (GiftOrderItems giftOrderItems : giftOrderItemsList) {
	                    //只有销售价大于成本价的才进行分润
	                    if(!StringUtil.isEmpty(giftOrderItems.getCost()) &&
	                    		giftOrderItems.getPrice().compareTo(giftOrderItems.getCost())>0){
	                        goodsCost=goodsCost.add(giftOrderItems.getCost().multiply(new BigDecimal(giftOrderItems.getBuyCount())));
	                        goodsPrice=goodsPrice.add(giftOrderItems.getPrice().multiply(new BigDecimal(giftOrderItems.getBuyCount())));
	                    }
	                }
	            }
	            
	          //如果销售总金额大于成本总金额,则进行分润
	            if(goodsPrice.compareTo(goodsCost)>0){
	                BigDecimal profitAmount=goodsPrice.subtract(goodsCost);//毛利润
	                //分润金额(毛利润2)
	                profitAmount=profitAmount.subtract(profitAmount.multiply(total).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
	                if(member.getIsProfit() == 0){//首购毛利润2的30%
		        		//if(order.getOrderAmount().compareTo(new BigDecimal(200))>0){
	        			profitAmount=profitAmount.multiply(profitParam.getProfitMemberFirst()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN);
	        			BigDecimal cloudPoint = order.getPayMoney();
	        			BigDecimal yellowCloud = profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
	        			BigDecimal redCloud = profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
	        			//拼装返回对象
	        			Map<String,Object> map = new HashMap<String, Object>();
	        			map.put("memberId", member.getMemberId());
	        			map.put("nickname", member.getNickname());
	        			map.put("pointType", 0);
	        			map.put("point", cloudPoint);
	        			map.put("getType", 0);
	        			returnList.add(map);
	        			map = new HashMap<String, Object>();
	        			map.put("memberId", member.getMemberId());
	        			map.put("nickname", member.getNickname());
	        			map.put("pointType", 1);
	        			map.put("point", yellowCloud);
	        			map.put("getType", 0);
	        			returnList.add(map);
	        			map = new HashMap<String, Object>();
	        			map.put("memberId", member.getMemberId());
	        			map.put("nickname", member.getNickname());
	        			map.put("pointType", 2);
	        			map.put("point", redCloud);
	        			map.put("getType", 0);
	        			returnList.add(map);
		        		//}
		        	}else{//重复分润 毛利润2的20%
		        		profitAmount=profitAmount.multiply(profitParam.getProfitMemberSec()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN);
	        			BigDecimal cloudPoint = order.getPayMoney();
	        			BigDecimal yellowCloud = profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
	        			BigDecimal redCloud = profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
	        			//拼装返回对象
	        			Map<String,Object> map = new HashMap<String, Object>();
	        			map.put("memberId", member.getMemberId());
	        			map.put("nickname", member.getNickname());
	        			map.put("pointType", 0);
	        			map.put("point", cloudPoint);
	        			map.put("getType", 1);
	        			returnList.add(map);
	        			map = new HashMap<String, Object>();
	        			map.put("memberId", member.getMemberId());
	        			map.put("nickname", member.getNickname());
	        			map.put("pointType", 1);
	        			map.put("point", yellowCloud);
	        			map.put("getType", 1);
	        			returnList.add(map);
	        			map = new HashMap<String, Object>();
	        			map.put("memberId", member.getMemberId());
	        			map.put("nickname", member.getNickname());
	        			map.put("pointType", 2);
	        			map.put("point", redCloud);
	        			map.put("getType", 1);
	        			returnList.add(map);
		        	}
		        	
		        	Integer parentId = member.getParentId();
		        	if(parentId > 0 ){//给上级分润
		        		profitAmount=profitAmount.multiply(profitParam.getProfitShopownerSub()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN);
	        			BigDecimal cloudPoint = order.getPayMoney();
	        			BigDecimal yellowCloud = profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
	        			BigDecimal redCloud = profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
	        			//拼装返回对象
	        			Member parentMember = memberDao.findMemerById(member.getParentId().longValue());
	        			Map<String,Object> map = new HashMap<String, Object>();
	        			map.put("memberId", member.getParentId());
	        			map.put("nickname", parentMember==null?"":parentMember.getNickname());
	        			map.put("pointType", 0);
	        			map.put("point", cloudPoint);
	        			map.put("getType", 2);
	        			returnList.add(map);
	        			map = new HashMap<String, Object>();
	        			map.put("memberId", member.getParentId());
	        			map.put("nickname", parentMember==null?"":parentMember.getNickname());
	        			map.put("pointType", 1);
	        			map.put("getType", 2);
	        			map.put("point", yellowCloud);
	        			returnList.add(map);
	        			map = new HashMap<String, Object>();
	        			map.put("memberId", member.getParentId());
	        			map.put("nickname", parentMember==null?"":parentMember.getNickname());
	        			map.put("pointType", 2);
	        			map.put("point", redCloud);
	        			map.put("getType", 2);
	        			returnList.add(map);
		        	}
	            }
	        }
		}
		
		return returnList;
	}

    @Override
    @Transactional
    public void autoCloseGiftOrder(GiftOrder giftOrder, BigDecimal hourOrderAutoClose) {
        /**
         *  ORDER_STATUS = - 9,
          DELETE_FLAG = 1
         */
        GiftOrder updateGiftOrder = new GiftOrder();
        updateGiftOrder.setOrderId(giftOrder.getOrderId());
        updateGiftOrder.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_cancel")));
        updateGiftOrder.setDeleteFlag(1);
        giftOrderDao.updateByPrimaryKeySelective(updateGiftOrder);
        
        //orderlog
        //添加日志
        GiftOrderLog orderLog=new GiftOrderLog();
        orderLog.setOrderId(giftOrder.getOrderId());
        orderLog.setOrderSn(giftOrder.getOrderSn());
        orderLog.setMessage("系统自动对订单"+giftOrder.getOrderSn()+"进行了关闭操作,该订单"+hourOrderAutoClose+"小时未支付");
        orderLog.setUserType(Integer.valueOf(DictionaryUtil.getDataValueByName("user_type", "system")));
        orderLog.setCreationTime(TimestampUtil.getNowTime());
        this.giftOrderLogDao.insertSelective(orderLog);
    }

    @Override
    @Transactional
    public void autoConfirmCommonOrder(GiftOrder giftOrder, BigDecimal dayCommonAutoConfirm) {
        GiftOrder updateGiftOrder=new GiftOrder();
        updateGiftOrder.setOrderId(giftOrder.getOrderId());
        updateGiftOrder.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status","order_rog")));
        updateGiftOrder.setShipStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status","ship_receipt")));
        updateGiftOrder.setSigningTime(new Timestamp(System.currentTimeMillis()));
        this.update(updateGiftOrder);
        
        //添加日志
        GiftOrderLog giftOrderLog=new GiftOrderLog();
        giftOrderLog.setOrderId(giftOrder.getOrderId());
        giftOrderLog.setOrderSn(giftOrder.getOrderSn());
        giftOrderLog.setMessage("系统自动对订单"+giftOrder.getOrderSn()+"进行了确认收货操作,该订单发货后"+dayCommonAutoConfirm+"天内买家未操作，系统自动确认收货");
        giftOrderLog.setUserType(Integer.valueOf(DictionaryUtil.getDataValueByName("user_type", "system")));
        giftOrderLog.setCreationTime(TimestampUtil.getNowTime());
        this.giftOrderLogDao.insertSelective(giftOrderLog);
        
    }

    @Override
    public void autoCloseOrderLegal(GiftOrder giftOrder, BigDecimal dayLegalAutoClose) {
        GiftOrder updateGiftOrder=new GiftOrder();
        updateGiftOrder.setOrderId(giftOrder.getOrderId());
        updateGiftOrder.setRightCloseStatus(1);//TODO 不可维权
        this.update(updateGiftOrder);
        
        //添加日志
        GiftOrderLog giftOrderLog=new GiftOrderLog();
        giftOrderLog.setOrderId(giftOrder.getOrderId());
        giftOrderLog.setOrderSn(giftOrder.getOrderSn());
        giftOrderLog.setMessage("系统自动对订单"+giftOrder.getOrderSn()+"进行了自动关闭维权功能操作,该订单已收货订单"+dayLegalAutoClose+"天内不发起维权，系统自动关闭维权功能"); 
        giftOrderLog.setUserType(Integer.valueOf(DictionaryUtil.getDataValueByName("user_type", "system")));
        giftOrderLog.setCreationTime(TimestampUtil.getNowTime());
        this.giftOrderLogDao.insertSelective(giftOrderLog);
        
    }
}
