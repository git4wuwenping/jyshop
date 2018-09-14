package com.qyy.jyshop.goods.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.goods.server.ProductStoreService;


@RestController
public class ProductStoreController extends AppBaseController{

    @Autowired
    private ProductStoreService productStoreService;
    
    /**
     * 修改商品库存
     * @author hwc
     * @created 2018年4月3日 上午9:15:17
     * @param orderId
     */
    @RequestMapping(value = "editProductStore")
    public void editProductStore(@RequestParam(value="orderId")Long orderId){
        System.out.println("减库存orderId:"+orderId);
        this.productStoreService.doEditStoreByOrderId(orderId);
    }
}
