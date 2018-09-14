package com.qyy.jyshop.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class HtmlUtils {

	public static String getKeyValueStr(Map<String, String> params){
        StringBuffer content = new StringBuffer();
        // 按照key做首字母升序排列
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < keys.size(); i++)
        {
            String key = (String) keys.get(i);
            String value = params.get(key);
            content.append((i == 0 ? "" : "&") + key + "=" + value);
        }
        String signSrc = content.toString();
        if (signSrc.startsWith("&"))
        {
            signSrc = signSrc.replaceFirst("&", "");
        }
        return signSrc;
	}
	public static String doPostData(String url,String xmlData)throws Exception{
		String returnXml = "";
		try{
			// 发送报文
			URL sendUrl = new URL(url.trim());
			URLConnection connection = sendUrl.openConnection();
		   
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			connection.setDoOutput(true);
		   
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			out.write(xmlData);
			out.flush();
			out.close();
	
			// 一旦发送成功，用以下方法就可以得到服务器的回应：
			String sCurrentLine = "";
			BufferedReader l_reader = new BufferedReader(new InputStreamReader(
		   			connection.getInputStream(),"UTF-8"));
		   	while ((sCurrentLine = l_reader.readLine()) != null) {
		   		returnXml += sCurrentLine + "\r\n";
		   	}
		   	l_reader.close();
		}catch(Exception e){
			throw new Exception(e.getMessage(),e);
		}
	  	return returnXml;
	}
	public static String doGetData(String url)throws Exception{
		String resultStr = "";
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,	new DefaultHttpMethodRetryHandler());
		try{
		    int statusCode = httpClient.executeMethod(getMethod);
		    if (statusCode != HttpStatus.SC_OK) {
		    	throw new Exception("请求出错: "+ getMethod.getStatusLine());
		    }
		    String sCurrentLine = "";
			BufferedReader l_reader = new BufferedReader(new InputStreamReader(
					getMethod.getResponseBodyAsStream(),"UTF-8"));
		   	while ((sCurrentLine = l_reader.readLine()) != null) {
		   		resultStr += sCurrentLine + "\r\n";
		   	}
    	}catch(Exception e){
    		throw new Exception(e.getMessage(),e);
	    }finally{
    		getMethod.releaseConnection();
	    }
	    return resultStr;
    }
	
	//无循环体获取
	public static Map<Object,Object> getRequestMapParseXml(String resXml){
        Document dd = null;
		try {
			dd = DocumentHelper.parseText(resXml);
		} catch (DocumentException e) {
			return null; 
		}
        Map<Object,Object> map = new HashMap<Object,Object>();
		if (dd != null) {
			Element root = dd.getRootElement();
			if (root == null) {
				return null;
			}
			List list = root.elements();
			if(list==null || list.size()<=0){
				return null;
			}
	        Iterator it = list.iterator();
	        while(it.hasNext()) {
	            Element e = (Element) it.next();
	            String k = e.getName();
	            String v = e.getText();
	            map.put(k, v);
	        }
		}
        return map;
	}
	//获取多循环体
	public static Map<String,Object> getXmlMap(String xmlData){
		Document dd = null;
		try {
			dd = DocumentHelper.parseText(xmlData);
		} catch (DocumentException e) {
			return null; 
		}
		return Dom2Map(dd);
	}
	private static Map<String, Object> Dom2Map(Document doc){  
        Map<String, Object> map = new HashMap<String, Object>();  
        if(doc == null)  
            return null;  
        Element root = doc.getRootElement();  
        for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {  
            Element e = (Element) iterator.next();  
            List list = e.elements();  
            if(list.size() > 0){  
            	if(map.get(e.getName())!=null){
            		List<Map> mapList = (List)map.get(e.getName());
            		mapList.add(Dom2Map(e));
            		map.put(e.getName(), mapList);
            	}else{
            		List<Map> mapList = new ArrayList<Map>();
            		mapList.add(Dom2Map(e));
            		map.put(e.getName(), mapList);  
            	}
            }else  
                map.put(e.getName(), e.getText());  
        }  
        return map;  
    }  
    private static Map Dom2Map(Element e){  
        Map map = new HashMap();  
        List list = e.elements();  
        if(list.size() > 0){  
            for (int i = 0;i < list.size(); i++) {  
                Element iter = (Element) list.get(i);  
                List mapList = new ArrayList();  
                  
                if(iter.elements().size() > 0){  
                    Map m = Dom2Map(iter);  
                    if(map.get(iter.getName()) != null){  
                        Object obj = map.get(iter.getName());  
                        if(!obj.getClass().getName().equals("java.util.ArrayList")){  
                            mapList = new ArrayList();  
                            mapList.add(obj);  
                            mapList.add(m);  
                        }  
                        if(obj.getClass().getName().equals("java.util.ArrayList")){  
                            mapList = (List) obj;  
                            mapList.add(m);  
                        }  
                        map.put(iter.getName(), mapList);  
                    }else  
                        map.put(iter.getName(), m);  
                }  
                else{  
                    if(map.get(iter.getName()) != null){  
                        Object obj = map.get(iter.getName());  
                        if(!obj.getClass().getName().equals("java.util.ArrayList")){  
                            mapList = new ArrayList();  
                            mapList.add(obj);  
                            mapList.add(iter.getText());  
                        }  
                        if(obj.getClass().getName().equals("java.util.ArrayList")){  
                            mapList = (List) obj;  
                            mapList.add(iter.getText());  
                        }  
                        map.put(iter.getName(), mapList);  
                    }else  
                        map.put(iter.getName(), iter.getText());  
                }  
            }  
        }else  
            map.put(e.getName(), e.getText());  
        return map;  
    }
}
