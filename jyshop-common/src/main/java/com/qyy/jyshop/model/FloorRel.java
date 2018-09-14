/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 描述
 * 
 * @author jiangbin
 * @created 2018年3月13日 下午4:14:45
 */

@Entity
@Table(name = "shop_floor_rel")
public class FloorRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fr_id")
    private Long frId; // id

    @Column(name = "floor_id")
    private Long floorId; // 楼层id

    @Column(name = "rel_id")
    private Long relId; // 关联id

    @Column(name = "desc")
    private String desc; // 描述

    @Column(name = "fr_order")
    private Short frOrder; // 排序

    public Long getFrId() {
        return frId;
    }

    public void setFrId(Long frId) {
        this.frId = frId;
    }

    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }

    public Long getRelId() {
        return relId;
    }

    public void setRelId(Long relId) {
        this.relId = relId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Short getFrOrder() {
        return frOrder;
    }

    public void setFrOrder(Short frOrder) {
        this.frOrder = frOrder;
    }

}
