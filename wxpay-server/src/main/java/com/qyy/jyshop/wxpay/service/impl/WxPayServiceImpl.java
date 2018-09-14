package com.qyy.jyshop.wxpay.service.impl;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.qyy.jyshop.dao.*;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.*;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;
import com.qyy.jyshop.wxpay.config.AppConfig;
import com.qyy.jyshop.wxpay.config.AppPayConfig;
import com.qyy.jyshop.wxpay.config.H5Config;
import com.qyy.jyshop.wxpay.config.WeixinPayConfig;
import com.qyy.jyshop.wxpay.feign.PayLogFeign;
import com.qyy.jyshop.wxpay.feign.ProductStoreFeign;
import com.qyy.jyshop.wxpay.service.WxPayService;
import com.qyy.jyshop.wxpay.util.*;
import com.qyy.jyshop.wxpay.util.WXPayConstants.SignType;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Service
public class WxPayServiceImpl extends AbstratService<Member> implements WxPayService{

    @Autowired
    private ProductStoreFeign productStoreFeign;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private PayOrderDao payOrderDao;
    @Autowired
    private GiftOrderDao giftOrderDao;
    @Autowired
    private BargainOrderDao bargainOrderDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private PayLogFeign payLogFeign;
    @Autowired
    private SpellOrderDao spellOrderDao;
    @Autowired
    private SpellDao spellDao;
    @Autowired
    private SpellActivityDao spellActivityDao;
    @Autowired
    private RightOrderDao rightOrderDao;
    @Autowired
    private H5Config h5Config;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private OrderItemsDao itemsDao;
    @Autowired
    private RightRelDao relDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductStoreDao productStoreDao;
    @Autowired
    private RightProcessDao processDao;

    @Override
    public Map<String, Object> getAppPayParams(BigDecimal payAmount, String orderSn,Integer orderType,String createIp, String token)throws Exception {
        
        if(StringUtil.isEmpty(orderSn))
            throw new AppErrorException("订单号为空,请求支付失败...");
        System.out.println("orderSn:"+orderSn);
//        payAmount=new BigDecimal(0.01);
        Map<String, String> payData = new HashMap<String, String>();
        payAmount=payAmount.multiply(new BigDecimal(100));
        payAmount=payAmount.setScale(0, BigDecimal.ROUND_DOWN);
        payData.put("appid",AppPayConfig.iosAppId);
        payData.put("mch_id",AppPayConfig.iosMchId);
        payData.put("nonce_str",WXPayUtil.generateNonceStr());
        payData.put("body","巨柚商城支付");
        payData.put("out_trade_no",orderSn);
        payData.put("total_fee",payAmount.toString());
        payData.put("spbill_create_ip",StringUtil.isEmpty(createIp)?"127.0.0.1": createIp);
        
        System.out.println("orderType:"+orderType);
        //普通订单支付回调地址
        if(Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "order")).equals(orderType)){
            payData.put("notify_url", AppPayConfig.orderNotifyUrl);
        //支付类订单回调地址
        }else if(Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "pay_order")).equals(orderType)){
            payData.put("notify_url", AppPayConfig.payNotifyUrl);
        }else if(Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "gift_order")).equals(orderType)){
            payData.put("notify_url", AppPayConfig.giftOrderNotifyUrl);
        }else if(Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "bargain_order")).equals(orderType)){
            payData.put("notify_url", WeixinPayConfig.bargainOrderNotifyUrl);
        }else if(Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "spell_order")).equals(orderType)){
            payData.put("notify_url", WeixinPayConfig.spellOrderNotifyUrl);
        }else{
            throw new AppErrorException("错误的订单类型....");
        }
        System.out.println("回调地址:"+payData.get("notify_url"));
        payData.put("trade_type","APP");
        
        String sign=WXPayUtil.generateSignature(payData, AppPayConfig.iosApiKey, SignType.MD5);  
        payData.put("sign", sign);
        
        Map<String,Object> params=this.getResMap(payData, AppPayConfig.payUrl);
        if(!StringUtil.isEmpty(params.get("result_code")) 
                && WXPayConstants.SUCCESS.equals(params.get("result_code").toString())){
            params.put("timestamp", String.valueOf(WXPayUtil.getCurrentTimestamp()));
            
            Map<String,String> payParam=new HashMap<String, String>();
            payParam.put("prepayid", params.get("prepay_id").toString());
            payParam.put("partnerid", params.get("mch_id").toString());
            payParam.put("appid", AppPayConfig.iosAppId);
            payParam.put("package", "Sign=WXPay");
            payParam.put("noncestr", params.get("nonce_str").toString());
            payParam.put("timestamp", params.get("timestamp").toString());
    
            String sign1=WXPayUtil.generateSignature(payParam, AppPayConfig.iosApiKey, SignType.MD5);  
            
            params.put("sign1",sign1);
        }
        return params;
        
    }
    
    @Override
    public Map<String,Object> getH5PayParams(BigDecimal payAmount,String orderSn,Integer orderType,String createIp, String token){
        
        try {
            
//            orderSn=String.valueOf(System.currentTimeMillis());
            Member member=this.getMember(token);
            String timestamp=String.valueOf(WXPayUtil.getCurrentTimestamp());
//            payAmount=new BigDecimal(0.01);
            payAmount=payAmount.multiply(new BigDecimal(100));
            payAmount=payAmount.setScale(0, BigDecimal.ROUND_DOWN);
            SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
            //wxb4868f50223328db wxf3c95148add7878f WeixinPayConfig.appId
            parameters.put("appid", WeixinPayConfig.appId); 
            //1226028202 1436779802 WeixinPayConfig.mchId
            parameters.put("mch_id", WeixinPayConfig.mchId);
            //oC550jrwIf9sc_cU-l62LKWLFIbo o0nqgwDTIlhf5Rpkb_jlH-VwJGx0 member.getOpenId()
            parameters.put("openid", member.getOpenId());
            parameters.put("nonce_str", WXPayUtil.generateNonceStr());
            parameters.put("body", "巨柚商城支付");// 购买支付信息
            parameters.put("out_trade_no", orderSn);// 订单号orderSn
            parameters.put("total_fee", payAmount.toString());// 总金额单位为分
            parameters.put("spbill_create_ip", StringUtil.isEmpty(createIp)?"127.0.0.1": createIp);
            
            System.out.println("orderType:"+orderType);
            //普通订单支付回调地址
            if(Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "order")).equals(orderType)){
                parameters.put("notify_url", WeixinPayConfig.orderNotifyUrl);
            //支付类订单回调地址
            }else if(Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "pay_order")).equals(orderType)){
                parameters.put("notify_url", WeixinPayConfig.payNotifyUrl);
            }else if(Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "gift_order")).equals(orderType)){
                parameters.put("notify_url", WeixinPayConfig.giftOrderNotifyUrl);
            }else if(Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "bargain_order")).equals(orderType)){
                parameters.put("notify_url", WeixinPayConfig.bargainOrderNotifyUrl);
            }else if(Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "spell_order")).equals(orderType)){
                parameters.put("notify_url", WeixinPayConfig.spellOrderNotifyUrl);
            }else{
                throw new AppErrorException("错误的订单类型....");
            }
            System.out.println("回调地址:"+parameters.get("notify_url"));
            
            
//            parameters.put("timestamp", timestamp);
            parameters.put("trade_type", "JSAPI");
            //12312312312321faieopqwireopwiqor QYY83qyy73qyy666qingyayuanxinxib WeixinPayConfig.apiKey
            String sign = this.createSign("UTF-8", parameters, WeixinPayConfig.apiKey);
            parameters.put("sign", sign);
            
            System.out.println("apiKey:"+WeixinPayConfig.apiKey);
            if(parameters!=null && parameters.size()>0){
                for (Map.Entry<Object, Object> entry : parameters.entrySet()) {
                    if(entry.getKey()!=null && entry.getKey().toString().trim().length()>0){
                      System.out.println(entry.getKey()+":" + entry.getValue() );
                    }
                }
            }
            String requestXML = getRequestXml(parameters);
            String resXml = UtilCommon.httpsRequest2(
                    WeixinPayConfig.payUrl, "POST", requestXML);
            Document doc = DocumentHelper.parseText(resXml);
            Element resEle = doc.getRootElement();
            Map<String,Object> params=XmlUtils.toMap(resEle);
            System.out.println(params.get("return_msg"));
            if(!StringUtil.isEmpty(params.get("result_code")) 
                    && WXPayConstants.SUCCESS.equals(params.get("result_code").toString())){
                

                String xml;
        
                    SortedMap<Object, Object> paras = new TreeMap<Object, Object>();
                    paras.put("appId", WeixinPayConfig.appId);
                    paras.put("timeStamp", timestamp);
                    paras.put("nonceStr", params.get("nonce_str"));
                    paras.put("package", "prepay_id=" + params.get("prepay_id"));
                    paras.put("signType", "MD5");
                    
                    String paySign = createSign("UTF-8", paras, WeixinPayConfig.apiKey);
                    paras.put("sign", paySign);
                    
                    xml = getRequestXml(paras);
                    
                    Document doc1 = DocumentHelper.parseText(xml);
                    Element resEle1 = doc1.getRootElement();
                    return XmlUtils.toMap(resEle1);
  
            }else{
                throw new AppErrorException(params.get("return_msg")==null?"未知错误":params.get("return_msg").toString());
            }
        } catch (Exception e) {
                System.out.println("获取预支付订单返回结果出错啦...");
                e.printStackTrace();
        }
        return null;
        
    }
    
    @Override
    @Transactional
    public void doOrderPayReturn(String strXml,Integer reqType){
        
        try {
            Map<String,String> resMap=WXPayUtil.xmlToMap(strXml);
            
            String returnCode="";
            
            if (resMap.containsKey("return_code")) {
                returnCode = resMap.get("return_code");
            }else {
                throw new Exception("解析状态码失败...");
            }
            
            if(returnCode.equals(WXPayConstants.SUCCESS)){
                if (!StringUtil.isEmpty(reqType)) {
                    boolean verifiStatus=false;
                    //h5
                    if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "h5")))){
                        verifiStatus=WXPayUtil.isSignatureValid(resMap, WeixinPayConfig.apiKey, SignType.MD5);
                    //ios
                    }else if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "ios_app")))){
                        verifiStatus=WXPayUtil.isSignatureValid(resMap, AppPayConfig.iosApiKey, SignType.MD5);
                    }
                    
                    String orderSn=resMap.get("out_trade_no");
                    if(verifiStatus && !StringUtil.isEmpty(orderSn)){
                        synchronized (orderSn) {
                            System.out.println("验证通过...");
                            Order order=this.orderDao.selectByOrderSnAndStatus(orderSn, 
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_not_pay")), null, null);
                            if(order!=null){
                                order.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_allocation")));
                                order.setPayStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")));
                                order.setShipStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_yes")));
                                order.setPaymentId(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "wxpay")));
                                order.setPaymentName(DictionaryUtil.getDataLabelByName("pay_type", "wxpay"));
                                order.setPaymentType("wxpay");
                                order.setPayMoney(StringUtil.isEmpty(resMap.get("total_fee"))?
                                        new BigDecimal(0):new BigDecimal(resMap.get("total_fee")).divide(new BigDecimal(100),2,BigDecimal.ROUND_DOWN));
                                order.setPaymentTime(new Timestamp(System.currentTimeMillis()));
                                
                                this.orderDao.updateOrderPayStatus(order.getOrderId(), 
                                        order.getOrderStatus(), //订单状态
                                        order.getPayStatus(), //支付状态
                                        order.getShipStatus(), //配送状态 
                                        order.getPaymentId(), //支付ID
                                        order.getPaymentName(),  //支付名称
                                        order.getPaymentType(), //支付类型
                                        order.getPayMoney(), //支付金额 
                                        order.getPaymentTime()); //支付时间
                                //修改用户为店长
//                                if(order.getOrderAmount().compareTo(new BigDecimal(200))>=0)
//                                    this.memberDao.updateMemberOfShopOwner(order.getMemberId(), 
//                                            new Timestamp(System.currentTimeMillis()));
                    
                                this.payLogFeign.addOrderPayLog(order);
                                this.productStoreFeign.editProductStore(order.getOrderId());
                            }
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    @Transactional
    public void doPayReturn(String strXml,Integer reqType){
        try {
            Map<String,String> resMap=WXPayUtil.xmlToMap(strXml);
            
            String returnCode="";
            
            if (resMap.containsKey("return_code")) {
                returnCode = resMap.get("return_code");
            }else {
                throw new Exception("解析状态码失败...");
            }
            
            if(returnCode.equals(WXPayConstants.SUCCESS)){
                if (!StringUtil.isEmpty(reqType)) {
                    
                    boolean verifiStatus=false;
                    if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "h5")))){
                        verifiStatus=WXPayUtil.isSignatureValid(resMap, WeixinPayConfig.apiKey, SignType.MD5);
                    }else if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "ios_app")))){
                        verifiStatus=WXPayUtil.isSignatureValid(resMap, AppPayConfig.iosApiKey, SignType.MD5);
                    }
                    
                    String paySn=resMap.get("out_trade_no");
                    
                    if(verifiStatus && !StringUtil.isEmpty(paySn)){
                        synchronized (paySn) {
                            PayOrder payOrder=this.payOrderDao.selectByPaySnAndPayStatus(paySn, 
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_no")));
                            if(payOrder!=null && !StringUtil.isEmpty(payOrder.getPayId())){
                                
                                payOrder.setPayTime(new Timestamp(System.currentTimeMillis()));
                                payOrder.setPayStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")));
                                payOrder.setPayAmount(StringUtil.isEmpty(resMap.get("total_fee"))?
                                        new BigDecimal(0):new BigDecimal(resMap.get("total_fee")));
                                payOrder.setPayAmount(payOrder.getPayAmount().divide(new BigDecimal(100),2,BigDecimal.ROUND_DOWN));
                                payOrder.setPaymentId(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "wxpay")));
                                payOrder.setPaymentName(DictionaryUtil.getDataLabelByName("pay_type", "wxpay"));
                                payOrder.setPaymentType("wxpay");
                                this.payOrderDao.updateByPrimaryKeySelective(payOrder);
                                
                                this.memberDao.updateMemberAdvanceOfAdd(payOrder.getMemberId(), payOrder.getOrderAmount());
                                
                                //修改用户为分润用户
                                if(payOrder.getOrderAmount().compareTo(new BigDecimal(200))>=0)
                                    this.memberDao.updateProfitMember(payOrder.getMemberId(), 
                                            new Timestamp(System.currentTimeMillis()));
                                this.payLogFeign.addPayLog(payOrder);
                            }
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    @Override
    @Transactional
    public void doBargainOrderReturn(String strXml,Integer reqType){
        
        try {
            Map<String,String> resMap=WXPayUtil.xmlToMap(strXml);
            
            String returnCode="";
            
            if (resMap.containsKey("return_code")) {
                returnCode = resMap.get("return_code");
            }else {
                throw new Exception("解析状态码失败...");
            }
            
            if(returnCode.equals(WXPayConstants.SUCCESS)){
                if (!StringUtil.isEmpty(reqType)) {
                    boolean verifiStatus=false;
                    //h5
                    if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "h5")))){
                        verifiStatus=WXPayUtil.isSignatureValid(resMap, WeixinPayConfig.apiKey, SignType.MD5);
                    //ios
                    }else if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "ios_app")))){
                        verifiStatus=WXPayUtil.isSignatureValid(resMap, AppPayConfig.iosApiKey, SignType.MD5);
                    }
                    
                    String orderSn=resMap.get("out_trade_no");
                    if(verifiStatus && !StringUtil.isEmpty(orderSn)){
                        synchronized (orderSn) {
                            System.out.println("验证通过...");
                            BargainOrder bargainOrder=this.bargainOrderDao.selectByOrderSnAndStatus(orderSn, 
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("bargain_order_status", "order_not_pay")));
                            if(bargainOrder!=null && !StringUtil.isEmpty(bargainOrder.getOrderId())){
                                BargainOrder updateBargainOrder=new BargainOrder();
                                updateBargainOrder.setOrderId(bargainOrder.getOrderId());
                                updateBargainOrder.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_allocation")));
                                updateBargainOrder.setPayStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")));
                                updateBargainOrder.setShipStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_yes")));
                                updateBargainOrder.setPaymentId(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "wxpay")));
                                updateBargainOrder.setPaymentName(DictionaryUtil.getDataLabelByName("pay_type", "wxpay"));
                                updateBargainOrder.setPaymentType("wxpay");
                                updateBargainOrder.setPayAmount(StringUtil.isEmpty(resMap.get("total_fee"))?
                                        new BigDecimal(0):new BigDecimal(resMap.get("total_fee")).divide(new BigDecimal(100),2,BigDecimal.ROUND_DOWN));
                                updateBargainOrder.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                                
                                this.bargainOrderDao.updateByPrimaryKeySelective(updateBargainOrder);
                                
                                this.memberDao.updateMemberOfShopOwner(bargainOrder.getMemberId(),
                                        new Timestamp(System.currentTimeMillis()));
                            }
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    @Override
    @Transactional
    public void doGiftOrderPayReturn(String strXml,Integer reqType){
        
        try {
            Map<String,String> resMap=WXPayUtil.xmlToMap(strXml);
            
            String returnCode="";
            
            if (resMap.containsKey("return_code")) {
                returnCode = resMap.get("return_code");
            }else {
                throw new Exception("解析状态码失败...");
            }
            
            if(returnCode.equals(WXPayConstants.SUCCESS)){
                if (!StringUtil.isEmpty(reqType)) {
                    boolean verifiStatus=false;
                    //h5
                    if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "h5")))){
                        verifiStatus=WXPayUtil.isSignatureValid(resMap, WeixinPayConfig.apiKey, SignType.MD5);
                    //ios
                    }else if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "ios_app")))){
                        verifiStatus=WXPayUtil.isSignatureValid(resMap, AppPayConfig.iosApiKey, SignType.MD5);
                    }
                    
                    String orderSn=resMap.get("out_trade_no");
                    if(verifiStatus && !StringUtil.isEmpty(orderSn)){
                        synchronized (orderSn) {
                            System.out.println("验证通过...");
                            GiftOrder giftOrder=this.giftOrderDao.selectByOrderSnAndStatus(orderSn, 
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_not_pay")), null, null);
                            if(giftOrder!=null){
                                GiftOrder updateOrder=new GiftOrder();
                                updateOrder.setOrderId(giftOrder.getOrderId());
                                updateOrder.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_allocation")));
                                updateOrder.setPayStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")));
                                updateOrder.setShipStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_yes")));
                                updateOrder.setPaymentId(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "wxpay")));
                                updateOrder.setPaymentName(DictionaryUtil.getDataLabelByName("pay_type", "wxpay"));
                                updateOrder.setPaymentType("wxpay");
                                updateOrder.setPayMoney(StringUtil.isEmpty(resMap.get("total_fee"))?
                                        new BigDecimal(0):new BigDecimal(resMap.get("total_fee")).divide(new BigDecimal(100),2,BigDecimal.ROUND_DOWN));
                                updateOrder.setPaymentTime(new Timestamp(System.currentTimeMillis()));
                                
                                this.giftOrderDao.updateByPrimaryKeySelective(updateOrder);
                                
                                this.memberDao.updateMemberOfShopOwner(giftOrder.getMemberId(), 
                                        new Timestamp(System.currentTimeMillis()));
                                
                                updateOrder.setOrderSn(giftOrder.getOrderSn());
                                updateOrder.setMemberId(giftOrder.getMemberId());
                            }
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void doSpellOrderReturn(String strXml,Integer reqType){
        try {
            Map<String,String> resMap=WXPayUtil.xmlToMap(strXml);

            String returnCode="";

            if (resMap.containsKey("return_code")) {
                returnCode = resMap.get("return_code");
            }else {
                throw new Exception("解析状态码失败...");
            }
            if(returnCode.equals(WXPayConstants.SUCCESS)){

                if (!StringUtil.isEmpty(reqType)) {
                    boolean verifiStatus=false;
                    //h5
                    if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "h5")))){
                        verifiStatus=WXPayUtil.isSignatureValid(resMap, WeixinPayConfig.apiKey, SignType.MD5);
                        //ios
                    }else if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "ios_app")))){
                        verifiStatus=WXPayUtil.isSignatureValid(resMap, AppPayConfig.iosApiKey, SignType.MD5);
                    }

                    String orderSn=resMap.get("out_trade_no");
                    if(verifiStatus && !StringUtil.isEmpty(orderSn)){
                        synchronized (orderSn) {
                            SpellOrder spellOrder = spellOrderDao.getOrderByOrderSn(orderSn);
                            Spell spell = spellDao.selectByPrimaryKey(spellOrder.getSpellId());
                            SpellActivity spellActivity = spellActivityDao.selectByPrimaryKey(spell.getActivityId());
                            Member member = memberDao.findMemerById(spellOrder.getMemberId());
                            Long nowDate = System.currentTimeMillis();
                            if(spell.getOriginatorId().equals(member.getMemberId())){
                                if(spellActivity.getStartDate().getTime() > nowDate || spellActivity.getStatus() == 0){
                                    throw new AppErrorException("活动未开始");
                                }
                                if(spellActivity.getEndDate().getTime() <= nowDate || spellActivity.getStatus() == 2){
                                    throw new AppErrorException("活动已结束");
                                }
                                spell.setStartDate(new Timestamp(nowDate));
                                Long endDate = nowDate + (spellActivity.getCycle() * 3600 * 1000);
                                if(endDate < spellActivity.getEndDate().getTime()){
                                    spell.setEndDate(new Timestamp(endDate));
                                }else{
                                    spell.setEndDate(spellActivity.getEndDate());
                                }
                                spell.setStatus(1);
                            }else{
                                if(spell.getStartDate() == null || spell.getStartDate().getTime() > nowDate || spell.getStatus() != 1){
                                    throw new AppErrorException("活动未开始");
                                }
                                if(spell.getEndDate().getTime() <= nowDate){
                                    throw new AppErrorException("活动已结束");
                                }
                                String memberStr = ";memberId=" + member.getMemberId() + ",nickname=" + member.getNickname() + ",face=" + member.getFace();
                                String participateDetails = spell.getParticipateDetails() + memberStr;
                                spell.setParticipateDetails(participateDetails);
                                spell.setParticipateNum(spell.getParticipateNum() + 1);
                                if(spell.getCompleteNum() <= spell.getParticipateNum()){
                                    spell.setStatus(2);
                                    spellActivity.setCompleteNum(spellActivity.getCompleteNum() + 1);
                                    spellActivityDao.updateByPrimaryKeySelective(spellActivity);
                                }
                            }
                            spellDao.updateByPrimaryKeySelective(spell);
                            spellOrder.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("spell_order_status", "order_allocation")));
                            spellOrder.setPayStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")));
                            spellOrder.setShipStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_yes")));
                            spellOrder.setPaymentId(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "advancepay")));
                            spellOrder.setPaymentName(DictionaryUtil.getDataLabelByName("pay_type", "advancepay"));
                            spellOrder.setPaymentType("advancepay");
                            spellOrder.setPayAmount(spellOrder.getOrderAmount());
                            spellOrder.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                            this.spellOrderDao.updateByPrimaryKeySelective(spellOrder);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, String> refundH5(Map<String, String> reqData)throws Exception{
        return refund(reqData,h5Config);
    }

    @Override
    public Map<String, String> refundApp(Map<String, String> reqData) throws Exception {
        return refund(reqData,appConfig);
    }

    private Map<String, String> refund(Map<String, String> reqData, WXPayConfig config) throws Exception {
        WXPay wxpay = new WXPay(config);
        return wxpay.refund(reqData);
    }

    @Override
    public void onRefundReturn(String resString, Integer reqType) {

    }

    private Map<String, Object> getResMap(Map<String, String> payData,String url)throws Exception{
        
        CloseableHttpResponse response = null;
        
        String payXml = WXPayUtil.mapToXml(payData);
        CloseableHttpClient client = null;
        HttpPost httpPost = new HttpPost(url);
        StringEntity entityParams = new StringEntity(payXml,"utf-8");
        httpPost.setEntity(entityParams);
        client = HttpClients.createDefault();
        response = client.execute(httpPost);
        
        return XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
    }
    
    
    private  String createSign(String characterEncoding,
            SortedMap<Object, Object> parameters, String api_key) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k)
                    && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + api_key);
        // System.out.println("请求参数拼接："+sb.toString());
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding)
                .toUpperCase();
        return sign;
    }
    
    
    public static String getRequestXml(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)
                    || "sign".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

}
