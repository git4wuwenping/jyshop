package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.TagRel;
import com.qyy.jyshop.supple.MyMapper;

public interface TagRelDao extends MyMapper<TagRel> {

	List<Map<String,Object>> selectTagRelByTagId(Long tagId);
	
	void deleteByTagId(Long tagId);
	
	/**
	 * 批量添加
	 * @param tagRelList
	 */
	void batchInsert(List<TagRel> tagRelList);

}
