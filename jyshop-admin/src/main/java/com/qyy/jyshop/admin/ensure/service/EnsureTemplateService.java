package com.qyy.jyshop.admin.ensure.service;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.EnsureTemplate;
import com.qyy.jyshop.model.EnsureTemplateGoodsRel;
import com.qyy.jyshop.pojo.PageAjax;

public interface EnsureTemplateService {

	PageAjax<Map<String,Object>> pageEnsureTemplate(PageAjax<Map<String,Object>> page);

	String deleteById(Long id);

	String add(EnsureTemplate EnsureTemplate,String tagIds);

	EnsureTemplate queryEnsureTemplateById(Long id);

	String edit(EnsureTemplate EnsureTemplate,String tagIds,String oldTagIds);

	List<String> selectTagList(Long templateId);

	List<EnsureTemplateGoodsRel> queryTemplateGoodsListByTemplateId(Long templateId);

	String addTemplateGoodsRel(Map<String, String> map);

	String delTemplateGoodsRel(Long id);

	String delAllTemplateGoodsRel(Long id);

	

}
