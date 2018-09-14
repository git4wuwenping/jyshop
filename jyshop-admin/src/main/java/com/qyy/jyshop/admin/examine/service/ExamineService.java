package com.qyy.jyshop.admin.examine.service;

import java.util.Map;

import com.qyy.jyshop.pojo.PageAjax;

public interface ExamineService {
	/**
	 * 商品驳回
	 * @author Tonny
	 * @date 2018年3月22日
	 */
	String examineGoodsSalesNo(Map<String, Object> map);
	/**
	 * 查询审核列表
	 * @author Tonny
	 * @date 2018年3月24日
	 */
	PageAjax<Map<String, Object>> pageExamineAjax(PageAjax<Map<String, Object>> page);
	/**
	 * 根据id查询审核详情用于回显查看
	 * @author Tonny
	 * @date 2018年3月24日
	 */
	Map<String, Object> selectExamineByexamineId(long examineId);
	/**
	 * 修改审核信息
	 * @author Tonny
	 * @date 2018年3月26日
	 */
	String updateExamineDetials(Map<String, Object> map);
	/**
	 * 审核通过
	 * @author Tonny
	 * @date 2018年3月28日
	 */
	String doGoodsSalesExamine(Long examineId, Integer valueOf);

}
