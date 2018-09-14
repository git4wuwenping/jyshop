package com.qyy.jyshop.admin.spell.service;

import com.qyy.jyshop.model.SpellActivity;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;

import java.util.Map;

public interface SpellActivityService {

    /**
     * 通过拼团活动ID查询拼团活动信息
     * @param activityId
     * @return
     */
    public SpellActivity queryByActivityId(Long activityId);

    /**
     * 分页查询拼团活动列表
     * @param page
     * @return
     */
    public PageAjax<Map<String,Object>> pageSpellActivity(PageAjax<Map<String,Object>> page);

    /**
     * 新增拼团活动
     * @param entity
     * @return
     */
    public AjaxResult saveOrUpdateSpellActivity(SpellActivity entity, String startDateStr, String endDateStr);

    /**
     * 编辑拼团活动
     * @param entity
     * @return
     */
    public AjaxResult updateEntity(SpellActivity entity);
}
