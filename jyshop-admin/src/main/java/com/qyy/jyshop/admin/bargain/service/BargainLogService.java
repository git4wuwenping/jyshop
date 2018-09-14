package com.qyy.jyshop.admin.bargain.service;

import com.qyy.jyshop.model.BargainLog;
import com.qyy.jyshop.model.OrderLog;

import java.util.List;

public interface BargainLogService {

    public List<BargainLog> queryByOrderId(Long orderId);
}
