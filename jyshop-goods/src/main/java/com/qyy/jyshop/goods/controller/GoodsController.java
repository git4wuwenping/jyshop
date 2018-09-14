package com.qyy.jyshop.goods.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.goods.server.CommentService;
import com.qyy.jyshop.goods.server.GoodsService;
import com.qyy.jyshop.goods.server.GoodsSpecService;
import com.qyy.jyshop.goods.server.ProductService;
import com.qyy.jyshop.model.ShopStore;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.shopstore.service.ShopStoreService;
/**
 * 首页管理
 * @author Tonny
 * @created 2017年12月25日 下午3:31:07
 */
@RestController
public class GoodsController extends AppBaseController{
	@Autowired
	private GoodsService goodsServiceImpl;
	//@Autowired
	//private GoodsSpecDao goodsSpecDao;
	@Autowired
	private ShopStoreService shopStoreServiceImpl;
	@Autowired
	private ProductService productService;
	@Autowired
	private GoodsSpecService goodsSpecService;
	@Autowired
	private CommentService commentServiceImpl;
	
	/**
	 * 首页商品展示
	 */
	@RequestMapping(value = "homeGoodsList")
    public Map<String,Object> homeGoodsList(Integer pageNo,Integer pageSize) {
		/*Map<String,Object> map=new HashMap<String, Object>();
        List<Goods> goodsList=this.goodsServiceImpl.homeGoodsList();
        map.put("goodsList", goodsList);*/
		PageAjax<Map<String,Object>> homeGoods =this.goodsServiceImpl.homeGoodsList(pageNo, pageSize);
        return this.outMessage(0, "获取成功", this.getPageMap(homeGoods));
    }
	
	/**
	 * 热门商品
	 */
	@RequestMapping(value = "hotGoods")
    public Map<String,Object> hotGoods(Integer pageNo,Integer pageSize) {
        PageAjax<Map<String,Object>> hotGoods=this.goodsServiceImpl.hotGoods(pageNo, pageSize);
        return this.outMessage(0, "获取成功", this.getPageMap(hotGoods));
    }
	
	/**
	 * 商品详情
	 */
	@RequestMapping(value = "goodsDetails")
    public Map<String,Object> goodsDetails(Long goodsId){
		Map<String,Object> map = new HashMap<String,Object>();
		//获取商品详情
		Map<String,Object> goods = this.goodsServiceImpl.getGoodsByGoodsId(goodsId);
		Integer comId =Integer.parseInt(goods.get("comId").toString());
		
		List<Map<String,Object>> GoodsGallery = this.goodsServiceImpl.selectGoodsGalleryByGoodsId(goodsId);
		//List<SpecVo> goodsSpecList = goodsSpecDao.selectSpecsByGoodsId(goodsId);
		//if(goods.getGoodsType()==0){
		//List<SpecVo> goodsSpecList = goodsSpecDao.selectSpecsByGoodsId(goodsId);
		if(comId!=0){
			//店铺信息
			map.put("shopStore", shopStoreServiceImpl.findShopStoreById(goods));
			//获取保证金
			int bond = this.goodsServiceImpl.findBondByComId(comId);
			map.put("bond", bond);
		}
		map.put("good", goods);
		map.put("GoodsGallery", GoodsGallery);
		//获取商品规格
		map.put("goodsSpecList", goodsSpecService.queryByGoodsId(goodsId));
		map.put("productList", this.productService.queryByGoodsId(goodsId));
        return map;
    }
	
	/**
	 * 根据商品ID获取货品列表与规格
	 * @author hwc
	 * @created 2018年4月14日 上午11:18:15
	 * @param goodsId
	 * @return
	 */
    @RequestMapping(value = "anon/queryProductList")
    public Map<String,Object> queryProductList(Long goodsId){
        try{
            Map<String,Object> returnMap = new HashMap<String,Object>();
            returnMap.put("goodsSpecList", goodsSpecService.queryByGoodsId(goodsId));
            returnMap.put("productList", this.productService.queryByGoodsId(goodsId));
            return this.outMessage(0, "获取成功" , returnMap);
        }catch(Exception ex){
            return this.outMessage(1, ex.getMessage() , null);
        }
    }
	
	/**
	 * 店铺信息以及店铺下商品信息
	 */
	@RequestMapping(value = "shopStoreInfo")
    public Map<String,Object> shopStoreInfo(Long goodsId,Integer pageNo,Integer pageSize){
		Map<String,Object> map = new HashMap<String,Object>();
		//获取商品信息
		Map<String,Object> good = this.goodsServiceImpl.getGoodsByGoodsId(goodsId);
		Integer comId =Integer.parseInt(good.get("comId").toString());
		//if(comId!=0 ){	//商户店铺
			//获取店铺信息
			ShopStore shop = this.shopStoreServiceImpl.findShopStoreById(good);
			map.put("shop", shop);
			//获取店铺下所有商品分页列表
			//Integer shopStoreId = (Integer) shop.get("shopStoreId");
			Integer shopStoreId = shop.getShopStoreId();
			PageAjax<Map<String,Object>> shopStoreGoodsList=this.goodsServiceImpl.shopStoreGoodsList(pageNo, pageSize,shopStoreId,comId);
			//map = this.getPageMap(shopStoreGoodsList);
			map.put("shopStoreGoodsList", shopStoreGoodsList);
		//}else{	//直营店铺
			//获取店铺信息
		//	ShopStore shop = this.shopStoreServiceImpl.findShopStoreById(good);
		//	map.put("shop", shop);
		//	Integer shopStoreId = Integer.parseInt(good.get("ShopStoreId").toString());
		//	PageAjax<Map<String,Object>> shopStoreGoodsList=this.goodsServiceImpl.shopStoreGoodsList(pageNo, pageSize,shopStoreId,comId);
		//	map.put("shopStoreGoodsList", shopStoreGoodsList);
		//}
		int bond = this.goodsServiceImpl.findBondByComId(comId);
		map.put("bond", bond);
		return  this.outMessage(0, "获取成功", map);
    }
	/***
	 * 商品详情评价列表
	 */
	@RequestMapping(value = "goodsCommentList")
	public Map<String,Object> selectGoodsCommentList(Long goodsId,Integer pageNo,Integer pageSize){
		PageAjax<Map<String,Object>> goodsCommentList = commentServiceImpl.selectGoodsCommentList(goodsId,pageNo,pageSize);
		Map<String, Object> map = this.getPageMap(goodsCommentList);
		return  this.outMessage(0, "获取成功", map);
	  }
	/**
	 * 店长礼包
	 */
	@RequestMapping(value = "shopownerGoods")
	public Map<String,Object> shopownerGoods(Integer pageNo,Integer pageSize){
		//Map<String,Object> map = new HashMap<String,Object>();
		PageAjax<Map<String,Object>> shopownerGoodsList = goodsServiceImpl.selectShopownerGoods(pageNo,pageSize);
		Map<String, Object> map = this.getPageMap(shopownerGoodsList);
		return  this.outMessage(0, "获取成功", map);
	  }
	
	/**
	 * 根据条件查询商品
	 */
	@RequestMapping(value = "selectGoodsByParams")
	public Map<String,Object> selectGoodsByParams(String goodName,int pageNo,int pageSize,Integer shopStoreId){
		PageAjax<Map<String, Object>> goodsList = goodsServiceImpl.searchGoodsByParams(goodName,pageNo,pageSize,shopStoreId);
		return this.outMessage(0, "获取成功", this.getPageMap(goodsList));
	}
	
}
