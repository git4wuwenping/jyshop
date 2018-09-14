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

import com.mysql.jdbc.StringUtils;
import com.mysql.jdbc.StringUtils;

/**
 * 描述
 * 
 * @author jiangbin
 * @created 2018年3月13日 下午4:14:45
 */

@Entity
@Table(name = "shop_floor")
public class Floor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "floor_id")
    private Long floorId; // 楼层id

    @Column(name = "parent_id")
    private Long parentId; // 父楼层id

    @Column(name = "floor_name")
    private String floorName; // 楼层名称

    @Column(name = "horizontal_size")
    private Integer horizontalSize; // 楼层横向放置个数

    @Column(name = "vertical_size")
    private Integer verticalSize; // 楼层纵向放置个数

    @Column(name = "floor_order")
    private Short floorOrder; // 楼层排序

    @Column(name = "disable")
    private Short disable; // 是否可用,0可用 1不可用

    @Column(name = "floor_type")
    private Short floorType; // 楼层类别,1分类楼层 2商品楼层 3品牌楼层

    @Column(name = "floor_desc")
    private String floorDesc; // 楼层描述

    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public Integer getHorizontalSize() {
        return horizontalSize;
    }

    public void setHorizontalSize(Integer horizontalSize) {
        this.horizontalSize = horizontalSize;
    }

    public Integer getVerticalSize() {
        return verticalSize;
    }

    public void setVerticalSize(Integer verticalSize) {
        this.verticalSize = verticalSize;
    }

    public Short getFloorOrder() {
        return floorOrder;
    }

    public void setFloorOrder(Short floorOrder) {
        this.floorOrder = floorOrder;
    }

    public Short getDisable() {
        return disable;
    }

    public void setDisable(Short disable) {
        this.disable = disable;
    }

    public Short getFloorType() {
        return floorType;
    }

    public void setFloorType(Short floorType) {
        this.floorType = floorType;
    }

    public String getFloorDesc() {
        return floorDesc;
    }

    public void setFloorDesc(String floorDesc) {
        this.floorDesc = floorDesc;
    }

    // 比较两个对象的内容是否相等 重写object 的equals方法
    @Override
    public boolean equals(Object obj) {

        Floor f1 = null;
        if (obj instanceof Floor) {// 如果传进来的对象是D1的子类那么就对属性做比较
            f1 = (Floor) obj;
            if (f1.floorId.compareTo(floorId) == 0
                    && f1.floorName.equals(floorName)
                    && f1.parentId.compareTo(parentId) == 0
                    && f1.horizontalSize.compareTo(horizontalSize) == 0
                    && f1.verticalSize.compareTo(verticalSize) == 0
                    && f1.floorOrder.compareTo(floorOrder) == 0
                    && f1.disable.compareTo(disable) == 0
                    && f1.floorType.compareTo(floorType) == 0
                    && ((StringUtils.isNullOrEmpty(f1.floorDesc) && !StringUtils.isNullOrEmpty(floorDesc))
                            || (!StringUtils.isNullOrEmpty(f1.floorDesc) && StringUtils.isNullOrEmpty(floorDesc)) || ((StringUtils
                            .isNullOrEmpty(f1.floorDesc) && StringUtils.isNullOrEmpty(floorDesc)) || (!StringUtils
                            .isNullOrEmpty(f1.floorDesc) && !StringUtils.isNullOrEmpty(floorDesc) && f1.floorDesc
                                .equals(floorDesc)))))
                return true;
            else
                return false;
        } else
            return false;
    }

}
