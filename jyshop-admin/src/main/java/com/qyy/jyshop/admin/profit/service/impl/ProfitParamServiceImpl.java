package com.qyy.jyshop.admin.profit.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.member.service.MemberService;
import com.qyy.jyshop.admin.profit.service.ProfitParamService;
import com.qyy.jyshop.admin.profit.service.ProfitService;
import com.qyy.jyshop.dao.GoodsDao;
import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.dao.OrderDao;
import com.qyy.jyshop.dao.ProfitParamDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.ProfitParam;
import com.qyy.jyshop.platform.job.ProfitJob;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.EntityReflectUtil;

@Service
public class ProfitParamServiceImpl extends AbstratService<ProfitParam> implements ProfitParamService {
	
	private final Log logger = LogFactory.getLog(ProfitParamServiceImpl.class);
	
    @Autowired
	private GoodsDao goodsDao;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private MemberService memberServiceImpl;
	@Autowired
	private RestTemplate restTemplate;
	@Value("${interface-url}")
    private String interfaceUrl;
	
	@Override
	@ServiceLog("查询分润设置")
    public ProfitParam queryProfitParam() {
        List<ProfitParam> profitParamList = this.queryAll();
        if(profitParamList!=null && profitParamList.size()>0)
            return profitParamList.get(0);
        else
            return null;
    }
	
	@Override
	@ServiceLog("修改分润设置")
	public String doEditProfitParam(Map<String, Object> map){

	    ProfitParam profitParam = EntityReflectUtil.toBean(ProfitParam.class,map);
	    BigDecimal total=profitParam.getTax().add(profitParam.getManagement());
	    total=total.add(profitParam.getOperation());
	    total=total.add(profitParam.getGain());
	    total=total.add(profitParam.getServiceCenter());
	    total=total.add(profitParam.getReferee());
	    total=total.add(profitParam.getOperator());
    		
	    if(total.compareTo(new BigDecimal(100))>0)
	        return "分润比例总和不能大于100";
    	
	    profitParam.setPresenter(profitParam.getServiceCenter().add(profitParam.getReferee()).add(profitParam.getOperator()));
	    profitParam.setProfitMemberFirstReal(new BigDecimal(100).subtract(total).multiply(profitParam.getProfitMemberFirst()).divide(new BigDecimal(10000)));
	    profitParam.setProfitMemberSecReal(new BigDecimal(100).subtract(total).multiply(profitParam.getProfitMemberSec()).divide(new BigDecimal(10000)));
	    profitParam.setProfitShopownerFirstReal(new BigDecimal(100).subtract(total).multiply(profitParam.getProfitShopownerFirst()).divide(new BigDecimal(10000)));
	    profitParam.setProfitShopownerSecReal(new BigDecimal(100).subtract(total).multiply(profitParam.getProfitShopownerSec()).divide(new BigDecimal(10000)));
	    profitParam.setProfitShopownerSubReal(new BigDecimal(100).subtract(total).multiply(profitParam.getProfitShopownerSub()).divide(new BigDecimal(10000)));
	    this.update(profitParam);
        
	    
	    ProfitParam profitParam1 = this.queryAll().get(0);
	    
	    String profitParamStr = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            profitParamStr = mapper.writeValueAsString(JSON.toJSON(profitParam1));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map1= new LinkedMultiValueMap<String, String>();
        map1.add("code", "CHEERTEA");
        map1.add("type", "profit");
        map1.add("profitParam", profitParamStr);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map1, headers);
        RestTemplate restTemplate=new RestTemplate();
        String body = restTemplate.postForEntity( interfaceUrl+"/basics/anon/saveShopBaseConf", request , String.class).getBody();
        
        JSONObject result=JSONObject.fromObject(body);
        if(result.getInt("res_code") != 0){
            throw new AppErrorException(result.getString("res_info"));
        }
        
	    redisDao.saveObject("shopParam_profitParam", profitParam1);
		return null;
	}

}
