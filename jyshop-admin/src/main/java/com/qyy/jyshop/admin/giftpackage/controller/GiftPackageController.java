package com.qyy.jyshop.admin.giftpackage.controller;


import com.alibaba.fastjson.JSONObject;
import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.giftpackage.service.GiftPackageGalleryService;
import com.qyy.jyshop.admin.giftpackage.service.GiftPackageProductService;
import com.qyy.jyshop.admin.giftpackage.service.GiftPackageService;
import com.qyy.jyshop.admin.goodstype.service.GoodsTypeService;
import com.qyy.jyshop.admin.goodstype.service.SpecService;
import com.qyy.jyshop.model.*;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.LongUtil;
import com.qyy.jyshop.util.StringUtil;
import com.qyy.jyshop.vo.SpecVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 礼包管理
 * @author caihk
 * 2018年1月27日 15:40:01
 */
@Controller
@RequestMapping("/admin/giftPackage")
public class GiftPackageController extends BaseController {
	
	@Autowired
	private GiftPackageService giftPackageService;
    @Autowired
    private GoodsTypeService goodsTypeService;
    @Autowired
    private SpecService specService;
    @Autowired
    private GiftPackageProductService giftPackageProductService;
    @Autowired
    private GiftPackageGalleryService giftPackageGalleryService;

    @Authority(opCode = "040107", opName = "店长礼包")
    @RequestMapping("giftPackageMain")
    public String giftPackageMain(Map<String, Object> map) {    
        return "giftpackage/giftpackage_main";
    }
    
    @ControllerLog("查询店长礼包列表")
    @RequestMapping("pageGiftPackageAjax")
    @ResponseBody
    @Authority(opCode = "04010701", opName = "查询店长礼包列表")
    public PageAjax<Map<String,Object>> pageGoodsAjax(PageAjax<Map<String,Object>> page) {
        return giftPackageService.pageGiftPackage(page);
    }

    @ControllerLog("添加礼包页面")
    @RequestMapping("preAddGiftPackage")
    @Authority(opCode = "04010702", opName = "添加商品页面")
    public String preAddGoods(Map<String, Object> map) {
        map.put("goodsTypeList", this.goodsTypeService.queryByComId(null));
        return "giftpackage/add_giftpackage";
    }

    @ControllerLog("添加礼包")
    @ResponseBody
    @RequestMapping("addGiftPackage")
    @Authority(opCode = "04010703", opName = "添加礼包")
    public AjaxResult addGiftPackage(@RequestParam Map<String, Object> map) {
        try {
            return AppUtil.returnObj(giftPackageService.doAddGiftPackage(map));
        } catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }

    @ControllerLog("编辑礼包页面")
    @RequestMapping("preEditGiftPackage/{gpId}")
    @Authority(opCode = "04010704", opName = "编辑礼包页面")
    public String preEditGiftPackage(@PathVariable("gpId") Long gpId, Map<String, Object> map) {
        GiftPackage giftPackage = giftPackageService.queryByGpId(gpId);
        map.put("giftPackage", giftPackage);

        map.put("goodsTypeList", this.goodsTypeService.queryByComId(giftPackage.getComId()));

        List<Spec> specList=new ArrayList<Spec>();
        if(!StringUtil.isEmpty(giftPackage.getTypeId()) && giftPackage.getTypeId()>0L){
            GoodsType goodsType=this.goodsTypeService.queryByTypeId((long)giftPackage.getTypeId());
            if(goodsType!=null && !StringUtil.isEmpty(goodsType.getSpecIds())){
                Long[] specIds= LongUtil.stringArrayToLongArray(goodsType.getSpecIds().split(","));
                specList=this.specService.queryBySpecIds(specIds);
            }
        }
        map.put("specList", specList);
        //商品规格列表
        List<SpecVo> goodsSpecList = specService.selectSpecsByGpId(gpId);
        map.put("goodsSpecListJson", JSONObject.toJSONString(goodsSpecList));
        map.put("goodsSpecList", goodsSpecList);

        //货品列表
        List<Map> giftPackageProductList = giftPackageProductService.queryByGpId(gpId);
        map.put("giftPackageProductList", giftPackageProductList);

        //商品相册列表
        List<GiftPackageGallery> giftPackageGalleryList = giftPackageGalleryService.queryByGpId(gpId);
        map.put("giftPackageGalleryList", giftPackageGalleryList);
        return "giftpackage/edit_giftpackage";
    }

    @ControllerLog("编辑礼包")
    @RequestMapping("editGiftPackage")
    @ResponseBody
    @Authority(opCode = "04010705", opName = "编辑礼包")
    public AjaxResult editGoods(@RequestParam Map<String, Object> map) {
        try {
            return AppUtil.returnObj(giftPackageService.editGiftPackage(map));
        } catch (RuntimeException e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    
    @ControllerLog("商品上架")
    @RequestMapping("giftPackageSalesYes/{gpId}")
    @ResponseBody
    @Authority(opCode = "04010707", opName = "商品上架")
    public AjaxResult giftPackageSalesYes(@PathVariable("gpId") Long gpId) {
        try{
            return AppUtil.returnObj(giftPackageService.doGiftPackageSalesExamine(gpId,
                    Integer.valueOf(DictionaryUtil.getDataValueByName("goods_market_enable", "sales_yes"))));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    @ControllerLog("商品下架")
    @RequestMapping("giftPackageSalesNo/{gpId}")
    @ResponseBody
    @Authority(opCode = "04010708", opName = "商品下架")
    public AjaxResult giftPackageSalesNo(@PathVariable("gpId") Long gpId) {
        try{
            return AppUtil.returnObj(giftPackageService.doGiftPackageSalesExamine(gpId,
                    Integer.valueOf(DictionaryUtil.getDataValueByName("goods_market_enable", "sales_no"))));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    

    @ControllerLog("删除礼包")
    @RequestMapping("delGiftPackage/{gpId}")
    @ResponseBody
    @Authority(opCode = "04010706", opName = "删除礼包")
    public AjaxResult delGiftPackage(@PathVariable("gpId") Long gpId) {
        try{
            return AppUtil.returnObj(giftPackageService.doDelGiftPackage(gpId));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
}
