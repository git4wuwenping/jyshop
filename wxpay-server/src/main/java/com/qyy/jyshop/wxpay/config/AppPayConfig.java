package com.qyy.jyshop.wxpay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "apppay")
public class AppPayConfig {

    public static String iosAppId; //微信应用AppId
    public static String iosAppSecret; //微信密钥
    public static String iosMchId; //微信支付商家ID
    public static String iosApiKey; //微信支付商家设置的密钥
    public static String payUrl; //支付请求地址
    public static String signType; //加密方式 
    public static String encoding; //编码
    public static String payNotifyUrl; //充值支付通知地址
    public static String orderNotifyUrl; //订单支付通知地址
    public static String giftOrderNotifyUrl; //礼包订单支付通知地址
    public static String bargainOrderNotifyUrl; //砍价订单支付通知地址
    public static String refundNotifyUrl; //申请退款通知地址

    public static String getIosAppId() {
        return iosAppId;
    }
    public static void setIosAppId(String iosAppId) {
        AppPayConfig.iosAppId = iosAppId;
    }
    public static String getIosAppSecret() {
        return iosAppSecret;
    }
    public static void setIosAppSecret(String iosAppSecret) {
        AppPayConfig.iosAppSecret = iosAppSecret;
    }
    public static String getIosMchId() {
        return iosMchId;
    }
    public static void setIosMchId(String iosMchId) {
        AppPayConfig.iosMchId = iosMchId;
    }
    public static String getIosApiKey() {
        return iosApiKey;
    }
    public static void setIosApiKey(String iosApiKey) {
        AppPayConfig.iosApiKey = iosApiKey;
    }
    public static String getEncoding() {
        return encoding;
    }
    public static void setEncoding(String encoding) {
        AppPayConfig.encoding = encoding;
    }
    public static String getPayUrl() {
        return payUrl;
    }
    public static void setPayUrl(String payUrl) {
        AppPayConfig.payUrl = payUrl;
    }
    public static String getSignType() {
        return signType;
    }
    public static void setSignType(String signType) {
        AppPayConfig.signType = signType;
    }
    public static String getPayNotifyUrl() {
        return payNotifyUrl;
    }
    public static void setPayNotifyUrl(String payNotifyUrl) {
        AppPayConfig.payNotifyUrl = payNotifyUrl;
    }
    public static String getOrderNotifyUrl() {
        return orderNotifyUrl;
    }
    public static void setOrderNotifyUrl(String orderNotifyUrl) {
        AppPayConfig.orderNotifyUrl = orderNotifyUrl;
    }
    public static String getGiftOrderNotifyUrl() {
        return giftOrderNotifyUrl;
    }
    public static void setGiftOrderNotifyUrl(String giftOrderNotifyUrl) {
        AppPayConfig.giftOrderNotifyUrl = giftOrderNotifyUrl;
    }
    public static String getBargainOrderNotifyUrl() {
        return bargainOrderNotifyUrl;
    }
    public static void setBargainOrderNotifyUrl(String bargainOrderNotifyUrl) {
        AppPayConfig.bargainOrderNotifyUrl = bargainOrderNotifyUrl;
    }

    public static String getRefundNotifyUrl() {
        return refundNotifyUrl;
    }

    public static void setRefundNotifyUrl(String refundNotifyUrl) {
        AppPayConfig.refundNotifyUrl = refundNotifyUrl;
    }
}
