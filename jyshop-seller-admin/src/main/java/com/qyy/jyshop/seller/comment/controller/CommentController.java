package com.qyy.jyshop.seller.comment.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.seller.comment.service.CommentService;
import com.qyy.jyshop.seller.common.annotation.Authority;
import com.qyy.jyshop.seller.common.annotation.ControllerLog;
import com.qyy.jyshop.seller.common.controller.BaseController;
import com.qyy.jyshop.util.AppUtil;

@Controller
@RequestMapping("/admin/comment")
public class CommentController extends BaseController {

	@Autowired
	private CommentService commentService;

	@Authority(opCode = "030301", opName = "评价管理")
	@RequestMapping("commentMain")
	public String commentMain() {
		return "comment/comment_main";
	}

	@ControllerLog("查询评价列表")
	@RequestMapping("pageCommentAjax")
	@ResponseBody
	@Authority(opCode = "03030101", opName = "查询评价列表")
	public PageAjax<Map<String,Object>> pageCommentAjax(PageAjax<Map<String,Object>> page) {
		return commentService.pageComment(page);
	}


//	@ControllerLog("修改评价状态")
//	@RequestMapping("updateCommentStatus/{id}")
//	@ResponseBody
//	@Authority(opCode = "03030102", opName = "修改评价状态")
//	public AjaxResult updateCommentStatus(@PathVariable Long id,Short updateStatus) {
//		try {
//			return AppUtil.returnObj(commentService.updateCommentStatus(id,updateStatus));
//		} catch (Exception e) {
//			return AppUtil.returnObj(2, e.getMessage());
//		}
//	}

	@Authority(opCode = "03030103", opName = "评价详情页面")
	@RequestMapping("preEditComment/{id}")
	public String preEditComment(@PathVariable Long id, Map<String, Object> map) {

		map.put("comment", commentService.queryCommentById(id));
		return "comment/edit_comment";
	}

	

//	@ControllerLog("删除评价")
//	@RequestMapping(value="delComment/{id}")
//	@ResponseBody
//	@Authority(opCode = "03030104", opName = "删除评价")
//	public AjaxResult delComment(@PathVariable Long id) {
//		try {
//			return AppUtil.returnObj(commentService.delCommentById(id));
//		} catch (Exception e) {
//			return AppUtil.returnObj(2, e.getMessage());
//		}
//	}
}
