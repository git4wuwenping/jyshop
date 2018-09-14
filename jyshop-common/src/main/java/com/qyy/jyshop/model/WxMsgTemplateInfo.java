/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 微信消息模版
 * 
 * @author jiangbin
 * @created 2018年4月12日 上午10:15:55
 */
@Table(name = "shop_wx_msg_template_info")
public class WxMsgTemplateInfo implements Serializable {

    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 模版ID
     */
    @Column(name = "template_id")
    private String templateId;

    /**
     * 模版标题
     */
    @Column(name = "template_title")
    private String templateTitle;

    /**
     * 一级行业
     */
    @Column(name = "first_industry")
    private String firstIndustry;

    /**
     * 二级行业
     */
    @Column(name = "second_industry")
    private String secondIndustry;

    /**
     * 链接地址
     */
    @Column(name = "call_url")
    private String callUrl;

    /**
     * @author jiangbin
     * @created 2018年4月12日 上午10:18:37
     * @return type
     */
    public Long getId() {
        return id;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 上午10:18:37
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 上午10:18:37
     * @return type
     */
    public String getTemplateId() {
        return templateId;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 上午10:18:37
     * @param templateId
     */
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 上午10:18:37
     * @return type
     */
    public String getTemplateTitle() {
        return templateTitle;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 上午10:18:37
     * @param templateTitle
     */
    public void setTemplateTitle(String templateTitle) {
        this.templateTitle = templateTitle;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 上午10:18:37
     * @return type
     */
    public String getFirstIndustry() {
        return firstIndustry;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 上午10:18:37
     * @param firstIndustry
     */
    public void setFirstIndustry(String firstIndustry) {
        this.firstIndustry = firstIndustry;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 上午10:18:37
     * @return type
     */
    public String getSecondIndustry() {
        return secondIndustry;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 上午10:18:37
     * @param secondIndustry
     */
    public void setSecondIndustry(String secondIndustry) {
        this.secondIndustry = secondIndustry;
    }

    public String getCallUrl() {
        return callUrl;
    }

    public void setCallUrl(String callUrl) {
        this.callUrl = callUrl;
    }

}
