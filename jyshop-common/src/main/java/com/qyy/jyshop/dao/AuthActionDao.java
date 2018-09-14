package com.qyy.jyshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.AuthAction;
import com.qyy.jyshop.supple.MyMapper;

public interface AuthActionDao  extends MyMapper<AuthAction>{

	/**
	 * 根据权限ID获取权限信息
	 * @param opId 权限ID
	 * @return 权限信息
	 */
	AuthAction selectByOpId(@Param("opId")Integer opId);
	
	/**
	 * 根据权限值和链接获取权限信息
	 * @author hwc
	 * @created 2017年11月20日 下午5:10:45
	 * @param opCode 权限值
     * @param opHref 权限链接
     * @return 权限信息
	 */
	AuthAction selectByOpCodeAndOpHref(@Param("opCode")String opCode,@Param("opHref")String opHref);
	
	/**
	 * 根据角色Id获取权限列表
	 * @param roleId
	 * @return
	 */
	List<AuthAction> selectByRoleId(@Param("roleId")Integer roleId);
}
