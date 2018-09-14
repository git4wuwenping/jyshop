package com.qyy.jyshop.order.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.order.service.LogiService;
import com.qyy.jyshop.order.service.OrderService;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;

@RestController
public class OrderController extends AppBaseController{

    @Autowired
    private OrderService orderService;
    @Autowired
    private LogiService logiService;
    
    /**
     * 获取订单结算数据
     * @author hwc
     * @created 2017年12月11日 下午4:09:19
     * @param goodsData
     * @param token
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "orderCheckout")
    public Map<String,Object> orderCheckout(@RequestParam("goodsData")String goodsData,String token)throws Exception{
        
        Map<String,Object> returnMap=this.orderService.getOrderCheckout(goodsData, token);
        return this.outMessage(0, "获取成功", returnMap);
    } 
    
    /**
     * 计算邮费
     * @author hwc
     * @created 2018年2月6日 上午11:23:47
     * @param goodsList
     * @param memberAddressId
     * @param token
     * @return
     */
    @RequestMapping(value = "computeOrderFreight")
    public Map<String,Object> onComputeOrderFreight(String goodsList,Long memberAddressId,String token){
        List<Map<String,Object>> freightList=this.orderService.computeOrderFreight(goodsList, memberAddressId,token);
        if(freightList!=null && freightList.size()>0){
            Map<String,Object> returnMap=new HashMap<String,Object>();
            returnMap.put("freightList", freightList);
            return this.outMessage(0, "计算成功", returnMap);
        }else{ 
            return this.outMessage(1, "计算失败", null);
        }
    }
    
    /**
     * 创建订单
     * @author hwc
     * @created 2017年12月20日 上午9:10:41
     * @param orderData
     * @param token
     * @return
     */
    @RequestMapping(value = "createGoodsOrder")
    public Map<String,Object> createGoodsOrder(String goodsList,Long memberAddressId,String cartIds,
            String memberRealName,String memberCardId,String token){
        Order order=this.orderService.doCreateOrder(goodsList, memberAddressId, cartIds,memberRealName,memberCardId,token);
        Map<String,Object> returnMap=new HashMap<String, Object>();
        returnMap.put("order", order);
        return this.outMessage(0, "创建订单成功", returnMap);
    }

    
    
    /***
     * 物流信息LogisticsInfo
     */
    @RequestMapping(value = "LogisticsInfo")
    public Map<String,Object> LogisticsInfo(Long orderId,String token,int type)throws Exception{
    	String orderType = null;
    	if(type==0){	//普通订单
    		orderType = "order";
    	}else if(type==1){
    		orderType = "pay_order";
    	}else if(type==2){
    		orderType = "gift_order";
    	}else if(type == 3){
    		orderType = "right_order";
    	}else if(type==4){	//砍价订单
    		orderType = "bargain_order";
    	}else if(type==5){	//拼团订单
            orderType = "spell_order";
        }
    	
    	Map<String,Object> returnMap = logiService.queryLogiDistributionInfo(orderId,
    	        Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", orderType)),token);
    	if(returnMap!=null && !StringUtil.isEmpty(returnMap.get("resp"))){
    	    JSONObject goodsJson= JSONObject.fromObject(returnMap.get("resp"));

    	    if(goodsJson.getInt("status")==200){
    	        returnMap.put("logiInfo", goodsJson.getJSONArray("data"));
    	        return this.outMessage(0, "获取成功", returnMap);
    	    }
    	}
        return this.outMessage(1, "获取失败", null);
    }
}
