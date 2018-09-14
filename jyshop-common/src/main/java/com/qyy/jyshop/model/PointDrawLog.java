/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

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
@Table(name = "shop_point_draw_log")
public class PointDrawLog implements Serializable {

    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @Id
    @Column(name = "log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @Column(name = "bill_no")
    private String billNo;// 提现单号

    @Column(name = "op_id")
    private Long opId;// 操作id

    @Column(name = "op_user")
    private String opUser;// 操作用户

    @Column(name = "point")
    private BigDecimal point;// 提现积分

    @Column(name = "point_before")
    private BigDecimal pointBefore;// 提现前积分

    @Column(name = "point_after")
    private BigDecimal pointAfter;// 提现后积分

    @Column(name = "point_type")
    private Integer pointType; // 积分类型 0:云积分 1:黄积分 2:红积分

    @Column(name = "message")
    private String message;// 内容

    @Column(name = "CREATION_TIME")
    private Timestamp createTime;// 创建时间

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Long getOpId() {
        return opId;
    }

    public void setOpId(Long opId) {
        this.opId = opId;
    }

    public String getOpUser() {
        return opUser;
    }

    public void setOpUser(String opUser) {
        this.opUser = opUser;
    }

    public BigDecimal getPoint() {
        return point;
    }

    public void setPoint(BigDecimal point) {
        this.point = point;
    }

    public Integer getPointType() {
        return pointType;
    }

    public void setPointType(Integer pointType) {
        this.pointType = pointType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getPointBefore() {
        return pointBefore;
    }

    public void setPointBefore(BigDecimal pointBefore) {
        this.pointBefore = pointBefore;
    }

    public BigDecimal getPointAfter() {
        return pointAfter;
    }

    public void setPointAfter(BigDecimal pointAfter) {
        this.pointAfter = pointAfter;
    }

}
