package com.qyy.jyshop.admin.ensure.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.qyy.jyshop.admin.ensure.service.EnsureTemplateService;
import com.qyy.jyshop.dao.EnsureTemplateGoodsRelDao;
import com.qyy.jyshop.dao.EnsureTemplateTagRelDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.EnsureTemplate;
import com.qyy.jyshop.model.EnsureTemplateGoodsRel;
import com.qyy.jyshop.model.EnsureTemplateTagRel;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.TimestampUtil;

@Service
public class EnsureTemplateServiceImpl extends AbstratService<EnsureTemplate> implements EnsureTemplateService {

    // @Autowired
    // private EnsureTemplateDao ensureTemplateDao;
    @Autowired
    private EnsureTemplateGoodsRelDao ensureTemplateGoodsRelDao;
    @Autowired
    private EnsureTemplateTagRelDao ensureTemplateTagRelDao;

    @Override
    public PageAjax<Map<String, Object>> pageEnsureTemplate(PageAjax<Map<String, Object>> page) {
        ParamData pd = this.getParamData(page.getPageNo(), page.getPageSize());
        return this.pageQuery("EnsureTemplateDao.selectTemplateByParams", pd);
    }

    @Override
    @Transactional
    public String deleteById(Long id) {
        Example example = new Example(EnsureTemplateGoodsRel.class);
        Criteria createCriteria = example.createCriteria();
        createCriteria.andEqualTo("templateId", id);
        List<EnsureTemplateGoodsRel> selectByExample = ensureTemplateGoodsRelDao.selectByExample(example);
        if (selectByExample != null && selectByExample.size() > 0) {
            throw new AppErrorException("该保障模板已关联商品，请取消所有关联商品再操作！");
        } else {
            this.delById(id);
            ensureTemplateGoodsRelDao.deleteByTemplateId(id);
            ensureTemplateTagRelDao.deleteByTemplateId(id);
            return null;
        }
    }

    @Override
    @Transactional
    public String add(EnsureTemplate ensureTemplate, String tagIds) {
        Example example = new Example(EnsureTemplate.class);
        Criteria createCriteria = example.createCriteria();
        createCriteria.andEqualTo("templateName", ensureTemplate.getTemplateName());
        List<EnsureTemplate> queryByExample = this.queryByExample(example);
        if (queryByExample != null && queryByExample.size() > 0) {
            throw new AppErrorException("保障模板名称已使用，请重新输入！");
        } else {

            ensureTemplate.setCreateTime(TimestampUtil.getNowTime());
            this.insert(ensureTemplate);
            Long templateId = ensureTemplate.getId();
            List<String> tagIdList = Arrays.asList(tagIds.split(","));

            if (StringUtils.isNotBlank(tagIds) && tagIdList != null && tagIdList.size() > 0) {
                List<EnsureTemplateTagRel> ensureTemplateTagRelList = new ArrayList<EnsureTemplateTagRel>();
                for (String tagId : tagIdList) {
                    EnsureTemplateTagRel templateTagRel = new EnsureTemplateTagRel();
                    templateTagRel.setTagId(Long.parseLong(tagId));
                    templateTagRel.setTemplateId(templateId);
                    ensureTemplateTagRelList.add(templateTagRel);
                }
                ensureTemplateTagRelDao.batchInsert(ensureTemplateTagRelList);
            }
            return null;
        }
    }

    @Override
    public EnsureTemplate queryEnsureTemplateById(Long id) {
        return this.queryByID(id);
    }

    @Override
    @Transactional
    public String edit(EnsureTemplate ensureTemplate, String tagIds, String oldTagIds) {
        Example example = new Example(EnsureTemplate.class);
        Criteria createCriteria = example.createCriteria();
        createCriteria.andEqualTo("templateName", ensureTemplate.getTemplateName());
        createCriteria.andNotEqualTo("id", ensureTemplate.getId());
        List<EnsureTemplate> queryByExample = this.queryByExample(example);
        if (queryByExample != null && queryByExample.size() > 0) {
            throw new AppErrorException("保障模板名称已使用，请重新输入！");
        } else {
            this.update(ensureTemplate);
            if (tagIds != null && oldTagIds != null && !tagIds.equals(oldTagIds)) {
                Long templateId = ensureTemplate.getId();
                if (StringUtils.isNotBlank(oldTagIds)) {
                    ensureTemplateTagRelDao.deleteByTemplateId(templateId);
                }
                if (StringUtils.isNotBlank(tagIds)) {
                    List<String> tagIdList = Arrays.asList(tagIds.split(","));
                    if (tagIdList != null && tagIdList.size() > 0) {
                        List<EnsureTemplateTagRel> ensureTemplateTagRelList = new ArrayList<EnsureTemplateTagRel>();
                        for (String tagId : tagIdList) {
                            EnsureTemplateTagRel templateTagRel = new EnsureTemplateTagRel();
                            templateTagRel.setTagId(Long.parseLong(tagId));
                            templateTagRel.setTemplateId(templateId);
                            ensureTemplateTagRelList.add(templateTagRel);
                        }
                        ensureTemplateTagRelDao.batchInsert(ensureTemplateTagRelList);
                    }
                }
            }
            return null;
        }
    }

    @Override
    public List<String> selectTagList(Long templateId) {
        List<String> returnList = new ArrayList<String>();
        Example example = new Example(EnsureTemplateTagRel.class);
        Criteria createCriteria = example.createCriteria();
        createCriteria.andEqualTo("templateId", templateId);
        List<EnsureTemplateTagRel> selectByExample = ensureTemplateTagRelDao.selectByExample(example);
        selectByExample.forEach(templateTagRel -> {
            returnList.add(templateTagRel.getTagId().toString());
        });
        return returnList;
    }

    @Override
    public List<EnsureTemplateGoodsRel> queryTemplateGoodsListByTemplateId(Long templateId) {
        Example example = new Example(EnsureTemplateGoodsRel.class);
        Criteria createCriteria = example.createCriteria();
        createCriteria.andEqualTo("templateId", templateId);
        List<EnsureTemplateGoodsRel> selectByExample = ensureTemplateGoodsRelDao.selectByExample(example);
        return selectByExample;
    }

    @Override
    @Transactional
    public String addTemplateGoodsRel(Map<String, String> map) {
        Long templateId = Long.parseLong(map.get("templateId"));
        String goodsIds = map.get("goodsIds");

        List<String> addList = Arrays.asList(goodsIds.split("\\|"));
        List<String> goodsIdList = Arrays.asList(goodsIds.split("\\|"));
        if (CollectionUtils.isEmpty(goodsIdList)) {
            return null;
        }

        // 一个商品只能关联单个保障模板
        // 不重复关联保障模板
        List<String> existList = ensureTemplateGoodsRelDao.selectTemplateGoodsIdExceptTemplateId(templateId);
        
        addList = addList.stream().filter(t -> !existList.contains(t)).collect(Collectors.toList());

        
        List<String> nowList = ensureTemplateGoodsRelDao.selectTemplateGoodsIdByTemplateId(templateId);
        
        addList = addList.stream().filter(t -> !nowList.contains(t)).collect(Collectors.toList());
        
        List<String> collect1 = goodsIdList.stream().filter(t -> nowList.contains(t)).collect(Collectors.toList());
        StringBuffer sb = new StringBuffer();
        if(collect1.size() > 0){
            sb.append("商品ID:"+collect1.toString()+"已经关联过该保障标签.");
        }
        List<String> collect2 = goodsIdList.stream().filter(t -> existList.contains(t)).collect(Collectors.toList());
        if(collect2.size() > 0){
            sb.append("商品ID:"+collect2.toString()+"已经关联过其他保障标签.");
        }
        
        List<EnsureTemplateGoodsRel> list = new ArrayList<EnsureTemplateGoodsRel>();
        addList.forEach(e -> {
            EnsureTemplateGoodsRel ensureTemplateGoodsRel = new EnsureTemplateGoodsRel();
            ensureTemplateGoodsRel.setTemplateId(templateId);
            ensureTemplateGoodsRel.setGoodsId(Long.parseLong(e));
            list.add(ensureTemplateGoodsRel);
        });
        if(CollectionUtils.isNotEmpty(addList)){
            sb.append("商品ID:"+addList.toString()+"成功关联该保障标签.");
            ensureTemplateGoodsRelDao.batchInsert(list);
        }
        return sb.toString();
    }

    @Override
    @Transactional
    public String delTemplateGoodsRel(Long id) {
        ensureTemplateGoodsRelDao.deleteByPrimaryKey(id);
        return null;
    }

    @Override
    @Transactional
    public String delAllTemplateGoodsRel(Long id) {
        ensureTemplateGoodsRelDao.deleteByTemplateId(id);
        return null;
    }

}
