package com.qyy.jyshop.goods.server;

import java.util.List;
import java.util.Map;

public interface ProductService {

    /**
     * 根据商品ID获取货品列表
     * @author hwc
     * @created 2018年1月6日 上午9:40:42
     * @param goodsId
     * @return
     */
    public List<Map<String,Object>> queryByGoodsId(Long goodsId);
}
