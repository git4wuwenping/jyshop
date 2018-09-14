package com.qyy.jyshop.basics.service;

import java.util.List;

import com.qyy.jyshop.model.Address;


public interface AddressService {

    /**
     * 获取所有的地址信息
     * @return
     */
    public List<Address> queryAddressList();

    /**
     * 根据父id获取地址列表
     * @param pId
     * @return
     */
    List<Address> listAddress(String pId);

    /**
     * 获取省份列表
     * @return
     */
    List<Address> listProvince();
}
