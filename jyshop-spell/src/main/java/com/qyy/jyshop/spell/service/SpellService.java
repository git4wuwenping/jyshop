package com.qyy.jyshop.spell.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SpellService {

    /**
     * 获取拼团首页内容
     * @param pageNo
     * @param pageSize
     * @param sortType
     * @param sortWay
     * @return
     */
    public Map<String, Object> getSpellActivityPage(Integer pageNo, Integer pageSize, Integer sortType, Integer sortWay);

    /**
     * 获取拼团活动详情
     * @param activityId
     * @param token
     * @return
     */
    public Map<String,Object> getSpellActivityByActivityId(Long activityId, String token);

    /**
     * 拼团订单预览
     * @param activityId
     * @param productId
     * @param token
     * @return
     */
    public Map<String,Object> getOrderCheckout(Long activityId, Long productId, String token);

    /**
     * 计算邮费
     * @param activityId
     * @param memberAddressId
     * @param token
     * @return
     */
    public BigDecimal getShipAmount(Long activityId, Long memberAddressId, String token);

    /**
     * 生成拼团订单
     * @param activityId
     * @param productId
     * @param memberAddressId
     * @param remark
     * @param token
     * @return
     */
    public Map<String, Object> createSpellOrder(Long activityId, Long productId, Long memberAddressId, String remark, String token);

    /**
     * 生成参与拼团订单
     * @param spellId
     * @param productId
     * @param memberAddressId
     * @param remark
     * @param token
     * @return
     */
    public Map<String, Object> participateSpell(Long spellId, Long productId, Long memberAddressId, String remark, String token);

    /**
     * 获取店铺中的拼团活动
     * @param goodsId
     * @return
     */
    public List<Map<String, Object>> getSpellActivityByGoodsId(Long goodsId);

    /**
     * 我发起的拼团活动列表
     * @param pageNo
     * @param pageSize
     * @param token
     * @return
     */
    public Map<String, Object> getMyInitiateSpell(Integer pageNo, Integer pageSize, String token);

    /**
     * 我参与的拼团活动列表
     * @param pageNo
     * @param pageSize
     * @param token
     * @return
     */
    public Map<String, Object> getMyParticipateSpell(Integer pageNo, Integer pageSize, String token);

    /**
     * 获取拼团详情
     * @param spellId
     * @return
     */
    public Map<String, Object> getSellDetail(Long spellId);

    /**
     * 获取订单详情
     * @param spellId
     * @param token
     * @return
     */
    public Map<String, Object> getOrderDetail(Long spellId, String token);
}
