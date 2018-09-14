/*
 * Copyright (c) 2005, 2017, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.order.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.model.Cart;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.order.service.CartService;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;
import com.qyy.jyshop.util.TimestampUtil;
import com.qyy.jyshop.dao.CartDao;
import com.qyy.jyshop.dao.ProductDao;
import com.qyy.jyshop.dao.ShopStoreDao;
import com.qyy.jyshop.exception.AppErrorException;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 购物车
 * @author hwc
 * @created 2017年11月22日 下午1:59:13
 */
@Service 
public class CartServiceImpl extends AbstratService<Cart> implements CartService{
    
    @Autowired
    private CartDao cartDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ShopStoreDao shopStoreDao;
    
    @Override
    public Boolean checkCartProduct(Long memberId,Long productId){
        
        if(StringUtil.isEmpty(memberId))
            throw new AppErrorException("用户Id不能为空");
        if(StringUtil.isEmpty(productId))
            throw new AppErrorException("货品Id不能为空");
        Example example = new Example(Cart.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("memberId", memberId);
        criteria.andEqualTo("productId", productId);
        return this.cartDao.selectCountByExample(example)>0;
     
    }
    
    @Override
    public PageAjax<Map<String,Object>> pageCart(String token,Integer pageNo,Integer pageSize){
        
        ParamData params = this.getParamData(pageNo,pageSize);
        params.put("memberId",  this.getMemberId(token));
        params.put("marketEnable",  DictionaryUtil.getDataValueByName("goods_market_enable", "sales_yes"));
        
        PageAjax<Map<String,Object>> page=this.pageQuery("CartDao.selectCartList", params);
        List<Map<String,Object>> goodsList=page.getRows();
        
        if(goodsList!=null && goodsList.size()>0){
            
            List<Map<String,Object>> comMapList=new ArrayList<Map<String,Object>>();
            
            addGoods:for(Map<String,Object> goodsMap : goodsList){
                
                for (Map<String,Object> comMap : comMapList) {
                    if(goodsMap.get("shopStoreId").toString().equals(
                            comMap.get("shopStoreId").toString())){
                        
                        @SuppressWarnings("unchecked")
                        List<Map<String,Object>> comGoodsList=(List<Map<String,Object>>)comMap.get("goodsList");
                        comGoodsList.add(goodsMap);
                        comMap.put("goodsCount", comGoodsList.size());
                        continue addGoods;
                    }
                }
                
                Map<String,Object> comMap=new HashMap<String, Object>();
                List<Map<String,Object>> comGoodsList=new ArrayList<Map<String,Object>>();
                comGoodsList.add(goodsMap);
                comMap.put("goodsList", comGoodsList);
                comMap.put("goodsCount", comGoodsList.size());
                comMap.put("shopStoreId", goodsMap.get("shopStoreId"));
                comMap.put("comName", goodsMap.get("comName"));
                comMapList.add(comMap);
            }
            page.setRows(comMapList);
            
        }
        return page;
    }
    
    @Override 
    @Transactional
    public String doAddCartGoods(String token,Long productId,Long activityId,Integer buyCount){
        
        Member member=this.getMember(token);
            
        if(productId==null )
            throw new AppErrorException("货品Id不能为空");

        Integer goodsCount=Integer.valueOf(buyCount==null?"1":buyCount.toString());
        
        if(goodsCount<=0)
            goodsCount=1;
        
        Map<String,Object> productMap = this.productDao.selectByProductId(productId,
                Integer.valueOf(DictionaryUtil.getDataValueByName("goods_market_enable", "sales_yes")));
        
        if(productMap==null)
            throw new AppErrorException("获取商品出错,无法加入购物车…………");
        
        if(productMap.get("store")==null || Integer.valueOf(productMap.get("store").toString())<=0)
            throw new AppErrorException("商品库存不足,无法加入购物车…………");
       

        //查看购物车是否己经包含该商品,如果是则修改购买数量.否则添加一条新的购物车货品数据
        if(this.checkCartProduct(member.getMemberId(), productId)){
            this.doEditCartOfAddCount(token, productId, buyCount);
        }else{
            Cart cart=new Cart();
            cart.setGoodsId(Long.valueOf(productMap.get("goodsId").toString()));
            cart.setProductId(Long.valueOf(productId.toString()));
            cart.setBuyCount(goodsCount);
            cart.setMemberId(member.getMemberId());
            cart.setActivityId(activityId==null?0:Long.valueOf(activityId.toString()));
            cart.setOriginal((String)productMap.get("original"));
            cart.setCreateDate(TimestampUtil.getNowTime());
            this.cartDao.insert(cart);
        }  
        
        return null;
    }
    

    @Override
    @Transactional
    public void doEditCartOfBuyCount(String token,Long productId,Integer buyCount){
        
        Member member=this.getMember(token);
        
        if(StringUtil.isEmpty(productId))
            throw new AppErrorException("货品Id不能为空.....");
        if(StringUtil.isEmpty(buyCount))
            throw new AppErrorException("购买数量不能为空.....");
        
        
        if(Integer.valueOf(buyCount.toString())<=0){
            throw new AppErrorException("购买数量不能小于0");
        }
        
        this.cartDao.updateCartOfBuyCount(member.getMemberId(), productId, buyCount);    
    }
    
    @Override
    @Transactional
    public void doEditCartOfAddCount(String token,Long productId,Integer buyCount){

        Member member=this.getMember(token);
        if(StringUtil.isEmpty(productId))
            throw new AppErrorException("货品Id不能为空.....");
        if(StringUtil.isEmpty(buyCount))
            throw new AppErrorException("购买数量不能为空.....");
        if(buyCount<=0)
            buyCount=1;
        this.cartDao.updateCartOfAddCount(member.getMemberId(), productId, buyCount);
    }
    
    
    
    @Override
    @Transactional
    public void doDelBatchCartGoods(String token,String cartIds){
        
        if(StringUtil.isEmpty(cartIds))
            throw new AppErrorException("购物车Id不能为空");
        Member member=this.getMember(token);

        List<Cart> cartList = new ArrayList<Cart>();
        Cart cart = null;
        for(String cartId: cartIds.split(",")){
            cart = new Cart();
            cart.setMemberId(member.getMemberId());
            cart.setCartId(Long.valueOf(cartId));
            cartList.add(cart);
        }
        cartDao.batchDel(cartList);
    }

}
