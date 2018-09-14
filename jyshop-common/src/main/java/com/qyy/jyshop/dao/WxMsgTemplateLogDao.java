package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.WxMsgTemplateLog;
import com.qyy.jyshop.supple.MyMapper;

public interface WxMsgTemplateLogDao  extends MyMapper<WxMsgTemplateLog>{

    List<Map<String,Object>> pageMsgLogData(Long tplId);
	
}
