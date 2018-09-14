/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * 描述
 *
 * @author wu
 * @created 2018/3/28 16:22
 */
@Entity
@Table(name="shop_order_right_process")
public class RightProcess
{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Timestamp createTime;
    private Long rightStatusId;
    private Long rightOrderId;

    @Transient
    private RightStatus rightStatus;

    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    @Temporal(TemporalType.TIMESTAMP)
    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getRightStatusId() {
        return this.rightStatusId;
    }

    public void setRightStatusId(Long rightStatusId) {
        this.rightStatusId = rightStatusId;
    }

    public Long getRightOrderId() {
        return this.rightOrderId;
    }

    public void setRightOrderId(Long rightOrderId) {
        this.rightOrderId = rightOrderId;
    }

    public RightStatus getRightStatus() {
        return this.rightStatus;
    }

    public void setRightStatus(RightStatus rightStatus) {
        this.rightStatus = rightStatus;
    }
}