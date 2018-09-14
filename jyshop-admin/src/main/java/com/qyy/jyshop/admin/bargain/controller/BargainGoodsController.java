/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.bargain.controller;

import com.qyy.jyshop.admin.bargain.service.BargainGoodsService;
import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.goods.service.GoodsService;
import com.qyy.jyshop.model.BargainGoods;
import com.qyy.jyshop.model.BargainRecomGoods;
import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

/**
 * 描述
 * 砍价控制类
 * @author wu
 * @created 2018/4/11 15:43
 */
@Controller
@RequestMapping("/admin/bargain")
public class BargainGoodsController extends BaseController{

    @Autowired
    private BargainGoodsService service;
    @Autowired
    private GoodsService goodsService;

    @RequestMapping("main")
    @Authority(opCode = "110301", opName = "活动列表")
    public String main(){
        return "bargain/main";
    }

    @ControllerLog("砍价活动列表分页")
    @Authority(opCode = "11030101", opName = "砍价活动列表分页")
    @RequestMapping("page")
    @ResponseBody
    public PageAjax<Map<String, Object>> page(PageAjax<BargainGoods> page) {
        PageAjax<Map<String, Object>> data = service.pageList(page);
        return data;
    }

    @ControllerLog("砍价活动添加页面")
    @RequestMapping("add")
    @Authority(opCode = "11030102", opName = "砍价活动添加")
    public String add(Long id, Map<String, Object> map){
        BargainGoods bargainGoods = service.queryByID(id);
        if(bargainGoods!=null){
            Goods goods = goodsService.queryByGoodsId(bargainGoods.getGoodsId());
            map.put("goodsName", goods.getName());
        }
        else {
            bargainGoods = new BargainGoods();
        }
        map.put("entity", bargainGoods);
        return "bargain/add";
    }

    @ControllerLog("砍价活动保存")
    @RequestMapping("save")
    @ResponseBody
    @Authority(opCode = "11030103", opName = "砍价活动保存")
    public AjaxResult save(BargainGoods entity, String endDateStr, BigDecimal priceMinx) {
        Long id = entity.getBargainId();
        if(id==null){
            return service.saveEntity(entity,endDateStr,priceMinx);
        }else {
            return service.updateEntity(entity,endDateStr,priceMinx);
        }
    }

    @ControllerLog("开启或禁用砍价活动")
    @RequestMapping("deal/{id}")
    @ResponseBody
    @Authority(opCode="11030104", opName="操作砍价活动")
    public AjaxResult deal(@PathVariable("id") Long id, Integer open) {
        return service.deal(id,open);
    }

    @ControllerLog("删除砍价活动")
    @RequestMapping("del/{id}")
    @ResponseBody
    @Authority(opCode="11030105", opName="删除砍价活动")
    public AjaxResult del(@PathVariable("id") Long id) {
        return service.deleteByID(id);
    }

    @ControllerLog("关联商品选择")
    @RequestMapping("select")
    @Authority(opCode="11030106", opName="砍价关联商品")
    public String selectGoods() {
        return "bargain/select";
    }

    @RequestMapping("recommend")
    @Authority(opCode = "110302", opName = "推荐商品")
    public String recommend(){
        return "bargain/recommend/main";
    }

    @ControllerLog("砍价推荐商品")
    @Authority(opCode = "11030201", opName = "砍价推荐商品分页")
    @RequestMapping("recommendPage")
    @ResponseBody
    public PageAjax<Map<String, Object>> pageList(PageAjax<BargainRecomGoods> page) {
        PageAjax<Map<String, Object>> data = service.recommendPage(page);
        return data;
    }

    @RequestMapping("recommendSelect")
    @Authority(opCode = "11030202", opName = "砍价推荐关联商品")
    public String recommendSelect(){
        return "bargain/recommend/select";
    }

    @ControllerLog("砍价推荐关联商品")
    @RequestMapping("recommendAdd")
    @ResponseBody
    @Authority(opCode = "11030203", opName = "砍价推荐关联商品添加")
    public AjaxResult addTagRel(@RequestParam Map<String,String> map) {
        try {
            return AppUtil.returnObj(service.recommendAdd(map));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }

    @ControllerLog("删除推荐商品")
    @RequestMapping("recommendDel/{id}")
    @ResponseBody
    @Authority(opCode="11030204", opName="删除推荐商品")
    public AjaxResult recommendDel(@PathVariable("id") Long id) {
        return service.delRecommendByID(id);
    }

}
