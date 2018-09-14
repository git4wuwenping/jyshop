package com.qyy.jyshop.basics.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.basics.service.AddressService;
import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.model.Address;
import com.qyy.jyshop.redis.RedisDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AddressController extends AppBaseController{

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private AddressService addressService;

    @RequestMapping("listAddressAjax/{id}")
    public List<Address> listAddressAjax(@PathVariable("id") String id) {
        return addressService.listAddress(id);
    }
    
    /**
     * 获取所有的地址
     * @author hwc
     * @created 2017年12月9日 下午4:59:52
     * @return
     */
    @RequestMapping("queryAddress")
    public Map<String,Object> queryAddress(){
        
        try{            
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("addressList", this.redisDao.getObject("addressList"));
            return this.outMessage(0, "请求成功...", resultMap);
        }catch (Exception e) {
            e.printStackTrace();
            return this.outMessage(2, "未知错误", null);
        }
    }
}
