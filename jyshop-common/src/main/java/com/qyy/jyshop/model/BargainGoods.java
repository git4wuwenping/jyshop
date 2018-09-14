/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 砍价活动
 * @author wu
 * @created 2018/4/11 16:26
 */
@Entity
@Table(name = "shop_bargain_goods")
public class BargainGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "bargain_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bargainId;   //主键ID
    
    private String title;   //标题
    
    @Column(name = "goods_id")
    private Long goodsId;   //商品ID
    
    @Column(name = "bargain_type")
    private Integer bargainType;   //砍价类型 0:常规 1:共享 2:免单

    @Column(name = "goods_price")
    private BigDecimal goodsPrice;  //商品价格

    @Column(name = "target_price")
    private BigDecimal targetPrice;  //标靶价格

    @Column(name = "is_first")
    private Integer isFirst;  //是否首刀砍价 0:否 1:是

    @Column(name = "first_price_min")
    private BigDecimal firstPriceMin;  //首刀最低金额

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

    private String image; //活动图片

    private String details; //详情

    @Column(name = "share_title")
    private String shareTitle; //分享标题

    @Column(name = "share_describe")
    private String shareDescribe; //分享描述

    private String rule; //活动规则

    @Column(name = "start_date")
    private Timestamp startDate; //活动开始时间

    @Column(name = "end_date")
    private Timestamp endDate; //活动结束时间

    private Integer takeDate; //砍价任务时间

    @Column(name = "create_date")
    private Timestamp createDate; //创建时间

    @Column(name = "is_open")
    private Integer isOpen; //是否开启 0:关闭 1:开启

    public Long getBargainId() {
        return bargainId;
    }

    public void setBargainId(Long bargainId) {
        this.bargainId = bargainId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public Integer getTakeDate() {
        return takeDate;
    }

    public void setTakeDate(Integer takeDate) {
        this.takeDate = takeDate;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Integer getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Integer isOpen) {
        this.isOpen = isOpen;
    }
}
