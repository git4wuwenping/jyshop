package com.qyy.jyshop.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shop_gift_order_items")
public class GiftOrderItems implements Serializable{

    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId; //主键id
    
    @Column(name = "order_id")
    private Long orderId; //订单id
    
    @Column(name = "order_sn")
    private String orderSn; //订单号
    
    @Column(name = "goods_id")
    private Long goodsId; //商品id
    
    @Column(name = "goods_name")
    private String goodsName; //商品名称
    
    @Column(name = "goods_image")
    private String goodsImage; //商品图像
    
    @Column(name = "product_id")
    private Long productId; //货品id
    
    @Column(name = "product_sn")
    private String productSn; //货品号
    
    @Column(name = "buy_count")
    private Integer buyCount; //销售量
    
    @Column(name = "ship_count")
    private Integer shipCount; //配送数量
    
    
    @Column(name = "goods_price")
    private BigDecimal goodsPrice; //商品价格
    private BigDecimal cost; //成本价
    private BigDecimal price; //销售金额
    private BigDecimal discount; //优惠价格
    
    @Column(name = "activity_id")
    private Integer activityId; //活动id
    
    @Column(name = "activity_name")
    private String activityName; //活动名称
    
    @Column(name = "activity_discount")
    private BigDecimal activityDiscount; //活动优惠价格
    
    @Column(name = "product_spec")
    private String productSpec; //货品规格
    
    @Column(name = "change_product_num")
    private Integer changeProductNum; //换货货品数量
    private Integer status; //0.正常 1.售后处理中  2已换货 3已退货
    
    @Column(name = "com_id")
    private Integer comId; //供应商id
    private BigDecimal weight; //商品重量(商品数*单个重量)
    private String unit; //重量单位
    
    @Column(name = "evaluate_status")
    private Integer evaluateStatus; //评论状态0未评论 1己评论

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductSn() {
        return productSn;
    }

    public void setProductSn(String productSn) {
        this.productSn = productSn;
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public Integer getShipCount() {
        return shipCount;
    }

    public void setShipCount(Integer shipCount) {
        this.shipCount = shipCount;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public BigDecimal getActivityDiscount() {
        return activityDiscount;
    }

    public void setActivityDiscount(BigDecimal activityDiscount) {
        this.activityDiscount = activityDiscount;
    }

    public String getProductSpec() {
        return productSpec;
    }

    public void setProductSpec(String productSpec) {
        this.productSpec = productSpec;
    }

    public Integer getChangeProductNum() {
        return changeProductNum;
    }

    public void setChangeProductNum(Integer changeProductNum) {
        this.changeProductNum = changeProductNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getEvaluateStatus() {
        return evaluateStatus;
    }

    public void setEvaluateStatus(Integer evaluateStatus) {
        this.evaluateStatus = evaluateStatus;
    }
    
}
