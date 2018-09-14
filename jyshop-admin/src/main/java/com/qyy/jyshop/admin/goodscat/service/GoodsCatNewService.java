package com.qyy.jyshop.admin.goodscat.service;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.GoodsCat;

/**
 * 商品分类
 */
public interface GoodsCatNewService {

	List<GoodsCat> listGoodsCatByParentId(Long parentId);

	String addGoodsCat(GoodsCat goodsCat);

	Map<String,Object> queryGoodsCatByCatId(Integer id);

	String editGoodsCat(GoodsCat goodsCat);

	String editGoodsCatDisable(Integer id, Integer disable);

    List<GoodsCat> listAll();
    
    List<Integer> selectAllSonCat(int parseInt);
}
