package com.qyy.jyshop.admin.ensure.controller;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.ensure.service.EnsureTagService;
import com.qyy.jyshop.admin.ensure.service.EnsureTemplateGoodsRelService;
import com.qyy.jyshop.admin.ensure.service.EnsureTemplateService;
import com.qyy.jyshop.model.EnsureTemplate;
import com.qyy.jyshop.model.EnsureTemplateGoodsRel;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;

@Controller
@RequestMapping("/admin/ensureTemplate")
public class EnsureTemplateController extends BaseController {

	@Autowired
	private EnsureTemplateService ensureTemplateService;
	@Autowired
	private EnsureTemplateGoodsRelService templateGoodsRelService;
	@Autowired
	private EnsureTagService ensureTagService;

	@Authority(opCode = "040402", opName = "保障模板")
	@RequestMapping("ensureTemplateMain")
	public String EnsureTemplateMain() {
		return "ensure/template/ensure_template_main";
	}

	@ControllerLog("查询保障模板列表")
	@RequestMapping("pageEnsureTemplateAjax")
	@ResponseBody
	@Authority(opCode = "04040201", opName = "查询保障模板列表")
	public PageAjax<Map<String,Object>> pageEnsureTemplateAjax(PageAjax<Map<String,Object>> page) {
		return ensureTemplateService.pageEnsureTemplate(page);
	}

	@Authority(opCode = "04040202", opName = "添加保障模板页面")
	@RequestMapping("preAddEnsureTemplate")
	public String preAddEnsureTemplate(Map<String,Object> map) {
		map.put("tagList", ensureTagService.selectAll());
		return "ensure/template/add_ensure_template";
	}

	@ControllerLog("添加保障模板")
	@RequestMapping("addEnsureTemplate")
	@ResponseBody
	@Authority(opCode = "04040203", opName = "添加保障模板")
	public AjaxResult addEnsureTemplate(EnsureTemplate EnsureTemplate,String tagIds) {
		try {
			return AppUtil.returnObj(ensureTemplateService.add(EnsureTemplate,tagIds));
		} catch (Exception e) {
			return AppUtil.returnObj(2, e.getMessage());
		}
	}

	@Authority(opCode = "04040204", opName = "修改保障模板页面")
	@RequestMapping("preEditEnsureTemplate/{id}")
	public String preEditEnsureTemplate(@PathVariable Long id, Map<String, Object> map) {
		map.put("ensureTemplate", ensureTemplateService.queryEnsureTemplateById(id));
		map.put("allTagList", ensureTagService.selectAll());
		map.put("tagIds", StringUtils.join(ensureTemplateService.selectTagList(id),","));
		return "ensure/template/edit_ensure_template";
	}

	@ControllerLog("修改保障模板")
	@RequestMapping("editEnsureTemplate")
	@ResponseBody
	@Authority(opCode = "04040205", opName = "修改保障模板")
	public AjaxResult editEnsureTemplate(EnsureTemplate EnsureTemplate,String tagIds,String oldTagIds) {
		try {
			return AppUtil.returnObj(ensureTemplateService.edit(EnsureTemplate,tagIds,oldTagIds));
		} catch (Exception e) {
			return AppUtil.returnObj(2, e.getMessage());
		}
	}

	@ControllerLog("删除保障模板")
	@RequestMapping("delEnsureTemplate/{id}")
	@ResponseBody
	@Authority(opCode = "04040206", opName = "删除保障模板")
	public AjaxResult delContent(@PathVariable Long id) {
		try {
			return AppUtil.returnObj(ensureTemplateService.deleteById(id));
		} catch (Exception e) {
			return AppUtil.returnObj(2, e.getMessage());
		}
	}
	
	@ControllerLog("设置商品页面")
	@Authority(opCode = "04040207", opName = "设置商品页面")
	@RequestMapping(value = "preEnsureTemplateGoodsSetting/{id}")
	public String preEnsureTemplateGoodsSetting(@PathVariable("id") Long templateId, Map<String, Object> map) {
//		List<EnsureTemplateGoodsRel> templateGoodsRel = ensureTemplateService.queryTemplateGoodsListByTemplateId(templateId);
//		map.put("tagGoodsList", templateGoodsRel);
		map.put("template", ensureTemplateService.queryEnsureTemplateById(templateId));
		return "ensure/template/template_goods_setting";
	}
	
	@ControllerLog("查询保障模板关联商品列表")
	@RequestMapping("pageTemplateGoodsRelAjax/{id}")
	@ResponseBody
	@Authority(opCode = "04040208", opName = "查询保障模板关联商品列表")
	public PageAjax<Map<String, Object>>  pageTemplateGoodsRelAjax(@PathVariable("id") Long templateId,PageAjax<EnsureTemplateGoodsRel> page) {
		return templateGoodsRelService.pageTemplateGoodsRel(templateId,page);
	}
	
	@RequestMapping("selectGoods")
    @Authority(opCode = "04040209", opName = "商品列表页面")
    public String selectGoods(@RequestParam String templateId,Map<String,Object> map) {
		map.put("templateId", templateId);
        return "ensure/template/goods_select";
    }
	
	
	@ControllerLog("添加保障模版关联商品")
	@RequestMapping("addTemplateGoodsRel")
	@ResponseBody
	@Authority(opCode = "04040210", opName = "添加保障模版关联商品")
	public AjaxResult addTemplateGoodsRel(@RequestParam Map<String,String> map) {
		try {
			return AppUtil.returnObj(ensureTemplateService.addTemplateGoodsRel(map));
		} catch (Exception e) {
			return AppUtil.returnObj(2, e.getMessage());
		}
	}
	@ControllerLog("删除保障模版关联商品")
	@RequestMapping("delTemplateGoodsRel/{id}")
	@ResponseBody
	@Authority(opCode = "04040211", opName = "删除保障模版关联商品")
	public AjaxResult delTemplateGoodsRel(@PathVariable Long id) {
		try {
			return AppUtil.returnObj(ensureTemplateService.delTemplateGoodsRel(id));
		} catch (Exception e) {
			return AppUtil.returnObj(2, e.getMessage());
		}
	}
	
	
	@ControllerLog("删除保障模版关联所有商品")
	@RequestMapping("delAllTemplateGoodsRel/{id}")
	@ResponseBody
	@Authority(opCode = "04040212", opName = "删除保障模版关联所有商品")
	public AjaxResult delAllTemplateGoodsRel(@PathVariable Long id) {
		try {
			return AppUtil.returnObj(ensureTemplateService.delAllTemplateGoodsRel(id));
		} catch (Exception e) {
			return AppUtil.returnObj(2, e.getMessage());
		}
	}
}
