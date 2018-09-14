/*
 * Copyright (c) 2005, 2017, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 地区
 */
@Entity
@Table(name = "bs_address")
public class Address implements Serializable{

    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private String addressId;
    
    @Column(name = "address_name")
    private String addressName;
    
    @Column(name = "name_py")
    private String namePy;
    
    @Column(name = "name_spy")
    private String nameSpy;
    
    @Column(name = "parent_id")
    private String parentId;
    
    @Column(name = "region_id")
    private String regionId;
    private String region;
    
    @Column(name = "region_py")
    private String regionPy;
    
    @Column(name = "region_spy")
    private String regionSpy;
    
    @Column(name = "country_id")
    private String countryId;
    private String country;
    
    @Column(name = "country_py")
    private String countryPy;
    
    @Column(name = "country_spy")
    private String countrySpy;
    
    @Column(name = "province_id")
    private String provinceId;
    private String province;
    
    @Column(name = "province_py")
    private String provincePy;
    
    @Column(name = "province_spy")
    private String provinceSpy;
    
    @Column(name = "city_id")
    private String cityId;
    private String city;
    
    @Column(name = "city_py")
    private String cityPy;
    
    @Column(name = "city_spy")
    private String citySpy;
    
    @Column(name = "district_id")
    private String districtId;
    private String district;
    
    @Column(name = "district_py")
    private String districtPy;
    
    @Column(name = "district_spy")
    private String districtSpy;
    
    @Column(name = "town_id")
    private String townId;
    private String town;
    
    @Column(name = "town_py")
    private String townPy;
    
    @Column(name = "town_spy")
    private String townSpy;
    
    @Column(name = "street_id")
    private String streetId;
    private String street;
    
    @Column(name = "street_py")
    private String streetPy;
    
    @Column(name = "street_spy")
    private String streetSpy;
    
    @Column(name = "address_class")
    private String addressClass;
    private String x;
    private String y;
    
    @Transient
    private List<Address> addressList;
    
    public String getAddressId() {
        return addressId;
    }
    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
    public String getAddressName() {
        return addressName;
    }
    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }
    public String getNamePy() {
        return namePy;
    }
    public void setNamePy(String namePy) {
        this.namePy = namePy;
    }
    public String getNameSpy() {
        return nameSpy;
    }
    public void setNameSpy(String nameSpy) {
        this.nameSpy = nameSpy;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getRegionId() {
        return regionId;
    }
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public String getRegionPy() {
        return regionPy;
    }
    public void setRegionPy(String regionPy) {
        this.regionPy = regionPy;
    }
    public String getRegionSpy() {
        return regionSpy;
    }
    public void setRegionSpy(String regionSpy) {
        this.regionSpy = regionSpy;
    }
    public String getCountryId() {
        return countryId;
    }
    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCountryPy() {
        return countryPy;
    }
    public void setCountryPy(String countryPy) {
        this.countryPy = countryPy;
    }
    public String getCountrySpy() {
        return countrySpy;
    }
    public void setCountrySpy(String countrySpy) {
        this.countrySpy = countrySpy;
    }
    public String getProvinceId() {
        return provinceId;
    }
    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getProvincePy() {
        return provincePy;
    }
    public void setProvincePy(String provincePy) {
        this.provincePy = provincePy;
    }
    public String getProvinceSpy() {
        return provinceSpy;
    }
    public void setProvinceSpy(String provinceSpy) {
        this.provinceSpy = provinceSpy;
    }
    public String getCityId() {
        return cityId;
    }
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCityPy() {
        return cityPy;
    }
    public void setCityPy(String cityPy) {
        this.cityPy = cityPy;
    }
    public String getCitySpy() {
        return citySpy;
    }
    public void setCitySpy(String citySpy) {
        this.citySpy = citySpy;
    }
    public String getDistrictId() {
        return districtId;
    }
    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }
    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public String getDistrictPy() {
        return districtPy;
    }
    public void setDistrictPy(String districtPy) {
        this.districtPy = districtPy;
    }
    public String getDistrictSpy() {
        return districtSpy;
    }
    public void setDistrictSpy(String districtSpy) {
        this.districtSpy = districtSpy;
    }
    public String getTownId() {
        return townId;
    }
    public void setTownId(String townId) {
        this.townId = townId;
    }
    public String getTown() {
        return town;
    }
    public void setTown(String town) {
        this.town = town;
    }
    public String getTownPy() {
        return townPy;
    }
    public void setTownPy(String townPy) {
        this.townPy = townPy;
    }
    public String getTownSpy() {
        return townSpy;
    }
    public void setTownSpy(String townSpy) {
        this.townSpy = townSpy;
    }
    public String getStreetId() {
        return streetId;
    }
    public void setStreetId(String streetId) {
        this.streetId = streetId;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getStreetPy() {
        return streetPy;
    }
    public void setStreetPy(String streetPy) {
        this.streetPy = streetPy;
    }
    public String getStreetSpy() {
        return streetSpy;
    }
    public void setStreetSpy(String streetSpy) {
        this.streetSpy = streetSpy;
    }
    public String getAddressClass() {
        return addressClass;
    }
    public void setAddressClass(String addressClass) {
        this.addressClass = addressClass;
    }
    public String getX() {
        return x;
    }
    public void setX(String x) {
        this.x = x;
    }
    public String getY() {
        return y;
    }
    public void setY(String y) {
        this.y = y;
    }
    public List<Address> getAddressList() {
        if(addressList==null)
            addressList=new ArrayList<Address>();
        return addressList;
    }
    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
    
}
