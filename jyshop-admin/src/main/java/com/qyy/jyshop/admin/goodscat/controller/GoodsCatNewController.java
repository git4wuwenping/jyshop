package com.qyy.jyshop.admin.goodscat.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.goodscat.service.GoodsCatNewService;
import com.qyy.jyshop.model.GoodsCat;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.util.AppUtil;

/**
 * 商品分类
 */
@Controller
@RequestMapping("/admin/goodscatNew")
public class GoodsCatNewController extends BaseController {
	@Autowired
	private GoodsCatNewService goodsCatNewService;

	@RequestMapping("goodsCatMain")
	@Authority(opCode = "040108", opName = "商品分类")
	public String preAddGoodsCat() {
		return "goodscat/new/goodscat_main";
	}


	@ControllerLog("根据父级分类ID获得分类列表")
	@RequestMapping("listGoodsCatByParentId/{id}")
	@ResponseBody
	@Authority(opCode = "04010801", opName = "根据父级分类ID获得分类列表")
	public AjaxResult listGoodsCatByParentId(@PathVariable("id") Long parentId) {
		return AppUtil.returnList(null,goodsCatNewService.listGoodsCatByParentId(parentId));
	}
	
	
	@RequestMapping("preAddGoodsCat")
	@Authority(opCode = "04010802", opName = "添加分类页面")
	public String preAddGoodsCat(@RequestParam Long parentId,@RequestParam String parentName,Map<String,Object> map) {
		map.put("parentId", parentId);
		map.put("parentName", parentName);
		return "goodscat/new/add_goodscat";
	}
	
	@ControllerLog("添加商品分类")
	@RequestMapping("addGoodsCat")
	@ResponseBody
	@Authority(opCode = "04010803", opName = "添加商品分类")
	public AjaxResult addGoodsCat(GoodsCat goodsCat) {
		return AppUtil.returnObj(goodsCatNewService.addGoodsCat(goodsCat));
	}
	
	@RequestMapping("preEditGoodsCat/{id}")
	@Authority(opCode = "04010804", opName = "修改分类页面")
	public String preEditGoodsCat(@PathVariable("id") Integer id, Map<String, Object> map) {
		map.put("category", goodsCatNewService.queryGoodsCatByCatId(id));
		return "goodscat/new/edit_goodscat";
	}
	
	@ControllerLog("修改商品分类")
	@RequestMapping("editGoodsCat")
	@ResponseBody
	@Authority(opCode = "04010805", opName = "修改商品分类")
	public AjaxResult editGoodsCat(GoodsCat goodsCat) {
		return AppUtil.returnObj(goodsCatNewService.editGoodsCat(goodsCat));
	}
	
	@ControllerLog("修改分类显示状态")
	@RequestMapping("editGoodsCatDisable/{id}")
	@ResponseBody
	@Authority(opCode = "04010806", opName = "修改分类显示状态")
	public AjaxResult editGoodsCatDisable(@PathVariable("id") Integer id,@RequestParam Integer disable) {
		return AppUtil.returnObj(goodsCatNewService.editGoodsCatDisable(id,disable));
	}
}
