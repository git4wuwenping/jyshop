package com.qyy.jyshop.order.service;

import net.sf.json.JSONArray;

import com.qyy.jyshop.model.GiftOrder;

public interface GiftOrderItemsService {

    public void doAddOrderItems(JSONArray goodsJsonArray,GiftOrder order);
}
