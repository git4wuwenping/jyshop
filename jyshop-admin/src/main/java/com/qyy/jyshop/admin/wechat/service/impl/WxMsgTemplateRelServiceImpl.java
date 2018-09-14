package com.qyy.jyshop.admin.wechat.service.impl;

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

import com.qyy.jyshop.admin.wechat.service.WxMsgTemplateRelService;
import com.qyy.jyshop.dao.WxMsgTemplateRelDao;
import com.qyy.jyshop.model.TagRel;
import com.qyy.jyshop.model.WxMsgTemplateRel;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class WxMsgTemplateRelServiceImpl extends AbstratService<WxMsgTemplateRel> implements WxMsgTemplateRelService {

    @Autowired
    private WxMsgTemplateRelDao wxMsgTemplateRelDao;
    
    @Transactional
    @Override
    public String addRel(Long[] memberIdArr,Long tplId) {
        List<Long> addList = Arrays.asList(memberIdArr);
        //List<Long> memberIdList = Arrays.asList(memberIdArr);
        
        if (CollectionUtils.isEmpty(addList)) {
            return null;
        }

        List<Long> nowList = new ArrayList<Long>();
        Example example = new Example(WxMsgTemplateRel.class);
        Criteria createCriteria = example.createCriteria();
        createCriteria.andEqualTo("tplId", tplId);
        List<WxMsgTemplateRel> relList = wxMsgTemplateRelDao.selectByExample(example);
        relList.forEach(rel -> {
            nowList.add(rel.getMemberId());
        });

        addList = addList.stream().filter(t -> !nowList.contains(t)).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(addList)) {
            List<WxMsgTemplateRel> list = new ArrayList<WxMsgTemplateRel>();
            for (Long memberId : addList) {
                WxMsgTemplateRel rel = new WxMsgTemplateRel();
                rel.setMemberId(memberId);
                rel.setTplId(tplId);
                list.add(rel);
            }
            wxMsgTemplateRelDao.batchInsert(list);
        }
        return null;
    }

    @Transactional
    @Override
    public String delRelByTplId(Long tplId) {
        wxMsgTemplateRelDao.deleteByTplId(tplId);
        return null;
    }

    @Override
    public PageAjax<Map<String, Object>> pageMemberRel(Long tplId,PageAjax<Map<String, Object>> page) {
        ParamData params = getParamData(page.getPageNo(), page.getPageSize());
        params.put("tplId", tplId);
        return this.pageQuery("WxMsgTemplateRelDao.selectMemberRelByTplId", params);
    }

    @Transactional
    @Override
    public String delMemberRel(Long id) {
        this.delById(id);
        return null;
    }

}
