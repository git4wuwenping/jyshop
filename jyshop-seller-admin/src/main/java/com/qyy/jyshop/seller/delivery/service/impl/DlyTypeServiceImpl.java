package com.qyy.jyshop.seller.delivery.service.impl;

import com.qyy.jyshop.dao.DlyTypeAreaDao;
import com.qyy.jyshop.dao.DlyTypeDao;
import com.qyy.jyshop.dao.GoodsDao;
import com.qyy.jyshop.model.Cart;
import com.qyy.jyshop.model.DlyType;
import com.qyy.jyshop.model.DlyTypeArea;
import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.seller.common.annotation.ServiceLog;
import com.qyy.jyshop.seller.delivery.service.DlyTypeService;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.EntityReflectUtil;
import com.qyy.jyshop.util.StringUtil;
import com.qyy.jyshop.util.TimestampUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service
public class DlyTypeServiceImpl extends AbstratService<DlyType> implements DlyTypeService {
    
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private DlyTypeDao dlyTypeDao;
    @Autowired
    private DlyTypeAreaDao dlyTypeAreaDao;
    
    @ServiceLog("查询配送方式列表")
    @Override
    public List<DlyType> queryByComId(Integer comId){
        if(StringUtil.isEmpty(comId))
            comId=this.getLoginUserComId();
        return this.dlyTypeDao.selectByComId(comId);
    }
    
    @ServiceLog("分页查询配送方式列表")
    @Override
    public PageAjax<Map<String, Object>> pageDlyType(PageAjax<Map<String, Object>> page) {
        ParamData paramData = this.getParamData(page.getPageNo(),page.getPageSize());
        paramData.put("comId", this.getLoginUserComId());
        return this.pageQuery("DlyTypeDao.selectDlyTypeByParams", paramData);
    }

    @Transactional
    @ServiceLog("添加配送方式")
    @Override
    public String addDlyType(Map<String, Object> map) {
        DlyType dlyType = EntityReflectUtil.toBean(DlyType.class, map);
        if (dlyType == null)
            return "获取配送方式失败";
        dlyType.setComId(this.getLoginUserComId());
        dlyType.setCreateTime(TimestampUtil.getNowTime());
        this.insert(dlyType);
        HttpServletRequest request = getRequest();
        String[] idArr = request.getParameterValues("id");
        String[] firstWeightPriceArr = request.getParameterValues("firstWeightPrices");
        String[] additionalWeightPriceArr = request.getParameterValues("additionalWeightPrices");
        String[] areaIdArr = request.getParameterValues("area_province_ids");
        String[] areaNameArr = request.getParameterValues("area_province_names");
        if(idArr!=null && idArr.length > 0){
            for(int i = 0; i<idArr.length; i++){
                DlyTypeArea dlyTypeArea = new DlyTypeArea();
                dlyTypeArea.setTypeId(dlyType.getTypeId());
                //首重价格
                dlyTypeArea.setFirstWeightPrice(new BigDecimal(Double.valueOf(firstWeightPriceArr[i])));
                //续重价格
                dlyTypeArea.setAdditionalWeightPrice(new BigDecimal(Double.valueOf(additionalWeightPriceArr[i])));
                //地区id
                dlyTypeArea.setAreaIdGroup(areaIdArr[i]);
                //地区名称
                dlyTypeArea.setAreaNameGroup(areaNameArr[i]);
                //首重续重
                dlyTypeArea.setFirstWeight(dlyType.getFirstWeight());
                dlyTypeArea.setAdditionalWeight(dlyType.getAdditionalWeight());
                //是否包邮
                dlyTypeArea.setFreeDly((Double.valueOf(firstWeightPriceArr[i]) > 0 && (Double.valueOf(additionalWeightPriceArr[i]) > 0)) ? 0 : 1);
                dlyTypeAreaDao.insertSelective(dlyTypeArea);
            }
        }
        return null;
    }

    @Transactional
    @ServiceLog("修改配送方式")
    @Override
    public String editDlyType(Map<String, Object> map) {
        DlyType dlyType = EntityReflectUtil.toBean(DlyType.class, map);
        if (dlyType == null)
            return "获取配送方式失败";
        this.updateByID(dlyType);
        HttpServletRequest request = getRequest();
        String[] idArr = request.getParameterValues("id");
        String[] firstWeightPriceArr = request.getParameterValues("firstWeightPrices");
        String[] additionalWeightPriceArr = request.getParameterValues("additionalWeightPrices");
        String[] areaIdArr = request.getParameterValues("area_province_ids");
        String[] areaNameArr = request.getParameterValues("area_province_names");
        if(idArr!=null && idArr.length > 0){
            DlyTypeArea dlyTypeArea1 = new DlyTypeArea();
            dlyTypeArea1.setTypeId(dlyType.getTypeId());
            List<DlyTypeArea> list = dlyTypeAreaDao.select(dlyTypeArea1);
            //先删除
            for (DlyTypeArea area: list) {
                if(!Arrays.asList(idArr).contains(area.getId()+"")){
                    dlyTypeAreaDao.delete(area);
                }
            }
            for(int i = 0; i<idArr.length; i++){
                DlyTypeArea dlyTypeArea = new DlyTypeArea();
                dlyTypeArea.setTypeId(dlyType.getTypeId());
                //首重价格
                dlyTypeArea.setFirstWeightPrice(new BigDecimal(Double.valueOf(firstWeightPriceArr[i])));
                //续重价格
                dlyTypeArea.setAdditionalWeightPrice(new BigDecimal(Double.valueOf(additionalWeightPriceArr[i])));
                //地区id
                dlyTypeArea.setAreaIdGroup(areaIdArr[i]);
                //地区名称
                dlyTypeArea.setAreaNameGroup(areaNameArr[i]);
                //首重续重
                dlyTypeArea.setFirstWeight(dlyType.getFirstWeight());
                dlyTypeArea.setAdditionalWeight(dlyType.getAdditionalWeight());
                //是否包邮
                dlyTypeArea.setFreeDly((Double.valueOf(firstWeightPriceArr[i]) > 0 && (Double.valueOf(additionalWeightPriceArr[i]) > 0)) ? 0 : 1);
                if(StringUtils.isBlank(idArr[i])){
                    dlyTypeAreaDao.insertSelective(dlyTypeArea);
                } else {
                    dlyTypeArea.setId(Long.valueOf(idArr[i]));
                    dlyTypeAreaDao.updateByPrimaryKeySelective(dlyTypeArea);
                }
            }
        }
        return null;
    }

    @ServiceLog("根据typeId查询配送方式")
    @Override
    public DlyType queryByTypeId(Long typeId) {
        DlyType dlyType = this.queryByID(typeId);
        return dlyType;
    }

    @Transactional
    @ServiceLog("根据typeId删除配送方式")
    @Override
    public String delByTypeId(Long typeId) {
        DlyType dlyType = this.queryByID(typeId);
        if (dlyType == null)
            return "配送方式不存在";
        
        Example example = new Example(Goods.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("dlyTypeId", typeId);
        
        if(this.goodsDao.selectCountByExample(example)>0){
            return "有商品己引用该模板，删除失败...";
        }else{
            this.delById(typeId);
            DlyTypeArea dlyTypeArea = new DlyTypeArea();
            dlyTypeArea.setTypeId(typeId);
            dlyTypeAreaDao.delete(dlyTypeArea);
        }
        return null;
    }
}