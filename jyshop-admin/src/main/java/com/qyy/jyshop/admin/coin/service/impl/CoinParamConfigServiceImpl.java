package com.qyy.jyshop.admin.coin.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qyy.jyshop.admin.coin.service.CoinParamConfigService;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.CoinParamConfig;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class CoinParamConfigServiceImpl extends AbstratService<CoinParamConfig> implements CoinParamConfigService {
    
    @Value("${interface-url}")
    private String interfaceUrl;
    
	@Override
	public CoinParamConfig querycoinParamConfig() {
		CoinParamConfig returnObject = new CoinParamConfig();
		List<CoinParamConfig> queryAll = this.queryAll();
		if(queryAll != null && queryAll.size()>0){
			returnObject = queryAll.get(0);
		}
		return returnObject;
	}

	@Override
	@Transactional
	public String edit(CoinParamConfig config) {
		if(config.getId() != null){
			this.updateByID(config);
		}else{
			this.insert(config);
		}
		CoinParamConfig coinParamConfig = this.queryAll().get(0);
		
		String coinParamConfigStr = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
          coinParamConfigStr = mapper.writeValueAsString(JSON.toJSON(coinParamConfig));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("code", "CHEERTEA");
		map.add("type", "coin");
		map.add("coinParamConfig", coinParamConfigStr);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		RestTemplate restTemplate=new RestTemplate();
		String body = restTemplate.postForEntity( interfaceUrl+"/basics/anon/saveShopBaseConf", request , String.class ).getBody();
		
        JSONObject result=JSONObject.fromObject(body);
        if(result.getInt("res_code") != 0){
            throw new AppErrorException(result.getString("res_info"));
        }
        redisDao.saveObject("shopParam_coinParamConfig", coinParamConfig);
		return null;
	}

}
