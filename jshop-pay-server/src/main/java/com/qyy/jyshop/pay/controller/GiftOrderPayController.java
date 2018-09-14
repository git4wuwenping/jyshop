package com.qyy.jyshop.pay.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.pay.feign.WxPayFeign;
import com.qyy.jyshop.pay.service.OrderService;
import com.qyy.jyshop.pay.util.XmlUtils;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;

@RestController
public class GiftOrderPayController extends AppBaseController{

    @Autowired
    private OrderService orderService;
    @Autowired
    private WxPayFeign wxPayFeign;
    
    protected HttpServletResponse response;
    protected HttpServletRequest request;
    
    @ModelAttribute
    public void setRes(HttpServletResponse response,HttpServletRequest request){
        this.response = response;
        this.request = request;
    }
    
    @RequestMapping(value = "giftOrderPay")
    public Map<String,Object> giftOrderPay(Long orderId,Integer payType,Integer reqType,String payPwd,String token){
        
        if(StringUtil.isEmpty(payType))
            return this.outMessage(1, "支付类型不能为空...", null);
        if(StringUtil.isEmpty(reqType))
            return this.outMessage(1, "请求类型不能为空...", null);
        
        if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "advancepay")))){
            this.orderService.doGiftOrderPay(orderId,payType,reqType,payPwd,token);
        }else if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "wxpay"))) || 
                payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "alipay")))){
            Map<String,Object> payParam=this.orderService.doGiftOrderPay(orderId, payType,reqType,payPwd,token);
            return this.outMessage(0, "获取成功", payParam);
        }else
            return this.outMessage(1, "无效的支付类型", null);
        return this.outMessage(0, "支付成功", null);
    }
    
    /**
     * 支付订单h5支付回调
     * @author hwc
     * @created 2018年1月22日 下午4:34:08
     * @param params
     */
    @RequestMapping(value = "h5GiftOrderPayReturn")
    public void h5OrderPayReturn(@RequestParam Map<String,Object> params){
        
        
        System.out.println("支付异步回调通知.....");
        String resString = XmlUtils.parseRequst(request);
        System.out.println("订单回调:"+resString);
        this.wxPayFeign.onGiftOrderPayReturn(resString,
                Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "h5"))); 
    }
    
    /**
     * app支付回调
     * @author hwc
     * @created 2018年1月9日 下午7:22:40
     * @param params
     */
    @RequestMapping(value = "appGiftOrderPayReturn")
    public void appGiftOrderPayReturn(@RequestParam Map<String,Object> params){
        
        System.out.println("支付异步回调通知.....");
        
        String resString = XmlUtils.parseRequst(request);
        System.out.println("订单回调:"+resString);
        this.wxPayFeign.onGiftOrderPayReturn(resString,
                Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "ios_app"))); 
    }

}
