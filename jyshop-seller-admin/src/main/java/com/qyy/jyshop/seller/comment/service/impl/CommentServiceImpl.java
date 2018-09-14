package com.qyy.jyshop.seller.comment.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.dao.CommentDao;
import com.qyy.jyshop.model.Comment;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.seller.comment.service.CommentService;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class CommentServiceImpl extends AbstratService<Comment> implements CommentService {
	@Autowired
	private CommentDao commentDao;

	@Override
	public PageAjax<Map<String, Object>> pageComment(PageAjax<Map<String, Object>> page) {
		ParamData params = this.getParamData(page.getPageNo(), page.getPageSize());
		params.put("comId", this.getLoginUserComId());
		return this.pageQuery("CommentDao.queryCommentByParam", params);
	}

	@Override
	@Transactional
	public String updateCommentStatus(Long id, Short updateStatus) {
		Comment comment = new Comment();
		comment.setCommentId(id);
		comment.setStatus(updateStatus);
		commentDao.updateByPrimaryKeySelective(comment);
		return null;
	}

	@Override
	public Map<String,Object> queryCommentById(Long id) {
		 Map<String,Object> returnMap = new HashMap<String, Object>();
		List<Map<String,Object>> list = commentDao.queryCommentById(id);
		if(CollectionUtils.isNotEmpty(list)){
			returnMap = list.get(0);
		}
		return returnMap;
	}

	@Override
	@Transactional
	public String delCommentById(Long id) {
		this.delById(id);
		return null;
	}

}
