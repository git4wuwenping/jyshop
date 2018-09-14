package com.qyy.jyshop.admin.coupon.service;

import java.util.Map;

import com.qyy.jyshop.model.Coupon;
import com.qyy.jyshop.pojo.PageAjax;

public interface CouponService {

    PageAjax<Map<String, Object>> pageCouponAjax(PageAjax<Map<String, Object>> page);

    String editCoupon(Coupon coupon);

    String addCoupon(Coupon coupon);

    String delCoupon(Long cpnId);

    Map<String, Object> selectCouponInfoByCpnId(Long cpnId);

    String grantCoupon(Long cpnId, Integer grantCount, Integer grantCount2);

    String grantCoupon(Long cpnId, String memberIds, Integer count, Integer gType);

    Coupon selectOne(Long cpnId);

}
