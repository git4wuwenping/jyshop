package com.qyy.jyshop.admin.coupon.service;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.CouponRel;
import com.qyy.jyshop.pojo.PageAjax;

public interface CouponRelService {


    String delRel(Long id);

    String delAllRel(String cpnSn);

    String addRel(Long[] relIdArr, String cpnSn, Integer relType);

    PageAjax<Map<String, Object>> pageRel(String cpnSn, Integer relType, PageAjax<Map<String, Object>> page);

    List<Map<String, Object>> queryGoodsCatRel(String cpnSn);

    List<Map<String, Object>> queryGoodsCatList();


}
