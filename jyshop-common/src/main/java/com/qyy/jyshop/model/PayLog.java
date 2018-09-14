package com.qyy.jyshop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shop_pay_log")
public class PayLog implements Serializable {

    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId; // 主键

    @Column(name = "order_id")
    private Long orderId; // 订单id

    @Column(name = "order_sn")
    private String orderSn; // 订单sn

    @Column(name = "order_type")
    private Integer orderType; // 订单sn

    @Column(name = "member_id")
    private Long memberId; // 会员id

    @Column(name = "method_id")
    private Integer methodId; // 方式ID

    @Column(name = "pay_method")
    private String payMethod; // 支付方式

    @Column(name = "pay_money")
    private BigDecimal payMoney; // 支付金额

    @Column(name = "advance_before")
    private BigDecimal advanceBefore; // 支付前余额

    @Column(name = "advance_after")
    private BigDecimal advanceAfter; // 支付后余额

    @Column(name = "pay_time")
    private Timestamp payTime; // 支付时间

    private String remark; // 备注

    @Column(name = "admin_remark")
    private String adminRemark; // 管理员备注

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Integer getMethodId() {
        return methodId;
    }

    public void setMethodId(Integer methodId) {
        this.methodId = methodId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getAdvanceBefore() {
        return advanceBefore;
    }

    public void setAdvanceBefore(BigDecimal advanceBefore) {
        this.advanceBefore = advanceBefore;
    }

    public BigDecimal getAdvanceAfter() {
        return advanceAfter;
    }

    public void setAdvanceAfter(BigDecimal advanceAfter) {
        this.advanceAfter = advanceAfter;
    }

    public String getAdminRemark() {
        return adminRemark;
    }

    public void setAdminRemark(String adminRemark) {
        this.adminRemark = adminRemark;
    }

}
