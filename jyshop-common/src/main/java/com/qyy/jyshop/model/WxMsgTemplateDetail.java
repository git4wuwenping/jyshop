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
@Table(name = "shop_wx_msg_template_detail")
public class WxMsgTemplateDetail implements Serializable {

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
    @Column(name = "tpl_id")
    private String tplId;

    /**
     * 列key
     */
    @Column(name = "col_key")
    private String colKey;

    /**
     * 列文本
     */
    @Column(name = "col_text")
    private String colText;

    /**
     * 文本示例
     */
    @Column(name = "col_eg_text")
    private String colEgText;

    /**
     * @author jiangbin
     * @created 2018年4月12日 上午10:20:12
     * @return type
     */
    public Long getId() {
        return id;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 上午10:20:12
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 上午10:26:04
     * @return type
     */
    public String getTplId() {
        return tplId;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 上午10:26:04
     * @param tplId
     */
    public void setTplId(String tplId) {
        this.tplId = tplId;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 上午10:20:12
     * @return type
     */
    public String getColKey() {
        return colKey;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 上午10:20:12
     * @param colKey
     */
    public void setColKey(String colKey) {
        this.colKey = colKey;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 上午10:20:12
     * @return type
     */
    public String getColText() {
        return colText;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 上午10:20:12
     * @param colText
     */
    public void setColText(String colText) {
        this.colText = colText;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 上午11:35:30
     * @return type
     */
    public String getColEgText() {
        return colEgText;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 上午11:35:30
     * @param colEgText
     */
    public void setColEgText(String colEgText) {
        this.colEgText = colEgText;
    }

}
