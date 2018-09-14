package com.qyy.jyshop.dao;

import java.util.List;

import com.qyy.jyshop.model.EnsureTemplateTagRel;
import com.qyy.jyshop.supple.MyMapper;

public interface EnsureTemplateTagRelDao  extends MyMapper<EnsureTemplateTagRel>{

	void batchInsert(List<EnsureTemplateTagRel> ensureTemplateTagRelList);

	void deleteByTemplateId(Long templateId);

}
