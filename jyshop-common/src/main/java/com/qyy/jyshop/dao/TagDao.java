package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.Tag;
import com.qyy.jyshop.supple.MyMapper;

public interface TagDao extends MyMapper<Tag> {
	
	List<Map<String,Object>> selectTagList();

}
