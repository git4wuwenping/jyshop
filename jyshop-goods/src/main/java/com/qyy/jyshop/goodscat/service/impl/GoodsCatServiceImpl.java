package com.qyy.jyshop.goodscat.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.conf.ShopBaseConf;
import com.qyy.jyshop.dao.GoodsCatDao;
import com.qyy.jyshop.dao.GoodsDao;
import com.qyy.jyshop.goodscat.service.GoodsCatService;
import com.qyy.jyshop.model.Cart;
import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.model.GoodsCat;
import com.qyy.jyshop.model.ProfitParam;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.EntityReflectUtil;
import com.qyy.jyshop.util.StringUtil;
@Service
public class GoodsCatServiceImpl extends AbstratService<GoodsCat> implements GoodsCatService {
	@Autowired
	private GoodsCatDao goodsCatDao;
	@Autowired
    private ShopBaseConf shopBaseConf;
	@Autowired
	private GoodsDao goodsDao;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map> getGoodsCatData() {
		List<GoodsCat> goodsCatList = goodsCatDao.selectGoodsCatListVisible();
        List<Map> topGoodsCatList = this.getChildrenList(goodsCatList, 0);
        if(topGoodsCatList!=null && topGoodsCatList.size()>0){
            for(Map map : topGoodsCatList){
                Integer topParentId = Integer.parseInt(map.get("catId").toString());
                List<Map> secGoodsCatList = this.getChildrenList(goodsCatList, topParentId);
                for (Map child : secGoodsCatList){
                    Integer secParentId = Integer.parseInt(child.get("catId").toString());
                    List<Map> thirdGoodsCatList= this.getChildrenList(goodsCatList, secParentId);
                    child.put("thirdGoodsCatList", thirdGoodsCatList);
                }
                map.put("secGoodsCatList", secGoodsCatList);
            }
        }
        return topGoodsCatList;
	}
	
	 @SuppressWarnings("rawtypes")
	private List<Map> getChildrenList(List<GoodsCat> goodsCatList, Integer parentId) {
	        List<Map> mapList = new ArrayList<>();
	        for (GoodsCat goodsCat: goodsCatList) {
	            if(goodsCat.getParentId().equals(parentId)){
	                Map map = EntityReflectUtil.toMap(goodsCat);
	                mapList.add(map);
	            }
	        }
	        return mapList;
	    }

	@Override
	public PageAjax<Map<String,Object>> getGoodsByCatId(Integer catId,Integer pageNo,Integer pageSize) {
		ParamData params = this.getParamData(pageNo,pageSize);
		//PageAjax<Map<String,Object>> goodsList = goodsDao.selectGoodsById(catId);
		ProfitParam profitParam = shopBaseConf.getParamConfig(ProfitParam.class, "shopParam_profitParam");
        BigDecimal redPointRatio =profitParam.getProfitMemberSecReal().compareTo(BigDecimal.ZERO) > 0 ? profitParam.getProfitMemberSecReal() : BigDecimal.ZERO;
        params.put("redPointRatio", redPointRatio);
        
		return  this.pageQuery("GoodsDao.selectGoodsById", params);
	}

	@Override
	public PageAjax<Map<String,Object>> orderGoods(String orderType, Integer sort, Integer catId,Integer shopStoreId,Integer pageNo,Integer pageSize) {
		ParamData params = this.getParamData(pageNo,pageSize);
		params.put("orderType", orderType);
		params.put("sort", sort);
		params.put("catId", catId);
		if(!StringUtil.isEmpty(shopStoreId))
			params.put("shopStoreId", shopStoreId);
		//List<Goods> orderGoods = goodsDao.orderGoods(orderType,sort,catId);
		ProfitParam profitParam = shopBaseConf.getParamConfig(ProfitParam.class, "shopParam_profitParam");
		BigDecimal redPointRatio =profitParam.getProfitMemberSecReal().compareTo(BigDecimal.ZERO) > 0 ? profitParam.getProfitMemberSecReal() : BigDecimal.ZERO;
		params.put("redPointRatio", redPointRatio);
		
		PageAjax<Map<String,Object>> pageQuery = this.pageQuery("GoodsDao.orderGoods", params);
		//goodsDao.orderGoods(orderType,sort,catId,shopStoreId);
		return pageQuery;
		/*PageAjax<Map<String,Object>> pageQuery = this.pageQuery("GoodsDao.orderGoods", params);*/
		//return goodsDao.orderGoods(orderType,sort,catId,shopStoreId,redPointRatio);
	}

	@Override
	public PageAjax<Map<String, Object>> selectGoodsByFirCatId(Integer pageNo, Integer pageSize, Integer catId) {
	    ParamData params = this.getParamData(pageNo,pageSize);
        ProfitParam profitParam = shopBaseConf.getParamConfig(ProfitParam.class, "shopParam_profitParam");
        if(profitParam.getProfitMemberSecReal().compareTo(BigDecimal.ZERO) >0){
            params.put("redPointRatio", profitParam.getProfitMemberSecReal());
        }else{
            params.put("redPointRatio", 0);
        }
		//PageAjax<Map<String,Object>> goodsList = goodsDao.selectGoodsById(catId);
		return  this.pageQuery("GoodsDao.selectGoodsByFirCatId", params);
	}

}
