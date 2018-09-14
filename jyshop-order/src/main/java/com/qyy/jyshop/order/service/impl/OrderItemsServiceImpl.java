package com.qyy.jyshop.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.dao.OrderItemsDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.model.OrderItems;
import com.qyy.jyshop.order.service.OrderItemsService;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.StringUtil;

@Service
public class OrderItemsServiceImpl extends AbstratService<OrderItems> implements OrderItemsService{

    @Autowired
    private OrderItemsDao orderItemsDao;
    
    @Override
    public void doAddOrderItems(JSONArray goodsJsonArray, Order order,List<Order> childOrderList) {
        
        if(order==null || StringUtil.isEmpty(order.getOrderId()))
            throw new AppErrorException("订单数据为空......");
        
        if(goodsJsonArray!=null && goodsJsonArray.size()>0){
            List<OrderItems> orderItemsList=new ArrayList<OrderItems>();
            
            for(int i=0;i<goodsJsonArray.size();i++){
                //获取货品信息
                JSONObject goodsJson=goodsJsonArray.getJSONObject(i);
                OrderItems orderItems=new OrderItems();
                
                if(childOrderList!=null && childOrderList.size()>0){
                	 getChildOrder:for (Order beChildOrder : childOrderList) {
                         if(beChildOrder.getComId().equals(Integer.valueOf(goodsJson.getString("comId"))) &&
                                 beChildOrder.getOrderType().equals(Integer.valueOf(goodsJson.getString("goodsType")))){
                        	 orderItems.setOrderId(beChildOrder.getOrderId());
                        	 orderItems.setOrderSn(beChildOrder.getOrderSn());
                             break getChildOrder;
                         }
                     }
                }else{
                	orderItems.setOrderId(order.getOrderId());
                	orderItems.setOrderSn(order.getOrderSn());
                }
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
            this.orderItemsDao.batchInsert(orderItemsList);
        }else{
            throw new AppErrorException("没有货品项,无法添加订单货品......");
        }   
    }

}
