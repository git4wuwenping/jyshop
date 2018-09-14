package com.qyy.jyshop;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.qyy.jyshop.gateway.filter.SessionFilter;

@EnableZuulProxy
@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(GatewayApplication.class).web(true).run(args);
	}
	
//	@Bean
//	public SessionFilter sessionFilter() {
//		return new SessionFilter();
//	}
}
