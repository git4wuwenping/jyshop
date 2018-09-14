package com.qyy.jyshop.seller.examine.service;

import java.util.List;
import java.util.Map;


public interface ExamineService {

	List<Map<String,Object>> selectExamineByGoodsIs(Long goodsId);
		
}
