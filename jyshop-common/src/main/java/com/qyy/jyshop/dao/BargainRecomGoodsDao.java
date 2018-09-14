package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.BargainRecomGoods;

import tk.mybatis.mapper.common.Mapper;

public interface BargainRecomGoodsDao extends Mapper<BargainRecomGoods>{

    /**
     * 获取砍价推荐商品
     * @author hwc
     * @created 2018年4月12日 下午4:34:36
     * @return
     */
    public List<Map<String,Object>> selectGoodsList();

    void batchInsert(List<BargainRecomGoods> list);
}
