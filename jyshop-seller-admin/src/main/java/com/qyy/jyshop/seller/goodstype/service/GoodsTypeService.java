package com.qyy.jyshop.seller.goodstype.service;

import java.util.List;

import com.qyy.jyshop.model.GoodsType;
import com.qyy.jyshop.pojo.PageAjax;

public interface GoodsTypeService {

    /**
     * 根据类型Id获取类型信息
     * @author hwc
     * @created 2018年1月3日 下午2:34:13
     * @param typeId
     * @return
     */
    public GoodsType queryByTypeId(Long typeId);
    
    /**
     * 根据供应商Id获取类型列表
     * @author hwc
     * @created 2018年1月5日 上午9:43:04
     * @param comId
     * @return
     */
    public List<GoodsType> queryByComId(Integer comId);
    
    /**
     * 获取型列表(分页)
     * @author hwc
     * @created 2018年1月3日 上午11:39:41
     * @param page
     * @param goodsType
     * @return
     */
    public PageAjax<GoodsType> pageGoodsType(PageAjax<GoodsType> page,GoodsType goodsType);
    
    /**
     * 添加类型
     * @author hwc
     * @created 2018年1月3日 下午2:32:06
     * @param goodsType
     */
    public String doAddGoodsType(GoodsType goodsType);
    
    /**
     * 修改类型
     * @author hwc
     * @created 2018年1月3日 下午2:32:09
     * @param goodsType
     */
    public String doEditGoodsType(GoodsType goodsType);
    
    /**
     * 删除类型
     * @author hwc
     * @created 2018年1月3日 下午2:32:12
     * @param typeId
     */
    public String doDelByTypeId(Long typeId);
    
    /**
     * 设置规格
     * @author hwc
     * @created 2018年1月3日 下午4:37:46
     * @param typeId
     * @param specIds
     * @return
     */
    public String doPutSpc(Long typeId,String specIds);
}
