package com.qyy.jyshop.alipay.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.qyy.jyshop.alipay.config.AlipayConfig;
import com.qyy.jyshop.alipay.feign.PayLogFeign;
import com.qyy.jyshop.alipay.feign.ProductStoreFeign;
import com.qyy.jyshop.alipay.service.AliPayService;
import com.qyy.jyshop.dao.GiftOrderDao;
import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.dao.OrderDao;
import com.qyy.jyshop.dao.PayOrderDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.GiftOrder;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.model.PayOrder;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;

@Service
public class AliPayServiceImpl  extends AbstratService<Member> implements AliPayService{

    @Autowired
    private ProductStoreFeign productStoreFeign;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private PayOrderDao payOrderDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private GiftOrderDao giftOrderDao;
    @Autowired
    private PayLogFeign payLogFeign;
    
    @Override
    public String getAppPayParams(BigDecimal payAmount, String orderSn, Integer orderType,
            String createIp, String token) throws Exception {
        
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", 
                AlipayConfig.APP_ID, 
                AlipayConfig.APP_PRIVATE_KEY, 
                "json", 
                AlipayConfig.CHARSET, 
                AlipayConfig.ALIPAY_PUBLIC_KEY, 
                "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        if(DictionaryUtil.getDataValueByName("order_type", "order").equals(orderType.toString()))
            model.setBody("巨柚商城_商品购买");
        else if(DictionaryUtil.getDataValueByName("order_type", "pay_order").equals(orderType.toString()))
            model.setBody("巨柚商城_充值");
        else if(DictionaryUtil.getDataValueByName("order_type", "gift_order").equals(orderType.toString()))
            model.setBody("巨柚商城_礼包购买");
        else
            throw new AppErrorException("错误的支付类型...");
        model.setSubject("巨柚商城");
        model.setOutTradeNo(orderSn);
        model.setTimeoutExpress("30m");
        model.setTotalAmount(payAmount.setScale(2, BigDecimal.ROUND_DOWN).toString());
        model.setProductCode("QUICK_MSECURITY_PAY");
//        ExtendParams ep=new ExtendParams();
//        ep.setHbFqNum(orderType.toString());
//        model.setExtendParams(ep);
        model.setPassbackParams(orderType.toString());
        request.setBizModel(model);
        request.setNotifyUrl("http://106.15.38.78:5555/pay/appAlipayOrderPayReturn");
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            System.out.println(response.getBody());
            return response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    @Transactional
    public void doPayReturn(Map<String,Object> returnMap){
        
        try {
            System.out.println("进来了...");
            if(returnMap!=null && returnMap.size()>0){
                for (Map.Entry<String, Object> entry : returnMap.entrySet()) {
                    if(entry.getKey()!=null && entry.getKey().trim().length()>0){
                      System.out.println(entry.getKey()+":" + entry.getValue() );
                    }
                }
            }
            Map<String,String> params = new HashMap<String,String>();
  
            System.out.println("赋值...");
            
            if(returnMap!=null && returnMap.size()>0){
                for (Map.Entry<String, Object> entry : returnMap.entrySet()) {
                    if(entry.getKey()!=null && entry.getKey().trim().length()>0){
                      System.out.println(entry.getKey()+":" + entry.getValue() );
                      params.put(entry.getKey(), String.valueOf(entry.getValue()));
                    }
                }
            }
//            
//            for (Iterator iter = returnMap.keySet().iterator(); iter.hasNext();) {
//                String name = (String) iter.next();
//                String[] values = (String[]) returnMap.get(name);
//                String valueStr = "";
//                for (int i = 0; i < values.length; i++) {
//                    valueStr = (i == values.length - 1) ? valueStr + values[i]
//                                : valueStr + values[i] + ",";
//                }
//                //乱码解决，这段代码在出现乱码时使用。
//                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
//                params.put(name, valueStr);
//            }
            
            
            if(params!=null && params.size()>0){
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if(entry.getKey()!=null && entry.getKey().trim().length()>0){
                      System.out.println(entry.getKey()+":" + entry.getValue() );
                    }
                }
            }
            
            boolean flag = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, "utf-8","RSA2");
            
            if(flag){
                
                if(DictionaryUtil.getDataValueByName("order_type", "order").equals(params.get("passback_params"))){
                
                    Order order=this.orderDao.selectByOrderSnAndStatus(params.get("out_trade_no"), 
                            Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_not_pay")), null, null);
                    if(order!=null){

                        order.setOrderId(order.getOrderId());
                        order.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_allocation")));
                        order.setPayStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")));
                        order.setShipStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_yes")));
                        order.setPaymentId(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "alipay")));
                        order.setPaymentName(DictionaryUtil.getDataLabelByName("pay_type", "alipay"));
                        order.setPaymentType("alipay");
                        order.setPayMoney(StringUtil.isEmpty(params.get("total_amount"))?
                                new BigDecimal(0):new BigDecimal(params.get("total_amount")));
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
                        
                        this.payLogFeign.addOrderPayLog(order);
                        this.productStoreFeign.editProductStore(order.getOrderId());
                    }
                }else if(DictionaryUtil.getDataValueByName("order_type", "gift_order").equals(params.get("passback_params"))){
                    GiftOrder giftOrder=this.giftOrderDao.selectByOrderSnAndStatus(params.get("out_trade_no"), 
                            Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_not_pay")), null, null);
                    if(giftOrder!=null){
                        GiftOrder updateOrder=new GiftOrder();
                        updateOrder.setOrderId(giftOrder.getOrderId());
                        updateOrder.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_allocation")));
                        updateOrder.setPayStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")));
                        updateOrder.setShipStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_yes")));
                        updateOrder.setPaymentId(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "alipay")));
                        updateOrder.setPaymentName(DictionaryUtil.getDataLabelByName("pay_type", "alipay"));
                        updateOrder.setPaymentType("alipay");
                        updateOrder.setPayMoney(StringUtil.isEmpty(params.get("total_amount"))?
                                new BigDecimal(0):new BigDecimal(params.get("total_amount")));
                        updateOrder.setPaymentTime(new Timestamp(System.currentTimeMillis()));
                        
                        this.giftOrderDao.updateByPrimaryKeySelective(updateOrder);
                        
                        this.memberDao.updateMemberOfShopOwner(giftOrder.getMemberId(), 
                                new Timestamp(System.currentTimeMillis()));
                        
                        updateOrder.setOrderSn(giftOrder.getOrderSn());
                        updateOrder.setMemberId(giftOrder.getMemberId());
                    }
                }else if(DictionaryUtil.getDataValueByName("order_type", "pay_order").equals(params.get("passback_params"))){
                    PayOrder payOrder=this.payOrderDao.selectByPaySnAndPayStatus(params.get("out_trade_no"),  
                            Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_no")));
                    if(payOrder!=null && !StringUtil.isEmpty(payOrder.getPayId())){
                        
                        payOrder.setPayTime(new Timestamp(System.currentTimeMillis()));
                        payOrder.setPayStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")));
                        payOrder.setPayAmount(StringUtil.isEmpty(params.get("total_amount"))?
                                new BigDecimal(0):new BigDecimal(params.get("total_amount")));
                        payOrder.setPayAmount(payOrder.getPayAmount().divide(new BigDecimal(100),2,BigDecimal.ROUND_DOWN));
                        payOrder.setPaymentId(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "alipay")));
                        payOrder.setPaymentName(DictionaryUtil.getDataLabelByName("pay_type", "alipay"));
                        payOrder.setPaymentType("alipay");
                        this.payOrderDao.updateByPrimaryKeySelective(payOrder);
                        
                        this.memberDao.updateMemberAdvanceOfAdd(payOrder.getMemberId(), payOrder.getOrderAmount());
                        
                        //修改用户为分润用户
                        if(payOrder.getOrderAmount().compareTo(new BigDecimal(200))>=0)
                            this.memberDao.updateProfitMember(payOrder.getMemberId(), 
                                    new Timestamp(System.currentTimeMillis()));
                        this.payLogFeign.addPayLog(payOrder);
                    }
                }else{
                    
                }
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        } 
//        catch (UnsupportedEncodingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        
    }

}
