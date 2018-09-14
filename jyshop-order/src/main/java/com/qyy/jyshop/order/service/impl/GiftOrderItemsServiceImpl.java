package com.qyy.jyshop.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.dao.GiftOrderItemsDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.GiftOrder;
import com.qyy.jyshop.model.GiftOrderItems;
import com.qyy.jyshop.order.service.GiftOrderItemsService;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.StringUtil;

@Service
public class GiftOrderItemsServiceImpl extends AbstratService<GiftOrderItems> implements GiftOrderItemsService{

    @Autowired
    private GiftOrderItemsDao giftOrderItemsDao;
    
    @Override
    public void doAddOrderItems(JSONArray goodsJsonArray, GiftOrder order) {
        
        if(order==null || StringUtil.isEmpty(order.getOrderId()))
            throw new AppErrorException("订单数据为空......");
        
        if(goodsJsonArray!=null && goodsJsonArray.size()>0){
            List<GiftOrderItems> orderItemsList=new ArrayList<GiftOrderItems>();
            
            for(int i=0;i<goodsJsonArray.size();i++){
                //获取货品信息
                JSONObject goodsJson=goodsJsonArray.getJSONObject(i);
                GiftOrderItems orderItems=new GiftOrderItems();
                
                orderItems.setOrderId(order.getOrderId());
                orderItems.setOrderSn(order.getOrderSn());
                orderItems.setGoodsId(goodsJson.getLong("goodsId"));
                orderItems.setGoodsName(goodsJson.get("name")==null?"":goodsJson.getString("name"));
                orderItems.setGoodsImage(goodsJson.get("image")==null?"":goodsJson.getString("image"));
                orderItems.setProductId(goodsJson.getLong("productId"));
                orderItems.setProductSn(goodsJson.getString("productSn"));
                orderItems.setProductSpec(goodsJson.get("specs")==null?"":goodsJson.getString("specs"));
                orderItems.setBuyCount(goodsJson.getInt("buyCount"));
                orderItems.setGoodsPrice(BigDecimal.valueOf(goodsJson.getDouble("price")));
                orderItems.setCost(BigDecimal.valueOf(goodsJson.getDouble("cost")));
                orderItems.setPrice(BigDecimal.valueOf(goodsJson.getDouble("price")));
                orderItems.setDiscount(new BigDecimal(0D));
                orderItems.setWeight(new BigDecimal(goodsJson.get("weight")==null?0:goodsJson.getDouble("weight")));
                orderItems.setUnit(goodsJson.get("unit")==null?"":goodsJson.getString("unit"));
                if(goodsJson.get("activityId")==null)
                    orderItems.setActivityId(0);
                else
                    orderItems.setActivityId(goodsJson.getInt("activityId"));
                orderItems.setActivityName(goodsJson.get("activityName")==null?"":goodsJson.getString("activityName"));
                orderItems.setActivityDiscount(new BigDecimal(goodsJson.get("activityDiscount")==null?0:
                    goodsJson.getDouble("activityDiscount")));
                orderItems.setComId(Integer.valueOf(goodsJson.get("comId")==null?"0":goodsJson.getString("comId")));
                orderItemsList.add(orderItems);
            }
            this.giftOrderItemsDao.batchInsert(orderItemsList);
        }else{
            throw new AppErrorException("没有货品项,无法添加订单货品......");
        }   
    }

}
