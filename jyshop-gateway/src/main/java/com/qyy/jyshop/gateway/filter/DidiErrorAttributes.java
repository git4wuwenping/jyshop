package com.qyy.jyshop.gateway.filter;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;

import java.util.Map;

//@Component
public class DidiErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes (
            RequestAttributes requestAttributes, boolean includeStackTrace){
    	System.out.println("in in in in in in");
        Map<String, Object> result = super.getErrorAttributes(requestAttributes, includeStackTrace);
        result.remove("exception");
        
        for (Map.Entry<String, Object> entry : result.entrySet()) {  
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        } 
        return result;
    }

}
