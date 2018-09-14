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
@Table(name = "shop_member_profit")
public class MemberProfit implements Serializable{

    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profit_id")
    private Long profitId; //主键ID
    
    @Column(name = "member_id")
    private Long memberId; //用户ID
    
    @Column(name = "order_id")
    private Long orderId; //订单Id
    
    @Column(name = "order_sn")
    private String orderSn; //订单号
    
    @Column(name = "profit_amount")
    private BigDecimal profitAmount; //可分润金额
    private BigDecimal ratio; //分润比例
    private BigDecimal amount; //分润金额
    
    @Column(name = "create_time")
    private Timestamp createTime; //创建时间
    private Integer status; //审核状态
    
    public Long getProfitId() {
        return profitId;
    }
    public void setProfitId(Long profitId) {
        this.profitId = profitId;
    }
    public Long getMemberId() {
        return memberId;
    }
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
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
    public BigDecimal getProfitAmount() {
        return profitAmount;
    }
    public void setProfitAmount(BigDecimal profitAmount) {
        this.profitAmount = profitAmount;
    }
    public BigDecimal getRatio() {
        return ratio;
    }
    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public Timestamp getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    
}
