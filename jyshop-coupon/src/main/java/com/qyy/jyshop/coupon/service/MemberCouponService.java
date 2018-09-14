package com.qyy.jyshop.coupon.service;

import java.util.Map;

import com.qyy.jyshop.pojo.PageAjax;


public interface MemberCouponService {

    void getCoupon(String token, Long cpnId);

    Map<String, Object> memberCouponCenter(String token);

    PageAjax<Map<String, Object>> memberCouponList(String token,Integer getFlag, Integer pageNo, Integer pageSize);

    void memberGetUnreceivedCoupon(String token, String sn);


}
