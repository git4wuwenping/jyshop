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
@Table(name = "shop_bargain_order_items")
public class BargainOrderItems  implements Serializable{

    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId; //主键ID
    
    @Column(name = "order_id")
    private Long orderId; //订单ID
    
    @Column(name = "order_sn")
    private String orderSn; //订单号
    
    @Column(name = "bargain_id")
    private Long bargainId; //砍价活动ID
    
    @Column(name = "goods_id")
    private Long goodsId; //砍价商品ID
    
    private String goodsName; //砍价商品名称

    private String goodsImage; //砍价商品图片
    
    @Column(name = "bargain_type")
    private Integer bargainType; //砍价类型 0:常规 1:共享 2:免单
    
    @Column(name = "goods_price")
    private BigDecimal goodsPrice; //商品价格
    
    @Column(name = "target_price")
    private BigDecimal targetPrice; //标靶价格
    
    @Column(name = "is_first")
    private Integer isFirst; //是否首刀砍价 0:否 1:是
    
    @Column(name = "first_price_min")
    private BigDecimal firstPriceMin; //首刀最低金额
    
    @Column(name = "first_price_max")
    private BigDecimal firstPriceMax; //首刀最大金额
    
    @Column(name = "bargain_mode")
    private Integer bargainMode; //砍价方式 0:固定金额 1:范围金额
    
    @Column(name = "price_min")
    private BigDecimal priceMin; //砍价最低金额
    
    @Column(name = "price_max")
    private BigDecimal priceMax; //砍价最大金额
    
    @Column(name = "is_free")
    private Integer isFree; //发起砍价人是否免单 0:否 1:是
    
    @Column(name = "free_price")
    private BigDecimal freePrice; //免单金额
    
    @Column(name = "take_date")
    private Timestamp takeDate; //砍价任务时间
    
    @Column(name = "member_ids")
    private String memberIds; //砍价用户Id数组
    
    @Column(name = "bargain_prices")
    private BigDecimal bargainPrices; //总砍价金额
    
    @Column(name = "bargain_moneys")
    private String bargainMoneys; //砍价金额数组
    
    @Column(name = "bargain_num")
    private Integer bargainNum; //总砍价次数
    
    @Transient
    private Integer buyNum; //购买人数
    @Transient
    private String details; //详情
    @Transient
    private String shareTitle; //分享标题
    @Transient
    private String shareDescribe; //分享描述
    @Transient
    private String rule; //活动规则
    
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

    public Integer getBargainType() {
        return bargainType;
    }

    public void setBargainType(Integer bargainType) {
        this.bargainType = bargainType;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(BigDecimal targetPrice) {
        this.targetPrice = targetPrice;
    }

    public Integer getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(Integer isFirst) {
        this.isFirst = isFirst;
    }

    public BigDecimal getFirstPriceMin() {
        return firstPriceMin;
    }

    public void setFirstPriceMin(BigDecimal firstPriceMin) {
        this.firstPriceMin = firstPriceMin;
    }

    public BigDecimal getFirstPriceMax() {
        return firstPriceMax;
    }

    public void setFirstPriceMax(BigDecimal firstPriceMax) {
        this.firstPriceMax = firstPriceMax;
    }

    public Integer getBargainMode() {
        return bargainMode;
    }

    public void setBargainMode(Integer bargainMode) {
        this.bargainMode = bargainMode;
    }

    public BigDecimal getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(BigDecimal priceMin) {
        this.priceMin = priceMin;
    }

    public BigDecimal getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(BigDecimal priceMax) {
        this.priceMax = priceMax;
    }

    public Integer getIsFree() {
        return isFree;
    }

    public void setIsFree(Integer isFree) {
        this.isFree = isFree;
    }

    public BigDecimal getFreePrice() {
        return freePrice;
    }

    public void setFreePrice(BigDecimal freePrice) {
        this.freePrice = freePrice;
    }

    public Timestamp getTakeDate() {
        return takeDate;
    }

    public void setTakeDate(Timestamp takeDate) {
        this.takeDate = takeDate;
    }

    public String getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(String memberIds) {
        this.memberIds = memberIds;
    }

    public BigDecimal getBargainPrices() {
        return bargainPrices;
    }

    public void setBargainPrices(BigDecimal bargainPrices) {
        this.bargainPrices = bargainPrices;
    }

    public String getBargainMoneys() {
        return bargainMoneys;
    }

    public void setBargainMoneys(String bargainMoneys) {
        this.bargainMoneys = bargainMoneys;
    }

    public Integer getBargainNum() {
        return bargainNum;
    }

    public void setBargainNum(Integer bargainNum) {
        this.bargainNum = bargainNum;
    }

    public Integer getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareDescribe() {
        return shareDescribe;
    }

    public void setShareDescribe(String shareDescribe) {
        this.shareDescribe = shareDescribe;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
    
}
