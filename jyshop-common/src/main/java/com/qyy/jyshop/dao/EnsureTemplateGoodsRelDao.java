package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.EnsureTemplateGoodsRel;
import com.qyy.jyshop.supple.MyMapper;

public interface EnsureTemplateGoodsRelDao  extends MyMapper<EnsureTemplateGoodsRel>{

	void deleteByTemplateId(Long id);
	
	void batchInsert(List<EnsureTemplateGoodsRel> list);

	List<Map<String,Object>> selectTemplateGoodsRelByTemplateId(Long templateId);

    List<String> selectTemplateGoodsIdExceptTemplateId(Long templateId);

    List<String> selectTemplateGoodsIdByTemplateId(Long templateId);
}
