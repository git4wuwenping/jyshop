package com.qyy.jyshop.goodscat.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.goodscat.service.GoodsCatService;
import com.qyy.jyshop.pojo.PageAjax;
/**
 * 商品类别
 * @author 22132
 *
 */
@RestController
//@RequestMapping("/goodsCat")
public class GoodsCatController extends AppBaseController{
	
	@Autowired
	private GoodsCatService goodsCatService;

	/**
	 * 获取商品类别
	 */
	@RequestMapping(value = "getGoodsCatData")
    public Map<String,Object> getGoodsCatData(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map> goodsCat = this.goodsCatService.getGoodsCatData();
		map.put("goodsCat", goodsCat);
        return map;
        //return this.outMessage(0, "获取成功", map);
    }
	
	/**
	 * 根据商品类型查询商品列表
	 */
	@RequestMapping(value = "getGoodsByCatId")
    public Map<String,Object> getGoodsByCatId(Integer catId,Integer pageNo,Integer pageSize){
		/*Map<String,Object> map = new HashMap<String,Object>();
		//List<Goods> goodsList = this.goodsCatService.getGoodsByCatId(catId);
		map.put("goodsList", goodsList);*/
		PageAjax<Map<String,Object>> shopStoreGoodsList=this.goodsCatService.getGoodsByCatId(pageNo, pageSize,catId);
        return this.outMessage(0, "获取成功", this.getPageMap(shopStoreGoodsList));
    }
	
	/**
	 * 商品列表排序
	 */
	@RequestMapping(value = "orderGoods")
    public Map<String,Object> orderGoods(String orderType,Integer sort,Integer catId,Integer pageNo,Integer pageSize,Integer shopStoreId){
//		PageAjax<Map<String,Object>>  orderGoodsList = this.go;
//		List<Map<String, Object>> orderGoodsList = null;
		PageAjax<Map<String,Object>>  page = null;
		if(orderType.equals("price") || orderType.equals("create_time") || orderType.equals("buy_count")){
			if(sort==1 || sort == 0){
//				page = new PageAjax<>();
				 //orderGoodsList =this.goodsCatService.orderGoods(orderType,sort,catId,pageNo,pageSize);
				page =this.goodsCatService.orderGoods(orderType,sort,catId,shopStoreId,pageNo,pageSize);
//				 page.setPageNo(0);
//				 page.setPageSize(10);
//				 page.setRows(orderGoodsList);
				/*map.put("orderGoodsList", orderGoodsList);*/
			}else{
				return this.outMessage(1, "排序规格参数错误", null);
			}
		}else{
			return this.outMessage(1, "排序方式参数错误", null);
		}
        return this.outMessage(0, "获取成功", this.getPageMap(page));
    }
	
	/**
	 * 查询一级分类下的所有商品
	 */
	@RequestMapping(value = "selectGoodsByFirCatId")
    public Map<String,Object> selectGoodsByFirCatId(Integer catId,Integer pageNo,Integer pageSize){
		PageAjax<Map<String,Object>> shopStoreGoodsList=this.goodsCatService.selectGoodsByFirCatId(pageNo, pageSize,catId);
        return this.outMessage(0, "获取成功", this.getPageMap(shopStoreGoodsList));
    }
	
}
