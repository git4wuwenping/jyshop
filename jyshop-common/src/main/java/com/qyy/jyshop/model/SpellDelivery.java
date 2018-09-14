package com.qyy.jyshop.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 拼团发货信息
 */
@Entity
@Table(name = "shop_spell_delivery")
public class SpellDelivery implements Serializable {

    /**
     * 发货单ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long deliveryId;

    /**
     * 发货类型0发货 1换货 2退货
     */
    @Column(name = "type")
    private Integer type;

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
     * 配送方式
     */
    @Column(name = "ship_type")
    private String shipType;

    /**
     * 物流公司ID
     */
    @Column(name = "logi_id")
    private Long logiId;

    /**
     * 物流公司代码
     */
    @Column(name = "logi_code")
    private String logiCode;

    /**
     * 物流公司名称
     */
    @Column(name = "logi_name")
    private String logiName;

    /**
     * 物流公司运单号
     */
    @Column(name = "logi_no")
    private String logiNo;

    /**
     * 物流费用
     */
    private BigDecimal money;

    /**
     * 收货人姓名
     */
    @Column(name = "ship_name")
    private String shipName;

    /**
     * 省ID
     */
    @Column(name = "province_id")
    private String provinceId;

    /**
     * 市ID
     */
    @Column(name = "city_id")
    private String cityId;

    /**
     * 区ID
     */
    @Column(name = "district_id")
    private String districtId;

    /**
     * 省
     */
    @Column(name = "province_name")
    private String provinceName; //省

    /**
     * 市
     */
    @Column(name = "city_name")
    private String cityName; //市

    /**
     * 区
     */
    @Column(name = "district_name")
    private String district_Name; //区

    /**
     * 收货地址
     */
    @Column(name = "ship_addr")
    private String shipAddr;

    /**
     * 邮编
     */
    @Column(name = "ship_zip")
    private String shipZip;

    /**
     * 收货人电话
     */
    @Column(name = "ship_tel")
    private String shipTel;

    /**
     * 收货人手机
     */
    @Column(name = "ship_mobile")
    private String shipMobile;

    /**
     * 收货人邮箱
     */
    @Column(name = "ship_email")
    private String shipEmail;

    /**
     * 发货仓库ID
     */
    @Column(name = "depot_id")
    private Long depotId;

    /**
     * 发货仓库编号
     */
    @Column(name = "depot_code")
    private String depotCode;

    /**
     * 发货仓库名称
     */
    @Column(name = "depot_name")
    private String depotName;

    /**
     * 操作用户ID
     */
    @Column(name = "op_id")
    private Long opId;

    /**
     * 操作用户名
     */
    @Column(name = "op_user")
    private String opUser;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Timestamp createDate;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public Long getLogiId() {
        return logiId;
    }

    public void setLogiId(Long logiId) {
        this.logiId = logiId;
    }

    public String getLogiCode() {
        return logiCode;
    }

    public void setLogiCode(String logiCode) {
        this.logiCode = logiCode;
    }

    public String getLogiName() {
        return logiName;
    }

    public void setLogiName(String logiName) {
        this.logiName = logiName;
    }

    public String getLogiNo() {
        return logiNo;
    }

    public void setLogiNo(String logiNo) {
        this.logiNo = logiNo;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrict_Name() {
        return district_Name;
    }

    public void setDistrict_Name(String district_Name) {
        this.district_Name = district_Name;
    }

    public String getShipAddr() {
        return shipAddr;
    }

    public void setShipAddr(String shipAddr) {
        this.shipAddr = shipAddr;
    }

    public String getShipZip() {
        return shipZip;
    }

    public void setShipZip(String shipZip) {
        this.shipZip = shipZip;
    }

    public String getShipTel() {
        return shipTel;
    }

    public void setShipTel(String shipTel) {
        this.shipTel = shipTel;
    }

    public String getShipMobile() {
        return shipMobile;
    }

    public void setShipMobile(String shipMobile) {
        this.shipMobile = shipMobile;
    }

    public String getShipEmail() {
        return shipEmail;
    }

    public void setShipEmail(String shipEmail) {
        this.shipEmail = shipEmail;
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

    public Long getOpId() {
        return opId;
    }

    public void setOpId(Long opId) {
        this.opId = opId;
    }

    public String getOpUser() {
        return opUser;
    }

    public void setOpUser(String opUser) {
        this.opUser = opUser;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}