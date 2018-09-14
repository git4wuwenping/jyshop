package com.qyy.jyshop.bargain.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.bargain.service.BargainOrderService;
import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.util.StringUtil;

@RestController
public class BargainOrderController extends AppBaseController{
	
    @Autowired
	private BargainOrderService bargainOrderService;
	
	/**
	 * 我发起的砍价列表
	 */
	@RequestMapping(value = "bargainOrderList")
    public Map<String,Object> bargainOrderList(String token,Integer pageNo,Integer pageSize) {
		Map<String,Object> bargainOrderMap = bargainOrderService.selectBargainOrderList(token,pageNo,pageSize);
	        return this.outMessage(0, "获取成功", bargainOrderMap);
	}
	
	/**
	 * 我参与的砍价列表
	 */
	/*@RequestMapping(value = "participateBargainOrderList")
    public Map<String,Object> participateBargainOrderList(String token,Integer pageNo,Integer pageSize) {
		PageAjax<Map<String,Object>> participateBargainOrderList = bargainOrderService.selectParticipateBargainOrderList(token,pageNo,pageSize);
	        return this.outMessage(0, "获取成功", this.getPageMap(participateBargainOrderList));
	}*/
	
	/**
	 * 创建砍价订单
	 * @author hwc
	 * @created 2018年4月12日 下午4:58:24
	 * @param token
	 * @param bargainId
	 * @return
	 */
	@RequestMapping(value = "createBargainOrder")
    public Map<String,Object> createBargainOrder(String token,Long bargainId) {
	    try{
	        Map<String,Object> returnMap=this.bargainOrderService.doCreateBargainOrder(bargainId, token);
	        if(!StringUtil.isEmpty(returnMap.get("res_info"))){
	            return this.outMessage(3, returnMap.get("res_info").toString() , null);
	        }
	        return this.outMessage(0, "获取成功" , returnMap);
	    }catch(Exception ex){
	        return this.outMessage(1, ex.getMessage() , null);
	    }
    }

	/**
	 * 获取砍价详情
	 * @author hwc
	 * @created 2018年4月13日 上午10:22:57
	 * @param token
	 * @param bargainId
	 * @return
	 */
    @RequestMapping(value = "queryBargainDetail")
    public Map<String,Object> queryBargainDetail(String token,Long orderId) {
        try{
            return this.outMessage(0, "获取成功" , this.bargainOrderService.queryBargainDetail(orderId, token));
        }catch(Exception ex){
            return this.outMessage(1, ex.getMessage() , null);
        }
    }
    
    /**
     * 用户砍价
     * @author hwc
     * @created 2018年4月13日 下午2:53:39
     * @param token
     * @param orderId
     * @return
     */
    @RequestMapping(value = "memberBargain")
    public Map<String,Object> memberBargain(String token,Long orderId) {
        try{
            BigDecimal bargainPrice=this.bargainOrderService.doMemberBargain(orderId, token);
            Map<String,Object> returnMap=new HashMap<String,Object>();
            returnMap.put("bargainPrice", bargainPrice);
            return this.outMessage(0, "砍价成功" , returnMap);
        }catch(Exception ex){
            return this.outMessage(1, ex.getMessage() , null);
        }
    }
    
    /**
     * 砍价订单结算页
     * @author hwc
     * @created 2018年4月14日 上午9:58:26
     * @param token
     * @param productId
     * @param orderId
     * @return
     */
    @RequestMapping(value = "bargainOrderCheckout")
    public Map<String,Object> bargainOrderCheckout(String token,Long productId,Long orderId){
        
        try{
            return this.outMessage(0, "获取成功" , this.bargainOrderService.doBargainOrderCheckout(orderId,productId,token));
        }catch(Exception ex){
            return this.outMessage(1, ex.getMessage() , null);
        }
        
    }
    
    
    /**
     * 设置订单收货地址
     * @author hwc
     * @created 2018年4月14日 上午11:32:05
     * @param token
     * @param addrId
     * @param orderId
     * @return
     */
    @RequestMapping(value = "relationOrderAddress")
    public Map<String,Object> relationOrderAddress(String token,Long addrId,Long orderId){
        
        try{
            Map<String, Object> returnMap=new HashMap<String,Object>();
            returnMap.put("payAmount", this.bargainOrderService.doEditOrderAddress(token, addrId, orderId));
            return this.outMessage(0, "下单成功" , returnMap);
        }catch(Exception ex){
            ex.printStackTrace();
            return this.outMessage(1, ex.getMessage(), null);
        }
        
    }
    

    /**
     * 确定收货 
     * @author hwc
     * @created 2018年4月19日 下午3:50:53
     * @param token
     * @param orderId
     * @return
     */
    @RequestMapping(value = "bargainOrderRog")
    public Map<String,Object> bargainOrderRog(String token,Long orderId){
        this.bargainOrderService.doBargainOrderRog(token, orderId);
        return this.outMessage(0, "收货成功", null);
    }
    
	
}
