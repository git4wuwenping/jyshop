package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.Cart;

import tk.mybatis.mapper.common.Mapper;


public interface CartDao extends Mapper<Cart>{

    /**
     * 获取用户购物车列表
     * @author hwc
     * @created 2017年11月24日 上午9:45:32
     * @param memberId
     * @param marketEnable
     * @return
     */
    List<Map<String,Object>> selectCartList(@Param("memberId")Long memberId,@Param("marketEnable")Integer marketEnable);
    
    /**
     * 修改某会员的购物车货品购买数量
     * @author hwc
     * @created 2017年11月24日 上午8:56:06
     * @param memberId
     * @param productId
     * @param buyCount
     */
    void updateCartOfBuyCount(@Param("memberId")Long memberId,@Param("productId")Long productId,
            @Param("buyCount")Integer buyCount);
    
    /**
     * 增加某会员的购物车货品购买数量
     * @author hwc
     * @created 2017年11月24日 上午8:56:06
     * @param memberId
     * @param productId
     * @param buyCount
     */
    void updateCartOfAddCount(@Param("memberId")Long memberId,@Param("productId")Long productId,
            @Param("buyCount")Integer buyCount);
    
    
    /**
     * 批量删除购物车
     * @author hwc
     * @created 2017年11月24日 上午9:08:40
     * @param cartList
     */
    void batchDel(List<Cart> cartList);
}
