package com.qyy.jyshop.admin.coupon.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.qyy.jyshop.admin.coupon.service.MemberCouponService;
import com.qyy.jyshop.model.MemberCoupon;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class MemberCouponServiceImpl extends AbstratService<MemberCoupon> implements MemberCouponService {

    @Override
    public PageAjax<Map<String, Object>> pageMemberCoupon(Long cpnId, PageAjax<Map<String, Object>> page) {
        ParamData params = this.getParamData(page.getPageNo(), page.getPageSize());
        params.put("cpnId", cpnId);
        return this.pageQuery("MemberCouponDao.selectMemberCouponList", params );
    }

}
