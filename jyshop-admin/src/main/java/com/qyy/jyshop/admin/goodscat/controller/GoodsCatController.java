package com.qyy.jyshop.admin.goodscat.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.pojo.TreeNode;
import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.goodscat.service.GoodsCatService;
import com.qyy.jyshop.model.GoodsCat;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;

/**
 * 商品分类
 */
@Controller
@RequestMapping("/admin/goodscat")
public class GoodsCatController extends BaseController {
	@Autowired
	private GoodsCatService goodsCatService;

	@RequestMapping("goodsCatMain")
	@Authority(opCode = "040102", opName = "商品分类")
	public String preAddGoodsCat(Map<String, Object> map) {
		List<GoodsCat> goodsCatList = goodsCatService.queryGoodsCatList();
		map.put("goodsCatList",goodsCatList);
		return "goodscat/goodscat_main";
	}

	@ControllerLog("查询商品分类列表")
	@RequestMapping("pageGoodsCatAjax")
	@ResponseBody
	@Authority(opCode = "04010201", opName = "查询商品分类列表")
	public PageAjax<Map<String, Object>> pageGoodsCatAjax(PageAjax<GoodsCat> page,@RequestParam(value = "catLevel",defaultValue = "0") Integer catLevel) {
		return goodsCatService.pageGoodsCat(page,catLevel);
	}

	@RequestMapping("preAddGoodsCat")
	@Authority(opCode = "04010202", opName = "添加分类页面")
	public String preAddGoodsCat(ModelMap modelMap) {
		List<GoodsCat> goodsCatList = goodsCatService.queryGoodsCatList();
		modelMap.addAttribute("goodsCatList", goodsCatList);
		return "goodscat/add_goodscat";
	}

	@ControllerLog("添加商品分类")
	@RequestMapping("addGoodsCat")
	@ResponseBody
	@Authority(opCode = "04010203", opName = "添加商品分类")
	public AjaxResult addGoodsCat(GoodsCat goodsCat) {
		return AppUtil.returnObj(goodsCatService.addGoodsCat(goodsCat));
	}

	@ControllerLog("删除商品分类")
	@RequestMapping("delGoodsCat/{id}")
	@ResponseBody
	@Authority(opCode = "04010204", opName = "删除商品分类")
	public AjaxResult delGoodsCat(@PathVariable("id") Integer id) {
		try {
			return AppUtil.returnObj(goodsCatService.delGoodsCat(id));
		} catch (Exception e) {
			return AppUtil.returnObj(2, e.getMessage());
		}
	}

	@RequestMapping("preEditGoodsCat/{id}")
	@Authority(opCode = "04010205", opName = "修改分类页面")
	public String preEditGoodsCat(@PathVariable("id") Integer id, Map<String, Object> map) {
		List<GoodsCat> goodsCatList = goodsCatService.queryGoodsCatList();
		map.put("goodsCatList", goodsCatList);
		map.put("category", this.goodsCatService.queryGoodsCatByCatId(id));
		return "goodscat/edit_goodscat";
	}

	@ControllerLog("修改商品分类")
	@RequestMapping("editGoodsCat")
	@ResponseBody
	@Authority(opCode = "04010206", opName = "修改商品分类")
	public AjaxResult editGoodsCat(GoodsCat goodsCat) {
		return AppUtil.returnObj(goodsCatService.editGoodsCat(goodsCat));
	}

	@ControllerLog("获取所有商品分类")
	@RequestMapping("getGoodsCatTreeResult")
	@ResponseBody
	@Authority(opCode = "04010207", opName = "获取所有商品分类")
	public List<TreeNode> getGoodsCatTreeResult() {
		return goodsCatService.getGoodsCatTreeResult();
	}

}
