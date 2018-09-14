package com.qyy.jyshop.admin.tag.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.qyy.jyshop.admin.tag.service.TagRelService;
import com.qyy.jyshop.dao.TagRelDao;
import com.qyy.jyshop.model.TagRel;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;

/**
 * @author Administrator
 *
 */
@Service
public class TagRelServiceImpl extends AbstratService<TagRel> implements TagRelService {

	@Autowired
	private TagRelDao tagRelDao;

	@Override
	public PageAjax<Map<String, Object>> pageTagRelAjax(Long tagId, PageAjax<Map<String, Object>> page) {
		ParamData params = this.getParamData(page.getPageNo(), page.getPageSize());
		params.put("tagId", tagId);
		return this.pageQuery("TagRelDao.selectTagRelByTagId", params);
	}

	@Override
	public String delTagRel(Long id) {
		this.deleteByID(id);
		return null;
	}

	@Override
	@Transactional
	public String addTagRel(Map<String, String> map) {
		Long tagId = Long.parseLong(map.get("tagId"));
		String goodsIds = map.get("goodsIds");

		List<String> addList = Arrays.asList(goodsIds.split("\\|"));
		List<String> goodsIdList = Arrays.asList(goodsIds.split("\\|"));
		if (CollectionUtils.isEmpty(goodsIdList)) {
			return null;
		}

		List<String> nowList = new ArrayList<String>();
		Example example = new Example(TagRel.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("tagId", tagId);
		List<TagRel> relList = tagRelDao.selectByExample(example);
		relList.forEach(rel -> {
			nowList.add(rel.getGoodsId().toString());
		});

		addList = addList.stream().filter(t -> !nowList.contains(t)).collect(Collectors.toList());

		if (CollectionUtils.isNotEmpty(addList)) {
			List<TagRel> list = new ArrayList<TagRel>();
			for (String goodIdStr : addList) {
				TagRel tagRel = new TagRel();
				tagRel.setGoodsId(Long.parseLong(goodIdStr));
				tagRel.setTagId(tagId);
				list.add(tagRel);
			}
			tagRelDao.batchInsert(list);
		}
		return null;
	}

	@Override
	public String delTagRelByTagId(Long tagId) {
		this.tagRelDao.deleteByTagId(tagId);
		return null;
	}

}
