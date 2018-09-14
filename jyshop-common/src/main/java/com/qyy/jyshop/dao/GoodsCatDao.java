package com.qyy.jyshop.dao;


import com.qyy.jyshop.model.GoodsCat;
import com.qyy.jyshop.supple.MyMapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface GoodsCatDao extends MyMapper<GoodsCat> {
    
    /**
     * 根据分类Id获取分类编码
     * @author hwc
     * @created 2018年1月23日 下午5:20:28
     * @param catId
     * @return
     */
    String selectOfCatCode(@Param("catId")Integer catId);
    
    /**
     * 根据排序查询所有的商品分类
     * @author wpq
     * @return
     */
    List<GoodsCat> selectGoodsCatList();
    
    /**
     * 根据父id查询分类
     * @author wpq
     * @return
     */
    List<Map> selectGoodsCatListByParentId(@Param("parentId")Integer parentId);
    
    /**
     * 查询分类
     */
    List<Map<String, Object>> selectGoodsCatList (Map<String,Object> params);

	Map<String, Object> queryGoodsCatByCatId(Integer id);

	List<GoodsCat> selectGoodsCatListVisible();

}