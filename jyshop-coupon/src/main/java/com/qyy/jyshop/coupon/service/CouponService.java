package com.qyy.jyshop.coupon.service;

import java.util.Map;

import com.qyy.jyshop.pojo.PageAjax;

public interface CouponService {

    PageAjax<Map<String, Object>> getCouponList(Integer pageNo, Integer pageSize);

    Map<String, Object> couponDetail(Long cpnId,Integer pageNo, Integer pageSize);

    

}
