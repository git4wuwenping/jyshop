package com.qyy.jyshop.coupon.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.coupon.service.CouponService;
import com.qyy.jyshop.dao.CouponDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.Coupon;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class CouponServiceImpl extends AbstratService<Coupon> implements CouponService {

    @Autowired
    private CouponDao couponDao;

    @Override
    public PageAjax<Map<String, Object>> getCouponList(Integer pageNo, Integer pageSize) {
        ParamData params = this.getParamData(pageNo, pageSize);
        return this.pageQuery("CouponDao.getCouponList", params);
    }

    @Override
    public Map<String, Object> couponDetail(Long cpnId, Integer pageNo, Integer pageSize) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        Coupon record = new Coupon();
        record.setCpnId(cpnId);
        // 满100 减 20 有效期 xxxx -xxxx 领取期 xxxx-xxxx
        Coupon coupon = couponDao.selectOne(record);
        retMap.put("coupon", coupon);
        // 能够使用优惠券的商品 列表
        Integer[] i = { 1, 2, 3 };
        if (coupon == null || numberNotIn(coupon.getCpnType(), i)) {
            throw new AppErrorException("出错啦...");
        }
        ParamData params = this.getParamData(pageNo, pageSize);
        params.put("cpnId", cpnId);
        params.put("cpnType", coupon.getCpnType());
        PageAjax<Map<String, Object>> couponGoodsPageList = this.pageQuery("CouponDao.getCouponGoodsListByCpnId",
                params);
        retMap.put("couponGoodsPageList", couponGoodsPageList);
        return retMap;
    }

    private boolean numberNotIn(Integer cpnType, Integer[] i) {
        for (Integer x : i) {
            if (x == cpnType) {
                return false;
            }
        }
        return true;
    }

}
