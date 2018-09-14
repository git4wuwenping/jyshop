package com.qyy.jyshop.pay.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.pay.feign.AliPayFeign;
import com.qyy.jyshop.pay.feign.WxPayFeign;
import com.qyy.jyshop.pay.service.OrderService;
import com.qyy.jyshop.pay.util.XmlUtils;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;

@RestController
public class OrderPayController extends AppBaseController{

    @Autowired
    private OrderService orderService;
    @Autowired
    private WxPayFeign wxPayFeign;
    @Autowired
    private AliPayFeign aliPayFeign;
    
    protected HttpServletResponse response;
    protected HttpServletRequest request;
    
    @ModelAttribute
    public void setRes(HttpServletResponse response,HttpServletRequest request){
        this.response = response;
        this.request = request;
    }
    
    /**
     * 订单支付
     * @author hwc
     * @created 2017年12月23日 上午10:01:21
     * @param orderId
     * @param payType
     * @param token
     * @return
     */
    @RequestMapping(value = "orderPay")
    public Map<String,Object> orderPay(Long orderId,Integer payType,Integer reqType,String payPwd,String token){
        
        if(StringUtil.isEmpty(payType))
            return this.outMessage(0, "支付类型不能为空...", null);
        if(StringUtil.isEmpty(reqType))
            return this.outMessage(0, "请求类型不能为空...", null);

        if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "advancepay")))){
            if(StringUtil.isEmpty(payPwd))
                return this.outMessage(1, "支付密码不能为空...", null);
            this.orderService.doOrderPay(orderId,payType,reqType,payPwd,token);
        }else if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "wxpay"))) || 
                payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "alipay")))){
            Map<String,Object> payParam=this.orderService.doOrderPay(orderId, payType,reqType,null,token);
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
    @RequestMapping(value = "h5OrderPayReturn")
    public void h5OrderPayReturn(@RequestParam Map<String,Object> params){
        
        
        System.out.println("支付异步回调通知.....");
        String resString = XmlUtils.parseRequst(request);
        System.out.println("订单回调:"+resString);
        this.wxPayFeign.onOrderPayReturn(resString,
                Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "h5"))); 
    }
    
    /**
     * app支付回调
     * @author hwc
     * @created 2018年1月9日 下午7:22:40
     * @param params
     */
    @RequestMapping(value = "appOrderPayReturn")
    public void appOrderPayReturn(@RequestParam Map<String,Object> params){
        
        System.out.println("支付异步回调通知.....");
        
        String resString = XmlUtils.parseRequst(request);
        System.out.println("订单回调:"+resString);
        this.wxPayFeign.onOrderPayReturn(resString,
                Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "ios_app"))); 
    }

    /**
     * 支付宝app支付回调
     * @author hwc
     * @created 2018年1月9日 下午7:22:40
     * @param params
     */
    @RequestMapping(value = "appAlipayOrderPayReturn")
    public void appAlipayOrderPayReturn(@RequestParam Map<String,Object> params){
        
        System.out.println("支付宝异步回调通知.....");
        if(params!=null && params.size()>0){
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if(entry.getKey()!=null && entry.getKey().trim().length()>0){
                  System.out.println(entry.getKey()+":" + entry.getValue() );
                }
            }
        }
//        params=new HashMap<String, Object>();
//        params.put("aa", "aaaaa");
        this.aliPayFeign.onAlipayReturn(params);
    }

    /**
     * 申请退款
     */
    @RequestMapping("anon/refund")
    public Map<String,String> refund(@RequestParam(value = "id")Long id,
                        @RequestParam(value = "outRefundNo")String outRefundNo,
                        @RequestParam(value = "refundFee")Integer refundFee,
                        @RequestParam(value = "totalFee")Integer totalFee,
                        @RequestParam(value = "type")String type) {
        return orderService.refund(id, type, outRefundNo, refundFee, totalFee);
    }

    /**
     * H5退款回调
     */
    @RequestMapping(value = "h5RefundReturn")
    public void onRefundReturn(){
        System.out.println("H5退款异步回调通知.....");
        String resString = XmlUtils.parseRequst(request);
        System.out.println("订单退款H5回调:"+resString);
        this.wxPayFeign.onRefundReturn(resString,
                Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "h5")));
    }

    /**
     * app退款回调
     */
    @RequestMapping(value = "appRefundReturn")
    public void appOrderPayReturn(){
        System.out.println("App退款异步回调通知.....");
        String resString = XmlUtils.parseRequst(request);
        System.out.println("订单退款App回调:"+resString);
        this.wxPayFeign.onRefundReturn(resString,
                Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "ios_app")));
    }

}
