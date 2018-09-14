package com.qyy.jyshop.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "shop_giftpackage_product")
public class GiftPackageProduct {
    /**
     * 货品id
     */
    @Id
    @Column(name = "gp_product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gpProductId;

    /**
     * 商品id
     */
    @Column(name = "gp_id")
    private Long gpId;

    /**
     * 货品名称
     */
    private String name;

    /**
     * 货品sn
     */
    private String sn;

    /**
     * 货品库存
     */
    private Integer store;

    /**
     * 货品价格
     */
    private BigDecimal price;

    /**
     * 成本
     */
    private BigDecimal cost;

    /**
     * 重量
     */
    private BigDecimal weight;

    /**
     * 该产品的库存预警值
     */
    @Column(name = "alarm_num")
    private Short alarmNum;

    /**
     * 是否可用 0:不可用 1:可用
     */
    @Column(name = "is_usable")
    private Byte isUsable;

    /**
     * 规格
     */
    private String specs;

    /**
     * 规格ids
     */
    @Column(name = "spec_ids")
    private String specIds;

    /**
     * 规格值ids
     */
    @Column(name = "spec_value_ids")
    private String specValueIds;

    /**
     * 获取货品id
     *
     * @return gp_product_id - 货品id
     */
    public Long getGpProductId() {
        return gpProductId;
    }

    /**
     * 设置货品id
     *
     * @param gpProductId 货品id
     */
    public void setGpProductId(Long gpProductId) {
        this.gpProductId = gpProductId;
    }

    /**
     * 获取商品id
     *
     * @return gp_id - 商品id
     */
    public Long getGpId() {
        return gpId;
    }

    /**
     * 设置商品id
     *
     * @param gpId 商品id
     */
    public void setGpId(Long gpId) {
        this.gpId = gpId;
    }

    /**
     * 获取货品名称
     *
     * @return name - 货品名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置货品名称
     *
     * @param name 货品名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取货品sn
     *
     * @return sn - 货品sn
     */
    public String getSn() {
        return sn;
    }

    /**
     * 设置货品sn
     *
     * @param sn 货品sn
     */
    public void setSn(String sn) {
        this.sn = sn;
    }

    /**
     * 获取货品库存
     *
     * @return store - 货品库存
     */
    public Integer getStore() {
        return store;
    }

    /**
     * 设置货品库存
     *
     * @param store 货品库存
     */
    public void setStore(Integer store) {
        this.store = store;
    }

    /**
     * 获取货品价格
     *
     * @return price - 货品价格
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置货品价格
     *
     * @param price 货品价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取成本
     *
     * @return cost - 成本
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * 设置成本
     *
     * @param cost 成本
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * 获取重量
     *
     * @return weight - 重量
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * 设置重量
     *
     * @param weight 重量
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /**
     * 获取该产品的库存预警值
     *
     * @return alarm_num - 该产品的库存预警值
     */
    public Short getAlarmNum() {
        return alarmNum;
    }

    /**
     * 设置该产品的库存预警值
     *
     * @param alarmNum 该产品的库存预警值
     */
    public void setAlarmNum(Short alarmNum) {
        this.alarmNum = alarmNum;
    }

    /**
     * 获取是否可用 0:不可用 1:可用
     *
     * @return is_usable - 是否可用 0:不可用 1:可用
     */
    public Byte getIsUsable() {
        return isUsable;
    }

    /**
     * 设置是否可用 0:不可用 1:可用
     *
     * @param isUsable 是否可用 0:不可用 1:可用
     */
    public void setIsUsable(Byte isUsable) {
        this.isUsable = isUsable;
    }

    /**
     * 获取规格
     *
     * @return specs - 规格
     */
    public String getSpecs() {
        return specs;
    }

    /**
     * 设置规格
     *
     * @param specs 规格
     */
    public void setSpecs(String specs) {
        this.specs = specs;
    }

    /**
     * 获取规格ids
     *
     * @return spec_ids - 规格ids
     */
    public String getSpecIds() {
        return specIds;
    }

    /**
     * 设置规格ids
     *
     * @param specIds 规格ids
     */
    public void setSpecIds(String specIds) {
        this.specIds = specIds;
    }

    /**
     * 获取规格值ids
     *
     * @return spec_value_ids - 规格值ids
     */
    public String getSpecValueIds() {
        return specValueIds;
    }

    /**
     * 设置规格值ids
     *
     * @param specValueIds 规格值ids
     */
    public void setSpecValueIds(String specValueIds) {
        this.specValueIds = specValueIds;
    }
}