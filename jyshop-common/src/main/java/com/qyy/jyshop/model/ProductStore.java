package com.qyy.jyshop.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shop_product_store")
public class ProductStore implements Serializable{

    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long storeId; //主键id
    
    @Column(name = "goods_id")
    private Long goodsId; //商品id
    
    @Column(name = "goods_sn")
    private String goodsSn; //商品号
    
    @Column(name = "com_id")
    private Integer comId; //商品所属供应商id
    
    @Column(name = "com_name")
    private String comName; //商品所属供应商名称
    
    @Column(name = "product_id")
    private Long productId; //货品id
    
    @Column(name = "product_sn")
    private String productSn; //货号
    
    @Column(name = "freeze_store")
    private Integer freezeStore; //冻结库存
    private Integer store; //库存
    
    @Column(name = "usable_store")
    private Integer usableStore; //可用库存
    
    @Column(name = "disabled_store")
    private Integer disabledStore; //不可用库存
    
    @Column(name = "depot_id")
    private Long depotId; //仓库id
    
    @Column(name = "depot_code")
    private String depotCode; //仓库编号
    
    @Column(name = "depot_name")
    private String depotName; //仓库名称
    
    @Column(name = "creation_time")
    private Timestamp creationTime; //创建时间

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsSn() {
        return goodsSn;
    }

    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
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

    public Integer getFreezeStore() {
        return freezeStore;
    }

    public void setFreezeStore(Integer freezeStore) {
        this.freezeStore = freezeStore;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public Integer getUsableStore() {
        return usableStore;
    }

    public void setUsableStore(Integer usableStore) {
        this.usableStore = usableStore;
    }

    public Integer getDisabledStore() {
        return disabledStore;
    }

    public void setDisabledStore(Integer disabledStore) {
        this.disabledStore = disabledStore;
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public void setDepotCode(String depotCode) {
        this.depotCode = depotCode;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }
    
}
