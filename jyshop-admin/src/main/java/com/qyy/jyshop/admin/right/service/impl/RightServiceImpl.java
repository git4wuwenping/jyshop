/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.right.service.impl;

import com.github.pagehelper.page.PageMethod;
import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.right.service.RightService;
import com.qyy.jyshop.dao.*;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.*;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import com.qyy.jyshop.util.TimestampUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
    @Autowired
    private OrderLogDao orderLogDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductStoreDao productStoreDao;
    @Value("${interface-url}")
    private String interfaceUrl;
    @Value("${spring.profiles.active}")
    private String springProfile;


    @ServiceLog("获取维权单列表(分页_ajax)")
    @Override
    public PageAjax<Map<String, Object>> pageList(PageAjax<RightOrder> page)
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
    @Transactional
    public String back(Long id) {
        //调用退款pay服务
        RightOrder rightOrder = queryByID(id);
        if(rightOrder==null){
            throw new AppErrorException("该维权信息不存在");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> paramMap= new LinkedMultiValueMap<>();
        BigDecimal val = new BigDecimal(100);
        paramMap.add("id", String.valueOf(rightOrder.getId()));
        paramMap.add("outRefundNo", rightOrder.getSerial());
        paramMap.add("refundFee", String.valueOf(rightOrder.getPrice().multiply(val).intValue()));
        paramMap.add("totalFee", String.valueOf(rightOrder.getPrice().multiply(val).intValue()));
        paramMap.add("type", "0");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, headers);
        RestTemplate restTemplate=new RestTemplate();
        String _interfaceUrl = "";
        if(!springProfile.equals("dev")){
            _interfaceUrl = interfaceUrl+"/pay";
        }else{
            _interfaceUrl = interfaceUrl;
        }
//        System.out.println("------------->"+_interfaceUrl+"/anon/refund");
        Map map = restTemplate.postForEntity( _interfaceUrl+"/anon/refund", request , Map.class ).getBody();
        if(map==null){
            return null;
        }
        if("1".equals(String.valueOf(map.get("res_code")))||"FAIL".equals(map.get("return_code"))){
            return "退款提交失败";
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

    @Override
    @ServiceLog("维权订单详情处理")
    @Transactional
    public String deal(Long id, boolean agree, String rejectReason) {
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
                updateRightStatus(entity, Long.valueOf(9L), "items_tuikuan", "items_daituikuan");
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

    @Override
    @Transactional
    public void autoTuikuan(BigDecimal time) {
        HashMap<String,Object> params = new HashMap();
        params.put("time",time.multiply(new BigDecimal(3600*24)));
        List<String> list = (List<String>) super.dao.findForList("RightOrderDao.tuikuanList",params);
        for(String str : list){
            try{
                Long id = Long.parseLong(str);
                //同意退款
                deal(id,true,"");
                //确认退款
                back(id);
                //添加日志
                RightRel rightRel = new RightRel();
                rightRel.setRightId(id);
                List<RightRel> relList = relDao.select(rightRel);
                if(relList.size()>0){
                    rightRel = relList.get(0);
                }
                Long itemsId = rightRel.getItemsId();
                OrderItems items = itemsDao.selectByPrimaryKey(itemsId);
                Order order = orderDao.selectByPrimaryKey(items.getOrderId());
                OrderLog orderLog=new OrderLog();
                orderLog.setOrderId(order.getOrderId());
                orderLog.setOrderSn(order.getOrderSn());
                orderLog.setMessage("系统自动对订单"+order.getOrderSn()+"的维权进行了确认退款操作,该订单维权后"+time+"天内卖家未操作，系统自动同意维权");
                orderLog.setUserType(Integer.valueOf(DictionaryUtil.getDataValueByName("user_type", "system")));
                orderLog.setCreationTime(TimestampUtil.getNowTime());
                orderLogDao.insertSelective(orderLog);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public RightOrder findBySerial(String serial) {
        RightOrder rightOrder = new RightOrder();
        rightOrder.setSerial(serial);
        return queryOne(rightOrder);
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
    private void updateByReject(RightOrder entity) {
        Long rightId = entity.getId();
        RightRel rightRel = new RightRel();
        rightRel.setRightId(rightId);
        List<RightRel> relList = relDao.select(rightRel);
        //获取维权前订单状态
        RightOrder rightOrder = queryByID(rightId);
        Integer orderStatus = rightOrder.getOrderStatus();
        for (RightRel rel : relList) {
            Long itemsId = rel.getItemsId();
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

    /**
     * 修改库存
     * @param id
     */
//    private void updateProduct(Long id){
//        RightRel rel = new RightRel();
//        rel.setRightId(id);
//        List<RightRel> rightRelList = relDao.select(rel);
//        List<OrderItems> orderItemsList = new ArrayList<>();
//        for(RightRel rightRel : rightRelList){
//            Long itemsId = rightRel.getItemsId();
//            OrderItems items = itemsDao.selectByPrimaryKey(itemsId);
//            orderItemsList.add(items);
//        }
//        for (OrderItems orderItems : orderItemsList) {
//            productDao.updateStoreOfCut(orderItems.getProductId(), orderItems.getBuyCount()*-1);
//            productStoreDao.updateStoreOfCut(orderItems.getProductId(), orderItems.getBuyCount()*-1);
//        }
//    }
}
