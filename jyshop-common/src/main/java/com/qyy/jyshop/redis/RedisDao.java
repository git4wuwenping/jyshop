package com.qyy.jyshop.redis;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RedisDao {

	@Autowired
	StringRedisTemplate stringRedisTemplate; 
	
	@Resource(name="stringRedisTemplate")
	ValueOperations<String,String> valOpsStr; 
	
	
	@Autowired
	RedisTemplate<Object, Object> redisTemplate; 
	
	@Resource(name="redisTemplate")
	ValueOperations<Object, Object> valOps; 


	public Object getObject(String key){
		return valOps.get(key);
	}
	
	public void saveObject(String key,Object value){
		this.valOps.set(key, value);
	}
	
	public void saveObject(String key,Object value,Integer second){
		this.valOps.set(key, value,second,TimeUnit.SECONDS);
	}
	
	public void delObject(String key){
		this.redisTemplate.delete(key);
	}
}
