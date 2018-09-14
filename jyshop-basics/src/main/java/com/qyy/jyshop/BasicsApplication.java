package com.qyy.jyshop;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
//import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import feign.Logger;


//可视化监控
//@EnableHystrixDashboard
//断路器支持
//@EnableCircuitBreaker
@EnableFeignClients
//@EnableDiscoveryClient
//@SpringBootApplication

//注解包含(@EnableCircuitBreaker,@EnableDiscoveryClient,@SpringBootApplication)
@SpringCloudApplication
public class BasicsApplication { 
	
	public static void main(String[] args) {
//	    args = new String[1];
//        args[0] = "--spring.profiles.active=hystrix-feign";
		new SpringApplicationBuilder(BasicsApplication.class).web(true).run(args);  
	}
	
	@Bean
	@LoadBalanced
	RestTemplate restTemplate() { 
		return new RestTemplate();
	}
	
	//日志级别,默认为none无日志输出
	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
	
	//使用fastjson做为json的解析器
//	@Bean
//    public HttpMessageConverters fastJsonHttpMessageConverters() {
//        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
//        fastConverter.setFastJsonConfig(fastJsonConfig);
//        HttpMessageConverter<?> converter = fastConverter;
//        return new HttpMessageConverters(converter);
//    }

}
