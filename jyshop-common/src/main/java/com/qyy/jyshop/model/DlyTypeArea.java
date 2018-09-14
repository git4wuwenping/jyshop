package com.qyy.jyshop.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "shop_dly_type_area")
public class DlyTypeArea {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 配送方式id
     */
    @Column(name = "type_id")
    private Long typeId;

    /**
     * 首重
     */
    @Column(name = "first_weight")
    private BigDecimal firstWeight;

    /**
     * 首重价格
     */
    @Column(name = "first_weight_price")
    private BigDecimal firstWeightPrice;

    /**
     * 续重
     */
    @Column(name = "additional_weight")
    private BigDecimal additionalWeight;

    /**
     * 续重价格
     */
    @Column(name = "additional_weight_price")
    private BigDecimal additionalWeightPrice;

    /**
     * 地区id
     */
    @Column(name = "area_id_group")
    private String areaIdGroup;

    /**
     * 地区
     */
    @Column(name = "area_name_group")
    private String areaNameGroup;

    /**
     * 是否包邮 0不包邮  1包邮
     */
    @Column(name = "free_dly")
    private Integer freeDly;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    /**
     * 获取首重
     *
     * @return first_weight - 首重
     */
    public BigDecimal getFirstWeight() {
        return firstWeight;
    }

    /**
     * 设置首重
     *
     * @param firstWeight 首重
     */
    public void setFirstWeight(BigDecimal firstWeight) {
        this.firstWeight = firstWeight;
    }

    /**
     * 获取首重价格
     *
     * @return first_weight_price - 首重价格
     */
    public BigDecimal getFirstWeightPrice() {
        return firstWeightPrice;
    }

    /**
     * 设置首重价格
     *
     * @param firstWeightPrice 首重价格
     */
    public void setFirstWeightPrice(BigDecimal firstWeightPrice) {
        this.firstWeightPrice = firstWeightPrice;
    }

    /**
     * 获取续重
     *
     * @return additional_weight - 续重
     */
    public BigDecimal getAdditionalWeight() {
        return additionalWeight;
    }

    /**
     * 设置续重
     *
     * @param additionalWeight 续重
     */
    public void setAdditionalWeight(BigDecimal additionalWeight) {
        this.additionalWeight = additionalWeight;
    }

    /**
     * 获取续重价格
     *
     * @return additional_weight_price - 续重价格
     */
    public BigDecimal getAdditionalWeightPrice() {
        return additionalWeightPrice;
    }

    /**
     * 设置续重价格
     *
     * @param additionalWeightPrice 续重价格
     */
    public void setAdditionalWeightPrice(BigDecimal additionalWeightPrice) {
        this.additionalWeightPrice = additionalWeightPrice;
    }

    /**
     * 获取地区id
     *
     * @return area_id_group - 地区id
     */
    public String getAreaIdGroup() {
        return areaIdGroup;
    }

    /**
     * 设置地区id
     *
     * @param areaIdGroup 地区id
     */
    public void setAreaIdGroup(String areaIdGroup) {
        this.areaIdGroup = areaIdGroup;
    }

    /**
     * 获取地区
     *
     * @return area_name_group - 地区
     */
    public String getAreaNameGroup() {
        return areaNameGroup;
    }

    /**
     * 设置地区
     *
     * @param areaNameGroup 地区
     */
    public void setAreaNameGroup(String areaNameGroup) {
        this.areaNameGroup = areaNameGroup;
    }

    public Integer getFreeDly() {
        return freeDly;
    }

    public void setFreeDly(Integer freeDly) {
        this.freeDly = freeDly;
    }

  
}