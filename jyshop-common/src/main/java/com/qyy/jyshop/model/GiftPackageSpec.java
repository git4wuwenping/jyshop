package com.qyy.jyshop.model;

import javax.persistence.*;

@Table(name = "shop_giftpackage_spec")
public class GiftPackageSpec {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 规格id
     */
    @Column(name = "spec_id")
    private Long specId;

    /**
     * 规格值id
     */
    @Column(name = "spec_value_id")
    private Long specValueId;

    /**
     * 礼包id
     */
    @Column(name = "gp_id")
    private Long gpId;

    /**
     * 礼包货品id
     */
    @Column(name = "gp_product_id")
    private Long gpProductId;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取规格id
     *
     * @return spec_id - 规格id
     */
    public Long getSpecId() {
        return specId;
    }

    /**
     * 设置规格id
     *
     * @param specId 规格id
     */
    public void setSpecId(Long specId) {
        this.specId = specId;
    }

    /**
     * 获取规格值id
     *
     * @return spec_value_id - 规格值id
     */
    public Long getSpecValueId() {
        return specValueId;
    }

    /**
     * 设置规格值id
     *
     * @param specValueId 规格值id
     */
    public void setSpecValueId(Long specValueId) {
        this.specValueId = specValueId;
    }

    /**
     * 获取礼包id
     *
     * @return gp_id - 礼包id
     */
    public Long getGpId() {
        return gpId;
    }

    /**
     * 设置礼包id
     *
     * @param gpId 礼包id
     */
    public void setGpId(Long gpId) {
        this.gpId = gpId;
    }

    /**
     * 获取礼包货品id
     *
     * @return gp_product_id - 礼包货品id
     */
    public Long getGpProductId() {
        return gpProductId;
    }

    /**
     * 设置礼包货品id
     *
     * @param gpProductId 礼包货品id
     */
    public void setGpProductId(Long gpProductId) {
        this.gpProductId = gpProductId;
    }
}