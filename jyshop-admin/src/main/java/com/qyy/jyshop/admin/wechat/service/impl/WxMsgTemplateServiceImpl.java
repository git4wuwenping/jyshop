/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.wechat.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qyy.jyshop.admin.wechat.service.WxMsgTemplateService;
import com.qyy.jyshop.dao.WxMsgTemplateDetailDao;
import com.qyy.jyshop.dao.WxMsgTemplateLogDao;
import com.qyy.jyshop.dao.WxMsgTemplateRelDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.WxMsgTemplateDetail;
import com.qyy.jyshop.model.WxMsgTemplateInfo;
import com.qyy.jyshop.model.WxMsgTemplateLog;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.TimestampUtil;
import com.qyy.jyshop.vo.WechatMsgTemplateData;

/**
 * 描述
 * 
 * @author jiangbin
 * @created 2018年4月12日 上午10:44:14
 */
@Service
public class WxMsgTemplateServiceImpl extends AbstratService<WxMsgTemplateInfo> implements WxMsgTemplateService {

    @Autowired
    private WxMsgTemplateDetailDao wxMsgTemplateDetailDao;
    @Autowired
    private WxMsgTemplateRelDao wxMsgTemplateRelDao;
    @Autowired
    private WxMsgTemplateLogDao wxMsgTemplateLogDao;
    
    @Value("${interface-url}")
    private String interfaceUrl;

    @Value("${spring.profiles.active}")
    private String springProfile;
    /**
     * 描述
     * 
     * @author jiangbin
     * @created 2018年4月12日 上午10:44:14
     * @param page
     * @return
     * @see com.qyy.jyshop.admin.wechat.service.WxMsgTemplateService#pageTemplate(com.qyy.jyshop.pojo.PageAjax)
     */
    @Override
    public PageAjax<WxMsgTemplateInfo> pageTemplate(PageAjax<WxMsgTemplateInfo> page) {
        return this.queryPageListByExample(page.getPageNo(), page.getPageSize(), null);
    }

    /**
     * 描述
     * 
     * @author jiangbin
     * @created 2018年4月12日 上午10:44:14
     * @param tplId
     * @return
     * @see com.qyy.jyshop.admin.wechat.service.WxMsgTemplateService#queryByTplId(java.lang.Long)
     */
    @Override
    public List<WxMsgTemplateDetail> queryDetailByTplId(Long tplId) {
        Example example = new Example(WxMsgTemplateDetail.class);
        Criteria createCriteria = example.createCriteria();
        createCriteria.andEqualTo("tplId", tplId);
        List<WxMsgTemplateDetail> selectByExample = wxMsgTemplateDetailDao.selectByExample(example);
        return selectByExample;
    }

    @Override
    public WxMsgTemplateInfo queryInfoById(Long tplId) {
        return this.queryByID(tplId);
    }

    @Override
    @Transactional
    public String sendMsg(Map<String, Object> map) {
        Boolean flag = true; //是否要记录日志
        WxMsgTemplateLog wxMsgTemplateLog = new WxMsgTemplateLog();
        wxMsgTemplateLog.setOpTime(TimestampUtil.getNowTime());
        
        // map {first=1, keyword1=1, keyword2=1, remark=1, sendType=0}
        List<Long> memberIdList = new ArrayList<Long>();
        try {
            String sendType = (String) map.get("sendType");
            String tplIdStr = (String) map.get("tplId");
            String content = (String) map.get("_content");
            
            WxMsgTemplateInfo entity = new WxMsgTemplateInfo();
            entity.setId(Long.parseLong(tplIdStr));
            WxMsgTemplateInfo queryOne = this.queryOne(entity);
            
            wxMsgTemplateLog.setTplId(Long.parseLong(tplIdStr));
            wxMsgTemplateLog.setMsgText(content);
            
            if (sendType.equals("0")) {// 发送给全站用户
                flag = false;
                wxMsgTemplateLog.setContent("暂不支持全站群发功能...");
                throw new AppErrorException("暂不支持全站群发功能...");
            } else {// 发送给指定用户
                memberIdList = wxMsgTemplateRelDao.selectMemberIdListByTplId(Long.parseLong(tplIdStr));
                if(memberIdList.size() == 0){
                    flag = false;
                    throw new AppErrorException("指定发送会员为空...");
                }
                String templatId = (String) map.get("templateId");
                String bgColor = "#000000";
                String callUrl = queryOne.getCallUrl();

                Map<String, WechatMsgTemplateData> param = new HashMap<>();
                
                Example example = new Example(WxMsgTemplateDetail.class);
                Criteria createCriteria = example.createCriteria();
                createCriteria.andEqualTo("tplId", Long.parseLong(tplIdStr));
                List<WxMsgTemplateDetail> WxMsgTemplateDetailList = wxMsgTemplateDetailDao.selectByExample(example);
                for(WxMsgTemplateDetail wxMsgTemplateDetail:WxMsgTemplateDetailList){
                    String colKey = wxMsgTemplateDetail.getColKey();
                    param.put(colKey, new WechatMsgTemplateData((String)map.get(colKey), "#696969"));
                }
                
                JSONObject fromObject = JSONObject.fromObject(param);
                
                //List<Long> memberIdList, String templatId, String callUrl, String bgColor, JSONObject jsonObject, String token
                
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                MultiValueMap<String, Object> paramMap= new LinkedMultiValueMap<String, Object>();
                paramMap.add("memberIds", StringUtils.join(memberIdList.toArray(), ","));
                paramMap.add("templatId", templatId);
                paramMap.add("callUrl", callUrl);
                paramMap.add("bgColor", bgColor);
                paramMap.add("jsonObjectStr", fromObject.toString());
                paramMap.add("token", "CHEERTEA");

                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(paramMap, headers);
                RestTemplate restTemplate=new RestTemplate(); ///basics
                String _interfaceUrl = "";
                if(!springProfile.equals("dev")){
                    _interfaceUrl = interfaceUrl + "/basics";
                }else{
                    _interfaceUrl = interfaceUrl;
                }
                String body = restTemplate.postForEntity( _interfaceUrl+"/anon/sendWechatMsgToUser", request , String.class ).getBody();
                //java.lang.ClassCastException: java.util.ArrayList cannot be cast to java.lang.String
                
                JSONObject result=JSONObject.fromObject(body);
                if(result.getInt("res_code") != 0){
                    wxMsgTemplateLog.setContent(result.getString("res_info"));
                    throw new AppErrorException(result.getString("res_info"));
                }else{
                    Map<String,Object> retMap = (Map)result.get("res_data");
                    String s = (String)retMap.get("ret");
                    wxMsgTemplateLog.setContent(s);
                    return s;
                }
            }
        } catch (Exception ex) {
            if(ex instanceof AppErrorException){
                throw new AppErrorException(ex.getMessage());
            }
            throw new AppErrorException("出错了...");
        }finally{
            if(flag){
                wxMsgTemplateLog.setEndTime(TimestampUtil.getNowTime());
                wxMsgTemplateLogDao.insert(wxMsgTemplateLog);
            }
        }
    }
}
