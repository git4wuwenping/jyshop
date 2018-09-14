package com.qyy.jyshop.util;

public class LongUtil {

    public static Long[] stringArrayToLongArray(String[] strArray){
        if(strArray!=null && strArray.length>0){
            Long[] longArray = new Long[strArray.length];
            
            for (int i = 0; i < strArray.length; i++) {
                longArray[i] = Long.parseLong(strArray[i]);
            }
            
            return longArray;
        }else{
            return null;
        }
    }
}
