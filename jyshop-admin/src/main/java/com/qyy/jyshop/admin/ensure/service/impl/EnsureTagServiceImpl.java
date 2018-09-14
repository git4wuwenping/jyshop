package com.qyy.jyshop.admin.ensure.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.qyy.jyshop.admin.ensure.service.EnsureTagService;
import com.qyy.jyshop.dao.EnsureTemplateTagRelDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.EnsureTag;
import com.qyy.jyshop.model.EnsureTemplateTagRel;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class EnsureTagServiceImpl extends AbstratService<EnsureTag> implements EnsureTagService {
	@Autowired
	private EnsureTemplateTagRelDao ensureTemplateTagRelDao;

	@Override
	public PageAjax<EnsureTag> pageEnsureTag(PageAjax<EnsureTag> page) {
	    Example example = new Example(EnsureTag.class);
	    example.setOrderByClause("tag_order asc");
	    
	    return this.queryPageListByExample(page.getPageNo(), page.getPageSize(), example);
	    
	    
	}

	@Override
	@Transactional
	public String deleteById(Long id) {
		Example example = new Example(EnsureTemplateTagRel.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("tagId", id);
		List<EnsureTemplateTagRel> selectByExample = ensureTemplateTagRelDao.selectByExample(example);
		if(selectByExample != null && selectByExample.size() > 0){
			throw new AppErrorException("该保障标签已被保障模板使用，请删除保障模板后再操作！");
		}else{
			this.delById(id);
			return null;
		}
	}

	@Override
	@Transactional
	public String add(EnsureTag ensureTag) {
		Example example = new Example(EnsureTag.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("tagName", ensureTag.getTagName());
		List<EnsureTag> queryByExample = this.queryByExample(example);
		if(queryByExample != null && queryByExample.size() > 0){
			throw new AppErrorException("保障标签名称已使用，请重新输入！");
		}else{
			this.insert(ensureTag);
			return null;
		}
	}

	@Override
	public EnsureTag queryEnsureTagById(Long id) {
		return this.queryByID(id);
	}

	@Override
	@Transactional
	public String edit(EnsureTag ensureTag) {
		Example example = new Example(EnsureTag.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("tagName", ensureTag.getTagName());
		createCriteria.andNotEqualTo("id", ensureTag.getId());
		List<EnsureTag> queryByExample = this.queryByExample(example);
		if(queryByExample != null && queryByExample.size() > 0){
			throw new AppErrorException("保障标签名称已使用，请重新输入！");
		}else{
			this.update(ensureTag);
			return null;
		}
	}

	@Override
	public List<EnsureTag> selectAll() {
		return this.queryAll();
	}

}
