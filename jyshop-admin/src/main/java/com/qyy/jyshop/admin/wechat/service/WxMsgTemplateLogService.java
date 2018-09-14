package com.qyy.jyshop.admin.wechat.service;

import java.util.Map;

import com.qyy.jyshop.pojo.PageAjax;

public interface WxMsgTemplateLogService {

    PageAjax<Map<String,Object>> pageMsgLog(Long tplId,PageAjax<Map<String,Object>> page);

}
