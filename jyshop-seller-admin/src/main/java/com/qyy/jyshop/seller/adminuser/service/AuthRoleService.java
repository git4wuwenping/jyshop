package com.qyy.jyshop.seller.adminuser.service;

import java.util.List;

import com.qyy.jyshop.model.AuthRole;
import com.qyy.jyshop.pojo.PageAjax;


public interface AuthRoleService {

	/**
	 * 根据角色Id获取角色信息
	 * @param roleId 角色Id
	 * @return
	 */
	public AuthRole queryAuthRoleByRoleId(Integer roleId);
	
	/**
	 * 根据供应商Id获取角色列表
	 * @author hwc
	 * @created 2017年11月20日 下午2:42:55
	 * @return
	 */
	public List<AuthRole> queryAuthRoleByComId(Integer comId);
	
	/**
	 * 获取角色列表(分页_ajax)
	 * @param page
	 * @param authRole
	 * @return
	 */
	public PageAjax<AuthRole> pageAuthRole(PageAjax<AuthRole> page,AuthRole authRole);
	
	/**
	 * 添加角色
	 * @param authRole
	 */
	public String doAddRole(AuthRole authRole);
	
	/**
	 * 修改角色
	 * @param authRole
	 */
	public String doEditRole(AuthRole authRole);
	
	/**
	 * 删除角色
	 * @param authRole
	 */
	public String doDelRole(Integer roleId);
	
	/**
	 * 用户绑定角色
	 * @param roleId
	 * @param ids
	 * @return
	 */
	public String doBindUser(int roleId, Integer[] ids);
	
	/**
	 * 角色绑定权限
	 * @param roleId
	 * @param opIds
	 * @return
	 */
	public String doBindOperat(int roleId, Integer[] opIds);
	
	/**
	 * 角色解除权限
	 * @param roleId
	 * @param opIds
	 * @return
	 */
	public String doUnBindOperat(int roleId, Integer[] opIds);
}
