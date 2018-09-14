package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.EnsureTemplate;
import com.qyy.jyshop.supple.MyMapper;

public interface EnsureTemplateDao  extends MyMapper<EnsureTemplate>{
	
	List<Map<String,Object>> selectTemplateByParams(Map<String,Object> params);

}
