package com.qyy.jyshop.order.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.controller.AppBaseController;


@RestController
public class LogiController extends AppBaseController{

    protected HttpServletResponse response;
    protected HttpServletRequest request;
    
    @ModelAttribute
    public void setRes(HttpServletResponse response,HttpServletRequest request){
        this.response = response;
        this.request = request;
    }
    

    @RequestMapping(value = "toLogiReturn")
    public void logiReturn(@RequestParam Map<String,Object> params){
        
        System.out.println("物流回调回调信息.....");
        if(params!=null && params.size()>0){
            System.out.println("物流回调回调信息11.....");
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if(entry.getKey()!=null && entry.getKey().trim().length()>0){
                    System.out.println("<"+entry.getKey()+">" + entry.getValue() + "</"+entry.getKey()+">");
                }
            }
        }
        
        Map orderMap = request.getParameterMap();
        params = new HashMap<String, Object>();

        Iterator orderIter = orderMap.keySet().iterator();
    
        while (orderIter.hasNext()) {
            Object key = orderIter.next();
            if (key == null)
                continue;
            String value = request.getParameter(key.toString());
            if (value != null) {
                params.put(key.toString(), value.toString());
            }
        }
        
        if(params!=null && params.size()>0){
            System.out.println("物流回调回调222.....");
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if(entry.getKey()!=null && entry.getKey().trim().length()>0){
                    System.out.println("1111<"+entry.getKey()+">" + entry.getValue() + "</"+entry.getKey()+">");
                }
            }
        }
    }
}
