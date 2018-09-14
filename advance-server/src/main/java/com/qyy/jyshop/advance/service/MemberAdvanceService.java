package com.qyy.jyshop.advance.service;

import java.math.BigDecimal;

public interface MemberAdvanceService {

    /**
     * 预存款支付
     * @author hwc
     * @created 2018年1月6日 上午10:58:40
     * @param payAmount
     * @param orderId
     * @param orderType
     * @param token
     */
    public Boolean doMemberAdvancePay(BigDecimal payAmount, Long orderId,Integer orderType,String payPwd,String token);
}
