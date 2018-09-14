package com.qyy.jyshop.goods.server;

public interface ProductStoreService {

    /**
     * 根据订单ID修改货品库存
     * @author hwc
     * @created 2018年4月3日 上午9:18:50
     * @param orderId
     */
    public void doEditStoreByOrderId(Long orderId);
}
