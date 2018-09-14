package com.qyy.jyshop.util;

import java.util.List;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {

    public static JSONObject getFromStrToJSONObject(String strFrom){
        
        if(strFrom==null || strFrom.trim().equals(""))
            return null;
        
        if(strFrom.substring(0,1).equals("\""))
            strFrom=strFrom.substring(1,strFrom.length()-1);
        strFrom=strFrom.replace("'", "\"").replace("&&", "&");
        String[] strArray = strFrom.split("&");
        
        String strJson="";
        
        for (int i = 0; i < strArray.length; i++) {
            String val = strArray[i];
            try {
                strJson += val.substring(0, val.indexOf("=")) + "='"
                        + java.net.URLDecoder.decode(val.substring( val.indexOf("=")+1), "UTF-8") + "',";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        strJson = "{"+strJson.substring(0,strJson.length()-1)+"}";
        JSONObject jsonObject = JSONObject.fromObject(strJson);
        
        return jsonObject;

    }
    
    public static String listToJson(List<?> list, String[] fields) throws Exception {
        if(fields != null && fields.length != 0) {
            JSONArray array = new JSONArray();
            for(Object bean:list) {
                JSONObject jo = new JSONObject();
                for(String field:fields) {
                    String valueGetter = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
                    jo.put(field, bean.getClass().getDeclaredMethod(valueGetter).invoke(bean));
                }
                array.add(jo);
            }
            return array.toString();
        } else {
            return JSONArray.fromObject(list).toString();
        }
    }
    
}
