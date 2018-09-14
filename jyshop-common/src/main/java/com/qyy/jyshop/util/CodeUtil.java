package com.qyy.jyshop.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CodeUtil {

	//上线时在重新整理下订单号_waitUpdate
	/**
     * 生成支付订单号(前10位是日期,日期后面2位是固定值用于判断是什么支付方式与支付方法,中间4位随机数字,后面4位为会员手机号后四位)
     * @param uname 用户名
     * @param orderType 
     * @return 订单号
     */
    public static String createPayOrderSn(String mobile,String payType,String payment)throws Exception{
        if(StringUtil.isEmpty(mobile))
            throw new Exception("手机号不能为空...");
        if(mobile.length()!=11)
            throw new Exception("手机号格式错误...");
        if(StringUtil.isEmpty(payType))
            throw new Exception("支付类型不能为空...");
        if(StringUtil.isEmpty(payment))
            throw new Exception("支付方式不能为空...");
        
        Random random = new Random();
        Integer x = random.nextInt(899999)+100000;
        SimpleDateFormat time=new SimpleDateFormat("yyMMddHHmm");
        if(payType.equals(DictionaryUtil.getDataValueByName("pay_type", "cup"))){
        	 time=new SimpleDateFormat("yyMMdd");
        	 return (time.format(new Date())+payType+payment+x);
        }
        	mobile=mobile.substring(mobile.length()-4);
        return (time.format(new Date())+payType+payment+mobile+x);
    }
    
    /**
     * 生成商户号
     * @param payType
     * @param payment
     * @return 商户号
     */
    public static String createMerchantSn(String payType,String payment)throws Exception{
        if(StringUtil.isEmpty(payType))
            throw new Exception("支付类型不能为空...");
        if(StringUtil.isEmpty(payment))
            throw new Exception("支付方式不能为空...");
        
        Random random = new Random();
        Integer x = random.nextInt(899999)+100000;
        SimpleDateFormat time=new SimpleDateFormat("yyyyMMdd");
        return (time.format(new Date())+payType+payment+x);
    }
    
    /**
     * 生成流水号
     * @param payType
     * @param payment
     * @return 商户号
     */
    public static String createMsgSn(String type)throws Exception{
        if(StringUtil.isEmpty(type))
            throw new Exception("请求类型不能为空...");
        if(type.length()==1)
        	type="0"+type;
        Random random = new Random();
        Integer x = random.nextInt(899999)+100000;
        SimpleDateFormat time=new SimpleDateFormat("MMddHH");
        return (time.format(new Date())+type+x);
    }
    
    /**
     * 生成快捷支付流水号
     * @param type
     */
    public static String createHandyMsgSn(String type)throws Exception{
        if(StringUtil.isEmpty(type))
            throw new Exception("请求类型不能为空...");
        if(type.length()==1)
        	type="0"+type;
        Random random = new Random();
        Integer x = random.nextInt(899999)+100000;
        SimpleDateFormat time=new SimpleDateFormat("yyMMddHHmmss");
        return (time.format(new Date())+01+type+x);
    }
    
    /**
     * 生成APP支付订单号
     * @param mobile
     * @param payType
     * @param payment
     * @return
     * @throws Exception
     */
    public static String createAppPayOrderSn(String mobile,String payType,String payment)throws Exception{
        if(StringUtil.isEmpty(mobile))
            throw new Exception("手机号不能为空...");
        if(mobile.length()!=11)
            throw new Exception("手机号格式错误...");
        if(StringUtil.isEmpty(payType))
            throw new Exception("支付类型不能为空...");
        if(StringUtil.isEmpty(payment))
            throw new Exception("支付方式不能为空...");
        
        Random random = new Random();
        Integer x = random.nextInt(899999)+100000;
        SimpleDateFormat time=new SimpleDateFormat("MMddHHmm");
        mobile=mobile.substring(mobile.length()-4);
        return (time.format(new Date())+payType+payment+mobile+x);
    }
}
