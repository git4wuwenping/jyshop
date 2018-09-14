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
@Table(name = "shop_profit_log")
public class ProfitLog implements Serializable{

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
    
    @Column(name = "member_id")
    private Long memberId; //用户ID
    
    @Column(name = "order_amount")
    private BigDecimal orderAmount; //订单金额
    
    @Column(name = "goods_amount")
    private BigDecimal goodsAmount; //商品总金额
    
    @Column(name = "goods_cost")
    private BigDecimal goodsCost; //分润商品成本价
    
    @Column(name = "goods_price")
    private BigDecimal goodsPrice; //分润商品销售价
    
    @Column(name = "profit_amount")
    private BigDecimal profitAmount; //分润金额
    
    @Column(name = "one_profit_ratio")
    private BigDecimal oneProfitRatio; //毛利润1
    
    @Column(name = "two_profit_ratio")
    private BigDecimal twoProfitRatio; //毛利润2
    private BigDecimal tax; //税收
    private BigDecimal management; //管理费
    private BigDecimal operation; //运营费
    private BigDecimal gain; //利润
    
    @Column(name = "service_center")
    private BigDecimal serviceCenter; //运营服务中心
    private BigDecimal referee; //推荐人
    private BigDecimal operator; //经办人
    
    @Column(name = "profit_member_first")
    private BigDecimal profitMemberFirst; //有分润会员首次购买分润比例
    
    @Column(name = "profit_member_sec")
    private BigDecimal profitMemberSec; //有分润会员重复购买分润比例
    
    @Column(name = "profit_shopowner_first")
    private BigDecimal profitShopownerFirst; //店长推荐首次购买分润比例
    
    @Column(name = "profit_shopowner_sec")
    private BigDecimal  profitShopownerSec; //店长推荐重复购买分润比例
    
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

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(BigDecimal goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public BigDecimal getGoodsCost() {
        return goodsCost;
    }

    public void setGoodsCost(BigDecimal goodsCost) {
        this.goodsCost = goodsCost;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getProfitAmount() {
        return profitAmount;
    }

    public void setProfitAmount(BigDecimal profitAmount) {
        this.profitAmount = profitAmount;
    }

    public BigDecimal getOneProfitRatio() {
        return oneProfitRatio;
    }

    public void setOneProfitRatio(BigDecimal oneProfitRatio) {
        this.oneProfitRatio = oneProfitRatio;
    }

    public BigDecimal getTwoProfitRatio() {
        return twoProfitRatio;
    }

    public void setTwoProfitRatio(BigDecimal twoProfitRatio) {
        this.twoProfitRatio = twoProfitRatio;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getManagement() {
        return management;
    }

    public void setManagement(BigDecimal management) {
        this.management = management;
    }

    public BigDecimal getOperation() {
        return operation;
    }

    public void setOperation(BigDecimal operation) {
        this.operation = operation;
    }

    public BigDecimal getGain() {
        return gain;
    }

    public void setGain(BigDecimal gain) {
        this.gain = gain;
    }

    public BigDecimal getServiceCenter() {
        return serviceCenter;
    }

    public void setServiceCenter(BigDecimal serviceCenter) {
        this.serviceCenter = serviceCenter;
    }

    public BigDecimal getReferee() {
        return referee;
    }

    public void setReferee(BigDecimal referee) {
        this.referee = referee;
    }

    public BigDecimal getOperator() {
        return operator;
    }

    public void setOperator(BigDecimal operator) {
        this.operator = operator;
    }

    public BigDecimal getProfitMemberFirst() {
        return profitMemberFirst;
    }

    public void setProfitMemberFirst(BigDecimal profitMemberFirst) {
        this.profitMemberFirst = profitMemberFirst;
    }

    public BigDecimal getProfitMemberSec() {
        return profitMemberSec;
    }

    public void setProfitMemberSec(BigDecimal profitMemberSec) {
        this.profitMemberSec = profitMemberSec;
    }

    public BigDecimal getProfitShopownerFirst() {
        return profitShopownerFirst;
    }

    public void setProfitShopownerFirst(BigDecimal profitShopownerFirst) {
        this.profitShopownerFirst = profitShopownerFirst;
    }

    public BigDecimal getProfitShopownerSec() {
        return profitShopownerSec;
    }

    public void setProfitShopownerSec(BigDecimal profitShopownerSec) {
        this.profitShopownerSec = profitShopownerSec;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    
}
