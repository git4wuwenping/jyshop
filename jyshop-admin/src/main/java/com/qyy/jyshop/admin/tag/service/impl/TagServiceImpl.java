package com.qyy.jyshop.admin.tag.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.page.PageMethod;
import com.qyy.jyshop.admin.tag.service.TagService;
import com.qyy.jyshop.dao.TagRelDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.Logi;
import com.qyy.jyshop.model.Tag;
import com.qyy.jyshop.model.TagRel;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.StringUtil;
import com.qyy.jyshop.util.TimestampUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * @author Administrator
 *
 */
@Service
public class TagServiceImpl extends AbstratService<Tag> implements TagService {
	
	@Autowired
	private TagRelDao tagRelDao;

	@Override
	public PageAjax<Map<String,Object>> pageTag(PageAjax<Map<String,Object>> page, Tag tag) {
		
		ParamData params = this.getParamData(page.getPageNo(),page.getPageSize());
        return this.pageQuery("TagDao.selectTagList", params);
	}

	@Override
	@Transactional
	public String doAddTag(Tag tag) {
		if(tag==null)
            return "添加的标签数据不能为空...";
		tag.setTagName(tag.getTagName().trim());
        if(StringUtil.isEmpty(tag.getTagName()))
            return "标签名称不能为空 ...";
        
        Example example = new Example(Tag.class);
        Criteria createCriteria = example.createCriteria();
        createCriteria.andEqualTo("tagName", tag.getTagName());
		List<Tag> queryByExample = this.queryByExample(example);
        if(!CollectionUtils.isEmpty(queryByExample))
            return "标签名称己存在,请换一个标签名称添加.";
        tag.setCreateTime(TimestampUtil.getNowTime());
        this.insert(tag);
        return null;
	}

	@Override
	public Tag queryByTagId(Long tagId) {
		return this.queryByID(tagId);
	}

	@Override
	@Transactional
	public String doEditTag(Tag tag) {
		if(tag==null)
            return "添加的标签数据不能为空...";
		tag.setTagName(tag.getTagName().trim());
        if(StringUtil.isEmpty(tag.getTagName()))
            return "标签名称不能为空 ...";
        

        Example example = new Example(Tag.class);
        Criteria createCriteria = example.createCriteria();
        createCriteria.andEqualTo("tagName", tag.getTagName());
		List<Tag> queryByExample = this.queryByExample(example);
        if(!CollectionUtils.isEmpty(queryByExample) && queryByExample.get(0).getTagId() != tag.getTagId())
            return "标签名称己存在,请换一个标签名称添加.";
        
        this.updateByID(tag);
        
        return null;
	}

	@Override
	@Transactional
	public String doDelTag(Long tagId) {
		Example example = new Example(TagRel.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("tagId", tagId);
		List<TagRel> selectByExample = tagRelDao.selectByExample(example);
		if(CollectionUtils.isNotEmpty(selectByExample)){
			throw new AppErrorException("该标签下包含关联的商品，请解除所有关联商品再删除！");
		}
		this.delById(tagId);
		return null;
	}

}
