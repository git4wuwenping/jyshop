package com.qyy.jyshop.seller.adminuser.service;

import java.util.List;

import com.qyy.jyshop.model.AuthAction;
import com.qyy.jyshop.pojo.PageAjax;


public interface AuthActionService {

	/**
	 * 根据操作Id获取操作信息
	 * @param opId 操作Id
	 * @return
	 */
	public AuthAction queryAuthActionByOpId(Integer opId);
	
	/**
	 * 获取所有的操作信息
	 * @return
	 */
	public List<AuthAction> queryAuthAction();
	
	/**
	 * 获取角色的操作权限
	 * @param roleId
	 * @return
	 */
	public List<AuthAction> queryAuthActionByRoleId(Integer roleId);
	
	/**
	 * 获取操作列表(分页_ajax)
	 * @param page
	 * @param authAction
	 * @return
	 */
	public PageAjax<AuthAction> pageAuthAction(PageAjax<AuthAction> page,AuthAction authAction);
}
