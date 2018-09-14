package com.qyy.jyshop.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    static final char SBC_CHAR_START = 65281; // 全角！  

    static final char SBC_CHAR_END = 65374; // 全角～  
    
    static final int CONVERT_STEP = 65248; // 全角半角转换间隔  
    
    static final char SBC_SPACE = 12288; // 全角空格 12288    
    
    static final char DBC_SPACE = ' '; // 半角空格 
    
	/**
     * 验证字符串是否为空
     * @param str 要验证的字符串
     * @return true为空 false不为空
     */
    public static boolean isEmpty(Object str) {
        
        if (str == null || "".equals(str.toString().trim()) || str.equals("\"\""))
            return true;
        String pattern = "\\S";
        Pattern p = Pattern.compile(pattern, 2 | Pattern.DOTALL);
        Matcher m = p.matcher(str.toString());
        return !m.find();
    }
    
    /**
     * 字符去掉空格
     * @param str
     * @return
     */
    public static String removeStrSpace(Object str){
    	if(str==null)
    		return "";
    	return str.toString().trim();
    }
    
    /**
     * 得到一个数字的大写(一到十之内)
     * @param num 要转义的数字
     * @return 数字的大写
     */
    public static String getChineseNum(int num) {
        String[] chineseNum = new String[] { "一", "二", "三", "四", "五", "六", "七","八", "九", "十" };
        return chineseNum[num];
    }
    
    /**
     * 保留小数点
     * @param val 数字
     * @param decimal 要保留的位数
     * @return 保留后的数字
     */
	public static double changeDouble(Double val, Integer decimal) {
		if (val == null)
			return 0D;
		String format = "#";
		if (decimal != null && decimal > 0) {
			for (int i = 0; i < decimal; i++) {
				if (i == 0) {
					format += ".0";
				} else {
					format += "0";
				}
			}
		}
		DecimalFormat df = new DecimalFormat(format);
		return Double.valueOf(df.format(val));
	}
    
	/**
	 * 生成固定位数的随机吗
	 * @param num 位数
	 * @return 
	 */
	public static String createNumStr(Integer num){
		if(StringUtil.isEmpty(num) || num<=0){
			return null;
		}
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < num; i++) {
			sb.append(random.nextInt(9));
		}
		return sb.toString();
	}
	
	/**
	 * md5加密
	 * @param str
	 * @return
	 */
	public static String md5(String str) {
		return md5(str,true);
	}
	
	public static String md5(String str, boolean zero) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
			return null;
		}
		byte[] resultByte = messageDigest.digest(str.getBytes());
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < resultByte.length; ++i) {
			int v = 0xFF & resultByte[i];
			if(v<16 && zero)
				result.append("0");
			result.append(Integer.toHexString(v));
		}
		return result.toString();
	}
	
	/**
	 * 判断是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){ 
		if(StringUtil.isEmpty(str))
			return false;
		Pattern pattern = Pattern.compile("[0-9]*"); 
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			return false; 
		} 
		return true; 
	}
	
	
	public static String[] objToStrArray(Object obj){
	    if(StringUtil.isEmpty(obj))
	        return null;
	    return obj.toString().split(",");
	}
	
	public static String strCover(Object strNum,int coverNum){
	    
	    if(StringUtil.isEmpty(strNum))
	        return null;
	    
	    int strLen =strNum.toString().length();  
        if (strLen <coverNum) {  
            while (strLen< coverNum) {  
                StringBuffer sb = new StringBuffer();  
                sb.append("0").append(strNum);//左补0   
                strNum= sb.toString();  
                strLen= strNum.toString().length();  
            }  
        }
        return strNum.toString();
	}
	
	public static boolean useLoop(String[] arr, String targetValue) {
	    for(String s: arr){
	        if(s.equals(targetValue))
	            return true;
	    }
	    return false;
	}
	
	
	public static String qj2bj(String src) {    
        if (src == null) {    
            return src;    
        }    
        StringBuilder buf = new StringBuilder(src.length());    
        char[] ca = src.toCharArray();    
        for (int i = 0; i < src.length(); i++) {    
            if (ca[i] >= SBC_CHAR_START && ca[i] <= SBC_CHAR_END) { // 如果位于全角！到全角～区间内    
                buf.append((char) (ca[i] - CONVERT_STEP));    
            } else if (ca[i] == SBC_SPACE) { // 如果是全角空格    
                buf.append(DBC_SPACE);    
            } else { // 不处理全角空格，全角！到全角～区间外的字符    
                buf.append(ca[i]);    
            }    
        }    
        return buf.toString();    
    } 
	
	public static void main(String[] args){
		System.out.println(StringUtil.isNumeric("1232a"));
	}
}
