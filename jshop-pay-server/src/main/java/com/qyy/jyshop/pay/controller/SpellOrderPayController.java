package com.qyy.jyshop.pay.controller;

import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.pay.feign.WxPayFeign;
import com.qyy.jyshop.pay.service.OrderService;
import com.qyy.jyshop.pay.util.XmlUtils;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class SpellOrderPayController extends AppBaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private WxPayFeign wxPayFeign;

    protected HttpServletRequest request;

    @RequestMapping(value = "spellOrderPay")
    public Map<String,Object> spellOrderPay(Long orderId, Integer payType, Integer reqType, String payPwd, String token){

        if(StringUtil.isEmpty(payType))
            return this.outMessage(0, "支付类型不能为空...", null);
        if(StringUtil.isEmpty(reqType))
            return this.outMessage(0, "请求类型不能为空...", null);

        if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "advancepay")))){
            if(StringUtil.isEmpty(payPwd))
                return this.outMessage(1, "支付密码不能为空...", null);
            this.orderService.doSpellOrderPay(orderId,payType,reqType,payPwd,token);
        }else if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "wxpay"))) ||
                payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "alipay")))){
            Map<String,Object> payParam=this.orderService.doSpellOrderPay(orderId, payType,reqType,null,token);
            return this.outMessage(0, "获取成功", payParam);
        }else
            return this.outMessage(1, "无效的支付类型", null);
        return this.outMessage(0, "支付成功", null);
    }

    /**
     * 支付订单h5支付回调
     * @param params
     */
    @RequestMapping(value = "h5SpellOrderPayReturn")
    public void h5SpellOrderPayReturn(@RequestParam Map<String,Object> params){

        try{
            System.out.println("支付异步回调通知.....");
            String resString = XmlUtils.parseRequst(request);
            System.out.println("订单回调:"+resString);
            this.wxPayFeign.onSpellOrderReturn(resString,
                    Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "h5")));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * app支付回调
     * @param params
     */
    @RequestMapping(value = "appSpellOrderPayReturn")
    public void appSpellOrderPayReturn(@RequestParam Map<String,Object> params){

        System.out.println("支付异步回调通知.....");

        String resString = XmlUtils.parseRequst(request);
        System.out.println("订单回调:"+resString);
        this.wxPayFeign.onSpellOrderReturn(resString,
                Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "ios_app")));
    }
}
