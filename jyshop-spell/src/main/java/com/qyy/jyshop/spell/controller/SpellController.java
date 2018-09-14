package com.qyy.jyshop.spell.controller;

import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.spell.service.SpellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SpellController extends AppBaseController {

    @Autowired
    private SpellService spellService;

    /**
     * 拼团首页
     * @param pageNo
     * @param pageSize
     * @param sortType
     * @param sortWay
     * @return
     */
    @RequestMapping(value = "anon/getSpellActivityPage")
    public Map<String, Object> getSpellActivityPage(Integer pageNo, Integer pageSize, Integer sortType, Integer sortWay){
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap = spellService.getSpellActivityPage(pageNo, pageSize, sortType, sortWay);
        return this.outMessage(0, "获取成功", returnMap);
    }

    /**
     * 获取拼团活动详情
     * @param activityId
     * @param token
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "anon/getActivityDetails")
    public Map<String, Object> getActivityDetails(Long activityId, String token)throws Exception{
        Map<String, Object> returnMap = spellService.getSpellActivityByActivityId(activityId, token);
        return this.outMessage(0, "获取成功", returnMap);
    }

    /**
     * 订单预览
     * @param activityId
     * @param productId
     * @param token
     * @return
     */
    @RequestMapping(value = "getOrderCheckout")
    public Map<String, Object> getOrderCheckout(Long activityId, Long productId, String token){
        Map<String, Object> returnMap = spellService.getOrderCheckout(activityId, productId, token);
        return this.outMessage(0, "获取成功", returnMap);
    }

    /**
     * 计算运费
     * @param activityId
     * @param memberAddressId
     * @param token
     * @return
     */
    @RequestMapping(value = "getShipAmount")
    public Map<String, Object> getShipAmount(Long activityId, Long memberAddressId, String token){
        Map<String, Object> returnMap = new HashMap<String, Object>();
        BigDecimal shipAmount = spellService.getShipAmount(activityId, memberAddressId, token);
        returnMap.put("shipAmount", shipAmount);
        return this.outMessage(0, "获取成功", returnMap);
    }

    /**
     * 获取店铺拼团活动
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "anon/getActivityByShop")
    public Map<String, Object> getActivityByShop(Long goodsId){
        Map<String, Object> returnMap = new HashMap<String, Object>();
        List<Map<String, Object>> list = spellService.getSpellActivityByGoodsId(goodsId);
        returnMap.put("activityList", list);
        return this.outMessage(0, "获取成功", returnMap);
    }

    /**
     * 创建拼团订单
     * @param activityId
     * @param productId
     * @param memberAddressId
     * @param remark
     * @param token
     * @return
     */
    @RequestMapping(value = "createSpellOrder")
    public Map<String, Object> createSpellOrder(Long activityId, Long productId, Long memberAddressId, String remark, String token){
        Map<String, Object> returnMap = spellService.createSpellOrder(activityId, productId, memberAddressId, remark, token);
        return this.outMessage(0, "获取成功", returnMap);
    }

    /**
     * 参与拼团
     * @param spellId
     * @param productId
     * @param memberAddressId
     * @param remark
     * @param token
     * @return
     */
    @RequestMapping(value = "participateSpell")
    public Map<String, Object> participateSpell(Long spellId, Long productId, Long memberAddressId, String remark, String token){
        Map<String, Object> returnMap = spellService.participateSpell(spellId, productId, memberAddressId, remark, token);
        return this.outMessage(0, "获取成功", returnMap);
    }

    /**
     * 我发起的拼团列表
     * @param token
     * @return
     */
    @RequestMapping(value = "getMySpellList")
    public Map<String, Object> getMySpellList(Integer pageNo, Integer pageSize, String token){
        Map<String, Object> returnMap = spellService.getMyInitiateSpell(pageNo, pageSize, token);
//        List<Map<String, Object>> list = spellService.getMyInitiateSpell(pageNo, pageSize, token);
//        returnMap.put("spellList", list);
        return this.outMessage(0, "获取成功", returnMap);
    }

    /**
     * 我参与的拼团列表
     * @param token
     * @return
     */
    @RequestMapping(value = "getMyParticipateSpell")
    public Map<String, Object> getMyParticipateSpell(Integer pageNo, Integer pageSize, String token){
        Map<String, Object> returnMap = spellService.getMyParticipateSpell(pageNo, pageSize, token);
//        List<Map<String, Object>> list = spellService.getMyParticipateSpell(pageNo, pageSize, token);
//        returnMap.put("spellList", list);
        return this.outMessage(0, "获取成功", returnMap);
    }

    /**
     * 拼团详情
     * @param spellId
     * @return
     */
    @RequestMapping(value = "anon/getSpellDetail")
    public Map<String, Object> getSpellDetail(Long spellId){
        Map<String, Object> returnMap = spellService.getSellDetail(spellId);
        return this.outMessage(0, "获取成功", returnMap);
    }

    /**
     * 订单详情
     * @param spellId
     * @param token
     * @return
     */
    @RequestMapping(value = "getOrderDetail")
    public Map<String, Object> getOrderDetail(Long spellId, String token){
        Map<String, Object> returnMap = spellService.getOrderDetail(spellId, token);
        return this.outMessage(0, "获取成功", returnMap);
    }

//    public static void main(String[] args){
//        Timestamp date = new Timestamp(Long.valueOf("1525017600000"));
//        System.out.println(date.getTime());
//    }
}