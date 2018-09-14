/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.seller.goodsreject.controller;

import com.qyy.jyshop.model.AuthUser;
import com.qyy.jyshop.model.GoodsRejectedAds;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.seller.common.annotation.Authority;
import com.qyy.jyshop.seller.common.annotation.ControllerLog;
import com.qyy.jyshop.seller.common.controller.BaseController;
import com.qyy.jyshop.seller.goodsreject.service.impl.GoodsRejectedAdsServiceImpl;
import com.qyy.jyshop.util.EntityReflectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 描述
 *
 * @author wu
 * @created 2018/3/28 17:06
 */
@Controller
@RequestMapping({"/admin/goodsRejectedAds"})
public class GoodsRejectedAdsController extends BaseController
{

    @Autowired
    private GoodsRejectedAdsServiceImpl service;

    @RequestMapping({"main"})
    @Authority(opCode="130101", opName="退货地址")
    public String main(Map<String, Object> map)
    {
        return "goodsrejectaddress/main";
    }

    @ControllerLog("查询退货地址列表")
    @RequestMapping({"page"})
    @ResponseBody
    @Authority(opCode="13010101", opName="查询退货地址列表")
    public PageAjax<Map<String, Object>> page(PageAjax<GoodsRejectedAds> page, GoodsRejectedAds entity) {
        Map map = EntityReflectUtil.toMap(entity);
        return this.service.pageList(page, map);
    }

    @RequestMapping({"add"})
    @Authority(opCode="13010102", opName="添加退货地址")
    public String add(ModelMap modelMap) {
        return "goodsrejectaddress/add";
    }

    @ControllerLog("保存退货地址")
    @RequestMapping(value={"save"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    @Authority(opCode="13010103", opName="保存退货地址")
    public AjaxResult save(GoodsRejectedAds entity) {
        AuthUser user = (AuthUser)getRequest().getAttribute("loginUser");
        entity.setComId(user.getComId());
        return this.service.saveOrUpdate(entity);
    }

    @ControllerLog("删除退货地址")
    @RequestMapping({"del/{id}"})
    @ResponseBody
    @Authority(opCode="13010104", opName="删除退货地址")
    public AjaxResult del(@PathVariable("id") Integer id) {
        return this.service.deleteByID(id);
    }

    @RequestMapping({"edit/{id}"})
    @Authority(opCode="13010105", opName="修改退货地址")
    public String edit(@PathVariable("id") Integer id, Map<String, Object> map)
    {
        GoodsRejectedAds ads = (GoodsRejectedAds)this.service.queryByID(id);
        map.put("ads", ads);
        return "goodsrejectaddress/edit";
    }
}