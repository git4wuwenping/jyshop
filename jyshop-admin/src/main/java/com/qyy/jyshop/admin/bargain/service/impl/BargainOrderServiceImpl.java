/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.bargain.service.impl;

 import com.qyy.jyshop.admin.bargain.service.BargainOrderService;
 import com.qyy.jyshop.admin.common.annotation.ServiceLog;
 import com.qyy.jyshop.dao.*;
 import com.qyy.jyshop.model.*;
 import com.qyy.jyshop.pojo.PageAjax;
 import com.qyy.jyshop.pojo.ParamData;
 import com.qyy.jyshop.supple.AbstratService;
 import com.qyy.jyshop.util.DictionaryUtil;
 import com.qyy.jyshop.util.StringUtil;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;
 import tk.mybatis.mapper.entity.Example;

 import java.sql.Timestamp;
 import java.util.Map;

 /**
 * 描述
 * 砍价活动订单业务
 * @author wu
 * @created 2018/4/17 10:25
 */
@Service
public class BargainOrderServiceImpl extends AbstratService<BargainOrder> implements BargainOrderService {

    @Autowired
    private LogiDao logiDao;
    @Autowired
    private BargainDeliveryDao deliveryDao;
    @Autowired
    private BargainLogDao logDao;
    @Autowired
    private BargainOrderDao dao;

    @Override
    @ServiceLog("砍价订单列表(分页_ajax)")
    public PageAjax<Map<String, Object>> page(PageAjax<BargainOrder> page) {
        ParamData params = getParamData(Integer.valueOf(page.getPageNo()), Integer.valueOf(page.getPageSize()));
        return pageQuery("BargainOrderDao.pageList", params);
    }

     @Override
     public BargainOrder queryByID(Long id) {
         return super.queryByID(id);
     }

     @Override
     @ServiceLog("砍价订单发货")
     @Transactional
     public String deliverySave(Map<String, Object> params) {
         if(StringUtil.isEmpty(params.get("logiNo")))
             return "获取物流单号不能为空....";
         BargainDelivery delivery=new BargainDelivery();
         Logi logi=logiDao.selectByPrimaryKey(Integer.valueOf(params.get("logiId").toString()));
         if(logi!=null){
             delivery.setLogiId(logi.getLogiId());
             delivery.setLogiCode(logi.getLogiCode());
             delivery.setLogiName(logi.getLogiName());
         }else{
             return "获取物流公司信息失败....";
         }
         BargainOrder order=dao.selectByPrimaryKey(Long.valueOf(params.get("orderId").toString()));
         if(order!=null){
             if(!order.getOrderStatus().equals(
                     Integer.valueOf(DictionaryUtil.getDataValueByName("bargain_order_status", "order_confirm")))){
                 return "订单状态错误,可能已进行过发货动作...";
             }
             /**
              * 如果是父订单,则直接修改订单状态与货运状态为己发货
              * 如果是子订单,则对所有子订单发货情况进行判断,如果己全部发货,则修改父订单为己发货,否则修改为部分发货
              */
             Integer orderStatus=Integer.valueOf(DictionaryUtil.getDataValueByName("bargain_order_status", "order_ship"));
             Integer shipStatus=Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_yes"));
             //修改父订单状态 
             BargainOrder updateCOrder=new BargainOrder();
             updateCOrder.setOrderId(order.getOrderId());
             updateCOrder.setOrderStatus(orderStatus);
             updateCOrder.setShipStatus(shipStatus);
             updateCOrder.setDeliverDate(new Timestamp(System.currentTimeMillis()));
             this.update(updateCOrder);

             delivery.setOrderId(order.getOrderId());
             delivery.setOrderSn(order.getOrderSn());
             delivery.setMemberId(order.getMemberId());
             delivery.setLogiNo(params.get("logiNo").toString());
             delivery.setOpId(Long.valueOf(this.getLoginUser().getId()));
             delivery.setOpUser(this.getLoginUser().getUsername());
             delivery.setCreationTime(new Timestamp(System.currentTimeMillis()));
             delivery.setType(Integer.valueOf(DictionaryUtil.getDataValueByName("delivery_type", "delivery")));
             this.deliveryDao.insertSelective(delivery);

             //添加日志
             BargainLog orderLog=new BargainLog();
             orderLog.setOrderId(order.getOrderId());
             orderLog.setOrderSn(order.getOrderSn());
             orderLog.setOpId(Long.valueOf(this.getLoginUser().getId()));
             orderLog.setOpUser(this.getLoginUser().getUsername());
             orderLog.setMessage(this.getLoginUser().getUsername()+"对订单"+order.getOrderSn()+"进行了发货操作,发货订单ID:"+delivery.getOrderId());
             orderLog.setUserType(Integer.valueOf(DictionaryUtil.getDataValueByName("user_type", "administrator")));
             orderLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
             this.logDao.insertSelective(orderLog);
         }else{
             return "获取发货订单信息失败...";
         }
         return null;
     }

     @Override
     @Transactional
     public String doCancleOrder(Long orderId) {
         BargainOrder order=this.queryByID(orderId);
         if(order==null)
             return "获取订单信息失败...";
//         if(this.getLoginUserComId().equals(0) || this.getLoginUserComId().equals(order.getComId())){
             order.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("bargain_order_status", "order_returnlation")));
             dao.updateByPrimaryKey(order);
             //添加日志
             BargainLog orderLog=new BargainLog();
             orderLog.setOrderId(order.getOrderId());
             orderLog.setOrderSn(order.getOrderSn());
             orderLog.setOpId(Long.valueOf(this.getLoginUser().getId()));
             orderLog.setOpUser(this.getLoginUser().getUsername());
             orderLog.setMessage(this.getLoginUser().getUsername()+"对砍价订单"+order.getOrderSn()+"进行了作废操作,作废订单ID:"+order.getOrderId());
             orderLog.setUserType(Integer.valueOf(DictionaryUtil.getDataValueByName("user_type", "administrator")));
             orderLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
             this.logDao.insertSelective(orderLog);
//         }else{
//             return "你没有权限操作该订单...";
//         }
         return null;
     }
 }
