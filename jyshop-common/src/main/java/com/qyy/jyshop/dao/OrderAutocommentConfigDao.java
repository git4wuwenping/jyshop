package com.qyy.jyshop.dao;

import com.qyy.jyshop.model.OrderAutocommentConfig;
import com.qyy.jyshop.supple.MyMapper;

public interface OrderAutocommentConfigDao  extends MyMapper<OrderAutocommentConfig>{

    OrderAutocommentConfig selectRandomOne();

}
