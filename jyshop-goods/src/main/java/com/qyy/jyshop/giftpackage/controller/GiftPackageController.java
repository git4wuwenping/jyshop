package com.qyy.jyshop.giftpackage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.giftpackage.service.GiftPackageProductService;
import com.qyy.jyshop.giftpackage.service.GiftPackageService;
import com.qyy.jyshop.giftpackage.service.GiftPackageSpecService;
import com.qyy.jyshop.model.GiftPackage;
import com.qyy.jyshop.pojo.PageAjax;
@RestController
public class GiftPackageController extends AppBaseController{
	
	@Autowired
	private GiftPackageService giftPackageService;
	@Autowired
	private GiftPackageSpecService giftPackageSpecService;
	@Autowired
	private GiftPackageProductService giftPackageProductService;
 
	/**
	 * 店长礼包列表
	 */
	@RequestMapping(value = "getGiftPackageList")
    public Map<String,Object> getGiftPackageByMemberId(Integer pageNo,Integer pageSize){
		PageAjax<Map<String,Object>> giftPackageList=this.giftPackageService.getGiftPackageByMemberId(pageNo, pageSize);
        return this.outMessage(0, "获取成功", this.getPageMap(giftPackageList));
    }
	
	/**
	 * 店长礼包详情
	 */
	@RequestMapping(value = "giftPackageDetails")
    public Map<String,Object> giftPackageDetails(Long gpId){
		Map<String,Object> map = new HashMap<String,Object>();
		//获取礼包商品详情
		Map<String,Object> giftPackage = this.giftPackageService.getGiftPackageByGpId(gpId);
		
		List<Map<String,Object>> GiftPackageGallery = this.giftPackageService.selectGiftPackageGalleryByGpId(gpId);
		
		map.put("giftPackage", giftPackage);
		map.put("giftPackageGallery", GiftPackageGallery);
		//获取礼包商品规格
		map.put("giftPackageSpecList", giftPackageSpecService.queryByGpId(gpId));
		
		map.put("giftPackageProductList", this.giftPackageProductService.queryByGpId(gpId));
		return this.outMessage(0, "获取成功",map);
    }
}
