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
import javax.persistence.Transient;

@Entity
@Table(name = "shop_bargain_order")
public class BargainOrder implements Serializable{

    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId; //订单ID
    
    @Column(name = "parent_id")
    private Long parentId; //订单父ID
    
    @Column(name = "order_sn")
    private String orderSn; //订单号
    
    @Column(name = "member_id")
    private Long memberId; //用户id
    
    @Column(name = "order_status")
    private Integer orderStatus; //订单状态
    
    @Column(name = "pay_status")
    private Integer payStatus; //支付状态
    
    @Column(name = "ship_status")
    private Integer shipStatus; //货运状态
    
    @Column(name = "payment_id")
    private Integer paymentId; //支付方式id
    
    @Column(name = "payment_name")
    private String paymentName; //支付方式名称
    
    @Column(name = "payment_type")
    private String paymentType; //支付类型
    
    
    private BigDecimal weight; //重量
    
    @Column(name = "goods_amount")
    private BigDecimal goodsAmount; //商品金额
    
    @Column(name = "ship_amount")
    private BigDecimal shipAmount; //运费
    
    @Column(name = "order_amount")
    private BigDecimal orderAmount; //订单金额
    
    @Column(name = "pay_amount")
    private BigDecimal payAmount; //支付金额
    
    @Column(name = "ship_id")
    private Integer shipId;
    
    @Column(name = "ship_type")
    private String shipType;
    
    @Column(name = "ship_address")
    private String shipAddress;
    
    @Column(name = "ship_name")
    private String shipName; //收货人
    
    @Column(name = "ship_addr")
    private String shipAddr; //收货详细地址
    
    @Column(name = "ship_zip")
    private String shipZip; //收货人邮编
    
    @Column(name = "ship_email")
    private String shipEmail; //收货人邮箱
    
    @Column(name = "ship_mobile")
    private String shipMobile; //收货人手机
    
    @Column(name = "ship_tel")
    private String shipTel; //收货人电话
    
    @Column(name = "ship_province_id")
    private String shipProvinceId; //配送地区-省id
    
    @Column(name = "ship_city_id")
    private String shipCityId; //配送地区-市id
    
    @Column(name = "ship_district_id")
    private String shipDistrictId; //配送地区-县id
    
    @Column(name = "ship_province_name")
    private String shipProvinceName; //配送地区-省名称
    
    @Column(name = "ship_city_name")
    private String shipCityName; //配送地区-市名称
    
    @Column(name = "ship_district_name")
    private String shipDistrictName; //配送地区-县名称
    
    private String remarks; //用户备注
    
    @Column(name = "admin_remarks")
    private String adminRemarks; //管理员备注
    
    @Column(name = "create_date")
    private Timestamp createDate; //创建时间
    
    @Column(name = "payment_date")
    private Timestamp  paymentDate; //支付时间
    
    @Column(name = "deliver_date")
    private Timestamp deliverDate; //发货时间
    
    @Column(name = "signing_date")
    private Timestamp signingDate;//收货时间
    
    @Column(name = "complete_date")
    private Timestamp completeDate; //完成时间
    
    @Transient
    private Integer bargainType; //砍价类型 0:常规 1:共享 2:免单
    
    @Transient
    private BigDecimal freePrice; //免单金额
    
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(Integer shipStatus) {
        this.shipStatus = shipStatus;
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

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(BigDecimal goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public BigDecimal getShipAmount() {
        return shipAmount;
    }

    public void setShipAmount(BigDecimal shipAmount) {
        this.shipAmount = shipAmount;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public Integer getShipId() {
        return shipId;
    }

    public void setShipId(Integer shipId) {
        this.shipId = shipId;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipAddr() {
        return shipAddr;
    }

    public void setShipAddr(String shipAddr) {
        this.shipAddr = shipAddr;
    }

    public String getShipZip() {
        return shipZip;
    }

    public void setShipZip(String shipZip) {
        this.shipZip = shipZip;
    }

    public String getShipEmail() {
        return shipEmail;
    }

    public void setShipEmail(String shipEmail) {
        this.shipEmail = shipEmail;
    }

    public String getShipMobile() {
        return shipMobile;
    }

    public void setShipMobile(String shipMobile) {
        this.shipMobile = shipMobile;
    }

    public String getShipTel() {
        return shipTel;
    }

    public void setShipTel(String shipTel) {
        this.shipTel = shipTel;
    }

    public String getShipProvinceId() {
        return shipProvinceId;
    }

    public void setShipProvinceId(String shipProvinceId) {
        this.shipProvinceId = shipProvinceId;
    }

    public String getShipCityId() {
        return shipCityId;
    }

    public void setShipCityId(String shipCityId) {
        this.shipCityId = shipCityId;
    }

    public String getShipDistrictId() {
        return shipDistrictId;
    }

    public void setShipDistrictId(String shipDistrictId) {
        this.shipDistrictId = shipDistrictId;
    }

    public String getShipProvinceName() {
        return shipProvinceName;
    }

    public void setShipProvinceName(String shipProvinceName) {
        this.shipProvinceName = shipProvinceName;
    }

    public String getShipCityName() {
        return shipCityName;
    }

    public void setShipCityName(String shipCityName) {
        this.shipCityName = shipCityName;
    }

    public String getShipDistrictName() {
        return shipDistrictName;
    }

    public void setShipDistrictName(String shipDistrictName) {
        this.shipDistrictName = shipDistrictName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAdminRemarks() {
        return adminRemarks;
    }

    public void setAdminRemarks(String adminRemarks) {
        this.adminRemarks = adminRemarks;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Timestamp getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(Timestamp deliverDate) {
        this.deliverDate = deliverDate;
    }

    public Timestamp getSigningDate() {
        return signingDate;
    }

    public void setSigningDate(Timestamp signingDate) {
        this.signingDate = signingDate;
    }

    public Timestamp getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Timestamp completeDate) {
        this.completeDate = completeDate;
    }

    public Integer getBargainType() {
        return bargainType;
    }

    public void setBargainType(Integer bargainType) {
        this.bargainType = bargainType;
    }

    public BigDecimal getFreePrice() {
        return freePrice;
    }

    public void setFreePrice(BigDecimal freePrice) {
        this.freePrice = freePrice;
    }
    
}
