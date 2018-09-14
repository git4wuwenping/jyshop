package com.qyy.jyshop.admin.coin.service;

import java.util.Map;

import com.qyy.jyshop.pojo.PageAjax;

public interface CoinDtlService {

	PageAjax<Map<String, Object>> pageCoinDtl(PageAjax<Map<String, Object>> page);

	Map<String,Object> queryDtlByMemberId(Long memberId);

}
