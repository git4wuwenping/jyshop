package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.Comment;
import com.qyy.jyshop.supple.MyMapper;

public interface CommentDao extends MyMapper<Comment>{
	
	List<Map<String, Object>> queryCommentById(Long commentId);

    List<Map> queryOrderComment(String orderSn);

    List<Map> queryGoodsComment(Integer goodsId);

	List<Map<String, Object>> queryCommentByParam(Map<String, Object> map);

	
}
