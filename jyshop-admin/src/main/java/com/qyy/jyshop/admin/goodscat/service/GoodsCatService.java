package com.qyy.jyshop.admin.goodscat.service;

import com.qyy.jyshop.model.GoodsCat;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.TreeNode;

import java.util.List;
import java.util.Map;

/**
 * 商品分类
 */
public interface GoodsCatService {
    /**
     * 获取分类列表
     * @return
     */
    List<GoodsCat> queryGoodsCatList();
    /**
     * 获取商品分类列表(分页_ajax)
     * @param page
     * @return
     */
    PageAjax<Map<String, Object>> pageGoodsCat(PageAjax<GoodsCat> page,Integer catLevel);

    /**
     * 添加一个商品分类
     * @param goodsCat
     * @return
     */
    String addGoodsCat(GoodsCat goodsCat);
    /**
     * 删除一个商品分类
     * @param id
     * @return
     */
    String delGoodsCat(Integer id);
    /**
     * 根据id获取一个商品分类
     * @param id
     * @return
     */
    GoodsCat queryGoodsCatByCatId(Integer id);

    /**
     * 修改商品分类
     * @param goodsCat
     * @return
     */
    String editGoodsCat(GoodsCat goodsCat);

    List<TreeNode> getGoodsCatTreeResult();
    
    
    
}
