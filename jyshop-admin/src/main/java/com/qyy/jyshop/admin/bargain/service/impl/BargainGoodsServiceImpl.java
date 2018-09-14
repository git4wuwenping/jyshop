/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.bargain.service.impl;

import com.qyy.jyshop.admin.bargain.service.BargainGoodsService;
import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.dao.BargainOrderDao;
import com.qyy.jyshop.dao.BargainOrderItemsDao;
import com.qyy.jyshop.dao.BargainRecomGoodsDao;
import com.qyy.jyshop.dao.MemberBargainDao;
import com.qyy.jyshop.model.*;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.AppUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 描述
 * 砍价后台处理业务类
 * @author wu
 * @created 2018/4/11 17:03
 */
@Service
public class BargainGoodsServiceImpl extends AbstratService<BargainGoods> implements BargainGoodsService {

    @Autowired
    private BargainRecomGoodsDao recomGoodsDao;
    @Autowired
    private BargainOrderItemsDao itemsDao;
    @Autowired
    private BargainOrderDao bargainOrderDao;
    @Autowired
    private MemberBargainDao memberBargainDao;

    @ServiceLog("砍价活动列表(分页_ajax)")
    public PageAjax<Map<String, Object>> pageList(PageAjax<BargainGoods> page)
    {
        ParamData params = getParamData(Integer.valueOf(page.getPageNo()), Integer.valueOf(page.getPageSize()));
        int comId = getLoginUserComId().intValue();
        params.put("comId", Integer.valueOf(comId));
        return pageQuery("BargainGoodsDao.pageList", params);
    }


    @ServiceLog("推荐商品列表(分页_ajax)")
    public PageAjax<Map<String, Object>> recommendPage(PageAjax<BargainRecomGoods> page)
    {
        ParamData params = getParamData(Integer.valueOf(page.getPageNo()), Integer.valueOf(page.getPageSize()));
//        int comId = getLoginUserComId().intValue();
//        params.put("comId", Integer.valueOf(comId));
        return pageQuery("BargainRecomGoodsDao.pageList", params);
    }

    @Override
    public AjaxResult saveEntity(BargainGoods entity, String endDateStr, BigDecimal priceMinx) {
        String msg = validateEntity(entity,endDateStr,priceMinx);
        if(StringUtils.isNotBlank(msg)){
            return AppUtil.returnObj(msg);
        }
        entity.setCreateDate(new Timestamp(System.currentTimeMillis()));
        entity.setStartDate(new Timestamp(System.currentTimeMillis()));
        return super.save(entity);
    }

    @Override
    @ServiceLog("删除砍价活动")
    @Transactional
    public AjaxResult deleteByID(Long id) {
        //判断当前砍价活动的状态，进行中的不能删除
        BargainGoods bargainGoods = queryByID(id);
        long end = bargainGoods.getEndDate().getTime();
        long now = new Date().getTime();
        if(bargainGoods.getIsOpen()==1 && end>now){
            return AppUtil.returnObj("进行中的砍价活动不可删除");
        }
        //删除：不能有砍价成功订单
        HashMap<String,Object> map = new HashMap<>();
        map.put("bargainId",id);
        List list = dao.findForList("BargainOrderDao.pageList",map);
        if(list.size()>0){
            return AppUtil.returnObj("当前活动已生成订单");
        }
        //级联删除砍价活动订单信息
        Example example = new Example(BargainOrderItems.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("bargainId", id);
        //砍价记录
        List<BargainOrderItems> itemsList = itemsDao.selectByExample(example);
        //订单记录
        List<BargainOrder> orderList = new CopyOnWriteArrayList<>();
        for(BargainOrderItems items : itemsList){
            BargainOrder entity = bargainOrderDao.selectByPrimaryKey(items.getOrderId());
            if(entity!=null){
                orderList.add(entity);
            }
        }
        Example exm = new Example(MemberBargain.class);
        Example.Criteria cr = exm.createCriteria();
        cr.andEqualTo("bargainId", id);
        //参与记录
        List<MemberBargain> bargainList = memberBargainDao.selectByExample(exm);
        if(itemsList.size()>0){
            itemsDao.batchDel(itemsList);
        }
        if(orderList.size()>0){
            bargainOrderDao.batchDel(orderList);
        }
        if(bargainList.size()>0){
            memberBargainDao.batchDel(bargainList);
        }
        return super.deleteByID(id);
    }

    @Override
    public BargainGoods queryByID(Long id) {
        return super.queryByID(id);
    }

    @Override
    public AjaxResult updateEntity(BargainGoods entity, String endDateStr, BigDecimal priceMinx) {
        String msg = validateEntity(entity,endDateStr,priceMinx);
        if(StringUtils.isNotBlank(msg)){
            return AppUtil.returnObj(msg);
        }
        entity.setCreateDate(new Timestamp(System.currentTimeMillis()));
        return super.update(entity);
    }

    @Override
    @ServiceLog("操作砍价活动状态")
    public AjaxResult deal(Long id, Integer open) {
        String msg = null;
        BargainGoods bargainGoods = queryByID(id);
        //开启和禁用操作
        if(bargainGoods!=null){
            Timestamp end = bargainGoods.getEndDate();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            //开启：若截至时间小于当前时间不能开启
            if(end.getTime()<now.getTime()){
                return AppUtil.returnObj("截至时间不能小于当前时间");
            }
            //禁用：不能有参与当前砍价活动的订单
            if(open==0){
                Example example = new Example(BargainOrderItems.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("bargainId", id);
                List<BargainOrderItems> list = itemsDao.selectByExample(example);
                if(list.size()>0){
                    return AppUtil.returnObj("当前活动已有人员参与砍价");
                }
            }
            bargainGoods.setIsOpen(open);
//            updateEntity(bargainGoods, endDateStr, priceMinx);
            super.update(bargainGoods);
        }else{
            return  AppUtil.returnObj("未找到该砍价活动");
        }
        return  AppUtil.returnObj(msg);
    }

    @Override
    @Transactional
    public String recommendAdd(Map<String, String> map) {
        String goodsIds = map.get("goodsIds");
        List<String> addList = Arrays.asList(goodsIds.split("\\|"));
        if (CollectionUtils.isEmpty(addList)) {
            return null;
        }
        List<String> nowList = new ArrayList<>();
        List<BargainRecomGoods> relList = recomGoodsDao.selectAll();
        relList.forEach(rel -> {
            nowList.add(rel.getGoodsId().toString());
        });
        addList = addList.stream().filter(t -> !nowList.contains(t)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(addList)) {
            List<BargainRecomGoods> list = new ArrayList<>();
            for (String goodIdStr : addList) {
                BargainRecomGoods rel = new BargainRecomGoods();
                rel.setGoodsId(Long.parseLong(goodIdStr));
                list.add(rel);
            }
            recomGoodsDao.batchInsert(list);
        }
        return null;
    }

    @Override
    public AjaxResult delRecommendByID(Long id) {
        BargainRecomGoods rel = new BargainRecomGoods();
        rel.setGoodsId(id);
        rel = recomGoodsDao.selectOne(rel);
        recomGoodsDao.delete(rel);
        return AppUtil.returnObj("");
    }

    /**
     * 后台验证
     * @param entity
     * @param endDateStr
     * @param priceMinx
     * @return
     */
    private String validateEntity(BargainGoods entity, String endDateStr, BigDecimal priceMinx){
        if(endDateStr.length() == 16) {
            entity.setEndDate(Timestamp.valueOf(endDateStr + ":00"));
        }else {
            entity.setEndDate(Timestamp.valueOf(endDateStr));
        }
        Long goodsId = entity.getGoodsId();
        if(goodsId==null){
            return "请选择关联商品";
        }
        BigDecimal price = entity.getGoodsPrice();
        BigDecimal targetPrice = entity.getTargetPrice();
        if(price.compareTo(targetPrice)<=0){
            return "标靶金额须小于商品原价";
        }
        BigDecimal fpm = entity.getFirstPriceMax();
        targetPrice = price.subtract(targetPrice);
        if(targetPrice.compareTo(fpm)<0){
            return "标靶金额须大于砍价范围最大值";
        }
        Integer mode = entity.getBargainMode();
        if(mode==0){
            entity.setPriceMin(priceMinx);
            if(priceMinx==null){
                return "固定金额不能为空";
            }
            if(targetPrice.compareTo(priceMinx)<0){
                return "标靶金额须大于固定金额";
            }
        }else if(mode==1){
            BigDecimal min = entity.getPriceMin();
            BigDecimal max = entity.getPriceMax();
            if(min==null||max==null){
                return "随机砍价范围不能为空";
            }
            if(max.compareTo(min)<=0){
                return "随机砍价范围最大值须大于最小值";
            }
            if(targetPrice.compareTo(max)<0){
                return "标靶金额须大于随机砍价范围最大值";
            }
        }
        if(StringUtils.isBlank(entity.getDetails()) ||StringUtils.isBlank(entity.getShareTitle())
                ||StringUtils.isBlank(entity.getShareDescribe()) ||StringUtils.isBlank(entity.getRule())){
            return "请填写活动详情";
        }
        Integer type = entity.getBargainType();
        BigDecimal freePrice = entity.getFreePrice();
        if(type==2&&freePrice==null){
            return "请填写免单固定金额";
        }
        return "";
    }

}
