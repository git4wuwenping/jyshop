package com.qyy.jyshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.AuthRole;
import com.qyy.jyshop.supple.MyMapper;



public interface AuthRoleDao extends MyMapper<AuthRole>{

    /**
     * 根据角色Id获取角色信息
     * @param roleId
     * @return
     */
    AuthRole selectByRoleId(@Param("roleId")Integer roleId);
    
	/**
	 * 根据角色名获取角色信息
	 * @param roleName
	 * @return
	 */
	AuthRole selectByRoleName(@Param("roleName") String roleName);
	
    /**
     * 根据角色Id获取角色信息
     * @param roleId
     * @return
     */
    List<AuthRole> selectByComId(@Param("comId")Integer comId);
}
