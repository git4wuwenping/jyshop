package com.qyy.jyshop.admin.goodstype.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.goodstype.service.GoodsTypeService;
import com.qyy.jyshop.dao.GiftPackageDao;
import com.qyy.jyshop.dao.GoodsDao;
import com.qyy.jyshop.dao.GoodsTypeDao;
import com.qyy.jyshop.model.GiftPackage;
import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.model.GoodsType;
import com.qyy.jyshop.model.OrderItems;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.StringUtil;

@Service
public class GoodsTypeServiceImple extends AbstratService<GoodsType> implements GoodsTypeService{

    @Autowired
    private GoodsTypeDao goodsTypeDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GiftPackageDao giftPackageDao;
    
    @ServiceLog("获取类型信息")
    @Override
    public GoodsType queryByTypeId(Long typeId){
        return this.goodsTypeDao.selectByPrimaryKey(typeId);
    }
    
    @ServiceLog("获取类型列表")
    @Override
    public List<GoodsType> queryByComId(Integer comId){
        if(StringUtil.isEmpty(comId))
            comId=this.getLoginUserComId();
        return this.goodsTypeDao.selectByComId(comId);
    }
    
    @ServiceLog("获取类型列表(分页)")
    @Override
    public PageAjax<GoodsType> pageGoodsType(PageAjax<GoodsType> page, GoodsType goodsType) {
        goodsType.setComId(this.getLoginUserComId());
        return this.queryPage(page, goodsType);
    }

    @ServiceLog("添加类型")
    @Transactional
    @Override
    public String doAddGoodsType(GoodsType goodsType) {
        
        if(goodsType==null)
            return "请求数据为空...";
        if(StringUtil.isEmpty(goodsType.getTypeName()))
            return "类型名称不能为空...";
        goodsType.setComId(this.getLoginUserComId());
        goodsType.setCreationTime(new Timestamp(System.currentTimeMillis()));
        this.goodsTypeDao.insertSelective(goodsType);
        return null;
    }

    @ServiceLog("修改类型")
    @Transactional
    @Override
    public String doEditGoodsType(GoodsType goodsType) {
        
        if(goodsType==null)
            return "请求数据为空...";
        if(StringUtil.isEmpty(goodsType.getTypeName()))
            return "类型名称不能为空...";
        
        this.goodsTypeDao.updateByPrimaryKeySelective(goodsType);
        return null;
    }

    @ServiceLog("删除类型")
    @Transactional
    @Override
    public String doDelByTypeId(Long typeId) {
        if(StringUtil.isEmpty(typeId))
            return "类型ID不能为空...";
        Example example = new Example(Goods.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("typeId", typeId);
        if(this.goodsDao.selectCountByExample(example)>0){
            return "该类型己引用,删除失败...";
        }else{
            example = new Example(GiftPackage.class);
            criteria = example.createCriteria();
            criteria.andEqualTo("typeId", typeId);
            if(this.giftPackageDao.selectCountByExample(example)>0){
                return "该类型己引用,删除失败...";
            }else{
                this.goodsTypeDao.deleteByPrimaryKey(typeId);
            }
        }
        return null;
    }

    @ServiceLog("设置规格")
    @Transactional
    @Override
    public String doPutSpc(Long typeId,String specIds){
        
        int i = this.goodsTypeDao.updateOfSpecIds(typeId, this.getLoginUserComId(), specIds);
        if(i>0)
            return null;
        else
            return "关联规格失败...";
    }

}
