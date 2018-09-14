package com.qyy.jyshop.wxpay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "h5")
public class WeixinPayConfig {

    public static String appId;//微信应用AppId
    public static String appSecret;//微信密钥
    public static String mchId; //微信支付商家ID
    public static String apiKey; //微信支付商家设置的密钥
    public static String encoding; //编码
    public static String payUrl; //支付请求地址
    public static String payNotifyUrl; //充值支付通知地址
    public static String orderNotifyUrl; //订单支付通知地址
    public static String giftOrderNotifyUrl; //订单支付通知地址
    public static String bargainOrderNotifyUrl; //砍价订单支付通知地址
    public static String spellOrderNotifyUrl; //拼团订单支付通知地址
    public static String refundNotifyUrl; //申请退款通知地址
    public static String signType; //加密方式
    
    public static String getAppId() {
        return appId;
    }
    public static void setAppId(String appId) {
        WeixinPayConfig.appId = appId;
    }
    public static String getAppSecret() {
        return appSecret;
    }
    public static void setAppSecret(String appSecret) {
        WeixinPayConfig.appSecret = appSecret;
    }
    public static String getMchId() {
        return mchId;
    }
    public static void setMchId(String mchId) {
        WeixinPayConfig.mchId = mchId;
    }
    public static String getApiKey() {
        return apiKey;
    }
    public static void setApiKey(String apiKey) {
        WeixinPayConfig.apiKey = apiKey;
    }
    public static String getEncoding() {
        return encoding;
    }
    public static void setEncoding(String encoding) {
        WeixinPayConfig.encoding = encoding;
    }
    public static String getPayUrl() {
        return payUrl;
    }
    public static void setPayUrl(String payUrl) {
        WeixinPayConfig.payUrl = payUrl;
    }
    public static String getPayNotifyUrl() {
        return payNotifyUrl;
    }
    public static void setPayNotifyUrl(String payNotifyUrl) {
        WeixinPayConfig.payNotifyUrl = payNotifyUrl;
    }
    public static String getOrderNotifyUrl() {
        return orderNotifyUrl;
    }
    public static void setOrderNotifyUrl(String orderNotifyUrl) {
        WeixinPayConfig.orderNotifyUrl = orderNotifyUrl;
    }
    public static String getSignType() {
        return signType;
    }
    public static void setSignType(String signType) {
        WeixinPayConfig.signType = signType;
    }
    public static String getGiftOrderNotifyUrl() {
        return giftOrderNotifyUrl;
    }
    public static void setGiftOrderNotifyUrl(String giftOrderNotifyUrl) {
        WeixinPayConfig.giftOrderNotifyUrl = giftOrderNotifyUrl;
    }
    public static String getBargainOrderNotifyUrl() {
        return bargainOrderNotifyUrl;
    }
    public static void setBargainOrderNotifyUrl(String bargainOrderNotifyUrl) {
        WeixinPayConfig.bargainOrderNotifyUrl = bargainOrderNotifyUrl;
    }

    public static String getSpellOrderNotifyUrl() {
        return spellOrderNotifyUrl;
    }

    public static void setSpellOrderNotifyUrl(String spellOrderNotifyUrl) {
        WeixinPayConfig.spellOrderNotifyUrl = spellOrderNotifyUrl;
    }

    public static String getRefundNotifyUrl() {
        return refundNotifyUrl;
    }

    public static void setRefundNotifyUrl(String refundNotifyUrl) {
        WeixinPayConfig.refundNotifyUrl = refundNotifyUrl;
    }
}
