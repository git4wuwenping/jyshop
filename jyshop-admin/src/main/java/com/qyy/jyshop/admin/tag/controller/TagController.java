package com.qyy.jyshop.admin.tag.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.tag.service.TagRelService;
import com.qyy.jyshop.admin.tag.service.TagService;
import com.qyy.jyshop.model.Tag;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;

@Controller
@RequestMapping("/admin/tag")
public class TagController extends BaseController {

	@Autowired
	private TagService tagService;
	@Autowired
	private TagRelService tagRelService;

	@Authority(opCode = "040301", opName = "标签列表")
	@RequestMapping(value = "tagMain", method = { RequestMethod.POST, RequestMethod.GET })
	public String tagMain() {
		return "tag/tag_main";
	}

	@ControllerLog("查询标签列表")
	@RequestMapping(value = "pageTagAjax", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	@Authority(opCode = "04030101", opName = "查询标签列表")
	public PageAjax<Map<String,Object>> pagetagAjax(PageAjax<Map<String,Object>> page, Tag tag) {
		return tagService.pageTag(page, tag);
	}

	@ControllerLog("添加标签页面")
	@Authority(opCode = "04030102", opName = "添加标签页面")
	@RequestMapping(value = "preAddTag", method = { RequestMethod.POST, RequestMethod.GET })
	public String preAddtag() {
		return "tag/add_tag";
	}

	@ControllerLog("添加标签")
	@RequestMapping(value = "addTag", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	@Authority(opCode = "04030103", opName = "添加标签")
	public AjaxResult addtag(Tag tag) {
		try {
			return AppUtil.returnObj(tagService.doAddTag(tag));
		} catch (Exception e) {
			return AppUtil.returnObj(2, e.getMessage());
		}
	}

	@ControllerLog("更新标签页面")
	@Authority(opCode = "04030104", opName = "查询标签详情")
	@RequestMapping(value = "preEditTag/{tagId}", method = { RequestMethod.POST, RequestMethod.GET })
	public String preEdittag(@PathVariable("tagId") Long tagId, Map<String, Object> map) {
		Tag tag = tagService.queryByTagId(tagId);
		map.put("tag", tag);
		return "tag/edit_tag";
	}

	@ControllerLog("修改标签")
	@RequestMapping(value = "editTag", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	@Authority(opCode = "04030105", opName = "修改标签")
	public AjaxResult edittag(Tag tag) {
		try {
			return AppUtil.returnObj(tagService.doEditTag(tag));
		} catch (Exception e) {
			return AppUtil.returnObj(2, e.getMessage());
		}
	}

	@ControllerLog("删除标签")
	@RequestMapping(value = "delTag/{tagId}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	@Authority(opCode = "04030106", opName = "删除标签")
	public AjaxResult deltag(@PathVariable("tagId") Long tagId) {
		try {
			return AppUtil.returnObj(tagService.doDelTag(tagId));
		} catch (Exception e) {
			return AppUtil.returnObj(2, e.getMessage());
		}
	}
	
	@ControllerLog("设置商品页面")
	@Authority(opCode = "04030107", opName = "设置商品页面")
	@RequestMapping(value = "preTagGoodsSetting/{tagId}", method = { RequestMethod.POST, RequestMethod.GET })
	public String preTagGoodsSetting(@PathVariable("tagId") Long tagId,@RequestParam String tagName,Map<String, Object> map) {
		Tag tag = new Tag();
		tag.setTagId(tagId);
		tag.setTagName(tagName);
		map.put("tag", tag);
		return "tag/tag_setting";
	}
	
	@RequestMapping("selectGoods")
    @Authority(opCode = "04030108", opName = "商品列表页面")
    public String selectGoods(Tag tag,Map<String,Object> map) {
		map.put("tag", tag);
        return "tag/goods_select";
    }
	
	/*@ControllerLog("修改标签商品")
	@Authority(opCode = "04030109", opName = "修改标签商品")
	@ResponseBody
	@RequestMapping(value = "editTagRel", method = { RequestMethod.POST, RequestMethod.GET })
	public AjaxResult editTagRel(Long tagId,String goodsIds,String goodsIdsOld) {
        try {
            return AppUtil.returnObj(tagRelService.editTagRel(tagId,goodsIds,goodsIdsOld));
        } catch (RuntimeException e) {
            this.logger.error("修改标签商品失败", e);
            return AppUtil.returnObj(2, e.getMessage());
        }
    }*/
	
	
	@ControllerLog("查询商品标签关联的商品列表")
	@RequestMapping("pageTagRelAjax/{id}")
	@ResponseBody
	@Authority(opCode = "04030109", opName = "查询商品标签关联的商品列表")
	public PageAjax<Map<String,Object>> pageTagRelAjax(@PathVariable("id") Long tagId,PageAjax<Map<String,Object>> page) {
		return tagRelService.pageTagRelAjax(tagId,page);
	}
	
	@ControllerLog("添加商品标签关联商品")
	@RequestMapping("addTagRel")
	@ResponseBody
	@Authority(opCode = "04030110", opName = "添加商品标签关联商品")
	public AjaxResult addTagRel(@RequestParam Map<String,String> map) {
		try {
			return AppUtil.returnObj(tagRelService.addTagRel(map));
		} catch (Exception e) {
			return AppUtil.returnObj(2, e.getMessage());
		}
	}
	
	@ControllerLog("删除商品标签关联商品")
	@RequestMapping("delTagRel/{id}")
	@ResponseBody
	@Authority(opCode = "04030111", opName = "删除商品标签关联商品")
	public AjaxResult delTagRel(@PathVariable Long id) {
		try {
			return AppUtil.returnObj(tagRelService.delTagRel(id));
		} catch (Exception e) {
			return AppUtil.returnObj(2, e.getMessage());
		}
	}
	
	@ControllerLog("清除商品标签所有关联商品")
	@RequestMapping("delAllTagRel/{id}")
	@ResponseBody
	@Authority(opCode = "04030112", opName = "清除商品标签所有关联商品")
	public AjaxResult delAllTagRel(@PathVariable Long id) {
		try {
			return AppUtil.returnObj(tagRelService.delTagRelByTagId(id));
		} catch (Exception e) {
			return AppUtil.returnObj(2, e.getMessage());
		}
	}
}
