package com.qyy.jyshop.util.excel.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.qyy.jyshop.util.excel.ExcelResources;

public class OrderForExcel {
    private Long orderId; // 订单id
    private Long memberId; // 订单id
    private String orderSn; // 订单号
    private String orderStatus; // 订单状态
    private String payStatus; // 付款状态
    //private Integer goodsCount;
    private Integer buyCount;
    private BigDecimal goodsAmount;
    private BigDecimal shipAmount;
    private Integer weight;
    private BigDecimal discount;
    private BigDecimal orderAmount;
    private Timestamp createTime;
    //private Integer shipType;
    private String paymentName;
    private BigDecimal payMoney;
    private Timestamp paymentTime;
    //private Integer isReceipt;
    private String shipName;
    private String shipMobile;
    //private String shipTel;
    private String shipAddress;
    private String shipZip;
    private String comName;
    private String nickname;
    private String goodsName;
    private String goodsPrice;
    private String productSn;

    @ExcelResources(title = "订单ID", order = 1, isCombine = true, isCombineCondition = true)
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    
    @ExcelResources(title = "订单编号", order = 2, isCombine = true)
    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }
    
    @ExcelResources(title = "下单时间", order = 3, isCombine = true)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    
    @ExcelResources(title = "支付时间", order = 4, isCombine = true)
    public Timestamp getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Timestamp paymentTime) {
        this.paymentTime = paymentTime;
    }
    
    @ExcelResources(title = "订单状态", order = 5)
    public String getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    
    @ExcelResources(title = "商品名称", order = 6)
    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    @ExcelResources(title = "商品号", order = 7)
    public String getProductSn() {
        return productSn;
    }

    public void setProductSn(String productSn) {
        this.productSn = productSn;
    }
    
    @ExcelResources(title = "供应商", order = 8)
    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }
    
    @ExcelResources(title = "商品单价", order = 9)
    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }
    
    @ExcelResources(title = "购买数量", order = 10)
    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }
    
    @ExcelResources(title = "商品总价", order = 11, isCombine = true)
    public BigDecimal getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(BigDecimal goodsAmount) {
        this.goodsAmount = goodsAmount;
    }
    
    @ExcelResources(title = "邮费", order = 12, isCombine = true)
    public BigDecimal getShipAmount() {
        return shipAmount;
    }

    public void setShipAmount(BigDecimal shipAmount) {
        this.shipAmount = shipAmount;
    }

    @ExcelResources(title = "总重量", order = 13, isCombine = true)
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
    
    @ExcelResources(title = "订单总价", order = 14, isCombine = true)
    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }
    
    @ExcelResources(title = "优惠金额", order = 15, isCombine = true)
    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    @ExcelResources(title = "实付金额", order = 16, isCombine = true)
    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }
    
    @ExcelResources(title = "支付状态", order = 17)
    public String getPayStatus() {
        return payStatus;
    }

    
    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    @ExcelResources(title = "支付方式", order = 18)
    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }
    
    @ExcelResources(title = "收货人ID", order = 19)
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @ExcelResources(title = "收货人姓名", order = 20)
    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }
    

    @ExcelResources(title = "昵称", order = 21)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @ExcelResources(title = "联系方式", order = 22)
    public String getShipMobile() {
        return shipMobile;
    }

    public void setShipMobile(String shipMobile) {
        this.shipMobile = shipMobile;
    }

    @ExcelResources(title = "收货地址", order = 23)
    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    @ExcelResources(title = "邮编", order = 24)
    public String getShipZip() {
        return shipZip;
    }

    public void setShipZip(String shipZip) {
        this.shipZip = shipZip;
    }

}
