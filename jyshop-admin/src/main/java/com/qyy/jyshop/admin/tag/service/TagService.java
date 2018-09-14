package com.qyy.jyshop.admin.tag.service;

import java.util.Map;

import com.qyy.jyshop.model.Tag;
import com.qyy.jyshop.pojo.PageAjax;

public interface TagService {

	PageAjax<Map<String,Object>> pageTag(PageAjax<Map<String,Object>> page, Tag tag);

	String doAddTag(Tag tag);

	Tag queryByTagId(Long tagId);

	String doEditTag(Tag tag);

	String doDelTag(Long tagId);

}
