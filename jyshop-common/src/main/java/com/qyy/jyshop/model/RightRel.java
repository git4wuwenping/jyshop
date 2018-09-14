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

/**
 * 描述
 *
 * @author wu
 * @created 2018/3/28 16:21
 */
@Entity
@Table(name="shop_order_right_rel")
public class RightRel
{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Long rightId;
    private Long itemsId;

    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRightId() {
        return this.rightId;
    }

    public void setRightId(Long rightId) {
        this.rightId = rightId;
    }

    public Long getItemsId() {
        return this.itemsId;
    }

    public void setItemsId(Long itemsId) {
        this.itemsId = itemsId;
    }
}