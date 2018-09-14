package com.qyy.jyshop.admin.shopstore.controller;

import java.util.Map;

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
import com.qyy.jyshop.admin.shopstore.service.ShopStoreService;
import com.qyy.jyshop.model.ShopStore;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;
import com.qyy.jyshop.util.DictionaryUtil;
@Controller
@RequestMapping("/admin/shopStore")
public class ShopStoreController extends BaseController{
	
	@Autowired
	private ShopStoreService storeServiceImpl;

	/**
	 * 分页查询店铺及店铺下的商户商品列表
	 */
	/*@ControllerLog("查询店铺及店铺下的商户商品")
	@RequestMapping("pageStoresAjax")
	@ResponseBody
	@Authority(opCode = "04010601", opName = "查询店铺及店铺下的商户商品")
	public PageAjax<ShopStore> pageStoresAjax(PageAjax<ShopStore> page) {
		PageAjax<ShopStore> pageStoresAjax = storeServiceImpl.pageStoresAjax(page);
		return pageStoresAjax;
	}*/
	
	/**
	 * 直营店铺
	 */
	@Authority(opCode = "120101", opName = "直营店铺")
	@RequestMapping("shopStoreMain")
	public String shopStoreMain(Map<String, Object> map){
		map.put("comId", 0);
		return "shopstore/shopStore_main";
	}
	/***
	 * 直营店铺列表
	 * @author Tonny
	 * @date 2018年3月5日
	 */
	@ControllerLog("直营店铺列表")
	@RequestMapping("pageShopStoreAjax")
	@ResponseBody
	@Authority(opCode = "12010101", opName = "查询直营店铺列表")
	public PageAjax<Map<String, Object>> pageShopStoreAjax(PageAjax<ShopStore> page) {
		return storeServiceImpl.selectShopStoreByParams(page);
	}
	/***
	 * 添加店铺页面
	 * @author Tonny
	 * @date 2018年3月5日
	 */
	@ControllerLog("添加店铺页面")
	@RequestMapping("preAddShopStore")
	@Authority(opCode = "", opName = "添加店铺页面")
	public String preAddUser(Map<String, Object> map) {
		return "shopstore/shopStore_add";
	}
	/**
	 * 添加店铺
	 */
	@ControllerLog("添加店铺")
	@RequestMapping("addShopStore")
	@ResponseBody
	@Authority(opCode = "", opName = "添加店铺")
	public AjaxResult addShopStore(@RequestParam Map<String, Object> map) {
		try{
			return AppUtil.returnObj(storeServiceImpl.addShopStore(map));
		}catch (Exception e) {
			return AppUtil.returnObj(2,e.getMessage());
		}
	}
	
	/**
	 * 编辑店铺页面
	 */
	@ControllerLog("编辑店铺页面")
	@RequestMapping("preEditShopStore/{shopStoreId}")
	@Authority(opCode = "", opName = "编辑店铺页面")
	public String preEditShopStore(@PathVariable("shopStoreId") int shopStoreId,Map<String, Object> map) {
		ShopStore shopStore = storeServiceImpl.selectShopStoreById(shopStoreId);
		map.put("shopStore", shopStore);
		return "shopstore/shopStore_edit";
	}
	/**
	 * 编辑店铺
	 */
	@ControllerLog("编辑店铺")
	@RequestMapping("editShopStore")
	@ResponseBody
	@Authority(opCode = "", opName = "编辑店铺")
	public AjaxResult editShopStore(@RequestParam Map<String, Object> map) throws Exception {
		return  AppUtil.returnObj(storeServiceImpl.editShopStore(map));
	}
	
	/**
	 * 删除店铺
	 */
	@ControllerLog("删除店铺")
	@RequestMapping("deleteShopStore/{shopStoreId}")
	@ResponseBody
	@Authority(opCode = "", opName = "删除店铺")
	public AjaxResult deleteShopStore(@PathVariable("shopStoreId") int shopStoreId) throws Exception {
		try {
			return AppUtil.returnObj(storeServiceImpl.deleteShopStore(shopStoreId));
		} catch (Exception e) {
			return  AppUtil.returnObj(2,e.getMessage());
		}
	}
	
	@ControllerLog("停用")
	@RequestMapping("disabledNo/{shopStoreId}")
	@ResponseBody
	@Authority(opCode = "", opName = "停用")
	public AjaxResult disabledNo(@PathVariable("shopStoreId") int shopStoreId) {
		try{
			return AppUtil.returnObj(storeServiceImpl.isDisabledExamine(shopStoreId,Integer.valueOf(DictionaryUtil.getDataValueByName("is_usable", "yes"))));
		}catch (Exception e) {
			return AppUtil.returnObj(2,e.getMessage());
		}
	}
	@ControllerLog("启用")
	@RequestMapping("disabledYes/{shopStoreId}")
	@ResponseBody
	@Authority(opCode = "", opName = "启用")
	public AjaxResult disabledYes(@PathVariable("shopStoreId") int shopStoreId) {
		try{
			return AppUtil.returnObj(storeServiceImpl.isDisabledExamine(shopStoreId,Integer.valueOf(DictionaryUtil.getDataValueByName("is_usable", "no"))));
		}catch (Exception e) {
			return AppUtil.returnObj(2,e.getMessage());
		}
	}
	
	@ControllerLog("校验店铺名")
	@RequestMapping(value="checkShopStoreName",method=RequestMethod.POST)
	@ResponseBody
	@Authority(opCode = "", opName = "校验店铺名称")
	public AjaxResult checkShopStoreName(@RequestParam("shopStoreName") String shopStoreName){
		try {
			String result = storeServiceImpl.selectShopStoreByName(shopStoreName);
			return AppUtil.returnObj(result);
		} catch (Exception e) {
			return  AppUtil.returnObj(2,e.getMessage());
		}
	}
	
	/*校验店铺信息*/
	
	@ControllerLog("校验店铺信息")
	@RequestMapping(value="checkShopStoreInformation",method=RequestMethod.POST)
	@ResponseBody
	@Authority(opCode = "", opName = "校验店铺信息")
	public AjaxResult checkShopStoreInformation(@RequestParam("shopStoreInformation") String shopStoreInformation){
		try {
			String result = storeServiceImpl.checkShopStoreInformation(shopStoreInformation);
			return AppUtil.returnObj(result);
		} catch (Exception e) {
			return  AppUtil.returnObj(2,e.getMessage());
		}
	}
	
}
