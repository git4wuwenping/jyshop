package com.qyy.jyshop.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.ExamineGoods;
import com.qyy.jyshop.supple.MyMapper;

public interface ExamineGoodsDao extends MyMapper<ExamineGoods> {
	
	List<Map<String,Object>> selectExamineByGoodsId(@Param("goodsId")Long goodsId);
	/**
	 * 根据条件查询审核列表
	 */
	List<Map<String, Object>> selectExamineByParams(Map<String,Object> params);
	/**
	 * 根据id查询审核详情用于回显查看
	 * @author Tonny
	 * @date 2018年3月24日
	 */
	List<Map<String, Object>> selectExamineByexamineId(@Param("examineId")long examineId);
	
	void updateExamineBygoodsId(@Param("goodsId")Long goodsId, 
								@Param("state")Short state, 
								@Param("type")Short type, 
								@Param("createTime")Timestamp createTime);
}
