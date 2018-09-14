package com.qyy.jyshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.AuthUser;
import com.qyy.jyshop.supple.MyMapper;



public interface AuthUserDao extends MyMapper<AuthUser>{
 
	/**
	 * 根据用户名查询用户
	 * @param username
	 * @return
	 */
	AuthUser selectByUsername(@Param("username")String username);
	
	/**
	 * 根据角色Id获取用户列表
	 * @param roleId
	 * @return
	 */
	List<AuthUser> selectByRoleId(@Param("roleId")Integer roleId);
	
	/**
	 * 根据供应商Id获取用户列表
	 * @author hwc
	 * @created 2017年11月21日 下午2:55:31
	 * @param comId
	 * @return
	 */
    List<AuthUser> selectByComId(@Param("comId")Integer comId);


    /**
     * 根据供应商ID更新用户
     */
    void updateUserById(AuthUser user);
    
}
