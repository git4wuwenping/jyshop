/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
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
 * @created 2018/3/28 16:20
 */

@Entity
@Table(name="shop_order_right")
public class RightOrder
{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String reason;
    private String statement;
    private String images;
    //申请维权时订单状态，用于取消维权是还原
    private Integer orderStatus;
    @Transient
    private List<RightProcess> processList;

    @Transient
    private Order order;

    @Transient
    private List<OrderItems> items;
    private String serial;

//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Timestamp createTime;
    private String status;
    private String sellerStatus;
    private BigDecimal price;
    @Transient
    private BigDecimal showTime;
    private String rejectReason;
    //维权单状态控制显示按钮
    @Transient
    private Long rightStatus;

    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Order getOrder()
    {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getStatement() {
        return this.statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getImages() {
        return this.images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public List<RightProcess> getProcessList()
    {
        return this.processList;
    }

    public void setProcessList(List<RightProcess> processList) {
        this.processList = processList;
    }

    public String getSerial() {
        return this.serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Timestamp getCreateTime()
    {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public List<OrderItems> getItems() {
        return this.items;
    }

    public void setItems(List<OrderItems> items) {
        this.items = items;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSellerStatus() {
        return this.sellerStatus;
    }

    public void setSellerStatus(String sellerStatus) {
        this.sellerStatus = sellerStatus;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getRejectReason() {
        return this.rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Long getRightStatus() {
        return rightStatus;
    }

    public void setRightStatus(Long rightStatus) {
        this.rightStatus = rightStatus;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getShowTime() {
        return showTime;
    }

    public void setShowTime(BigDecimal showTime) {
        this.showTime = showTime;
    }
}