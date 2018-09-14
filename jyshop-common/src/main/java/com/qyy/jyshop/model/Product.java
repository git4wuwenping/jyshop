/*
 * Copyright (c) 2005 2017 CHEERTEA Technology Co.Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 货品
 */
@Entity
@Table(name = "shop_product")
public class Product implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId; //货品id
    
    @Column(name = "goods_id")
    private Long goodsId; // 商品id
    
    private String name; //货品名称
    private String sn; //货品sn
    private Integer store; //货品库存
    private BigDecimal price; //货品价格
    private String specs; //规格
    private BigDecimal cost; //成本
    private BigDecimal weight; //重量
    
    @Column(name = "alarm_num")
    private String alarmNum; //产品的库存预警值
    
    @Column(name = "is_usable")
    private Integer isUsable; //是否可用
    
    @Column(name = "spec_ids")
    private String specIds;
    
    @Column(name = "spec_value_ids")
    private String specValueIds;

    @Column(name = "com_id")
    private Integer comId;
  
    
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getAlarmNum() {
        return alarmNum;
    }

    public void setAlarmNum(String alarmNum) {
        this.alarmNum = alarmNum;
    }

    public Integer getIsUsable() {
        return isUsable;
    }

    public void setIsUsable(Integer isUsable) {
        this.isUsable = isUsable;
    }

    public String getSpecIds() {
        return specIds;
    }

    public void setSpecIds(String specIds) {
        this.specIds = specIds;
    }

    public String getSpecValueIds() {
        return specValueIds;
    }

    public void setSpecValueIds(String specValueIds) {
        this.specValueIds = specValueIds;
    }

	public Integer getComId() {
		return comId;
	}

	public void setComId(Integer comId) {
		this.comId = comId;
	}
   

}
