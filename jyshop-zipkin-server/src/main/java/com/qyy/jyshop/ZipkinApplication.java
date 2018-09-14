package com.qyy.jyshop;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;

import zipkin.server.EnableZipkinServer;

//@EnableZipkinServer
@EnableZipkinStreamServer
@SpringBootApplication
public class ZipkinApplication { 
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(ZipkinApplication.class).web(true).run(args);  
	}

}
