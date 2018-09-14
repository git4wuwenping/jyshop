/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.floor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.common.utils.CommUtil;
import com.qyy.jyshop.admin.floor.service.FloorService;
import com.qyy.jyshop.admin.goods.service.GoodsService;
import com.qyy.jyshop.admin.goodscat.service.GoodsCatNewService;
import com.qyy.jyshop.model.AuthUser;
import com.qyy.jyshop.model.Floor;
import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.model.GoodsCat;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.TreeNode;
import com.qyy.jyshop.util.AppUtil;
import com.qyy.jyshop.util.StringUtil;

/**
 * 描述
 * 
 * @author jiangbin
 * @created 2018年3月13日 下午4:36:52
 */

@Controller
@RequestMapping("/admin/floor")
public class FloorController extends BaseController {

    @Autowired
    private FloorService floorService;
    @Autowired
    private GoodsCatNewService goodsCatNewService;
    @Autowired
    private GoodsService goodsService;

    @Authority(opCode = "140101", opName = "楼层管理")
    @RequestMapping(value = "floorMain", method = { RequestMethod.POST, RequestMethod.GET })
    public String floorMain() {
        return "floor/floor_main";
    }

    @RequestMapping(value = "pageFloorAjax", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    @Authority(opCode = "14010101", opName = "查询楼层列表")
    public PageAjax<Floor> pageFloorAjax(PageAjax<Floor> page, Floor Floor) {
        return floorService.pageFloor(page, Floor);
    }

    @ControllerLog("添加楼层页面")
    @Authority(opCode = "14010102", opName = "添加楼层页面")
    @RequestMapping(value = "preAddFloor", method = { RequestMethod.POST, RequestMethod.GET })
    public String preAddFloor() {
        return "floor/add_floor";
    }

    @ControllerLog("添加楼层")
    @RequestMapping(value = "addFloor", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    @Authority(opCode = "14010103", opName = "添加楼层")
    public AjaxResult addFloor(Floor floor) {
        try {
            return AppUtil.returnObj(floorService.doAddFloor(floor));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }

    @ControllerLog("查询楼层详情")
    @Authority(opCode = "14010104", opName = "查询楼层详情")
    @RequestMapping(value = "preEditFloor/{floorId}", method = { RequestMethod.POST, RequestMethod.GET })
    public String preEditFloor(@PathVariable("floorId") Long floorId, Map<String, Object> map) {
        Floor floor = floorService.queryByFloorId(floorId);
        map.put("floor", floor);
        return "floor/edit_floor";
    }

    @ControllerLog("修改楼层")
    @RequestMapping(value = "editFloor", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    @Authority(opCode = "14010105", opName = "修改楼层")
    public AjaxResult editFloor(Floor floor, Short floorTypeOld) {
        try {
            return AppUtil.returnObj(floorService.doEditFloor(floor, floorTypeOld));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }

    @ControllerLog("删除楼层")
    @RequestMapping(value = "delFloor/{floorId}", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    @Authority(opCode = "14010106", opName = "删除楼层")
    public AjaxResult delFloor(@PathVariable("floorId") Long floorId) {
        try {
            return AppUtil.returnObj(floorService.doDelFloor(floorId));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }

    /*
     * @ControllerLog("清除楼层关联信息")
     * 
     * @RequestMapping(value = "delAllFloorRel/{floorId}", method = {
     * RequestMethod.POST, RequestMethod.GET })
     * 
     * @ResponseBody
     * 
     * @Authority(opCode = "14010109", opName = "清除楼层关联信息") public AjaxResult
     * delAllFloorRel(@PathVariable Long floorId) { try { return
     * AppUtil.returnObj(floorService.delAllFloorRel(floorId)); } catch
     * (Exception e) { return AppUtil.returnObj(2, e.getMessage()); } }
     */

    @ControllerLog("编辑子楼层页面")
    @Authority(opCode = "14010107", opName = "编辑子楼层页面")
    @RequestMapping(value = "preEditFloorSon/{floorId}", method = { RequestMethod.POST, RequestMethod.GET })
    public String preEditFloorSon(@PathVariable("floorId") Long floorId, Map<String, Object> map) {
        map.put("fatherFloor", floorService.queryByFloorId(floorId));
        map.put("floorList", floorService.queryFloorByParentId(floorId));
        return "floor/edit_floor_son";
    }

    @ControllerLog("保存子楼层")
    @RequestMapping(value = "saveFloorSon", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    @Authority(opCode = "14010108", opName = "保存子楼层")
    public AjaxResult saveFloorSon(@RequestParam Map<String, Object> map) {
        try {
            return AppUtil.returnObj(floorService.saveFloorSon(map));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }

    @ControllerLog("编辑楼层模版")
    @Authority(opCode = "14010109", opName = "编辑楼层模版")
    @RequestMapping(value = "preEditFloorRel/{floorId}", method = { RequestMethod.POST, RequestMethod.GET })
    public String preEditFloorRel(@PathVariable("floorId") Long floorId, Map<String, Object> map) {
        map.put("floorParent", floorService.queryByFloorId(floorId));
        map.put("floorList", floorService.queryFloorByParentId(floorId));
        map.put("goodsCatList", floorService.queryGoodsCatListByParentId(floorId));
        map.put("goodsList", floorService.queryGoodsListByParentIds(floorId));
        return "floor/edit_floor_template";
    }

    @ControllerLog("编辑分类楼层")
    @Authority(opCode = "14010110", opName = "编辑分类楼层")
    @RequestMapping(value = "preEditFloorGoodsCatRel/{floorId}", method = { RequestMethod.POST, RequestMethod.GET })
    public String preEditFloorGoodsCatRel(@PathVariable("floorId") Long floorId, Long parentId, Integer maxSize,
            Map<String, Object> map) {
        List<Map<String, Object>> hasList = floorService.queryFloorGoodsCatRel(floorId);
        List<Map<String, Object>> allList = floorService.queryGoodsCatList();
        List<Long> ids = hasList.stream().map(e -> Long.parseLong(e.get("catId").toString()))
                .collect(Collectors.toList());
        for (Map<String, Object> m : allList) {
            if (ids.contains(Long.parseLong(m.get("catId").toString()))) {
                m.put("status", 1);
            } else {
                m.put("status", 0);
            }
        }
        map.put("relList", allList);
        map.put("floorId", floorId);
        map.put("parentId", parentId);
        map.put("maxSize", maxSize);
        return "floor/edit_floor_goodscat_rel";
    }

    @ControllerLog("保存分类楼层模板")
    @Authority(opCode = "14010111", opName = "保存分类楼层模板")
    @RequestMapping(value = "saveFloorGoodsCatRel", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public AjaxResult saveFloorGoodsCatRel(@RequestParam Integer floorId,
            @RequestParam(name = "frIds[]") Integer[] frIds) {
        try {
            return AppUtil.returnObj(floorService.saveFloorRel(floorId, frIds));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }

    @ControllerLog("编辑商品楼层")
    @Authority(opCode = "14010112", opName = "编辑商品楼层")
    @RequestMapping(value = "preEditFloorGoodsRel/{floorId}", method = { RequestMethod.POST, RequestMethod.GET })
    public String preEditFloorGoodsRel(@PathVariable("floorId") Long floorId, String floorName, Long parentId,
            Integer maxSize, Map<String, Object> map) {
        // 分类信息
        List<GoodsCat> goodsCatList = goodsCatNewService.listAll();
        map.put("goodsCatList", goodsCatList);
        List<Map<String, Object>> goodsList = floorService.queryFloorGoodsRel(floorId);
        map.put("goodsList", goodsList);
        Floor floor = new Floor();
        floor.setFloorId(floorId);
        floor.setFloorName(floorName);
        map.put("floor", floor);
        map.put("parentId", parentId);
        map.put("maxSize", maxSize);
        return "floor/edit_floor_goods_rel";
    }

    @ControllerLog("保存商品楼层模板")
    @Authority(opCode = "14010113", opName = "保存商品楼层模板")
    @RequestMapping(value = "saveFloorGoodsRel", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public AjaxResult saveFloorGoodsRel(@RequestParam Integer floorId, @RequestParam(name = "goodsIds") String goodsIds) {
        try {
            Integer[] goodsIdArr = {};
            List<Integer> goodsIdList = new ArrayList<Integer>();
            String[] split = goodsIds.split(",");
            Arrays.asList(split).forEach(e -> goodsIdList.add(Integer.parseInt(e)));
            goodsIdArr = goodsIdList.toArray(goodsIdArr);
            return AppUtil.returnObj(floorService.saveFloorRel(floorId, goodsIdArr));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }

    @RequestMapping({ "goodsFloorListGoodsLoad" })
    @ResponseBody
    public String goodsFloorListGoodsLoad(HttpServletRequest request, HttpServletResponse response, String currentPage,
            String gcId, String goodsName) {
        List<Integer> goodsCatIds = new ArrayList<Integer>();
        if (!StringUtil.isEmpty(gcId)) {
            goodsCatIds = goodsCatNewService.selectAllSonCat(Integer.parseInt(gcId));
            goodsCatIds.add(Integer.parseInt(gcId));
        }
        PageAjax<Goods> pageGoods = floorService.pageGoods(currentPage, gcId, goodsCatIds, goodsName);
        String result = CommUtil.getPageAjaxDom(CommUtil.getURL(request) + "/admin/floor/goodsFloorListGoodsLoad", "",
                "&gcId=" + gcId + "&goodsName=" + goodsName, pageGoods);
        return result;
    }
}
