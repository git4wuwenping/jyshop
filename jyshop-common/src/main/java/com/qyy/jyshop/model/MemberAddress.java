package com.qyy.jyshop.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户收货地址
 */
@Entity
@Table(name = "shop_member_address")
public class MemberAddress implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addr_id")
    private Long addrId; //地址id
    
    @Column(name = "member_id")
    private Long memberId; //会员id
    private String name; //姓名
    
    @Column(name = "province_id")
    private String provinceId; //省份id
    
    @Column(name = "province_name")
    private String provinceName; //省份名称
    
    @Column(name = "city_id")
    private String cityId; //城市id
    
    @Column(name = "city_name")
    private String cityName; //城市名称
    
    @Column(name = "district_id")
    private String districtId; //县id 
    
    @Column(name = "district_name")
    private String districtName; //县名称
    
    @Column(name = "address_detail")
    private String addressDetail; //详细地址
    private String zip; //邮编
    private String tel; //电话
    private String mobile; //手机号
    
    @Column(name = "def_addr")
    private Integer defAddr; //默认地址 1:默认地址

    public Long getAddrId() {
        return addrId;
    }

    public void setAddrId(Long addrId) {
        this.addrId = addrId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getDefAddr() {
        return defAddr;
    }

    public void setDefAddr(Integer defAddr) {
        this.defAddr = defAddr;
    }
  
}
