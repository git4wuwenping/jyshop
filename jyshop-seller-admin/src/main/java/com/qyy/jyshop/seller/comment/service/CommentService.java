package com.qyy.jyshop.seller.comment.service;

import java.util.Map;

import com.qyy.jyshop.pojo.PageAjax;

public interface CommentService {

	PageAjax<Map<String, Object>> pageComment(PageAjax<Map<String, Object>> page);

	String updateCommentStatus(Long id, Short updateStatus);

	Map<String,Object> queryCommentById(Long id);

	String delCommentById(Long id);

}
