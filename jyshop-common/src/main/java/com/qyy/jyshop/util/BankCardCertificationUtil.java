package com.qyy.jyshop.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 银行卡认证工具类
 * @author 22132
 *
 */
public class BankCardCertificationUtil {
	/**
	 * APISTORE_GET
	 * @param strUrl
	 * @param param
	 * @param appcode
	 * @return
	 */
	public static String APISTORE_GET(String strUrl, String param, String appcode) {
		
		String returnStr = null; // 返回结果定义
		URL url = null;
		HttpURLConnection httpURLConnection = null;
		try {
			url = new URL(strUrl + "?" + param);
			httpURLConnection = (HttpURLConnection) url.openConnection();			
			httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
			httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpURLConnection.setRequestProperty("Authorization", "APPCODE " + appcode);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestMethod("GET"); // get方式
			httpURLConnection.setUseCaches(false); // 不用缓存
			httpURLConnection.connect();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}

			reader.close();
			returnStr = buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
		return returnStr;
	}
	
	/*public static void test(String strUrl, String cardNum, String cardholder, String mobile,String appcode){
		String host = "https://aliyun-bankcard-verify.apistore.cn";
	    String path = "/bank";
	    String method = "GET";
	    Map<String, String> headers = new HashMap<String, String>();
	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE " + appcode);
	    Map<String, String> querys = new HashMap<String, String>();
	    querys.put("Mobile", mobile);
	    //querys.put("bankcard", "bankcard");
	    querys.put("cardNo", cardNum);
	    querys.put("realName", cardholder);

	    try {
	    	*//**
	    	* 重要提示如下:
	    	* HttpUtils请从
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
	    	* 下载
	    	*
	    	* 相应的依赖请参照
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
	    	*//*
	    	HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
	    	System.out.println(response.toString());
	    	//获取response的body
	    	return EntityUtils.toString(response.getEntity());
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	} */
}
