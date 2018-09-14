package com.qyy.jyshop.admin.ensure.service;

import java.util.Map;

import com.qyy.jyshop.model.EnsureTemplateGoodsRel;
import com.qyy.jyshop.pojo.PageAjax;

public interface EnsureTemplateGoodsRelService {

    PageAjax<Map<String, Object>>  pageTemplateGoodsRel(Long templateId, PageAjax<EnsureTemplateGoodsRel> page);

}
