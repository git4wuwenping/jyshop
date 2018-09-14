package com.qyy.jyshop.basics.service;

import java.util.Map;

public interface WxService {

    public Map<String,Object> wxLogin(String code,Integer reqType, Integer parentId);

    public Map<String, Object> wxSign(String url);
}
