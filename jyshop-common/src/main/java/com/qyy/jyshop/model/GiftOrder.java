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

import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;

@Entity
@Table(name = "shop_gift_order")
public class GiftOrder implements Serializable {

    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId; // 订单id

    @Column(name = "order_sn")
    private String orderSn; // 订单号

    @Column(name = "parent_id")
    private Long parentId; // 父订单id

    @Column(name = "member_id")
    private Long memberId; // 会员id

    @Column(name = "order_status")
    private Integer orderStatus; // 订单状态

    @Column(name = "pay_status")
    private Integer payStatus; // 付款状态

    @Column(name = "ship_status")
    private Integer shipStatus; // 货运状态

    @Column(name = "payment_id")
    private Integer paymentId; // 支付id

    @Column(name = "payment_name")
    private String paymentName; // 支付名称

    @Column(name = "payment_type")
    private String paymentType; // 支付类型

    @Column(name = "payment_time")
    private Timestamp paymentTime; // 支付时间

    @Column(name = "ship_id")
    private Integer shipId; // 配送方式id

    @Column(name = "ship_type")
    private String shipType; // 配送方式

    @Column(name = "ship_address")
    private String shipAddress; // 配送地址

    @Column(name = "pay_money")
    private BigDecimal payMoney; // 支付金额

    @Column(name = "ship_name")
    private String shipName; // 收货人姓名

    @Column(name = "ship_addr")
    private String shipAddr; // 收货地址

    @Column(name = "ship_zip")
    private String shipZip; // 收货人邮编

    @Column(name = "ship_email")
    private String shipEmail; // 收货人邮箱

    @Column(name = "ship_mobile")
    private String shipMobile; // 收货人手机

    @Column(name = "ship_tel")
    private String shipTel; // 收货人电话

    @Column(name = "ship_day")
    private String shipDay; // 送货日期

    @Column(name = "goods_amount")
    private BigDecimal goodsAmount; // 商品总额

    @Column(name = "ship_amount")
    private BigDecimal shipAmount; // 配送费用
    private BigDecimal discount; // 优惠金额

    @Column(name = "order_amount")
    private BigDecimal orderAmount; // 订单总额
    private BigDecimal weight; // 商品重量

    @Column(name = "goods_count")
    private Integer goodsCount; // 商品数量
    private String remarks; // 备注

    @Column(name = "complete_time")
    private Timestamp completeTime; // 完成时间

    @Column(name = "ship_province_id")
    private String shipProvinceId; // 配送地区-省

    @Column(name = "ship_city_id")
    private String shipCityId; // 配送地区-城市

    @Column(name = "ship_district_id")
    private String shipDistrictId; // 配送地区-区

    @Column(name = "ship_province_name")
    private String shipProvinceName; // 配送地区-省名

    @Column(name = "ship_city_name")
    private String shipCityName; // 配送地区-城市名

    @Column(name = "ship_district_name")
    private String shipDistrictName; // 配送地区-区名

    @Column(name = "the_sign")
    private String theSign; // 签收人

    @Column(name = "signing_time")
    private Timestamp signingTime; // 签收时间

    @Column(name = "allocation_time")
    private Timestamp allocationTime; // 配货时间

    @Column(name = "coupon_id")
    private Long couponId; // 优惠卷id
    private Integer source; // 订单来源 1:pc

    @Column(name = "order_type")
    private Integer orderType; // 订单类型 0普通订单 1预售订单 2活动订单

    @Column(name = "is_receipt")
    private Integer isReceipt; // 是否索要发票 0没有 1有

    @Column(name = "deliver_time")
    private Timestamp deliverTime; // 发货时间

    @Column(name = "buy_count")
    private Integer buyCount; // 购买数量

    @Column(name = "member_remarks")
    private String memberRemarks; // 会员备注

    @Column(name = "receipt_head")
    private String receiptHead; // 发票抬头

    @Column(name = "receipt_content")
    private String receiptContent; // 发票内容

    @Column(name = "receipt_mobile")
    private String receiptMobile; // 发票手机

    @Column(name = "receipt_email")
    private String receiptEmail; // 发票邮箱

    @Column(name = "checkout_status")
    private Integer checkoutStatus; // 结算状态 0:未结算1:己结算

    @Column(name = "com_id")
    private Integer comId; // 供应商id

    @Column(name = "evaluate_status")
    private Integer evaluateStatus; // 评论状态0未评论 1己评论

    @Column(name = "split_order")
    private Integer splitOrder; // 是否拆单 0:否 1:是

    @Column(name = "top_order_status")
    private Integer topOrderStatus; // 前一个订单状态

    @Column(name = "create_time")
    private Timestamp createTime; // 创建时间

    @Column(name = "delete_flag")
    private Integer deleteFlag; // 删除标识 0:正常 1:已经删除

    @Column(name = "profit_status")
    private Integer profitStatus;// 分润状态

    @Column(name = "sub_profit_status")
    private Integer subProfitStatus;// 下级分润状态

    @Column(name = "right_close_status")
    private Integer rightCloseStatus;// 维权关闭状态 0-可维权 1-关闭维权

    @Transient
    private String orderStatusName;

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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public Timestamp getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Timestamp paymentTime) {
        this.paymentTime = paymentTime;
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

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
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

    public String getShipDay() {
        return shipDay;
    }

    public void setShipDay(String shipDay) {
        this.shipDay = shipDay;
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

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Timestamp getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Timestamp completeTime) {
        this.completeTime = completeTime;
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

    public String getTheSign() {
        return theSign;
    }

    public void setTheSign(String theSign) {
        this.theSign = theSign;
    }

    public Timestamp getSigningTime() {
        return signingTime;
    }

    public void setSigningTime(Timestamp signingTime) {
        this.signingTime = signingTime;
    }

    public Timestamp getAllocationTime() {
        return allocationTime;
    }

    public void setAllocationTime(Timestamp allocationTime) {
        this.allocationTime = allocationTime;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getIsReceipt() {
        return isReceipt;
    }

    public void setIsReceipt(Integer isReceipt) {
        this.isReceipt = isReceipt;
    }

    public Timestamp getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(Timestamp deliverTime) {
        this.deliverTime = deliverTime;
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public String getMemberRemarks() {
        return memberRemarks;
    }

    public void setMemberRemarks(String memberRemarks) {
        this.memberRemarks = memberRemarks;
    }

    public String getReceiptHead() {
        return receiptHead;
    }

    public void setReceiptHead(String receiptHead) {
        this.receiptHead = receiptHead;
    }

    public String getReceiptContent() {
        return receiptContent;
    }

    public void setReceiptContent(String receiptContent) {
        this.receiptContent = receiptContent;
    }

    public String getReceiptMobile() {
        return receiptMobile;
    }

    public void setReceiptMobile(String receiptMobile) {
        this.receiptMobile = receiptMobile;
    }

    public String getReceiptEmail() {
        return receiptEmail;
    }

    public void setReceiptEmail(String receiptEmail) {
        this.receiptEmail = receiptEmail;
    }

    public Integer getCheckoutStatus() {
        return checkoutStatus;
    }

    public void setCheckoutStatus(Integer checkoutStatus) {
        this.checkoutStatus = checkoutStatus;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getEvaluateStatus() {
        return evaluateStatus;
    }

    public void setEvaluateStatus(Integer evaluateStatus) {
        this.evaluateStatus = evaluateStatus;
    }

    public Integer getSplitOrder() {
        return splitOrder;
    }

    public void setSplitOrder(Integer splitOrder) {
        this.splitOrder = splitOrder;
    }

    public Integer getTopOrderStatus() {
        return topOrderStatus;
    }

    public void setTopOrderStatus(Integer topOrderStatus) {
        this.topOrderStatus = topOrderStatus;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getProfitStatus() {
        return profitStatus;
    }

    public void setProfitStatus(Integer profitStatus) {
        this.profitStatus = profitStatus;
    }

    public Integer getSubProfitStatus() {
        return subProfitStatus;
    }

    public void setSubProfitStatus(Integer subProfitStatus) {
        this.subProfitStatus = subProfitStatus;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {

        if (StringUtil.isEmpty(orderStatusName) && !StringUtil.isEmpty(this.orderStatus))
            this.orderStatusName = DictionaryUtil.getDataLabelByValue("order_status", this.orderStatus.toString());
        else
            this.orderStatusName = orderStatusName;
    }

    public Integer getRightCloseStatus() {
        return rightCloseStatus;
    }

    public void setRightCloseStatus(Integer rightCloseStatus) {
        this.rightCloseStatus = rightCloseStatus;
    }

}
