package com.qyy.jyshop.seller.delivery.service.impl;

import com.qyy.jyshop.dao.AddressDao;
import com.qyy.jyshop.model.Address;
import com.qyy.jyshop.seller.delivery.service.AddressService;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl extends AbstratService<Address> implements AddressService {

    @Autowired
    private AddressDao addressDao;
    
    @Override
    public List<Address> queryAddressList(){

        //获取所有省
        List<Address> provinceList=this.addressDao.selectProvince();
             
        if(provinceList!=null && provinceList.size()>0){
            
            //获取所有的市
            List<Address> cityList=this.addressDao.selectCity();
            
            //获取所有的县
            List<Address> districtList=this.addressDao.selectDistrict();
            
            if(cityList!=null && cityList.size()>0){
                
                if(districtList!=null && districtList.size()>0){
                    //将县添加到市底下
                    for(int i=0;i<districtList.size();i++){
                        Address district=districtList.get(i);
                        districtEndAdd:for(Address city:cityList){
                            if(!StringUtil.isEmpty(district.getParentId()) && 
                                    city.getAddressId().equals(district.getParentId())){
                                city.getAddressList().add(district);
                                break districtEndAdd;
                            }
                        }
                    }
                }
                
                //将市添加到省底下
                for(int i=0;i<cityList.size();i++){
                    Address city=cityList.get(i);
                    cityEndAdd:for(Address province:provinceList){
                        if(!StringUtil.isEmpty(city.getParentId()) &&
                                province.getAddressId().equals(city.getParentId())){
                            province.getAddressList().add(city);
                            break cityEndAdd;
                        }
                    }
                }
            }
    
        }
        
        return provinceList;
    }

    @Override
    public List<Address> listAddress(String pId) {
        return addressDao.selectByParentId(pId);
    }

    @Override
    public List<Address> listProvince() {
        return addressDao.selectProvince();
    }
}
