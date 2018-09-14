package com.qyy.jyshop.seller.order.service;

import java.util.List;
import com.qyy.jyshop.model.OrderItems;

public interface OrderItemsService {

    public List<OrderItems> queryByOrderId(Long orderId);
}
