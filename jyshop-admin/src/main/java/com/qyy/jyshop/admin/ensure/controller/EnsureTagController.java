package com.qyy.jyshop.admin.ensure.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.ensure.service.EnsureTagService;
import com.qyy.jyshop.model.EnsureTag;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;

@Controller
@RequestMapping("/admin/ensureTag")
public class EnsureTagController extends BaseController {

	@Autowired
	private EnsureTagService ensureTagService;

	@Authority(opCode = "040401", opName = "保障标签")
	@RequestMapping("ensureTagMain")
	public String ensureTagMain() {
		return "ensure/tag/ensure_tag_main";
	}

	@ControllerLog("查询保障标签列表")
	@RequestMapping("pageEnsureTagAjax")
	@ResponseBody
	@Authority(opCode = "04040101", opName = "查询保障标签列表")
	public PageAjax<EnsureTag> pageEnsureTagAjax(PageAjax<EnsureTag> page) {
		return ensureTagService.pageEnsureTag(page);
	}

	@Authority(opCode = "04040102", opName = "添加保障标签页面")
	@RequestMapping("preAddEnsureTag")
	public String preAddEnsureTag() {
		return "ensure/tag/add_ensure_tag";
	}

	@ControllerLog("添加保障标签")
	@RequestMapping("addEnsureTag")
	@ResponseBody
	@Authority(opCode = "04040103", opName = "添加保障标签")
	public AjaxResult addEnsureTag(EnsureTag ensureTag) {
		try {
			return AppUtil.returnObj(ensureTagService.add(ensureTag));
		} catch (Exception e) {
			return AppUtil.returnObj(2, e.getMessage());
		}
	}

	@Authority(opCode = "04040104", opName = "修改保障标签页面")
	@RequestMapping("preEditEnsureTag/{id}")
	public String preEditEnsureTag(@PathVariable Long id, Map<String, Object> map) {
		map.put("ensureTag", ensureTagService.queryEnsureTagById(id));
		return "ensure/tag/edit_ensure_tag";
	}

	@ControllerLog("修改保障标签")
	@RequestMapping("editEnsureTag")
	@ResponseBody
	@Authority(opCode = "04040105", opName = "修改保障标签")
	public AjaxResult editEnsureTag(EnsureTag ensureTag) {
		try {
			return AppUtil.returnObj(ensureTagService.edit(ensureTag));
		} catch (Exception e) {
			return AppUtil.returnObj(2, e.getMessage());
		}
	}

	@ControllerLog("删除保障标签")
	@RequestMapping("delEnsureTag/{id}")
	@ResponseBody
	@Authority(opCode = "04040106", opName = "删除保障标签")
	public AjaxResult delContent(@PathVariable Long id) {
		try {
			return AppUtil.returnObj(ensureTagService.deleteById(id));
		} catch (Exception e) {
			return AppUtil.returnObj(2, e.getMessage());
		}
	}
}
