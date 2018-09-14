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
@Table(name = "shop_point_log")
public class PointLog implements Serializable{

    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId; //主键ID
    
    @Column(name = "order_id")
    private Long orderId; //订单Id
    
    @Column(name = "order_sn")
    private String orderSn; //订单号
    
    @Column(name = "member_id")
    private Long memberId; //用户ID
    
    @Column(name = "cloud_point")
    private BigDecimal cloudPoint; //云积分
    
    @Column(name = "yellow_point")
    private BigDecimal yellowPoint; //黄积分
    
    @Column(name = "red_point")
    private BigDecimal redPoint; //红积分
    
    @Column(name = "create_time")
    private Timestamp createTime; //'创建时间

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

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public BigDecimal getCloudPoint() {
        return cloudPoint;
    }

    public void setCloudPoint(BigDecimal cloudPoint) {
        this.cloudPoint = cloudPoint;
    }

    public BigDecimal getYellowPoint() {
        return yellowPoint;
    }

    public void setYellowPoint(BigDecimal yellowPoint) {
        this.yellowPoint = yellowPoint;
    }

    public BigDecimal getRedPoint() {
        return redPoint;
    }

    public void setRedPoint(BigDecimal redPoint) {
        this.redPoint = redPoint;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
