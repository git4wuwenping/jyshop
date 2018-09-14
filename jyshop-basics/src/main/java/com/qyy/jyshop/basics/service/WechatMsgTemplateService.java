package com.qyy.jyshop.basics.service;

import java.util.List;

import net.sf.json.JSONObject;

public interface WechatMsgTemplateService {

    String sendMsgs(List<Long> memberIdList, String templatId, String callUrl, String bgColor, List<JSONObject> jsonObject);
    
    String sendMsg(List<Long> memberIdList, String templatId, String callUrl, String bgColor, JSONObject jsonObject);

}
