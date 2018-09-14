package com.qyy.jyshop.dao;

import com.qyy.jyshop.model.SpellActivity;
import com.qyy.jyshop.supple.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SpellActivityDao  extends MyMapper<SpellActivity> {

    /**
     * 根据条件查询拼团活动列表
     * @param params
     * @return
     */
    List<Map<String,Object>> getSpellActivityByParams(Map<String,Object> params);

    /**
     * 查询拼团活动详情
     * @param activityId
     * @return
     */
    Map<String,Object> getSpellActivityByActivityId(@Param("activityId")Long activityId);

    /**
     * 获取拼团活动信息
     * @param activityId
     * @return
     */
    SpellActivity findByActivityId(@Param("activityId")Long activityId);

    /**
     * 根据店铺ID获取拼团活动列表
     * @param shopStoreId
     * @return
     */
    List<Map<String, Object>> getSpellActivityByShopStoreId(@Param("shopStoreId")Long shopStoreId);

    /**
     * 获取推荐拼团活动
     * @return
     */
    List<Map<String, Object>> getSpellActivityByRecommended();
}
