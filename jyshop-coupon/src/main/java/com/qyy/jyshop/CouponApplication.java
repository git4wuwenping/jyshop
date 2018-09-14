package com.qyy.jyshop;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;

@SpringCloudApplication
public class CouponApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CouponApplication.class).web(true).run(args);
    }

}
