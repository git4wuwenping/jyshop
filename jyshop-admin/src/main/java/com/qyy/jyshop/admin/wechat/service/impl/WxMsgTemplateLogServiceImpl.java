package com.qyy.jyshop.admin.wechat.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.qyy.jyshop.admin.wechat.service.WxMsgTemplateLogService;
import com.qyy.jyshop.model.WxMsgTemplateLog;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class WxMsgTemplateLogServiceImpl extends AbstratService<WxMsgTemplateLog> implements WxMsgTemplateLogService {

    @Override
    public PageAjax<Map<String,Object>> pageMsgLog(Long tplId,PageAjax<Map<String,Object>> page) {
        ParamData params = this.getParamData(page.getPageNo(), page.getPageSize());
        params.put("tplId", tplId);
        return this.pageQuery("WxMsgTemplateLogDao.pageMsgLogData", params);
    }

}
