package com.qyy.jyshop.admin.giftorder.service;

import java.util.List;

import com.qyy.jyshop.model.GiftOrderLog;

public interface GiftOrderLogService {

    public List<GiftOrderLog> queryByOrderId(Long orderId);
}
