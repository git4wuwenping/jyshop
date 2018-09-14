/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.order.service.impl;

import com.github.pagehelper.page.PageMethod;
import com.qyy.jyshop.dao.*;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.*;
import com.qyy.jyshop.order.service.RightService;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DateUtil;
import com.qyy.jyshop.util.DictionaryUtil;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 描述
 *
 * @author wu
 * @created 2018/3/28 17:34
 */
@Service
public class RightServiceImpl extends AbstratService<RightOrder> implements RightService {
    @Autowired
    private RightOrderDao dao;
    @Autowired
    private RightProcessDao processDao;
    @Autowired
    private RightStatusDao statusDao;
    @Autowired
    private RightRelDao relDao;
    @Autowired
    private OrderItemsDao itemsDao;
    @Autowired
    private GoodsRejectedAdsDao adsDao;
    @Autowired
    private DeliveryDao deliveryDao;
    @Autowired
    private LogiDao logiDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private CompanyDao companyDao;

    private DecimalFormat df;

    public RightServiceImpl()
    {
        this.df = new DecimalFormat("00000");
    }

    @Transactional
    public RightOrder submit(RightOrder entity, Long[] itemsIds) {
        String prefix = DateUtil.dateToDateString(new Date(), "yyyyMMdd");
        String nowTime = DateUtil.dateToDateString(new Date(), "yyyy-MM-dd");
        Example example = new Example(RightOrder.class);
        example.setOrderByClause("serial desc");
        example.createCriteria().andLike("createTime", "%" + nowTime + "%");
        List lists = queryByExample(example);

        AtomicInteger ai = new AtomicInteger(1);
        if (lists.size() > 0) {
            RightOrder ro = (RightOrder)lists.get(0);
            String serial = ro.getSerial();
            int num = Integer.parseInt(serial.substring(prefix.length(), serial.length()));
            ai.getAndAdd(num);
        }
        String serial = this.df.format(ai.get());
        entity.setSerial(prefix + serial);
        entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        RightProcess process = new RightProcess();
        process.setCreateTime(entity.getCreateTime());
        String type = entity.getType();
        String tuikuan = DictionaryUtil.getDataValueByName("right_type", "tuikuan");
        if(type.equals(tuikuan)){
            //仅退款
            process.setRightStatusId(8L);
        }else {
            //退货退款
            process.setRightStatusId(1L);
        }
        //前台显示待审核
        String status = DictionaryUtil.getDataValueByName("order_items_status", "items_daishenhe");
        //后台显示待审核
        String sellerStatus = DictionaryUtil.getDataValueByName("order_items_status", "items_daishenhe");
        entity.setStatus(status);
        entity.setSellerStatus(sellerStatus);
        Integer before = null;
        List<RightRel> list = new ArrayList<RightRel>();
        RightRel rightRel;
        for (Long itemsId : itemsIds) {
            OrderItems items = itemsDao.selectByPrimaryKey(itemsId);
            Order order = orderDao.selectByPrimaryKey(items.getOrderId());
            if (order == null) {
                throw new AppErrorException("该订单信息不存在");
            }
            //判断该订单是否可维权
            Integer orderStatus = order.getOrderStatus();
            Integer order_pay = Integer.parseInt(DictionaryUtil.getDataValueByName("order_status", "order_pay"));
            Integer order_rog = Integer.parseInt(DictionaryUtil.getDataValueByName("order_status", "order_rog"));
            if(orderStatus<order_pay||orderStatus>order_rog){
                throw new AppErrorException("该订单状态不能维权");
            }
            Integer cs = order.getRightCloseStatus();
            if(cs>0){
                throw new AppErrorException("该订单状态不能维权");
            }
            before = order.getOrderStatus();
            rightRel = new RightRel();
            rightRel.setItemsId(itemsId);
            RightRel rel = relDao.selectOne(rightRel);
            if (rel != null) {
                list.add(rel);
            }
        }
        updateBySubmit(itemsIds);
        //将订单原先状态记录
        entity.setOrderStatus(before);
        if (list.size() == 0) {
            save(entity);
            for (Long itemsId : itemsIds) {
                rightRel = new RightRel();
                rightRel.setItemsId(itemsId);
                RightRel rel = relDao.selectOne(rightRel);
                if(rel!=null){
                    throw new AppErrorException("该商品已存在维权信息不能重复维权");
                }
                rightRel.setRightId(entity.getId());
                relDao.insert(rightRel);
            }
            process.setRightOrderId(entity.getId());
        }
        else {
            rightRel = list.get(0);
            entity.setId(rightRel.getRightId());
            entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            process.setRightOrderId(rightRel.getRightId());
            //若申请超过2次，则不让重新维权
            Example processExm = new Example(RightProcess.class);
            Criteria criteria = processExm.createCriteria();
            criteria.andEqualTo("rightOrderId", rightRel.getRightId());
            if(type.equals(tuikuan)){
                criteria.andEqualTo("rightStatusId", 8L);
            }else {
                criteria.andEqualTo("rightStatusId", 1L);
            }
            List<RightProcess> processList = processDao.selectByExample(processExm);
            if(processList.size()>1){
                throw new AppErrorException("不能重复维权超过2次");
            }
            update(entity);
        }
        this.processDao.insert(process);
        entity.setRightStatus(process.getRightStatusId());
        return entity;
    }

    public Map<String, Object> queryDetailById(Long id)
    {
        Map<String, Object> dataMap = new HashMap();
        RightOrder entity = queryByID(id);
        if (entity == null) {
            throw new AppErrorException("该维权信息不存在");
        }
        Order order = null;
        Example example = new Example(RightRel.class);
        example.createCriteria().andEqualTo("rightId", id);
        List<RightRel> rightRels = relDao.selectByExample(example);
        List<OrderItems> itemsList = new ArrayList<OrderItems>();
        for (RightRel rightRel : rightRels) {
            Long itemsId = rightRel.getItemsId();
            OrderItems items = itemsDao.selectByPrimaryKey(itemsId);
            order = orderDao.selectByPrimaryKey(items.getOrderId());
            if (order == null) {
                throw new AppErrorException("该订单信息不存在");
            }
            itemsList.add(items);
        }
        Company company = companyDao.selectByPrimaryKey(order.getComId());
        order.setComName(company.getComName());
        dataMap.put("order", order);
        entity = makeRightProcess(entity, order.getComId());
        dataMap.put("right", entity);
        dataMap.put("items", itemsList);
        return dataMap;
    }

    private RightOrder makeRightProcess(RightOrder entity, Integer comId)
    {
        Long id = entity.getId();
        Example processExm = new Example(RightProcess.class);
        processExm.setOrderByClause("id desc");
        processExm.createCriteria().andEqualTo("rightOrderId", id);
        List<RightProcess> processList = processDao.selectByExample(processExm);
        //获取最新的维权进度id放到维权订单状态中
        RightProcess rightProcess = processList.get(0);
        entity.setRightStatus(rightProcess.getRightStatusId());
        for (RightProcess process : processList) {
            RightStatus status = statusDao.selectByPrimaryKey(process.getRightStatusId());
            Long statusId = status.getId();
            if (statusId.longValue() == 2L)
            {
                GoodsRejectedAds ads = new GoodsRejectedAds();
                ads.setComId(comId);
                ads.setUsed("1");
                ads = this.adsDao.selectOne(ads);
                status.setAddress(ads);
            }

            String text = status.getProcessText();
            if (StringUtils.indexOf(text, '$') > 0) {
                Map valuesMap = new HashMap();
                valuesMap.put("countPrice", entity.getPrice());
                StrSubstitutor sub = new StrSubstitutor(valuesMap);
                String processText = sub.replace(text);
                status.setProcessText(processText);
            }
            process.setRightStatus(status);
        }
        entity.setProcessList(processList);
        //替换文本
        entity.setType(DictionaryUtil.getDataLabelByValue("right_type", entity.getType()));
        entity.setStatus(DictionaryUtil.getDataLabelByValue("order_items_status", entity.getStatus()));
        return entity;
    }

    @Transactional
    public Map<String, Object> cancelById(Long id)
    {
        Map map = new HashedMap();
        boolean flag = false;
        RightOrder entity = queryByID(id);
        if (entity == null) {
            throw new AppErrorException("该维权信息不存在");
        }
        String type = entity.getType();
        String tuihuotuikuan = DictionaryUtil.getDataValueByName("right_type", "tuihuotuikuan");
        if(StringUtils.isBlank(tuihuotuikuan)){
            tuihuotuikuan = "1";
        }
        if(type.equals(tuihuotuikuan)){
            //退货退款
            flag = true;
        }
        RightProcess process = new RightProcess();
        process.setCreateTime(new Timestamp(System.currentTimeMillis()));
        process.setRightOrderId(entity.getId());
        String status;
        String sellerStatus;
        if (flag) {
            process.setRightStatusId(7L);
            status = DictionaryUtil.getDataValueByName("order_items_status", "items_tuikuanclose");
            sellerStatus = DictionaryUtil.getDataValueByName("order_items_status", "items_quxiao");
        }
        else {
            process.setRightStatusId(12L);
            status = DictionaryUtil.getDataValueByName("order_items_status", "items_quxiao");
            sellerStatus = DictionaryUtil.getDataValueByName("order_items_status", "items_quxiao");
        }
        RightRel rightRel = new RightRel();
        rightRel.setRightId(entity.getId());
        List<RightRel> relList = relDao.select(rightRel);
        //取消维权后还原显示，若原先为待发货或者待评价
        updateByCancel(relList);
        this.processDao.insert(process);
        entity.setStatus(status);
        entity.setSellerStatus(sellerStatus);
        update(entity);

        //更新订单与商品信息
        //替换文本
        entity.setType(DictionaryUtil.getDataLabelByValue("right_type", entity.getType()));
        entity.setStatus(DictionaryUtil.getDataLabelByValue("order_items_status", entity.getStatus()));
        map.put("entity", entity);
        return map;
    }

    public PageAjax<Map<String, Object>> page(PageAjax<RightOrder> page, String token, BigDecimal secondsRefundAutoAgree, BigDecimal secondsBackAutoAgree)
    {
        PageMethod.startPage(page.getPageNo(), page.getPageSize());
        ParamData params = getParamData(Integer.valueOf(page.getPageNo()), Integer.valueOf(page.getPageSize()));
        Long memberId = getMemberId(token);
        params.put("memberId", memberId);
        params.put("tk", secondsRefundAutoAgree);
        params.put("thtk", secondsBackAutoAgree);
        return pageQuery("RightOrderDao.pageList", params);
    }

    public Map<String, Object> queryDelivery(Long rightOrderId)
    {
        Map dataMap = new HashMap();
        Delivery delivery = new Delivery();
        delivery.setOrderId(rightOrderId);
        delivery.setType(2);
        delivery = deliveryDao.selectOne(delivery);
        dataMap.put("delivery", delivery);
        List logiList = this.logiDao.selectAll();
        dataMap.put("logiList", logiList);
        return dataMap;
    }

    @Transactional
    public Map<String, Object> submitDelivery(Delivery delivery)
    {
        RightRel rightRel = new RightRel();
        rightRel.setRightId(delivery.getOrderId());
        List relList = this.relDao.select(rightRel);
        if (relList.size() == 0) {
            throw new AppErrorException("该订单维权信息不存在");
        }
        RightRel rel = (RightRel)relList.get(0);
        RightOrder right = queryByID(rel.getRightId());
        Map dataMap = new HashMap();
        Long rightOrderId = delivery.getOrderId();
        Delivery entity = new Delivery();
        entity.setOrderId(rightOrderId);
        entity.setType(2);
        entity = deliveryDao.selectOne(entity);

        Logi logi = logiDao.selectByPrimaryKey(delivery.getLogiId());
        OrderItems items = itemsDao.selectByPrimaryKey(rel.getItemsId());
        delivery.setOrderSn(items.getOrderSn());
        delivery.setLogiCode(logi.getLogiCode());
        delivery.setLogiId(logi.getLogiId());
        delivery.setLogiName(logi.getLogiName());
        delivery.setCreationTime(new Timestamp(System.currentTimeMillis()));
        delivery.setType(2);
        if (entity == null) {
            deliveryDao.insert(delivery);
        } else {
            delivery.setDeliveryId(entity.getDeliveryId());
            deliveryDao.updateByPrimaryKey(delivery);
        }
        String status = DictionaryUtil.getDataValueByName("order_items_status", "items_daishouhuo");
        String sellerStatus = DictionaryUtil.getDataValueByName("order_items_status", "items_daishouhuo");
        right.setStatus(status);
        right.setSellerStatus(sellerStatus);
        RightProcess process = new RightProcess();
        process.setCreateTime(new Timestamp(System.currentTimeMillis()));
        process.setRightStatusId(Long.valueOf(3L));
        process.setRightOrderId(right.getId());
        dao.updateByPrimaryKey(right);
        processDao.insert(process);
        dataMap.put("delivery", delivery);
        return dataMap;
    }

    @Override
    public Member getMember(String token) {
        return super.getMember(token);
    }

    /**
     * 提交维权时判断该订单是否还存在原先的状态，如待发货，待评价等
     * @param itemsIds
     */
    private void updateBySubmit(Long[] itemsIds){
        boolean flag = true;
        //更新订单商品状态
        Order order = null;
        Integer orderStatus = Integer.parseInt(DictionaryUtil.getDataValueByName("order_status", "order_return_apply"));
        String quxiao = DictionaryUtil.getDataValueByName("order_items_status", "items_quxiao");
        for(Long itemsId : itemsIds){
            OrderItems items = itemsDao.selectByPrimaryKey(itemsId);
            order = orderDao.selectByPrimaryKey(items.getOrderId());
            items.setStatus(Integer.parseInt(DictionaryUtil.getDataValueByName("order_items_status", "items_handle")));
            itemsDao.updateByPrimaryKey(items);
        }
        if(order!=null){
            //得到该订单的商品数
            List<OrderItems> orderItems = itemsDao.selectByOrderId(order.getOrderId());
            Set<Long> set = new HashSet<>();
            for(OrderItems oi : orderItems){
                Long id = oi.getItemId();
                set.add(id);
            }
            //移除当前维权传入ids
            for(Long itemsId : itemsIds){
                set.remove(itemsId);
            }
            for(Long id : set){
                RightRel rel = new RightRel();
                rel.setItemsId(id);
                RightRel rightRel = relDao.selectOne(rel);
                if(rightRel==null){
                    flag = false;
                    break;
                }else{
                    Long rightId = rightRel.getRightId();
                    RightOrder rightOrder = dao.selectByPrimaryKey(rightId);
                    if(quxiao.equals(rightOrder.getSellerStatus())){
                        flag = false;
                        break;
                    }
                }
            }
            if(flag){
                //若相等则更新订单状态
                order.setOrderStatus(orderStatus);
                orderDao.updateByPrimaryKey(order);
            }
        }
    }

    /**
     * 取消维权后还原显示，若原先为待发货或者待评价
     * @param rels
     */
    private void updateByCancel(List<RightRel> rels){
        for(RightRel rightRel : rels){
            Long rightId = rightRel.getRightId();
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

}
