package com.qyy.jyshop.admin.coupon.service;

import java.util.Map;

import com.qyy.jyshop.pojo.PageAjax;

public interface MemberCouponService {

    PageAjax<Map<String, Object>> pageMemberCoupon(Long cpnId, PageAjax<Map<String, Object>> page);

}
