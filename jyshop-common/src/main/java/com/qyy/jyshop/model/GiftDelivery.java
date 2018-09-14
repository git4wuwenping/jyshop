package com.qyy.jyshop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shop_gift_delivery")
public class GiftDelivery implements Serializable{

    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long deliveryId; //货运单         
    
    private Integer type; //发货类型0发货 1换货 2退货
    
    @Column(name = "order_id")
    private Long orderId; //订单id
    
    @Column(name = "order_sn")
    private String orderSn; //订单号         
    
    @Column(name = "member_id")
    private Long memberId; //会员id
    private BigDecimal money; //物流费用   
    
    @Column(name = "ship_type")
    private String shipType; //配送方式           
    
    @Column(name = "logi_id")
    private Integer logiId; //物流公司id  
    
    @Column(name = "logi_code")
    private String logiCode; //物流公司代码       
    
    @Column(name = "logi_name")
    private String logiName; //物流公司名称       
    
    @Column(name = "logi_no")
    private String logiNo; //配送公司运单号    
    
    @Column(name = "ship_name")
    private String shipName; //收货人         
    
    @Column(name = "province_id")
    private String provinceId; //省id 
    
    @Column(name = "city_id")
    private String cityId; //市id   
    
    @Column(name = "district_id")
    private String districtId; //区id   
    private String province; //省     
    private String city; //市     
    private String district; //区     
    
    @Column(name = "ship_addr")
    private String shipAddr; //收货地址           
    
    @Column(name = "ship_zip")
    private String shipZip; //邮编  
    
    @Column(name = "ship_tel")
    private String shipTel; //收货人电话          
    
    @Column(name = "ship_mobile")
    private String shipMobile; //收货人手机        
    
    @Column(name = "ship_email")
    private String shipEmail; //收货人邮箱          
    
    @Column(name = "depot_id")
    private Long depotId; //发货仓库id         
    
    @Column(name = "depot_code")
    private String depotCode; //发货仓库编号       
    
    @Column(name = "depot_name")
    private String depotName; //发货仓库名称       
    
    @Column(name = "op_id")
    private Long opId; //操作用户id          
    
    @Column(name = "op_user")
    private String opUser; //操作用户名          
    
    @Column(name = "creation_time")
    private Timestamp creationTime; //创建时间     
    private String remark; //备注 
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
    public Long getMemberId() {
        return memberId;
    }
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public String getShipType() {
        return shipType;
    }
    public void setShipType(String shipType) {
        this.shipType = shipType;
    }
    public Integer getLogiId() {
        return logiId;
    }
    public void setLogiId(Integer logiId) {
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
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district;
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
    public Timestamp getCreationTime() {
        return creationTime;
    }
    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
