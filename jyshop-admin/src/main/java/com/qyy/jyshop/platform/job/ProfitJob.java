package com.qyy.jyshop.platform.job;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.qyy.jyshop.admin.profit.service.ProfitService;
import com.qyy.jyshop.dao.GiftOrderDao;
import com.qyy.jyshop.dao.OrderDao;
import com.qyy.jyshop.dao.ProfitParamDao;
import com.qyy.jyshop.model.ProfitParam;
import com.qyy.jyshop.util.DictionaryUtil;

@Component
public class ProfitJob {
    private final Log logger = LogFactory.getLog(ProfitJob.class);
    public static final Integer ORDER_GIFT = 1;
    public static final Integer ORDER_COMMON = 0;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private GiftOrderDao giftOrderDao;
    @Autowired
    private ProfitParamDao profitParamDao;
    @Autowired
    private ProfitService profitService;
    
    /**
     * 每十分钟执行一次
     */
    @Scheduled(cron="0 0 0/8 * * ?")
    //@Scheduled(cron="0 0/2 * * * ?")
    public void doFirstProfitOrder() {  
        
        logger.info("TASK.1用户首次分润处理开始......");  
        //step1 处理礼包订单分润
        logger.info("TASK.1处理礼包订单分润开始......");  
        List<Map<String,Object>> giftOrderList=this.giftOrderDao.selectProfitOrder(
                Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_complete")), 
                Integer.valueOf(DictionaryUtil.getDataValueByName("profit_status", "profit_no")),0);
        if(giftOrderList!=null && giftOrderList.size()>0){
            ProfitParam profitParam=this.profitParamDao.selectAll().get(0);
            for (Map<String, Object> orderMap : giftOrderList) {
                this.profitService.doOrderFirstProfit(orderMap, profitParam,ProfitJob.ORDER_GIFT);
            }
        }
        logger.info("TASK.1处理礼包订单分润结束......");
        //step2 处理普通订单分润
        logger.info("TASK.1处理普通订单分润开始......");
        List<Map<String,Object>> orderList=this.orderDao.selectProfitOrder(
                Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_complete")), 
                Integer.valueOf(DictionaryUtil.getDataValueByName("profit_status", "profit_no")),
                new BigDecimal(200),0);
        
        if(orderList!=null && orderList.size()>0){
            ProfitParam profitParam=this.profitParamDao.selectAll().get(0);
            for (Map<String, Object> orderMap : orderList) {
                this.profitService.doOrderFirstProfit(orderMap, profitParam,ProfitJob.ORDER_GIFT);
            }
        }
        logger.info("TASK.1处理普通订单分润结束......");
        logger.info("TASK.1用户首次分润处理结束......");  
    }
    
    
    /**
     * 每五分钟执行一次
     */
    @Scheduled(cron="0 0 0/12 * * ?")
    //@Scheduled(cron="0 0/2 * * * ?")
    public void doProfitOrder() {  
        
        logger.info("TASK.2用户分润处理开始......");  
        //STEP1处理普通订单分润
        logger.info("TASK.2处理普通订单分润开始......");
        List<Map<String,Object>> orderList=this.orderDao.selectProfitOrder(
                Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_complete")), 
                Integer.valueOf(DictionaryUtil.getDataValueByName("profit_status", "profit_no")),
                new BigDecimal(0),1);
        
        if(orderList!=null && orderList.size()>0){
            ProfitParam profitParam=this.profitParamDao.selectAll().get(0);
            for (Map<String, Object> orderMap : orderList) {
                this.profitService.doOrderProfit(orderMap, profitParam,ProfitJob.ORDER_COMMON);
            }
        }
        logger.info("TASK.2处理普通订单分润结束......");
        //STEP2处理礼包订单分润
        logger.info("TASK.2处理礼包订单分润开始......");
        List<Map<String,Object>> giftOrderList=this.giftOrderDao.selectProfitOrder(
                Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_complete")), 
                Integer.valueOf(DictionaryUtil.getDataValueByName("profit_status", "profit_no")),1);
        
        if(giftOrderList!=null && giftOrderList.size()>0){
            ProfitParam profitParam=this.profitParamDao.selectAll().get(0);
            for (Map<String, Object> orderMap : giftOrderList) {
                this.profitService.doOrderProfit(orderMap, profitParam,ProfitJob.ORDER_GIFT);
            }
        }
        logger.info("TASK.2处理礼包订单分润结束......");
        logger.info("TASK.2用户分润处理结束......");  
    }
    
    
    /**
     * 每30分钟执行一次
     */ 
    @Scheduled(cron="0 0 0/11 * * ?")
    //@Scheduled(cron="0 0/2 * * * ?")
    public void doShopownerSubProfitOrder() {  
        
        logger.info("TASK.3店家下级分润处理开始......");  
        //STEP1处理普通订单分润
        logger.info("TASK.3处理普通订单分润开始......");
        List<Map<String,Object>> orderList=this.orderDao.selectShopownerSubProfitOrder(
                Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_complete")), 
                Integer.valueOf(DictionaryUtil.getDataValueByName("profit_status", "profit_no")));
        
        if(orderList!=null && orderList.size()>0){
            ProfitParam profitParam=this.profitParamDao.selectAll().get(0);
            for (Map<String, Object> orderMap : orderList) {
                this.profitService.doShopownerSubProfitOrder(orderMap, profitParam,ProfitJob.ORDER_COMMON);
            }
        }
        logger.info("TASK.3处理普通订单分润结束......");
        //STEP2处理礼包订单分润
        logger.info("TASK.3处理礼包订单分润开始......");
        List<Map<String,Object>> giftOrderList=this.giftOrderDao.selectShopownerSubProfitOrder(
                Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_complete")), 
                Integer.valueOf(DictionaryUtil.getDataValueByName("profit_status", "profit_no")));
        
        if(giftOrderList!=null && giftOrderList.size()>0){
            ProfitParam profitParam=this.profitParamDao.selectAll().get(0);
            for (Map<String, Object> orderMap : giftOrderList) {
                this.profitService.doShopownerSubProfitOrder(orderMap, profitParam,ProfitJob.ORDER_GIFT);
            }
        }
        logger.info("TASK.3处理礼包订单分润结束......");
        logger.info("TASK.3店家下级分润处理结束......");  
    }
}
