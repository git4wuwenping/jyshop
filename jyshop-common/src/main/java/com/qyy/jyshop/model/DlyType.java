package com.qyy.jyshop.model;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Table(name = "shop_dly_type")
public class DlyType {
    /**
     * 主键
     */
    @Id
    @Column(name = "type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeId;

    /**
     * 配送方式名称
     */
    private String name;

    /**
     * 供应商id
     */
    @Column(name = "com_id")
    private Integer comId;

    /**
     * 是否统一配置 0:不是 1:是
     */
    @Column(name = "is_same")
    private Integer isSame;

    /**
     * 首重
     */
    @Column(name = "first_weight")
    private BigDecimal firstWeight;

    /**
     * 续重
     */
    @Column(name = "additional_weight")
    private BigDecimal additionalWeight;

    /**
     * 首重价格
     */
    @Column(name = "first_weight_price")
    private BigDecimal firstWeightPrice;

    /**
     * 续重价格
     */
    @Column(name = "additional_weight_price")
    private BigDecimal additionalWeightPrice;

    
    @Transient
    private List<DlyTypeArea> dlyTypeAreaList;
    
    /**
     * 是否可用 0:不可用 1:可用
     */
    private Integer disabled;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    /**
     * 获取配送方式名称
     *
     * @return name - 配送方式名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置配送方式名称
     *
     * @param name 配送方式名称
     */
    public void setName(String name) {
        this.name = name;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }
    
    public Integer getIsSame() {
        return isSame;
    }

    public void setIsSame(Integer isSame) {
        this.isSame = isSame;
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

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<DlyTypeArea> getDlyTypeAreaList() {
        return dlyTypeAreaList;
    }

    public void setDlyTypeAreaList(List<DlyTypeArea> dlyTypeAreaList) {
        this.dlyTypeAreaList = dlyTypeAreaList;
    }
    
    
}