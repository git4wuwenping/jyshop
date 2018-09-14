package com.qyy.jyshop.platform.job;

import java.math.BigDecimal;
import java.util.List;

import com.qyy.jyshop.admin.right.service.RightService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.qyy.jyshop.admin.comment.service.CommentService;
import com.qyy.jyshop.admin.common.conf.ShopBaseConf;
import com.qyy.jyshop.admin.giftorder.service.GiftOrderService;
import com.qyy.jyshop.admin.order.service.OrderService;
import com.qyy.jyshop.dao.GiftOrderDao;
import com.qyy.jyshop.dao.OrderDao;
import com.qyy.jyshop.model.GiftOrder;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.model.OrderParamConfig;
import com.qyy.jyshop.util.DictionaryUtil;


@Component
public class OrderFlowJob {
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private GiftOrderDao giftOrderDao;
	@Autowired
	private ShopBaseConf shopBaseConf;
	@Autowired
	private OrderService orderService;
	@Autowired
	private GiftOrderService giftOrderService;
	@Autowired
	private CommentService commentService;
	@Autowired
    private RightService rightService;
	
	private final Log logger = LogFactory.getLog(OrderFlowJob.class);
	
	//在线支付未支付订单x小时后自动关闭
	@Scheduled(cron="0 0/9 * * * ?")
	public void autoCloseOrder(){
		logger.info("在线支付未支付订单自动关闭 处理开始...");
		OrderParamConfig orderParamConfig = shopBaseConf.getParamConfig(OrderParamConfig.class, "shopParam_orderParamConfig");
		BigDecimal hourOrderAutoClose = orderParamConfig.getHourOrderAutoClose();
		
		Example example = new Example(Order.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andCondition("create_time < DATE_SUB(NOW(), INTERVAL "+ hourOrderAutoClose.multiply(new BigDecimal(3600)) +" SECOND)");
		createCriteria.andEqualTo("orderStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_not_pay")));
		createCriteria.andEqualTo("payStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_no")));
		createCriteria.andEqualTo("deleteFlag", 0);
		List<Order> orderList = orderDao.selectByExample(example);
		for(Order order:orderList){
		    this.orderService.autoCloseOrder(order,hourOrderAutoClose);
		}
		
		Example example1 = new Example(GiftOrder.class);
        Criteria createCriteria1 = example1.createCriteria();
        createCriteria1.andCondition("create_time < DATE_SUB(NOW(), INTERVAL "+ hourOrderAutoClose.multiply(new BigDecimal(3600)) +" SECOND)");
        createCriteria1.andEqualTo("orderStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_not_pay")));
        createCriteria1.andEqualTo("payStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_no")));
        createCriteria1.andEqualTo("deleteFlag", 0);
        List<GiftOrder> giftOrderList = giftOrderDao.selectByExample(example1);
        for(GiftOrder giftOrder:giftOrderList){
            this.giftOrderService.autoCloseGiftOrder(giftOrder,hourOrderAutoClose);
        }
		
		logger.info("在线支付未支付订单自动关闭 处理完成...");
	}
	
	
	//普通商品，发货后x天后买家未操作，系统自动确认收货
	@Scheduled(cron="0 0/55 * * * ?")
	public void autoConfirmCommonOrder(){
	    logger.info("普通商品发货若干天后买家未操作，统自动确认收货 处理开始...");
		OrderParamConfig orderParamConfig = shopBaseConf.getParamConfig(OrderParamConfig.class, "shopParam_orderParamConfig");
		BigDecimal dayCommonAutoConfirm = orderParamConfig.getDayCommonAutoConfirm();
		
		Example example = new Example(Order.class);
        Criteria createCriteria = example.createCriteria();
        createCriteria.andCondition("DELIVER_TIME < DATE_SUB(NOW(), INTERVAL "+ dayCommonAutoConfirm.multiply(new BigDecimal(3600*24)) +" SECOND)");
        createCriteria.andEqualTo("orderStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("order_status","order_ship")));
        createCriteria.andEqualTo("payStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")));
        createCriteria.andEqualTo("shipStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_yes")));
        createCriteria.andEqualTo("orderType", 0); //TODO 订单类型 0普通订单 1海外订单 2活动订单
        createCriteria.andEqualTo("deleteFlag", 0);
        List<Order> orderList = orderDao.selectByExample(example);
        for(Order order:orderList){
            this.orderService.autoConfirmCommonOrder(order,dayCommonAutoConfirm);
        }
        
        Example example1 = new Example(GiftOrder.class);
        Criteria createCriteria1 = example1.createCriteria();
        createCriteria1.andCondition("DELIVER_TIME < DATE_SUB(NOW(), INTERVAL "+ dayCommonAutoConfirm.multiply(new BigDecimal(3600*24)) +" SECOND)");
        createCriteria1.andEqualTo("orderStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("order_status","order_ship")));
        createCriteria1.andEqualTo("payStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")));
        createCriteria1.andEqualTo("shipStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_yes")));
        createCriteria1.andEqualTo("orderType", 0); //TODO 订单类型 0普通订单 1海外订单 2活动订单
        createCriteria1.andEqualTo("deleteFlag", 0);
        List<GiftOrder> giftOrderList = giftOrderDao.selectByExample(example1);
        for(GiftOrder giftOrder:giftOrderList){
            this.giftOrderService.autoConfirmCommonOrder(giftOrder,dayCommonAutoConfirm);
        }
        logger.info("普通商品发货若干天后买家未操作，统自动确认收货 处理完成...");
	}
	
	//已收货订单若干天内不发起维权，系统自动关闭维权功能
	@Scheduled(cron="0 0/56 * * * ?")
	public void autoCloseOrderLegal(){
	    logger.info("已收货订单若干天内不发起维权，系统自动关闭维权功能 处理开始...");
        OrderParamConfig orderParamConfig = shopBaseConf.getParamConfig(OrderParamConfig.class, "shopParam_orderParamConfig");
        BigDecimal dayLegalAutoClose = orderParamConfig.getDayLegalAutoClose();
        
        Example example = new Example(Order.class);
        Criteria createCriteria = example.createCriteria();
        createCriteria.andCondition("Signing_Time < DATE_SUB(NOW(), INTERVAL "+ dayLegalAutoClose.multiply(new BigDecimal(3600*24)) +" SECOND)");
        createCriteria.andEqualTo("orderStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("order_status","order_rog")));
        createCriteria.andEqualTo("shipStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_receipt")));
        createCriteria.andEqualTo("rightCloseStatus", 0); //TODO
        createCriteria.andEqualTo("deleteFlag", 0); //TODO
        List<Order> orderList = orderDao.selectByExample(example);
        for(Order order:orderList){
            this.orderService.autoCloseOrderLegal(order,dayLegalAutoClose);
        }
        
        Example example1 = new Example(GiftOrder.class);
        Criteria createCriteria1 = example1.createCriteria();
        createCriteria1.andCondition("Signing_Time < DATE_SUB(NOW(), INTERVAL "+ dayLegalAutoClose.multiply(new BigDecimal(3600*24)) +" SECOND)");
        createCriteria1.andEqualTo("orderStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("order_status","order_rog")));
        createCriteria1.andEqualTo("shipStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_receipt")));
        createCriteria1.andEqualTo("rightCloseStatus", 0); //TODO
        createCriteria1.andEqualTo("deleteFlag", 0); //TODO
        List<GiftOrder> giftOrderList = giftOrderDao.selectByExample(example1);
        for(GiftOrder giftOrder:giftOrderList){
            this.giftOrderService.autoCloseOrderLegal(giftOrder,dayLegalAutoClose);
        }
        
        logger.info("已收货订单若干天内不发起维权，系统自动关闭维权功能 处理完成...");
	}
	
	//海外商品若干天内买家未操作，系统自动确认收货
	@Scheduled(cron="0 0/57 * * * ?")
    public void autoConFirmOverSeaOrder(){
        logger.info("海外商品若干天内买家未操作，系统自动确认收货 处理开始...");
        OrderParamConfig orderParamConfig = shopBaseConf.getParamConfig(OrderParamConfig.class, "shopParam_orderParamConfig");
        BigDecimal dayOverseaAutoConfirm = orderParamConfig.getDayOverseaAutoConfirm();

        Example example = new Example(Order.class);
        Criteria createCriteria = example.createCriteria();
        createCriteria.andCondition("DELIVER_TIME < DATE_SUB(NOW(), INTERVAL "+ dayOverseaAutoConfirm.multiply(new BigDecimal(3600*24)) +" SECOND)");
        createCriteria.andEqualTo("orderStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("order_status","order_ship")));
        createCriteria.andEqualTo("payStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")));
        createCriteria.andEqualTo("shipStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_yes")));
        createCriteria.andEqualTo("orderType", 1); //TODO 订单类型 0普通订单 1海外订单 2活动订单
        createCriteria.andEqualTo("deleteFlag", 0);
        List<Order> orderList = orderDao.selectByExample(example);
        for(Order order:orderList){
            this.orderService.autoConfirmCommonOrder(order,dayOverseaAutoConfirm);
        }
        
        Example example1 = new Example(GiftOrder.class);
        Criteria createCriteria1 = example1.createCriteria();
        createCriteria1.andCondition("DELIVER_TIME < DATE_SUB(NOW(), INTERVAL "+ dayOverseaAutoConfirm.multiply(new BigDecimal(3600*24)) +" SECOND)");
        createCriteria1.andEqualTo("orderStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("order_status","order_ship")));
        createCriteria1.andEqualTo("payStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")));
        createCriteria1.andEqualTo("shipStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_yes")));
        createCriteria1.andEqualTo("orderType", 1); //TODO 订单类型 0普通订单 1海外订单 2活动订单
        createCriteria1.andEqualTo("deleteFlag", 0);
        List<GiftOrder> giftOrderList = giftOrderDao.selectByExample(example1);
        for(GiftOrder giftOrder:giftOrderList){
            this.giftOrderService.autoConfirmCommonOrder(giftOrder,dayOverseaAutoConfirm);
        }
        
        logger.info("海外商品若干天内买家未操作，系统自动确认收货 处理完成...");
    }

//    @Scheduled(cron="0 */1 * * * ?")
    //买家发起退款申请若干天内未处理，系统将家发起退自动同意维权
    @Scheduled(cron="0 0/58 * * * ?")
    public void autoAgreeOrderRefundLegal(){
        logger.info("买家发起退款申请若干天内未处理，系统将自动同意维权 处理开始...");
        OrderParamConfig orderParamConfig = shopBaseConf.getParamConfig(OrderParamConfig.class, "shopParam_orderParamConfig");
        BigDecimal dayRefundAutoAgree = orderParamConfig.getDayRefundAutoAgree();
        //得到需要自动同意维权的订单
        rightService.autoTuikuan(dayRefundAutoAgree);

        //TODO
        logger.info("买家发起退款申请若干天内未处理，系统将自动同意维权 处理完成...");
    }
    
    //买家发起退货退款申请若干天后未处理，系统将自动同意维权
    //@Scheduled(cron="0 0/56 * * * ?")
    public void autoAgreeOrderBack(){
        logger.info("买家发起退货退款申请若干天后未处理，系统将自动同意维权 处理开始...");
        OrderParamConfig orderParamConfig = shopBaseConf.getParamConfig(OrderParamConfig.class, "shopParam_orderParamConfig");
        BigDecimal dayBackAutoAgree = orderParamConfig.getDayBackAutoAgree();
        //TODO
        logger.info("买家发起退货退款申请若干天后未处理，系统将自动同意维权 处理完成...");
    }
    
    //买家维权退货若干天后未处理，系统将自动同意退款 
    //@Scheduled(cron="0 0/57 * * * ?")
    public void autoAgreeOrderReturn(){
        logger.info("买家维权退货若干天后未处理，系统将自动同意退款 处理开始...");
        OrderParamConfig orderParamConfig = shopBaseConf.getParamConfig(OrderParamConfig.class, "shopParam_orderParamConfig");
        BigDecimal dayReturnAutoAgree = orderParamConfig.getDayReturnAutoAgree();
        //TODO
        logger.info("买家维权退货若干天后未处理，系统将自动同意退款 处理完成...");
    }
    
    //卖家同意退货若干天后买家未处理，系统自动关闭维权，不可再次发起
    //@Scheduled(cron="0 0/58 * * * ?")
    public void autoCloseOrderLegalLimit(){
        logger.info("卖家同意退货若干天后买家未处理，系统自动关闭维权，不可再次发起 处理开始...");
        OrderParamConfig orderParamConfig = shopBaseConf.getParamConfig(OrderParamConfig.class, "shopParam_orderParamConfig");
        BigDecimal dayDealLegalLimit = orderParamConfig.getDayDealLegalLimit();
        //TODO
        logger.info("卖家同意退货若干天后买家未处理，系统自动关闭维权，不可再次发起 处理完成...");
    }
    
    //已收货订单若干天后自动评价，默认为好评，随机上传某条好评内容
    @Scheduled(cron="0 0/59 * * * ?")
    public void autoComment(){
        logger.info("已收货订单若干天后自动评价，默认为好评，随机上传某条好评内容  处理开始...");
        OrderParamConfig orderParamConfig = shopBaseConf.getParamConfig(OrderParamConfig.class, "shopParam_orderParamConfig");
        BigDecimal dayAutoComment = orderParamConfig.getDayAutoComment();
        
        Example example = new Example(Order.class);
        Criteria createCriteria = example.createCriteria();
        createCriteria.andCondition("SIGNING_TIME < DATE_SUB(NOW(), INTERVAL "+ dayAutoComment.multiply(new BigDecimal(3600*24)) +" SECOND)");
        createCriteria.andEqualTo("orderStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("order_status","order_rog")));
        createCriteria.andEqualTo("shipStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_receipt")));
        createCriteria.andEqualTo("evaluateStatus", 0);
        createCriteria.andEqualTo("deleteFlag", 0);
        List<Order> orderList = orderDao.selectByExample(example);
        for(Order order:orderList){
            commentService.autoOrderComment(order,dayAutoComment);
        }
        
        
        Example example1 = new Example(GiftOrder.class);
        Criteria createCriteria1 = example1.createCriteria();
        createCriteria1.andCondition("SIGNING_TIME < DATE_SUB(NOW(), INTERVAL "+ dayAutoComment.multiply(new BigDecimal(3600*24)) +" SECOND)");
        createCriteria1.andEqualTo("orderStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("order_status","order_rog")));
        createCriteria1.andEqualTo("shipStatus", Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_receipt")));
        createCriteria1.andEqualTo("evaluateStatus", 0);
        createCriteria1.andEqualTo("deleteFlag", 0);
        List<GiftOrder> giftOrderList = giftOrderDao.selectByExample(example1);
        for(GiftOrder giftOrder:giftOrderList){
            commentService.autoGiftOrderComment(giftOrder,dayAutoComment);
        }
        
        logger.info("已收货订单若干天后自动评价，默认为好评，随机上传某条好评内容 处理完成...");
    }
}
