package com.qyy.jyshop.admin.spell.controller;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.delivery.service.DlyTypeService;
import com.qyy.jyshop.admin.delivery.service.LogiService;
import com.qyy.jyshop.admin.goods.service.GoodsService;
import com.qyy.jyshop.admin.member.service.MemberService;
import com.qyy.jyshop.admin.spell.service.*;
import com.qyy.jyshop.model.*;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/admin/spell")
public class SpellController extends BaseController {

    @Autowired
    private SpellService spellService;

    @Autowired
    private SpellActivityService spellActivityService;

    @Autowired
    private SpellOrderService spellOrderService;

    @Autowired
    private SpellOrderItemsService spellOrderItemsService;

    @Autowired
    private SpellRecomGoodsService spellRecomGoodsService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private DlyTypeService dlyTypeService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private LogiService logiService;

    @Authority(opCode = "110401", opName = "拼团活动列表")
    @RequestMapping("spellActivityMain")
    public String spellActivityMain(Map<String, Object> map) {
        return "spell/activity_main";
    }

    @ResponseBody
    @Authority(opCode = "11040101", opName = "分页查询拼团活动")
    @RequestMapping("pageSpellActivityAjax")
    public PageAjax<Map<String,Object>> pageSpellActivityAjax(PageAjax<Map<String,Object>> page) {
        return spellActivityService.pageSpellActivity(page);
    }

    @Authority(opCode = "11040102", opName = "添加拼团活动页面")
    @RequestMapping("addSpellActivity")
    public String addSpellActivity(Long activityId, Map<String, Object> map) {
        SpellActivity spellActivity = null;
        if(activityId != null){
            spellActivity = spellActivityService.queryByActivityId(activityId);
            if(spellActivity!=null){
                Goods goods = goodsService.queryByGoodsId(spellActivity.getGoodsId());
                map.put("goodsName", goods.getName());
            }
        }
        if(spellActivity == null){
            spellActivity = new SpellActivity();
        }
        // 当前登录的用户
        AuthUser loginUser = (AuthUser) BaseController.getRequest().getAttribute("loginUser");
        map.put("dlyTypeList", this.dlyTypeService.queryByComId(loginUser.getComId()));
        map.put("entity", spellActivity);
        return "spell/add_activity";
    }

    @ControllerLog("添加拼团活动")
    @ResponseBody
    @Authority(opCode = "11040103", opName = "添加拼团活动")
    @RequestMapping("saveSpellActivity")
    public AjaxResult saveSpellActivity(SpellActivity entity, String startDateStr, String endDateStr) {
        return spellActivityService.saveOrUpdateSpellActivity(entity, startDateStr, endDateStr);
    }

    @ControllerLog("关联商品选择")
    @RequestMapping("selectGoods")
    @Authority(opCode="11040104", opName="拼团关联商品")
    public String selectGoods() {
        return "spell/add/select_goods";
    }

    @ControllerLog("拼团列表")
    @Authority(opCode = "110402", opName = "拼团列表")
    @RequestMapping("spellMain")
    public String spellMain(Map<String, Object> map) {
        return "spell/spell_main";
    }

    @ResponseBody
    @ControllerLog("分页查询拼团列表")
    @Authority(opCode = "11040201", opName = "分页查询拼团列表")
    @RequestMapping("pageSpellAjax")
    public PageAjax<Map<String,Object>> pageSpellAjax(PageAjax<Map<String,Object>> page) {
        return spellService.pageSpell(page);
    }

    @ResponseBody
    @ControllerLog("拼团详情")
    @Authority(opCode = "11040202", opName = "拼团详情")
    @RequestMapping("spellDetail")
    public ModelAndView spellDetail(@PathVariable("spellId")Long spellId, ModelMap model){
        Spell spell = spellService.getSpellDetail(spellId);
        model.put("spell",spell);
        return new ModelAndView("spell/spell_detail",model);
    }

    @Authority(opCode = "110404", opName = "推荐商品")
    @RequestMapping("recommend")
    public String recommend(){
        return "spell/recommend/main";
    }

    @ControllerLog("拼团推荐商品")
    @Authority(opCode = "11040401", opName = "拼团推荐商品分页")
    @RequestMapping("recommendPage")
    @ResponseBody
    public PageAjax<Map<String, Object>> recommendPage(PageAjax<SpellRecomGoods> page) {
        PageAjax<Map<String, Object>> data = spellRecomGoodsService.recommendPage(page);
        return data;
    }

    @ControllerLog("拼团推荐关联商品")
    @RequestMapping("recommendSelect")
    @Authority(opCode = "11040402", opName = "拼团推荐关联商品")
    public String recommendSelect(){
        return "spell/recommend/select";
    }

    @ControllerLog("拼团推荐关联商品添加")
    @RequestMapping("recommendAdd")
    @ResponseBody
    @Authority(opCode = "11040403", opName = "拼团推荐关联商品添加")
    public AjaxResult addTagRel(@RequestParam Map<String,String> map) {
        try {
            return AppUtil.returnObj(spellRecomGoodsService.recommendAdd(map));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }

    @Authority(opCode = "110403", opName = "拼团订单")
    @RequestMapping("orderMain")
    public String orderMain(){
        return "spell/order/main";
    }

    @ResponseBody
    @Authority(opCode = "11040301", opName = "分页查询拼团订单")
    @RequestMapping("pageSpellOrderAjax")
    public PageAjax<Map<String,Object>> pageSpellOrderAjax(PageAjax<Map<String,Object>> page) {
        return spellOrderService.pageSpellOrder(page);
    }

    @ResponseBody
    @ControllerLog("拼团订单详情")
    @Authority(opCode = "11040302", opName = "拼团订单详情")
    @RequestMapping("spellOrderDetail")
    public ModelAndView spellOrderDetail(Long orderId, ModelMap model){
        SpellOrder spellOrder = spellOrderService.queryByID(orderId);
        model.put("order", spellOrder);
        if(spellOrder!=null){
            Member member = memberService.preDetaileCustomer(spellOrder.getMemberId());
            model.put("member", member);
            SpellOrderItems items = spellOrderItemsService.getItemsByOrderId(orderId);
            model.put("items",items);
//            Goods goods = goodsService.queryByGoodsId(spellOrder);
        }
        return new ModelAndView("spell/order/detail",model);
    }

    @ControllerLog("拼团订单发货页面")
    @Authority(opCode = "11040303", opName = "拼团订单发货页面")
    @RequestMapping("delivery/{orderId}")
    public String preOrderDelivery(@PathVariable("orderId")Long orderId,ModelMap model) {
        model.put("logiList",logiService.queryLogi());
        model.put("order",spellOrderService.queryByID(orderId));
        return "spell/order/delivery";
    }
}