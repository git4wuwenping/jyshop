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
public class BargainOrderPayController extends AppBaseController{
    
    @Autowired
    private OrderService orderService;
    @Autowired
    private WxPayFeign wxPayFeign;
    
    protected HttpServletRequest request;
    
    @ModelAttribute
    public void setRes(HttpServletResponse response,HttpServletRequest request){
        this.request = request;
    }
    
    
    /**
     * 砍价支付
     * @author hwc
     * @created 2018年4月14日 下午3:57:46
     * @param orderId
     * @param payType
     * @param reqType
     * @param payPwd
     * @param token
     * @return
     */
    @RequestMapping(value = "bargainOrderPay")
    public Map<String,Object> bargainOrderPay(Long orderId,Integer payType,Integer reqType,String payPwd,String token){
        
        if(StringUtil.isEmpty(payType))
            return this.outMessage(0, "支付类型不能为空...", null);
        if(StringUtil.isEmpty(reqType))
            return this.outMessage(0, "请求类型不能为空...", null);

        if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "advancepay")))){
            if(StringUtil.isEmpty(payPwd))
                return this.outMessage(1, "支付密码不能为空...", null);
            this.orderService.doBargainOrderPay(orderId,payType,reqType,payPwd,token);
        }else if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "wxpay"))) || 
                payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "alipay")))){
            Map<String,Object> payParam=this.orderService.doBargainOrderPay(orderId, payType,reqType,null,token);
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
    @RequestMapping(value = "h5BargainOrderReturn")
    public void h5BargainOrderReturn(@RequestParam Map<String,Object> params){
        
        try{
            System.out.println("支付异步回调通知.....");
            String resString = XmlUtils.parseRequst(request);
            System.out.println("订单回调:"+resString);
            this.wxPayFeign.onBargainOrderReturn(resString,
                    Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "h5"))); 
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * app支付回调
     * @author hwc
     * @created 2018年1月9日 下午7:22:40
     * @param params
     */
    @RequestMapping(value = "appBargainOrderReturn")
    public void appBargainOrderReturn(@RequestParam Map<String,Object> params){
        
        System.out.println("支付异步回调通知.....");
        
        String resString = XmlUtils.parseRequst(request);
        System.out.println("订单回调:"+resString);
        this.wxPayFeign.onBargainOrderReturn(resString,
                Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "ios_app"))); 
    }

}
