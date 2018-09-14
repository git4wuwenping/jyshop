package com.qyy.jyshop.admin.store.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.goods.service.GoodsService;
import com.qyy.jyshop.admin.store.service.StoreService;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;


/**
 * @author Tonny
 *	库存管理
 */
@Controller
@RequestMapping("/admin/store")
public class StoreController extends BaseController {

	@Autowired
	private GoodsService goodsServiceImpl;
	@Autowired
	private StoreService storeServiceImpl;
	/**
	 * 库存管理首页
	 */
	@Authority(opCode = "040501", opName = "库存管理首页")
	@RequestMapping("storeMain")
	public String storeMain(Map<String, Object> map) {    
		map.put("comId", 0);
		return "store/store_main";
	}
	/**
	 * 分页查询库存列表
	 */
	@ControllerLog("查询库存列表")
	@RequestMapping("pageStoreAjax")
	@ResponseBody
	@Authority(opCode = "04050101", opName = "查询商品列表")
	public PageAjax<Map<String,Object>> pageStoreAjax(PageAjax<Map<String,Object>> page) {
		return goodsServiceImpl.pageStoreAjax(page);
	}
	
	/**
	 * 编辑货品库存页面
	 */
	@Authority(opCode = "04050102", opName = "编辑货品库存页面")
	@RequestMapping("preEditStore/{goodsId}")
	public String preEditStore(@PathVariable("goodsId") Long goodsId,Map<String, Object> map) {    
		List<Map<String, Object>> goodsStoreList =  storeServiceImpl.selectProductStoreByGoodsId(goodsId);
		map.put("goodsStoreList", goodsStoreList);
		map.put("goodsId", goodsId);
		return "store/store_edit";
	}
	/**
	 * 编辑货品库存
	 * @author Tonny
	 * @date 2018年3月22日
	 */
	@ControllerLog("编辑货品库存")
    @RequestMapping("editStore")
    @Authority(opCode = "04050103", opName = "编辑货品库存")
	@ResponseBody
    public AjaxResult editStore(@RequestParam("dataJson") String dataJson, Map<String, Object> map) {
		//JSONArray fromObject = JSONArray.parseArray(dataJson);
		//List<Map<String, Object>> jsonList = (List<Map<String, Object>>) fromObject.parse(dataJson);
		try {
			return AppUtil.returnObj(storeServiceImpl.editStore(dataJson));
		} catch (Exception e) {
			return AppUtil.returnObj(2,e.getMessage());
		}
		
	}

}
