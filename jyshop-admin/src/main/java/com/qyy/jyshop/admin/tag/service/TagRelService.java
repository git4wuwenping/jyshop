package com.qyy.jyshop.admin.tag.service;

import java.util.Map;

import com.qyy.jyshop.pojo.PageAjax;

public interface TagRelService {

	PageAjax<Map<String, Object>> pageTagRelAjax(Long tagId, PageAjax<Map<String, Object>> page);

	String delTagRel(Long id);

	String addTagRel(Map<String, String> map);
	
	String delTagRelByTagId(Long tagId);

}
