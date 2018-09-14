package com.qyy.jyshop.pay.controller;

import java.math.BigDecimal;
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
import com.qyy.jyshop.pay.service.PayOrderService;
import com.qyy.jyshop.pay.util.XmlUtils;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;

@RestController
public class PayController extends AppBaseController{

    @Autowired
    private PayOrderService payOrderService;
    @Autowired
    private WxPayFeign wxPayFeign;
    
    protected HttpServletResponse response;
    protected HttpServletRequest request;
    
    @ModelAttribute
    public void setRes(HttpServletResponse response,HttpServletRequest request){
        this.response = response;
        this.request = request;
    }
    
    /**
     * 支付充值
     * @author hwc
     * @created 2018年1月11日 上午9:00:22
     * @param orderAmount
     * @param payType
     * @param token
     * @return
     */
    @RequestMapping(value = "pay")
    public Map<String,Object> pay(BigDecimal orderAmount,Integer payType,Integer reqType,String token){
        
        if(StringUtil.isEmpty(payType))
            return this.outMessage(0, "支付类型不能为空...", null);
        if(StringUtil.isEmpty(reqType))
            return this.outMessage(0, "请求类型不能为空...", null);
        
        if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "wxpay"))) || 
                payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "alipay")))){
            Map<String,Object> payParam=this.payOrderService.doPayRecharge(orderAmount, payType, reqType, token);
            return this.outMessage(0, "请求成功", payParam);
        }else
            return this.outMessage(1, "无效的支付类型", null);
    }
    
    /**
     * 支付订单app支付回调
     * @author hwc
     * @created 2018年1月9日 下午7:22:40
     * @param params
     */
    @RequestMapping(value = "h5PayReturn")
    public void h5PayReturn(@RequestParam Map<String,Object> params){
        
        System.out.println("支付异步回调通知h5.....");
        String resString = XmlUtils.parseRequst(request);
        System.out.println("充值回调:"+resString);
        this.wxPayFeign.onPayReturn(resString,
                Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "h5"))); 
    }
    
    /**
     * 支付订单app支付回调
     * @author hwc
     * @created 2018年1月9日 下午7:22:40
     * @param params
     */
    @RequestMapping(value = "appPayReturn")
    public void appPayReturn(@RequestParam Map<String,Object> params){
        
        System.out.println("支付异步回调通知.....");
        String resString = XmlUtils.parseRequst(request);
        System.out.println("充值回调:"+resString);
        this.wxPayFeign.onPayReturn(resString,
                Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "ios_app"))); 
    }
    
  
    
}
