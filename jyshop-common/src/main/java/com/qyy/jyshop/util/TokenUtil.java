package com.qyy.jyshop.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import com.thoughtworks.xstream.core.util.Base64Encoder;


public class TokenUtil {

	/**
	 * 生成token
	 * @param memberId 会员Id
	 * @return 
	 */
    public static String generateTokeCode(Long memberId){
        String value = System.currentTimeMillis()+new Random().nextInt()+memberId+"";
        System.out.println(value); 
       
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] b = md.digest(value.getBytes());
            //Base64编码
            Base64Encoder be = new Base64Encoder();
            be.encode(b);
            System.out.println(be.encode(b)); 
            return be.encode(b);//制定一个编码
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }
//    public static void main(String[] args) {
//    	TokenUtil.generateTokeCode(11L);
//    }
}
