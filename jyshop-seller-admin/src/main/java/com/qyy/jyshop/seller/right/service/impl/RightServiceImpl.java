/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.seller.right.service.impl;

import com.github.pagehelper.page.PageMethod;
import com.qyy.jyshop.dao.*;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.*;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.seller.common.annotation.ServiceLog;
import com.qyy.jyshop.seller.right.service.RightService;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述
 *
 * @author wu
 * @created 2018/3/29 11:30
 */
@Service
public class RightServiceImpl extends AbstratService<RightOrder> implements RightService {

    @Autowired
    private RightOrderDao dao;
    @Autowired
    private OrderItemsDao itemsDao;
    @Autowired
    private RightStatusDao statusDao;
    @Autowired
    private RightProcessDao processDao;
    @Autowired
    private GoodsRejectedAdsDao rejectedAdsDao;
    @Autowired
    private RightRelDao relDao;
    @Autowired
    private DeliveryDao deliveryDao;
    @Autowired
    private OrderDao orderDao;

    @ServiceLog("获取维权单列表(分页_ajax)")
    @Override
    public PageAjax<Map<String, Object>> pageList(PageAjax<RightOrder> page, Map map)
    {
        PageMethod.startPage(page.getPageNo(), page.getPageSize());
        ParamData params = getParamData(Integer.valueOf(page.getPageNo()), Integer.valueOf(page.getPageSize()));

        int comId = getLoginUserComId().intValue();
        params.put("comId", Integer.valueOf(comId));
        params.put("tk", 0);
        params.put("thtk", 0);
        return pageQuery("RightOrderDao.pageList", params);
    }

    @ServiceLog("维权订单详情")
    @Override
    public Map<String, Object> queryDetailById(Long id)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        RightOrder entity = queryByID(id);
        if (entity == null) {
            throw new AppErrorException("该维权信息不存在");
        }
        RightRel rel = new RightRel();
        rel.setRightId(entity.getId());
        List<RightRel> rightRelList = relDao.select(rel);
        List<OrderItems> itemsList = new ArrayList<OrderItems>();
        for (RightRel rightRel : rightRelList) {
            Long itemsId = rightRel.getItemsId();
            OrderItems items = itemsDao.selectByPrimaryKey(itemsId);
            itemsList.add(items);
        }
        Delivery delivery = new Delivery();
        delivery.setType(2);
        delivery.setOrderId(id);
        Delivery del = deliveryDao.selectOne(delivery);
        entity.setItems(itemsList);
        //替换文本
        entity.setType(DictionaryUtil.getDataLabelByValue("right_type", entity.getType()));
        entity.setSellerStatus(DictionaryUtil.getDataLabelByValue("order_items_status", entity.getSellerStatus()));
        map.put("rightOrder",entity);
        map.put("delivery",del);
        return map;
    }

    @ServiceLog("确认退款")
    @Override
    public String back(Long id) {
        RightOrder rightOrder = queryByID(id);
        String type = rightOrder.getType();
        String tuikuan = DictionaryUtil.getDataValueByName("right_type", "tuikuan");
        if(type.equals(tuikuan)){
            //系统退款待实现代码...
            //仅退款
            updateRightStatus(rightOrder, Long.valueOf(10L), "items_tuikuansuccess", "items_tuikuansuccess");
        }else {
            //退货退款
            updateRightStatus(rightOrder, Long.valueOf(5L), "items_tuikuansuccess", "items_tuikuansuccess");
        }
        return null;
    }

    @ServiceLog("确认收货")
    @Override
    public String revGoods(Long id) {
        RightOrder rightOrder = queryByID(id);
        //确认收货
        updateRightStatus(rightOrder, Long.valueOf(4L), "items_tuikuan", "items_tuikuan");
        return null;
    }

    @ServiceLog("维权订单详情处理")
    @Override
    public String deal(Long id, boolean agree, String rejectReason)
    {
        String msg = "";
        RightOrder entity = queryByID(id);
        String type = entity.getType();
        String tuihuotuikuan = DictionaryUtil.getDataValueByName("right_type", "tuihuotuikuan");
        if (agree) {
            //同意
            if(type.equals(tuihuotuikuan)){
                //退货退款
                GoodsRejectedAds ads = new GoodsRejectedAds();
                ads.setUsed("1");
                GoodsRejectedAds address = rejectedAdsDao.selectOne(ads);
                if (address == null) {
                    return "未设置默认退货地址";
                }
                updateRightStatus(entity, Long.valueOf(2L), "items_daituihuo", "items_daituihuo");
            }
            else {
                //仅退款
                updateRightStatus(entity, Long.valueOf(9L), "items_tuikuan", "items_tuikuan");
            }
        } else {
            //拒绝
            entity.setRejectReason(rejectReason);
            if (type.equals(tuihuotuikuan)) {
                //退货退款
                updateRightStatus(entity, Long.valueOf(6L), "items_tuikuanclose", "items_jujue");
            }
            else {
                //仅退款
                updateRightStatus(entity, Long.valueOf(11L), "items_jujue", "items_jujue");
            }
            updateByReject(entity);
        }
        return msg;
    }

    @Transactional
    public void updateRightStatus(RightOrder entity, Long statusIds, String str, String seller) {
        String status = DictionaryUtil.getDataValueByName("order_items_status", str);
        String sellerStatus = DictionaryUtil.getDataValueByName("order_items_status", seller);
        entity.setStatus(status);
        entity.setSellerStatus(sellerStatus);
        RightProcess process = new RightProcess();
        process.setCreateTime(new Timestamp(System.currentTimeMillis()));
        process.setRightStatusId(statusIds);
        process.setRightOrderId(entity.getId());
        dao.updateByPrimaryKey(entity);
        processDao.insert(process);
    }

    /**
     * 拒绝维权申请后还原显示，若原先为待发货或者待评价
     * @param entity
     */
    private void updateByReject(RightOrder entity){
        Long rightId = entity.getId();
        RightRel rightRel = new RightRel();
        rightRel.setRightId(rightId);
        rightRel = relDao.selectOne(rightRel);
        Long itemsId = rightRel.getItemsId();
        //获取维权前订单状态
        RightOrder rightOrder = queryByID(rightId);
        Integer orderStatus = rightOrder.getOrderStatus();
        //更新商品订单状态
        OrderItems items = itemsDao.selectByPrimaryKey(itemsId);
        items.setStatus(Integer.parseInt(DictionaryUtil.getDataValueByName("order_items_status", "items_normal")));
        itemsDao.updateByPrimaryKey(items);
        //更新订单状态
        Order order = orderDao.selectByPrimaryKey(items.getOrderId());
        order.setOrderStatus(orderStatus);
        orderDao.updateByPrimaryKey(order);
    }
}
