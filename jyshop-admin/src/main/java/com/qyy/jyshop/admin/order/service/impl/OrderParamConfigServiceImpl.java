package com.qyy.jyshop.admin.order.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qyy.jyshop.admin.order.service.OrderParamConfigService;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.OrderParamConfig;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class OrderParamConfigServiceImpl extends AbstratService<OrderParamConfig> implements OrderParamConfigService{

    @Autowired
    private RestTemplate restTemplate;
    @Value("${interface-url}")
    private String interfaceUrl;
    
	@Override
	public OrderParamConfig queryOrderParamConfig() {
		OrderParamConfig returnObject = new OrderParamConfig();
		List<OrderParamConfig> queryAll = this.queryAll();
		if(queryAll != null && queryAll.size()>0){
			returnObject = queryAll.get(0);
		}
		return returnObject;
	}

	@Override
	public String edit(OrderParamConfig config) {
		if(config.getId() != null){
			System.out.println("更新");
			this.updateByID(config);
		}else{
			System.out.println("新增");
			this.insert(config);
		}
		OrderParamConfig orderParamConfig = this.queryAll().get(0);

		String orderParamConfigStr = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            orderParamConfigStr = mapper.writeValueAsString(JSON.toJSON(orderParamConfig));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("code", "CHEERTEA");
        map.add("type", "order");
        map.add("orderParamConfig", orderParamConfigStr);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        RestTemplate restTemplate=new RestTemplate();
        String body = restTemplate.postForEntity( interfaceUrl+"/basics/anon/saveShopBaseConf", request , String.class ).getBody();
        
        JSONObject result=JSONObject.fromObject(body);
        if(result.getInt("res_code") != 0){
            throw new AppErrorException(result.getString("res_info"));
        }
        
        redisDao.saveObject("shopParam_orderParamConfig", orderParamConfig);
		return null;
	}

}
