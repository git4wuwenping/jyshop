/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.bargain.service.impl;

import com.qyy.jyshop.admin.bargain.service.BargainOrderItemsService;
import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.model.BargainOrderItems;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.DataTablePage;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 描述
 * 砍价记录业务
 * @author wu
 * @created 2018/4/16 15:14
 */
@Service
public class BargainOrderItemsServiceImpl extends AbstratService<BargainOrderItems>
        implements BargainOrderItemsService {

    @Override
    @ServiceLog("砍价记录列表(分页_ajax)")
    public PageAjax<Map<String, Object>> page(PageAjax<BargainOrderItems> page) {
        ParamData params = getParamData(Integer.valueOf(page.getPageNo()), Integer.valueOf(page.getPageSize()));
        return pageQuery("BargainOrderItemsDao.pageList", params);
    }

    @Override
    public BargainOrderItems queryByOrderId(Long id) {
        BargainOrderItems items = new BargainOrderItems();
        items.setOrderId(id);
        BargainOrderItems obj = queryOne(items);
        if(obj==null){
            throw new RuntimeException("未找到该订单");
        }
        return obj;
    }

    @ServiceLog("砍价分页")
    @Override
    public DataTablePage<Map<String,Object>> pageDataTable(Map<String, Object> map) {
        Integer start = Integer.parseInt(map.get("start").toString());
        Integer pageSize = Integer.parseInt(map.get("length").toString());
        Integer draw = Integer.parseInt(map.get("draw").toString());
        ParamData params = this.getParamData(start/pageSize+1, pageSize);
        params.put("orderId", map.get("orderId"));
        PageAjax<Map<String,Object>> page = this.pageQuery("MemberBargainDao.pageDataTable", params );
        return new DataTablePage(draw, page.getRows(),page.getTotal(), page.getTotal());
    }

}
