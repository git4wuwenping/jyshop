package com.qyy.jyshop.admin.ensure.service;

import java.util.List;

import com.qyy.jyshop.model.EnsureTag;
import com.qyy.jyshop.pojo.PageAjax;

public interface EnsureTagService {

	PageAjax<EnsureTag> pageEnsureTag(PageAjax<EnsureTag> page);

	String deleteById(Long id);

	String add(EnsureTag ensureTag);

	EnsureTag queryEnsureTagById(Long id);

	String edit(EnsureTag ensureTag);

	List<EnsureTag> selectAll();

}
