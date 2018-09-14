package com.qyy.jyshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.AuthRoleAction;
import com.qyy.jyshop.supple.MyMapper;



public interface AuthRoleActionDao extends MyMapper<AuthRoleAction> {

	/**
	 * 批量添加
	 * @param authRoleActionList
	 */
	void batchInsert(List<AuthRoleAction> authRoleActionList);
	
	/**
	 * 根据角色Id删除关联权限
	 * @author hwc
	 * @created 2017年11月21日 上午10:25:25
	 * @param roleId
	 */
	void deleteByRoleId(@Param("roleId")Integer roleId);
	
	/**
	 * 批量删除
	 * @param authRoleActionList
	 */
	void batchDel(List<AuthRoleAction> authRoleActionList);
}