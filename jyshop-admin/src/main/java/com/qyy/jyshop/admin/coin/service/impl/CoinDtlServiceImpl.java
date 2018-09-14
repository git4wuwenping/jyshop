package com.qyy.jyshop.admin.coin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.qyy.jyshop.admin.coin.service.CoinDtlService;
import com.qyy.jyshop.dao.CoinDtlDao;
import com.qyy.jyshop.model.CoinDtl;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class CoinDtlServiceImpl extends AbstratService<CoinDtl> implements CoinDtlService{
	
	@Autowired
	private CoinDtlDao coinDtlDao;

	@Override
	public PageAjax<Map<String, Object>> pageCoinDtl(PageAjax<Map<String, Object>> page) {
		ParamData params = this.getParamData(page.getPageNo(), page.getPageSize());
		return this.pageQuery("CoinDtlDao.queryCoinDtlByParam", params);
	}

	@Override
	public Map<String, Object> queryDtlByMemberId(Long memberId) {
		Map<String,Object> map = new HashMap<String, Object>();
		
		Example example = new Example(CoinDtl.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("memberId", memberId);
		example.setOrderByClause("create_time desc");
		List<CoinDtl> coinDtlList = this.queryByExample(example);
		map.put("coinDtlList", coinDtlList);
		
		Map<String,Object> memberInfo = coinDtlDao.queryMemberCoinInfoByMemberId(memberId);
		map.put("memberInfo", memberInfo);
		return map;
	}

}
