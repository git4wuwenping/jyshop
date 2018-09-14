package com.qyy.jyshop.admin.ensure.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.qyy.jyshop.admin.ensure.service.EnsureTemplateGoodsRelService;
import com.qyy.jyshop.model.EnsureTemplateGoodsRel;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class EnsureTemplateGoodsRelServiceImpl extends AbstratService<EnsureTemplateGoodsRel> implements
		EnsureTemplateGoodsRelService {

	@Override
	public PageAjax<Map<String, Object>> pageTemplateGoodsRel(Long templateId, PageAjax<EnsureTemplateGoodsRel> page) {
	    
	    
	    ParamData params = getParamData(page.getPageNo(), page.getPageSize());
	    params.put("templateId", templateId);
        return this.pageQuery("EnsureTemplateGoodsRelDao.selectTemplateGoodsRelByTemplateId", params );
	    
	}

}
