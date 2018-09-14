package com.qyy.jyshop.order.controller;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.model.GiftOrder;
import com.qyy.jyshop.order.service.GiftOrderService;
import com.qyy.jyshop.order.service.LogiService;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;

@RestController
public class GiftOrderController extends AppBaseController{

    @Autowired
    private GiftOrderService giftOrderService;
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
    @RequestMapping(value = "giftOrderCheckout")
    public Map<String,Object> orderCheckout(@RequestParam("goodsData")String goodsData,String token)throws Exception{
        
        Map<String,Object> returnMap=this.giftOrderService.getOrderCheckout(goodsData, token);
        return this.outMessage(0, "获取成功", returnMap);
    } 
    
    /**
     * 创建订单
     * @author hwc
     * @created 2017年12月20日 上午9:10:41
     * @param orderData
     * @param token
     * @return
     */
    @RequestMapping(value = "createGiftOrder")
    public Map<String,Object> createGoodsOrder(String goodsList,Long memberAddressId,String cartIds,String token){
        GiftOrder order=this.giftOrderService.doCreateOrder(goodsList, memberAddressId, cartIds, token);
        Map<String,Object> returnMap=new HashMap<String, Object>();
        returnMap.put("order", order);
        return this.outMessage(0, "创建订单成功", returnMap);
    }
    
    /***
     * 物流信息LogisticsInfo
     */
    @RequestMapping(value = "giftLogisticsInfo")
    public Map<String,Object> LogisticsInfo(Long orderId,String token)throws Exception{
        
        
        Map<String,Object> returnMap = logiService.queryLogiDistributionInfo(orderId,
                Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "gift_order")),token);
        
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
