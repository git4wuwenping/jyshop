/*
 * Copyright (c) 2005, 2017, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.order.service;

import java.util.Map;

import com.qyy.jyshop.pojo.PageAjax;


public interface CartService {

    /**
     * 判断某用户是否己添加了某货品到购物车
     * @author hwc
     * @created 2017年11月22日 下午4:07:21
     * @param memberId 会员Id
     * @param productId  货品Id
     * @return true:己添加 false:未添加
     */
    public Boolean checkCartProduct(Long memberId,Long productId);
    

    /**
     * 获取会员购物车商品列表
     * @author hwc
     * @created 2017年11月24日 上午10:33:51
     * @param token
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageAjax<Map<String,Object>> pageCart(String token,Integer pageNo,Integer pageSize);
    
    /**
     * 添加商品到购物车中
     * @author hwc
     * @created 2017年11月22日 下午2:05:13
     * @param token
     * @param productId
     * @param activityId
     * @param buyCount
     * @return
     */
    public String doAddCartGoods(String token,Long productId,Long activityId,Integer buyCount);
    
    /**
     * 修改购物车货品购买数量
     * @author hwc
     * @created 2017年11月24日 上午9:13:35
     * @param token
     * @param productId
     * @param buyCount
     */
    public void doEditCartOfBuyCount(String token,Long productId,Integer buyCount);
    
    /**
     * 增加某会员的购物车货品购买数量
     * @author hwc
     * @created 2017年11月24日 上午8:53:26
     * @param token
     * @param productId
     * @param buyCount
     */
    public void doEditCartOfAddCount(String token,Long productId,Integer buyCount);
    

    /**
     * 批量删除购物车商品
     * @author hwc
     * @created 2017年11月24日 上午9:02:14
     * @param memberId
     * @param cartIds
     */
    public void doDelBatchCartGoods(String token,String cartIds);
}
