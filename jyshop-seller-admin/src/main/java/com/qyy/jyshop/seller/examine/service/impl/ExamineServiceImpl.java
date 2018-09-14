package com.qyy.jyshop.seller.examine.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.dao.ExamineGoodsDao;
import com.qyy.jyshop.model.ExamineGoods;
import com.qyy.jyshop.seller.examine.service.ExamineService;
import com.qyy.jyshop.supple.AbstratService;
@Service
public class ExamineServiceImpl  extends AbstratService<ExamineGoods> implements ExamineService {
	@Autowired
	private ExamineGoodsDao examineDao;

	@Override
	public List<Map<String,Object>> selectExamineByGoodsIs(Long goodsId) {
		
		return examineDao.selectExamineByGoodsId(goodsId);
	}
	
	
}
