package com.qyy.jyshop;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableFeignClients
@SpringCloudApplication
public class OrderApplication {

    public static void main(String[] args) {
        
        new SpringApplicationBuilder(OrderApplication.class).web(true).run(args);  
    }
}
