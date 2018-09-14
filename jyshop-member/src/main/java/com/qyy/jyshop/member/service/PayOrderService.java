package com.qyy.jyshop.member.service;

import java.math.BigDecimal;

public interface PayOrderService {

	BigDecimal selectPayAmount(String token);

}
