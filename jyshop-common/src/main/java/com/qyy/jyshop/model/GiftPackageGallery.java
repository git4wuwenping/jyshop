package com.qyy.jyshop.model;

import javax.persistence.*;

@Table(name = "shop_giftpackage_gallery")
public class GiftPackageGallery {
    @Id
    @Column(name = "img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imgId;

    /**
     * 礼包id
     */
    @Column(name = "gp_id")
    private Long gpId;

    /**
     * 图片
     */
    private String image;

    /**
     * 是否默认
     */
    private Integer isdefault;

    /**
     * @return img_id
     */
    public Integer getImgId() {
        return imgId;
    }

    /**
     * @param imgId
     */
    public void setImgId(Integer imgId) {
        this.imgId = imgId;
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
     * 获取图片
     *
     * @return image - 图片
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置图片
     *
     * @param image 图片
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * 获取是否默认
     *
     * @return isdefault - 是否默认
     */
    public Integer getIsdefault() {
        return isdefault;
    }

    /**
     * 设置是否默认
     *
     * @param isdefault 是否默认
     */
    public void setIsdefault(Integer isdefault) {
        this.isdefault = isdefault;
    }
}