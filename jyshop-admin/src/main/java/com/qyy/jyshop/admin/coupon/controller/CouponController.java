package com.qyy.jyshop.admin.coupon.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.coupon.service.CouponRelService;
import com.qyy.jyshop.admin.coupon.service.CouponService;
import com.qyy.jyshop.admin.coupon.service.MemberCouponService;
import com.qyy.jyshop.model.Coupon;
import com.qyy.jyshop.model.CouponRel;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;

@Controller
@RequestMapping("/admin/coupon")
public class CouponController extends BaseController {

    @Autowired
    private CouponService couponService;
    @Autowired
    private MemberCouponService memberCouponService;
    @Autowired
    private CouponRelService couponRelService;

    @Authority(opCode = "110201", opName = "优惠券管理")
    @RequestMapping("couponMain")
    public String couponMain(Map<String, Object> map) {
        return "coupon/coupon_main";
    }

    @ControllerLog("查询优惠券列表")
    @RequestMapping("pageCouponAjax")
    @ResponseBody
    @Authority(opCode = "11020101", opName = "查询优惠券列表")
    public PageAjax<Map<String, Object>> pageCouponAjax(PageAjax<Map<String, Object>> page) {
        return couponService.pageCouponAjax(page);
    }

    @ControllerLog("添加优惠券页面")
    @RequestMapping("preAddCoupon")
    @Authority(opCode = "11020102", opName = "添加优惠券页面")
    public String preAddCoupon(Map<String, Object> map) {
        map.put("cpnSn", UUID.randomUUID().toString());
        return "coupon/coupon_add";
    }

    @ControllerLog("添加优惠券")
    @RequestMapping("addCoupon")
    @ResponseBody
    @Authority(opCode = "11020103", opName = "添加优惠券")
    public AjaxResult addCoupon(Coupon coupon) {
        try {
            return AppUtil.returnObj(couponService.addCoupon(coupon));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }

    @ControllerLog("优惠券编辑页面")
    @RequestMapping("preEditCoupon/{cpnId}")
    @Authority(opCode = "11020104", opName = "优惠券编辑页面")
    public String preEditCoupon(@PathVariable("cpnId") Long cpnId, Map<String, Object> map) {
        Coupon coupon = this.couponService.selectOne(cpnId);
        map.put("coupon", coupon);
        return "coupon/coupon_edit";
    }

    @ControllerLog("更新优惠券")
    @RequestMapping("editCoupon")
    @ResponseBody
    @Authority(opCode = "11020105", opName = "更新优惠券")
    public AjaxResult editCoupon(Coupon coupon) throws Exception {
        return AppUtil.returnObj(couponService.editCoupon(coupon));
    }

    @ControllerLog("删除优惠券")
    @RequestMapping("delCoupon/{cpnId}")
    @ResponseBody
    @Authority(opCode = "11020106", opName = "删除优惠券")
    public AjaxResult delCoupon(@PathVariable("cpnId") Long cpnId) throws Exception {
        return AppUtil.returnObj(couponService.delCoupon(cpnId));
    }

    @ControllerLog("选择商品页面")
    @RequestMapping("selectGoods")
    @Authority(opCode = "11020107", opName = "选择商品页面")
    public String selectGoods(String cpnSn, Map<String, Object> map) {
        map.put("cpnSn", cpnSn);
        return "coupon/goods_select";
    }

    
    
    @ControllerLog("选择商品分类页面")
    @RequestMapping("selectCat")
    @Authority(opCode = "11020108", opName = "选择商品分类页面")
    public String selectCat(String cpnSn, Map<String, Object> map) {
        
        
        List<Map<String, Object>> hasList = couponRelService.queryGoodsCatRel(cpnSn);
        List<Map<String, Object>> allList = couponRelService.queryGoodsCatList();
        List<Long> ids = hasList.stream().map(e -> Long.parseLong(e.get("catId").toString()))
                .collect(Collectors.toList());
        for (Map<String, Object> m : allList) {
            if (ids.contains(Long.parseLong(m.get("catId").toString()))) {
                m.put("status", 1);
            } else {
                m.put("status", 0);
            }
        }
        map.put("relList", allList);
        map.put("cpnSn", cpnSn);
        
        return "coupon/cat_select";
    }
    
    
    
    @ControllerLog("选择供应商页面")
    @RequestMapping("selectCom")
    @Authority(opCode = "11020109", opName = "选择供应商页面")
    public String selectCom(String cpnSn, Map<String, Object> map) {
        map.put("cpnSn", cpnSn);
        return "coupon/com_select";
    }
    
    @ControllerLog("添加关联")
    @RequestMapping("addRel")
    @ResponseBody
    @Authority(opCode = "11020110", opName = "添加关联")
    public AjaxResult addRel(@RequestParam(name="relIdArr[]") Long[] relIdArr,@RequestParam String cpnSn,@RequestParam Integer relType) {
        try {
            return AppUtil.returnObj(couponRelService.addRel(relIdArr,cpnSn,relType));// 关联类型：0-分类 1-商品 2-供应商
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }

    /*@ControllerLog("查询关联商品列表")
    @RequestMapping("pageGoodsRelAjax")
    @ResponseBody
    @Authority(opCode = "11020107", opName = "查询关联商品列表")
    public PageAjax<Map<String, Object>> pageGoodsRelAjax(String cpnSn, PageAjax<CouponRel> page) {
        return couponRelService.pageGoodsRel(cpnSn, page);
    }

    @ControllerLog("查询关联分类列表")
    @RequestMapping("pageCatRelAjax")
    @ResponseBody
    @Authority(opCode = "11020108", opName = "查询关联分类列表")
    public PageAjax<Map<String, Object>> pageCatRelAjax(String cpnSn, PageAjax<CouponRel> page) {
        return couponRelService.pageCatRel(cpnSn, page);
    }

    @ControllerLog("查询关联供应商列表")
    @RequestMapping("pageComRelAjax")
    @ResponseBody
    @Authority(opCode = "11020109", opName = "查询关联供应商列表")
    public PageAjax<Map<String, Object>> pageComRelAjax(String cpnSn, PageAjax<CouponRel> page) {
        return couponRelService.pageComRel(cpnSn, page);
    }*/

    @ControllerLog("查询关联列表")
    @RequestMapping("pageRelAjax")
    @ResponseBody
    @Authority(opCode = "11020111", opName = "查询关联列表")
    public PageAjax<Map<String, Object>> pageRelAjax(String cpnSn, Integer relType, PageAjax<Map<String, Object>> page) {
        return couponRelService.pageRel(cpnSn,relType,page);// 关联类型：0-分类 1-商品 2-供应商
    }
    
    @ControllerLog("删除关联")
    @RequestMapping("delRel/{id}")
    @ResponseBody
    @Authority(opCode = "11020112", opName = "删除关联")
    public AjaxResult delRel(@PathVariable Long id) {
        try {
            return AppUtil.returnObj(couponRelService.delRel(id));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }

    @ControllerLog("删除所有关联")
    @RequestMapping("delAllRel")
    @ResponseBody
    @Authority(opCode = "11020113", opName = "删除所有关联")
    public AjaxResult delAllRel(String cpnSn) {
        try {
            return AppUtil.returnObj(couponRelService.delAllRel(cpnSn));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }

    @ControllerLog("优惠券发放页面")
    @RequestMapping("preGrantCoupon/{cpnId}")
    @Authority(opCode = "11020114", opName = "优惠券发放页面")
    public String preGrantCoupon(@PathVariable Long cpnId,@RequestParam Integer grantType, Map<String, Object> map) {
        Map<String,Object> coupon = this.couponService.selectCouponInfoByCpnId(cpnId);
        String page = "";
        switch (grantType) { //发放类型：0-线上发放1-线下发放2-直接到账3-注册赠送
            case 0:
                coupon.put("grantTypeName", "线上发放");
                page = "coupon/grant/oo_grant";
                break;
            case 1:
                coupon.put("grantTypeName", "线下发放");
                page = "coupon/grant/oo_grant";
                break;
            case 2:
                coupon.put("grantTypeName", "直接到账");
                page = "coupon/grant/member_grant";
                break;
            default:
                break;
        }
        map.put("coupon", coupon);
        return page;
    }
    
    @ControllerLog("发放优惠券")
    @RequestMapping("grantCoupon")
    @ResponseBody
    @Authority(opCode = "11020115", opName = "添加优惠券")
    public AjaxResult grantCoupon(@RequestParam Long cpnId,@RequestParam Integer grantCount,@RequestParam Integer grantType) {
        try {
            return AppUtil.returnObj(couponService.grantCoupon(cpnId,grantType,grantCount));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }
    
    @ControllerLog("选择发送会员页面")
    @Authority(opCode = "11020116", opName = "选择发送会员页面")
    @RequestMapping(value = "selectMember", method = { RequestMethod.POST, RequestMethod.GET })
    public String selectMember() {
        return "coupon/grant/member_select";
    }
    
    @ControllerLog("发放优惠券")
    @RequestMapping("grantCouponMember")
    @ResponseBody
    @Authority(opCode = "11020117", opName = "添加优惠券")
    public AjaxResult grantCouponMember(@RequestParam Long cpnId,@RequestParam String memberIds,@RequestParam Integer count,@RequestParam Integer gType) {
        try {
            return AppUtil.returnObj(couponService.grantCoupon(cpnId,memberIds,count,gType));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }
    
    
    @ControllerLog("优惠券查看页面")
    @RequestMapping("viewCoupon/{cpnId}")
    @Authority(opCode = "11020118", opName = "优惠券查看页面")
    public String viewCoupon(@PathVariable Long cpnId,@RequestParam Integer grantType, Map<String, Object> map) {
        Map<String,Object> coupon = this.couponService.selectCouponInfoByCpnId(cpnId);
        switch (grantType) { //发放类型：0-线上发放1-线下发放2-直接到账3-注册赠送
            case 0:
                coupon.put("grantTypeName", "线上发放");
                break;
            case 1:
                coupon.put("grantTypeName", "线下发放");
                break;
            case 2:
                coupon.put("grantTypeName", "直接到账");
                break;
            case 3:
                coupon.put("grantTypeName", "注册赠送");
                break;
            default:
                break;
        }
        map.put("coupon", coupon);
        return "coupon/view/member_coupon";
    }
    
    @ControllerLog("查询优惠券发放列表")
    @RequestMapping("pageMemberCouponAjax/{cpnId}")
    @ResponseBody
    @Authority(opCode = "11020119", opName = "查询关联列表")
    public PageAjax<Map<String, Object>> pageMemberCouponAjax(@PathVariable Long cpnId, PageAjax<Map<String, Object>> page) {
        return memberCouponService.pageMemberCoupon(cpnId,page);
    }
}
