package com.qyy.jyshop.admin.spell.service.impl;

import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.spell.service.SpellActivityService;
import com.qyy.jyshop.dao.GoodsDao;
import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.model.SpellActivity;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Map;

@Service
public class SpellActivityServiceImpl extends AbstratService<SpellActivity> implements SpellActivityService {

    @Autowired
    private GoodsDao goodsDao;

    @ServiceLog("查询拼团活动详情")
    @Override
    public SpellActivity queryByActivityId(Long activityId){
        return this.queryByID(activityId);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @ServiceLog("查询拼团活动分页列表")
    @Override
    public PageAjax<Map<String,Object>> pageSpellActivity(PageAjax<Map<String,Object>> page) {
        ParamData params = this.getParamData(page.getPageNo(),page.getPageSize());
        return this.pageQuery("SpellActivityDao.getSpellActivityByParams", params);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @ServiceLog("新增拼团活动")
    @Override
    public AjaxResult saveOrUpdateSpellActivity(SpellActivity entity, String startDateStr, String endDateStr) {

        if(startDateStr.length() == 16) {
            entity.setStartDate(Timestamp.valueOf(startDateStr + ":00"));
        }else {
            entity.setStartDate(Timestamp.valueOf(startDateStr));
        }
        if(endDateStr.length() == 16) {
            entity.setEndDate(Timestamp.valueOf(endDateStr + ":00"));
        }else {
            entity.setEndDate(Timestamp.valueOf(endDateStr));
        }
        Long id = entity.getActivityId();
        if(entity.getSort() == null){
            entity.setSort(999);
        }
        Long nowDate = System.currentTimeMillis();
        if(nowDate < entity.getStartDate().getTime()){
            entity.setStatus(0);
        }
        if(nowDate >= entity.getStartDate().getTime() && nowDate < entity.getEndDate().getTime()){
            entity.setStatus(1);
        }
        if(nowDate >= entity.getEndDate().getTime()){
            entity.setStatus(2);
        }

        if(id==null){
            entity.setCreateDate(new Timestamp(System.currentTimeMillis()));
            entity.setLastModify(new Timestamp(System.currentTimeMillis()));
            entity.setNum(0);
            entity.setRealNum(0);
            entity.setCompleteNum(0);
//            entity.setStatus(0);

            Goods goods = goodsDao.selectByGoodsId(entity.getGoodsId());
            entity.setComId(Long.valueOf(goods.getComId()));
            entity.setShopStoreId(Long.valueOf(goods.getShopStoreId()));

            return super.save(entity);
        }else {
            return super.update(entity);
        }
    }

    @Override
    public AjaxResult updateEntity(SpellActivity entity) {
        return super.update(entity);
    }
}
