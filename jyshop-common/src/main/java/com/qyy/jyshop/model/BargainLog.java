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
@Table(name = "shop_bargain_log")
public class BargainLog implements Serializable{

    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId; //日志ID
    
    @Column(name = "order_id")
    private Long orderId; //订单ID
    
    @Column(name = "order_sn")
    private String orderSn; //订单号
    
    @Column(name = "op_id")
    private Long opId; //用户ID
    
    @Column(name = "op_user")
    private String opUser; //用户名
    
    @Column(name = "bargain_id")
    private Long bargainId; //砍价活动ID
    
    @Column(name = "goods_id")
    private Long goodsId; //商品ID
    private String message; //日志内容
    
//    @Column(name = "create_date")
//    private Timestamp createDate; //创建时间
//
    @Column(name = "payment_id")
    private String paymentId; //支付方式ID
    
    @Column(name = "payment_name")
    private String paymentName; //支付方式名称
    
    @Column(name = "payment_type")
    private String paymentType; //支付方式
    
    @Column(name = "user_type")
    private Integer userType; //用户类型 用户:0 管理员:1 系统:2
    
    @Column(name = "member_id")
    private Long memberId; //发起砍价用户ID
    
    @Column(name = "bargain_member_id")
    private Long bargainMemberId; //帮助砍价用户ID
    
    @Column(name = "bargain_price")
    private BigDecimal bargainPrice; //砍价金额
    
    @Column(name = "member_bargain_id")
    private Long memberBargainId; //砍价记录ID
    
    @Column(name = "create_time")
    private Timestamp createTime; //创建时间

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

    public Long getBargainId() {
        return bargainId;
    }

    public void setBargainId(Long bargainId) {
        this.bargainId = bargainId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public Timestamp getCreateDate() {
//        return createDate;
//    }
//
//    public void setCreateDate(Timestamp createDate) {
//        this.createDate = createDate;
//    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
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

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getBargainMemberId() {
        return bargainMemberId;
    }

    public void setBargainMemberId(Long bargainMemberId) {
        this.bargainMemberId = bargainMemberId;
    }

    public BigDecimal getBargainPrice() {
        return bargainPrice;
    }

    public void setBargainPrice(BigDecimal bargainPrice) {
        this.bargainPrice = bargainPrice;
    }

    public Long getMemberBargainId() {
        return memberBargainId;
    }

    public void setMemberBargainId(Long memberBargainId) {
        this.memberBargainId = memberBargainId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    
}
