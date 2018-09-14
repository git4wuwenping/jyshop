package com.qyy.jyshop.seller.goodscat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.pojo.TreeNode;
import com.qyy.jyshop.seller.common.annotation.Authority;
import com.qyy.jyshop.seller.common.annotation.ControllerLog;
import com.qyy.jyshop.seller.common.controller.BaseController;
import com.qyy.jyshop.seller.goodscat.service.GoodsCatService;

/**
 * 商品分类
 */
@Controller
@RequestMapping("/admin/goodscat")
public class GoodsCatController extends BaseController{
    
    @Autowired
    private GoodsCatService goodsCatService;
    
    @ControllerLog("获取所有商品分类")
    @RequestMapping("getGoodsCatTreeResult")
    @ResponseBody
    @Authority(opCode = "04010207", opName = "获取所有商品分类")
    public List<TreeNode> getGoodsCatTreeResult() {
        return goodsCatService.getGoodsCatTreeResult();
    }

}
