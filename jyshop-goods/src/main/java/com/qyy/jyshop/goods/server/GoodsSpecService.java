package com.qyy.jyshop.goods.server;

import java.util.List;
import java.util.Map;

public interface GoodsSpecService {

    /**
     * 根据商品ID获取商品规格
     * @author jiangbin
     * @param goodsId
     * @return
     */
    public List<Map<String,Object>> queryByGoodsId(Long goodsId);
}
