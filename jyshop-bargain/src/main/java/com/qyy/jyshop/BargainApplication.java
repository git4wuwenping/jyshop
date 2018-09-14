package com.qyy.jyshop;


import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import feign.Logger;

@EnableFeignClients
@SpringCloudApplication
public class BargainApplication { 
	
	public static void main(String[] args) {
	    new SpringApplicationBuilder(BargainApplication.class).web(true).run(args);  
	}
	
	//日志级别,默认为none无日志输出
	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
	
}
