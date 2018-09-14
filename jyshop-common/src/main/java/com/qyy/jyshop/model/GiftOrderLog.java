package com.qyy.jyshop.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "shop_gift_order_log")
public class GiftOrderLog implements Serializable{

    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId; //日志id
    
    @Column(name = "order_id")
    private Long orderId; //订单id
    
    @Column(name = "order_sn")
    private String orderSn; //订单号
    
    @Column(name = "op_id")
    private Long opId; //操作id
    
    @Column(name = "op_user")
    private String opUser; //操作用户
    
    private String message; //内容
    
    @Column(name = "creation_time")
    private Timestamp creationTime; //创建时间
    
    @Column(name = "payment_id")
    private Integer paymentId; //支付方式id
    
    @Column(name = "payment_name")
    private String paymentName; //支付方式名称
    
    @Column(name = "payment_type")
    private String paymentType; //支付方式
    
    @Column(name = "user_type")
    private Integer userType; //用户类型 用户:0 管理员:1 系统:2

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
    
}
