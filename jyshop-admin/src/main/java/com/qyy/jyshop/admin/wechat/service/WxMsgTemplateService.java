/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.wechat.service;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.WxMsgTemplateDetail;
import com.qyy.jyshop.model.WxMsgTemplateInfo;
import com.qyy.jyshop.pojo.PageAjax;

/**
 * 描述
 * 
 * @author jiangbin
 * @created 2018年4月12日 上午10:43:13
 */
public interface WxMsgTemplateService {

    PageAjax<WxMsgTemplateInfo> pageTemplate(PageAjax<WxMsgTemplateInfo> page);

    List<WxMsgTemplateDetail> queryDetailByTplId(Long tplId);

    WxMsgTemplateInfo queryInfoById(Long tplId);

    String sendMsg(Map<String, Object> map);

}
