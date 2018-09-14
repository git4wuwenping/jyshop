package com.qyy.jyshop;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableFeignClients
@SpringCloudApplication
public class QueueApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(QueueApplication.class).web(true).run(args);  
    }
}
 