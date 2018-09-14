 package com.qyy.jyshop;

import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@SpringBootApplication 
@EnableCaching
@EnableTransactionManagement
@ComponentScan
@EnableAutoConfiguration 
@EnableScheduling
public class AdminApplication extends SpringBootServletInitializer{  
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AdminApplication.class);
	}
	
	public static void main(String[] args)throws Exception {
	  	    
	    SpringApplication.run(AdminApplication.class, args);
	}	
	
	@Bean
    @LoadBalanced
    RestTemplate restTemplate() { 
        return new RestTemplate();
    }
	
	@RequestMapping("/")
	String home() {
		System.out.println("登陆页...");
		return "login";
	}
	
	
	@RequestMapping("/404")
	String notFound() {
		return "common/404";
	}
	
	@RequestMapping("/500")
	String error() {
		return "common/500";
	}

	@Bean
    @SuppressWarnings({ "rawtypes", "unchecked"})
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
			throws UnknownHostException {
		RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
		template.setConnectionFactory(redisConnectionFactory);
		
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);  
		ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);  
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        
        template.setValueSerializer(jackson2JsonRedisSerializer); 
        template.setKeySerializer(new StringRedisSerializer()); 
        
        template.afterPropertiesSet();  
		return template;
	}

}
