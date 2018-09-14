package com.qyy.jyshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.qyy.jyshop.datasource.DynamicDataSourceRegister;

@SpringBootApplication
@MapperScan(basePackages = "com.qyy.jyshop.dao")
@Import(DynamicDataSourceRegister.class)
public class CommonApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(CommonApplication.class, args);
	}
}
