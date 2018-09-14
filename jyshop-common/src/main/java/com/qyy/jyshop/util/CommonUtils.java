package com.qyy.jyshop.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class CommonUtils {

	public static String getBasePath(HttpServletRequest request){
		String path = request.getContextPath();
        int port = request.getServerPort();
        String basePath = request.getScheme() + "://" + request.getServerName() + (port == 80 ? "" : ":" + port) + path + "/";
        return basePath;
	}
	 /**
	  * 获取本机Ip 
	  *  通过 获取系统所有的networkInterface网络接口 然后遍历 每个网络下的InterfaceAddress组。
	  *  获得符合 <code>InetAddress instanceof Inet4Address</code> 条件的一个IpV4地址
	  */
	@SuppressWarnings("rawtypes")
	public static String localIp(){
		String ip = null;
		Enumeration allNetInterfaces;
		try {
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();            
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				List<InterfaceAddress> InterfaceAddress = netInterface.getInterfaceAddresses();
				for (InterfaceAddress add : InterfaceAddress) {
					InetAddress Ip = add.getAddress();
					if (Ip != null && Ip instanceof Inet4Address) {
						ip = Ip.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return ip;
	}
	public static String createTimestampRandom() {
		int length = 3;
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		String randomStr = generateLenString(length);
		return timestamp+randomStr;
	}
	/**
	 * 生成随机字符串
	 */
	public static String createNoncestr(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";//含有字符和数字的字符串
		Random random = new Random();//随机类初始化
		StringBuffer sb = new StringBuffer();//StringBuffer类生成，为了拼接字符串

		for (int i = 0; i < length; ++i) {
			int number = random.nextInt(62);// [0,62)

			sb.append(str.charAt(number));
		}
		return sb.toString();
	}
	/**
	 * 生成指定长度的随机字符串
	 * 
	 * @param length
	 *            指定字符串长度
	 * @return
	 */
	public static String generateLenString(int length) {
		char[] cResult = new char[length];
		int[] flag = { 0, 0, 0 }; // A-Z, a-z, 0-9
		int i = 0;
		while (flag[0] == 0 || flag[1] == 0 || flag[2] == 0 || i < length) {
			i = i % length;
			int f = (int) (Math.random() * 3 % 3);
			if (f == 0)
				cResult[i] = (char) ('A' + Math.random() * 26);
			else if (f == 1)
				cResult[i] = (char) ('a' + Math.random() * 26);
			else
				cResult[i] = (char) ('0' + Math.random() * 10);
			flag[f] = 1;
			i++;
		}
		return new String(cResult);
	}
	public static String starfyMobile(String mobile, int start, int end) {
	    if (StringUtils.isNotBlank(mobile)) {
	        String str1 = mobile.substring(0, start) + "";
	        String str2 = "";
	        for (int i = 0, len = end - start; i <= len; i++) {
                str2 += "*";
            }
	        String str3 = mobile.substring(end, mobile.length() -1);
	        return str1 + str2 + str3;
	    }
	    return "";
	}
	
	/**
	 * str空判断
	 * 
	 * @param str
	 * @return
	 * @author guoyx
	 */
	public static boolean isnull(String str) {
		if (null == str || str.equalsIgnoreCase("null")
				|| str.trim().equals("")) {
			return true;
		} else
			return false;
	}
	
	/**
     * 获取随机N位数字的字符串
     * @param n
     * @return
     */
    public static String getRandStr(int n) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < n; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sb.append(rand);
        }
        return sb.toString();
    }
}
