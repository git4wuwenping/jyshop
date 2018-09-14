package com.qyy.jyshop.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;
@Entity
@Table(name = "shop_company")
public class Company implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 供应商id
     */
    @Id
    @Column(name = "com_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer comId;

    /**
     * 店铺名称
     */
    @Column(name = "store_name")
    private String storeName;
    /**
     * 供应商名称
     */
    @Column(name = "com_name")
    private String comName;

    /**
     * 供应商类型
     */
    @Column(name = "com_type")
    private Integer comType;

    /**
     * 供应商公司地址
     */
    @Column(name = "com_address")
    private String comAddress;

    /**
     * 供应商联系人
     */
    @Column(name = "com_contact")
    private String comContact;

    /**
     * 供应商手机号
     */
    @Column(name = "com_mobile")
    private String comMobile;

    /**
     * 供应商电话
     */
    @Column(name = "com_tel")
    private String comTel;

    /**
     * 供应商email
     */
    @Column(name = "com_email")
    private String comEmail;

    /**
     * 是否可用
     */
    private Integer disabled;

    /**
     * 备注
     */
    private String remark;

    /**
     * 省份id
     */
    @Column(name = "province_id")
    private String provinceId;

    /**
     * 城市id
     */
    @Column(name = "city_id")
    private String cityId;

    /**
     * 县id
     */
    @Column(name = "region_id")
    private String regionId;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 县
     */
    private String region;
    
    /**
     * 保证金
     */
    private Integer bond;
    
    /**
     * 供应商编码
     */
    @Column(name = "com_code")
	private Long comCode;
    
	@Column(name = "parent_id")
	private Integer parentId;		//推荐人id
	
	@Column(name = "add_time")
	private Timestamp addTime;			//创建时间
	

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Timestamp getAddTime() {
		return addTime;
	}

	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}


    public Integer getBond() {
		return bond;
	}

	public void setBond(Integer bond) {
		this.bond = bond;
	}
	
	/**
     * 获取供应商id
     *
     * @return com_id - 供应商id
     */
    public Integer getComId() {
        return comId;
    }

    public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	/**
     * 设置供应商id
     *
     * @param comId 供应商id
     */
    public void setComId(Integer comId) {
        this.comId = comId;
    }

    /**
     * 获取供应商名称
     *
     * @return com_name - 供应商名称
     */
    public String getComName() {
        return comName;
    }

    /**
     * 设置供应商名称
     *
     * @param comName 供应商名称
     */
    public void setComName(String comName) {
        this.comName = comName;
    }

    /**
     * 获取供应商类型
     *
     * @return com_type - 供应商类型
     */
    public Integer getComType() {
        return comType;
    }

    /**
     * 设置供应商类型
     *
     * @param comType 供应商类型
     */
    public void setComType(Integer comType) {
        this.comType = comType;
    }

    /**
     * 获取供应商公司地址
     *
     * @return com_address - 供应商公司地址
     */
    public String getComAddress() {
        return comAddress;
    }

    /**
     * 设置供应商公司地址
     *
     * @param comAddress 供应商公司地址
     */
    public void setComAddress(String comAddress) {
        this.comAddress = comAddress;
    }

    /**
     * 获取供应商联系人
     *
     * @return com_contact - 供应商联系人
     */
    public String getComContact() {
        return comContact;
    }

    /**
     * 设置供应商联系人
     *
     * @param comContact 供应商联系人
     */
    public void setComContact(String comContact) {
        this.comContact = comContact;
    }

    /**
     * 获取供应商手机号
     *
     * @return com_mobile - 供应商手机号
     */
    public String getComMobile() {
        return comMobile;
    }

    /**
     * 设置供应商手机号
     *
     * @param comMobile 供应商手机号
     */
    public void setComMobile(String comMobile) {
        this.comMobile = comMobile;
    }

    /**
     * 获取供应商电话
     *
     * @return com_tel - 供应商电话
     */
    public String getComTel() {
        return comTel;
    }

    /**
     * 设置供应商电话
     *
     * @param comTel 供应商电话
     */
    public void setComTel(String comTel) {
        this.comTel = comTel;
    }

    /**
     * 获取供应商email
     *
     * @return com_email - 供应商email
     */
    public String getComEmail() {
        return comEmail;
    }

    /**
     * 设置供应商email
     *
     * @param comEmail 供应商email
     */
    public void setComEmail(String comEmail) {
        this.comEmail = comEmail;
    }

    /**
     * 获取是否可用
     *
     * @return disabled - 是否可用
     */
    public Integer getDisabled() {
        return disabled;
    }

    /**
     * 设置是否可用
     *
     * @param disabled 是否可用
     */
    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取省份id
     *
     * @return province_id - 省份id
     */
    public String getProvinceId() {
        return provinceId;
    }

    /**
     * 设置省份id
     *
     * @param provinceId 省份id
     */
    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    /**
     * 获取城市id
     *
     * @return city_id - 城市id
     */
    public String getCityId() {
        return cityId;
    }

    /**
     * 设置城市id
     *
     * @param cityId 城市id
     */
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    /**
     * 获取县id
     *
     * @return region_id - 县id
     */
    public String getRegionId() {
        return regionId;
    }

    /**
     * 设置县id
     *
     * @param regionId 县id
     */
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    /**
     * 获取省份
     *
     * @return province - 省份
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省份
     *
     * @param province 省份
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取城市
     *
     * @return city - 城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置城市
     *
     * @param city 城市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取县
     *
     * @return region - 县
     */
    public String getRegion() {
        return region;
    }

    /**
     * 设置县
     *
     * @param region 县
     */
    public void setRegion(String region) {
        this.region = region;
    }

	public Long getComCode() {
		return comCode;
	}

	public void setComCode(Long comCode) {
		this.comCode = comCode;
	}
}
