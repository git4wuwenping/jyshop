package com.qyy.jyshop.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 拼团订单项
 */
@Entity
@Table(name = "shop_spell_order_items")
public class SpellOrderItems implements Serializable {

    /**
     * 订单项ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Timestamp createDate;

    /**
     * 最近一次修改时间
     */
    @Column(name = "last_modify")
    private Timestamp lastModify;

    /**
     * 订单ID
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 订单号
     */
    @Column(name = "order_sn")
    private String orderSn;

    /**
     * 商品ID
     */
    @Column(name = "goods_id")
    private Long goodsId;

    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 商品图片
     */
    @Column(name = "goods_image")
    private String goodsImage;

    /**
     * 货品ID
     */
    @Column(name = "product_id")
    private Long productId;

    /**
     * 货号
     */
    @Column(name = "product_sn")
    private String productSn;

    /**
     * 货品规格
     */
    @Column(name = "product_specs")
    private String productSpecs;

    /**
     * 商品价格
     */
    @Column(name = "goods_price")
    private BigDecimal goodsPrice;

    /**
     * 供应商ID
     */
    @Column(name = "com_id")
    private Long comId;

    /**
     * 店铺ID
     */
    @Column(name = "shop_store_id")
    private Long shopStoreId;

    /**
     * 商品重量
     */
    @Column(name = "weight")
    private BigDecimal weight;

    /**
     * 重量单位
     */
    @Column(name = "unit")
    private String unit;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getLastModify() {
        return lastModify;
    }

    public void setLastModify(Timestamp lastModify) {
        this.lastModify = lastModify;
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

    public String getProductSpecs() {
        return productSpecs;
    }

    public void setProductSpecs(String productSpecs) {
        this.productSpecs = productSpecs;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Long getComId() {
        return comId;
    }

    public void setComId(Long comId) {
        this.comId = comId;
    }

    public Long getShopStoreId() {
        return shopStoreId;
    }

    public void setShopStoreId(Long shopStoreId) {
        this.shopStoreId = shopStoreId;
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
}
