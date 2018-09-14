package com.qyy.jyshop.admin.giftorder.service;

import java.util.List;

import com.qyy.jyshop.model.GiftOrderItems;

public interface GiftOrderItemsService {

    public List<GiftOrderItems> queryByOrderId(Long orderId);
}
