package com.qyy.jyshop;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableFeignClients
@SpringCloudApplication
public class SpellApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpellApplication.class, args);
    }
}