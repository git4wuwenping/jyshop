package com.qyy.jyshop.admin.wechat.service;

import java.util.Map;

import com.qyy.jyshop.pojo.PageAjax;

public interface WxMsgTemplateRelService {

    String addRel(Long[] memberIdArr,Long tplId);

    String delRelByTplId(Long tplId);

    PageAjax<Map<String, Object>> pageMemberRel(Long tplId,PageAjax<Map<String, Object>> page);

    String delMemberRel(Long id);

}
