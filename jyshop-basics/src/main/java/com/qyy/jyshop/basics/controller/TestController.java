/*
 * Copyright (c) 2005, 2017, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.basics.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.qyy.jyshop.basics.feign.OrderLogFeign;
import com.qyy.jyshop.basics.service.TestService;
import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.OrderLog;

import feign.FeignException;



/**
 * 描述
 * @author hwc
 * @created 2017年12月1日 下午4:35:44
 */
@RestController
public class TestController extends AppBaseController{

    @Autowired
    private OrderLogFeign orderLogFeign;
    @Autowired
    private RestTemplate restTemplate;
    
    
    @Autowired
    private TestService testService;
    
    @RequestMapping("queryTestList")
    public String queryTestList(){
//        try{
        testService.Test();
//        return null;
        //System.out.println(restTemplate.getForEntity("http://jyshop-log/queryOrderLogList", String.class).getBody());
//        }catch(AppErrorException a){
//            return "aaaaabbb";
//        }catch (HystrixRuntimeException e) {
//            System.out.println(e.getMessage());
//            return e.getCause().getLocalizedMessage();
//        }catch (Exception e) {
//            System.out.print(e.getClass());
//            return "aaaaabbb2222";
//        }
        return "1111111";
    }
      
}

