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
 * 广告
 * 
 * @author jiangbin
 * @created 2018年3月15日 下午2:43:51
 */
@Table(name = "shop_adv")
public class Adv implements Serializable {

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
     * 广告标题
     */
    @Column(name = "adv_title")
    private String advTitle;

    /**
     * 所属广告位id
     */
    @Column(name = "adv_space_id")
    private Long advSpaceId;

    /**
     * 广告链接
     */
    @Column(name = "adv_href")
    private String advHref;

    /**
     * 广告图片
     */
    @Column(name = "adv_attach")
    private String advAttach;
    /**
     * 是否可用 1为启用 0为停用
     */
    @Column(name = "used")
    private Short used;

    @Column(name = "adv_order")
    private Integer advOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdvTitle() {
        return advTitle;
    }

    public void setAdvTitle(String advTitle) {
        this.advTitle = advTitle;
    }

    public Long getAdvSpaceId() {
        return advSpaceId;
    }

    public void setAdvSpaceId(Long advSpaceId) {
        this.advSpaceId = advSpaceId;
    }

    public String getAdvHref() {
        return advHref;
    }

    public void setAdvHref(String advHref) {
        this.advHref = advHref;
    }

    public String getAdvAttach() {
        return advAttach;
    }

    public void setAdvAttach(String advAttach) {
        this.advAttach = advAttach;
    }

    public Short getUsed() {
        return used;
    }

    public void setUsed(Short used) {
        this.used = used;
    }

    public Integer getAdvOrder() {
        return advOrder;
    }

    public void setAdvOrder(Integer advOrder) {
        this.advOrder = advOrder;
    }

}
