/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 描述
 *
 * @author wu
 * @created 2018/3/28 16:22
 */
@Entity
@Table(name="shop_order_right_status")
public class RightStatus
{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String processText;

    @Transient
    private GoodsRejectedAds address;

    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcessText() {
        return this.processText;
    }

    public void setProcessText(String processText) {
        this.processText = processText;
    }

    public GoodsRejectedAds getAddress() {
        return this.address;
    }

    public void setAddress(GoodsRejectedAds address) {
        this.address = address;
    }
}
