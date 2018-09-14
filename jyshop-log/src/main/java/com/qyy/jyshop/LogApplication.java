package com.qyy.jyshop;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class LogApplication {

	public static void main(String[] args) throws Exception{ 
		new SpringApplicationBuilder(LogApplication.class).web(true).run(args);
	}
}
