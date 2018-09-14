package com.qyy.jyshop.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 拼团订单
 */
@Entity
@Table(name = "shop_spell_order")
public class SpellOrder implements Serializable {

    /**
     * 订单ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Timestamp createDate;

    /**
     * 最后修改时间
     */
    @Column(name = "last_modify")
    private Timestamp lastModify;

    /**
     * 订单号
     */
    @Column(name = "order_sn")
    private String orderSn;

    /**
     * 拼团ID
     */
    @Column(name = "spell_id")
    private Long spellId;

    /**
     * 拼团类型（0：普通团，1：夺宝团，2：阶梯团，3：试用团，4：免费团）
     */
    @Column(name = "spell_type")
    private Integer spellType;

    /**
     * 用户ID
     */
    @Column(name = "member_id")
    private Long memberId;

    /**
     * 订单状态(0：待支付，1：已支付，2：已发货，3：已完成，-9：已取消, -8：已退款，-7：作废)
     */
    @Column(name = "order_status")
    private Integer orderStatus;

//    /**
//     * 拼单状态（0:未开始，1：正在拼团，2：拼团成功，3：拼团失败，4：取消拼团）
//     */
//    @Column(name = "spell_status")
//    private Integer spellStatus;

    /**
     * 支付状态
     */
    @Column(name = "pay_status")
    private Integer payStatus;

    /**
     * 货运状态
     */
    @Column(name = "ship_status")
    private Integer shipStatus;

    /**
     * 支付方式ID
     */
    @Column(name = "payment_id")
    private Integer paymentId;

    /**
     * 支付方式名称
     */
    @Column(name = "payment_name")
    private String paymentName;

    /**
     * 支付类型
     */
    @Column(name = "payment_type")
    private String paymentType;

    /**
     * 重量
     */
    @Column(name = "weight")
    private BigDecimal weight;

    /**
     * 运费
     */
    @Column(name = "ship_amount")
    private BigDecimal shipAmount;

    /**
     * 商品金额
     */
    @Column(name = "goods_amount")
    private BigDecimal goodsAmount;

    /**
     * 订单金额
     */
    @Column(name = "order_amount")
    private BigDecimal orderAmount;

    /**
     * 支付金额
     */
    @Column(name = "pay_amount")
    private BigDecimal payAmount;

    /**
     * 是否免单
     */
    @Column(name = "is_free")
    private Boolean isFree;

    /**
     * 收货人
     */
    @Column(name = "ship_name")
    private String shipName;

    /**
     * 收货详细地址
     */
    @Column(name = "ship_addr")
    private String shipAddr;

    /**
     * 收货地址邮编
     */
    @Column(name = "ship_zip")
    private String shipZip;

    /**
     * 收货人邮箱
     */
    @Column(name = "ship_email")
    private String shipEmail;

    /**
     * 收货人手机
     */
    @Column(name = "ship_mobile")
    private String shipMobile;

    /**
     * 收货人电话
     */
    @Column(name = "ship_tel")
    private String shipTel;

    /**
     * 配送地区-省ID
     */
    @Column(name = "ship_province_id")
    private String shipProvinceId;

    /**
     * 配送地区-市ID
     */
    @Column(name = "ship_city_id")
    private String shipCityId;

    /**
     * 配送地区-区ID
     */
    @Column(name = "ship_district_id")
    private String shipDistrictId;

    /**
     * 配送地区-省
     */
    @Column(name = "ship_province_name")
    private String shipProvinceName;

    /**
     * 配送地区-市
     */
    @Column(name = "ship_city_name")
    private String shipCityName;

    /**
     * 配送地区-区
     */
    @Column(name = "ship_district_name")
    private String shipDistrictName;

    /**
     * 用户备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 管理员备注
     */
    @Column(name = "admin_remark")
    private String adminRemark;

    /**
     * 支付时间
     */
    @Column(name = "payment_date")
    private Timestamp  paymentDate;

    /**
     * 发货时间
     */
    @Column(name = "deliver_date")
    private Timestamp deliverDate;

    /**
     * 完成时间
     */
    @Column(name = "complete_date")
    private Timestamp completeDate;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getLastModify() {
        return lastModify;
    }

    public void setLastModify(Timestamp lastModify) {
        this.lastModify = lastModify;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Long getSpellId() {
        return spellId;
    }

    public void setSpellId(Long spellId) {
        this.spellId = spellId;
    }

    public Integer getSpellType() {
        return spellType;
    }

    public void setSpellType(Integer spellType) {
        this.spellType = spellType;
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

//    public Integer getSpellStatus() {
//        return spellStatus;
//    }
//
//    public void setSpellStatus(Integer spellStatus) {
//        this.spellStatus = spellStatus;
//    }

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

    public BigDecimal getShipAmount() {
        return shipAmount;
    }

    public void setShipAmount(BigDecimal shipAmount) {
        this.shipAmount = shipAmount;
    }

    public BigDecimal getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(BigDecimal goodsAmount) {
        this.goodsAmount = goodsAmount;
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

    public Boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAdminRemark() {
        return adminRemark;
    }

    public void setAdminRemark(String adminRemark) {
        this.adminRemark = adminRemark;
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

    public Timestamp getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Timestamp completeDate) {
        this.completeDate = completeDate;
    }
}
