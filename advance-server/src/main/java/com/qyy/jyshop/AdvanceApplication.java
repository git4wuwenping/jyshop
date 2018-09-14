package com.qyy.jyshop;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringCloudApplication
public class AdvanceApplication {

    public static void main(String[] args) {
        
        new SpringApplicationBuilder(AdvanceApplication.class).web(true).run(args);  
    }
}
