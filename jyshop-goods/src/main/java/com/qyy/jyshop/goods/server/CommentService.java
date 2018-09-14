package com.qyy.jyshop.goods.server;

import java.util.Map;

import com.qyy.jyshop.pojo.PageAjax;

public interface CommentService {
	/**
	 * 查询商品详情评价列表
	 * @author Tonny
	 * @date 2018年3月19日
	 */
	PageAjax<Map<String, Object>> selectGoodsCommentList(Long goodsId, Integer pageNo, Integer pageSize);
}
