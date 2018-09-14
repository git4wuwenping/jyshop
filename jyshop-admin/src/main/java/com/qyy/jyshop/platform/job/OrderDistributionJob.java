package com.qyy.jyshop.platform.job;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.qyy.jyshop.admin.order.service.OrderService;

//@Component
public class OrderDistributionJob {

	private final Log logger = LogFactory.getLog(OrderDistributionJob.class);
	
	@Autowired
	private OrderService orderService;
	
    @Scheduled(cron="0 0 0/12 * * ?")
    public void automaticReceipt() {  
        
        logger.info("开始拆单......");  
        
        List<Long> orderIdList=this.orderService.queryPayOrderId();
        if(orderIdList!=null && orderIdList.size()>0){
            for(Long orderId:orderIdList){
                this.orderService.automaticDistribution(orderId);
            }
        }
        
        logger.info("结束拆单......");  
    }
}
