package com.qyy.jyshop.smsLog.service;

import java.util.Map;

import com.google.gson.JsonObject;

public interface SmsLogService {

	Map<String, Object> sendSmsCode(String token,String mobile, Integer type) throws Exception;

	Boolean checkCode(String token,String mobile, String code, Integer type);

	boolean checkCodeBankCard(String token, String mobile, String code, Integer type, String json, String path);

}
