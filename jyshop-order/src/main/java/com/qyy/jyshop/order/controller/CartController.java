/*
 * Copyright (c) 2005, 2017, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.order.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.order.service.CartService;
import com.qyy.jyshop.pojo.PageAjax;


/**
 * 购物车管理
 * @author hwc
 * @created 2017年11月22日 下午1:57:07
 */
@RestController
public class CartController extends AppBaseController{

    @Autowired
    private CartService cartService;
  

    /**
     * 获取购物车商品列表
     * @author hwc
     * @created 2017年11月24日 上午9:50:50
     * @param token
     */
    @RequestMapping(value = "pageCart")
    public Map<String,Object> queryCartList(String token,Integer pageNo,Integer pageSize) {

        PageAjax<Map<String,Object>> pageCart=this.cartService.pageCart(token,pageNo,pageSize);
        
        return this.outMessage(0, "获取成功", this.getPageMap(pageCart));

    }
    
    /**
     * 添加商品到购物车
     * @author hwc
     * @created 2017年11月22日 下午2:06:41
     * @param token
     * @param productId
     * @param activityId
     * @param buyCount
     * @return
     */
    @RequestMapping(value = "addCartGoods")
    public Map<String,Object> addCartGoods(String token,Long productId,Long activityId,Integer buyCount){

        this.cartService.doAddCartGoods(token, productId,activityId,buyCount);
        return this.outMessage(0, "添加成功", null);
      
    }
    
    /**
     * 修改购物车商品购买数量
     * @author hwc
     * @created 2017年11月24日 上午9:55:01
     * @param token
     * @param productId
     * @param buyCount
     */
    @RequestMapping(value = "editCartOfBuyCount" )
    public Map<String,Object> editCartOfBuyCount(String token,Long productId,Integer buyCount){
               
        this.cartService.doEditCartOfBuyCount(token, productId, buyCount);
        return this.outMessage(0, "修改成功", null);
 
    }


    /**
     * 删除购物车商品
     * @author hwc
     * @created 2017年11月24日 上午9:56:28
     * @param token
     * @param cartIds
     */
    @RequestMapping(value = "delCartGoods" )
    public Map<String,Object> delCartGoods(String token,String cartIds){
        
        this.cartService.doDelBatchCartGoods(token, cartIds);;
        return this.outMessage(0, "删除成功", null);
    }
    
}
