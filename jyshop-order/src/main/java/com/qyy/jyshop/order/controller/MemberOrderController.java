package com.qyy.jyshop.order.controller;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.order.service.OrderService;
import com.qyy.jyshop.pojo.PageAjax;

@RestController
public class MemberOrderController  extends AppBaseController{

    @Autowired
    private OrderService orderService;
    
    /**
     * 获取用户订单列表(分页)
     * @author hwc
     * @created 2017年12月20日 上午11:12:46
     * @param token 
     * @param orderType
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "pageOrder")
    public Map<String,Object> pageOrder(String token,Integer orderType,Integer pageNo,Integer pageSize) {
         
        PageAjax<Map<String,Object>> pageOrder=this.orderService.pageOrder(token,orderType,pageNo, pageSize);
        return this.outMessage(0, "获取成功", this.getPageMap(pageOrder));
    }
    
    /**
     * 获取用户订单详情
     * @author hwc
     * @created 2017年12月20日 下午3:15:10
     * @param token
     * @param orderId
     * @return
     */
    @RequestMapping(value = "orderDetails")
    public Map<String,Object> orderDetails(String token,Long orderId) {
         
        Map<String,Object> returnMap=this.orderService.queryOrderDetails(token,orderId);
        return this.outMessage(0, "获取成功", returnMap);
    }
    
    /**
     * 取消订单
     * @author hwc
     * @created 2017年12月20日 下午3:47:59
     * @param token
     * @param orderId
     */
    @RequestMapping(value = "orderCancel")
    public Map<String,Object> orderCancel(String token,Long orderId){
        this.orderService.doOrderCancel(token,orderId);
        return this.outMessage(0, "订单已取消", null);
    }
    
    /**
     * 确定收货 
     * @author hwc
     * @created 2017年12月20日 下午4:24:57
     * @param token
     * @param orderId
     */
    @RequestMapping(value = "orderRog")
    public Map<String,Object> orderRog(String token,Long orderId){
        this.orderService.doOrderRog(token,orderId);
        return this.outMessage(0, "收货成功", null);
    }
    
    /**
     * 删除订单
     * @author hwc
     * @created 2017年12月20日 下午4:25:07
     * @param token
     * @param orderId
     */
    @RequestMapping(value = "delOrder")
    public Map<String,Object> delOrder(String token,Long orderId){
        this.orderService.doDelOrder(token, orderId);
        return this.outMessage(0, "订单己删除", null);
    }
    
    
    @RequestMapping(value = "orderMaintain")
    public Map<String,Object> orderMaintain(String token,Long orderId){
        this.orderService.doOrderMaintain(token,orderId);
        return this.outMessage(0, "维权成功", null);
    }
    
    /**
     * 个人中心获取各类订单数量
     */
    @RequestMapping(value = "orderCount")
    public Map<String,Object> orderCount(String token){
        Map<String, Object> map = new HashedMap();
        map.put("orderCount", this.orderService.orderCount(token));
		return this.outMessage(0, "获取成功", map);
    }
}
