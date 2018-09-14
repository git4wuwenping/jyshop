package com.qyy.jyshop.seller.delivery.service;

import com.qyy.jyshop.model.DlyType;
import com.qyy.jyshop.pojo.PageAjax;

import java.util.List;
import java.util.Map;

public interface DlyTypeService {
    
    /**
     * 根据供应商ID获取配送方式列表
     * @author hwc
     * @created 2018年3月16日 上午9:43:17
     * @param comId
     * @return
     */
    List<DlyType> queryByComId(Integer comId);
    
    /**
     * 分页查询配送方式
     * @param page
     * @return
     */
    PageAjax<Map<String, Object>> pageDlyType(PageAjax<Map<String, Object>> page);

    /**
     * 添加配送方式
     * @param map
     * @return
     */
    String addDlyType(Map<String, Object> map);
    /**
     * 修改配送方式
     * @param map
     * @return
     */
    String editDlyType(Map<String, Object> map);
    /**
     * 根据typeId查询配送方式详情
     * @param typeId
     * @return
     */
    DlyType queryByTypeId(Long typeId);
    /**
     * 根据typeId删除配送方式
     * @param typeId
     * @return
     */
    String delByTypeId(Long typeId);

}
