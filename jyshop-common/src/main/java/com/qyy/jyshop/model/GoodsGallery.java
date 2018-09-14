package com.qyy.jyshop.model;

import java.io.Serializable;

import javax.persistence.*;
@Entity
@Table(name = "shop_goods_gallery")
public class GoodsGallery implements Serializable{
    /**
     * 主键
     */
	private static final long serialVersionUID = 1L;
	
    @Id
    @Column(name = "img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imgId;

    /**
     * 商品id
     */
    @Column(name = "goods_id")
    private Long goodsId;

    /**
     * 图片
     */
    private String image;

    /**
     * 是否默认
     */
    private Integer isdefault;

    /**
     * 获取主键
     *
     * @return img_id - 主键
     */
    public Integer getImgId() {
        return imgId;
    }

    /**
     * 设置主键
     *
     * @param imgId 主键
     */
    public void setImgId(Integer imgId) {
        this.imgId = imgId;
    }

    /**
     * 获取商品id
     *
     * @return goods_id - 商品id
     */
    public Long getGoodsId() {
        return goodsId;
    }

    /**
     * 设置商品id
     *
     * @param goodsId 商品id
     */
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
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