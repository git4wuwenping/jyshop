package com.qyy.jyshop.seller.adminuser.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.seller.adminuser.service.AuthActionService;
import com.qyy.jyshop.seller.common.annotation.ServiceLog;
import com.qyy.jyshop.dao.AuthActionDao;
import com.qyy.jyshop.model.AuthAction;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.supple.AbstratService;


/**
 * 权限操作
 */
@Service
public class AuthActionServiceImpl extends AbstratService<AuthAction> implements AuthActionService{

	@Autowired
	private AuthActionDao authActionDao;
	
	@Override
	@ServiceLog("根据操作Id获取操作信息")
	public AuthAction queryAuthActionByOpId(Integer opId) {
		return this.authActionDao.selectByOpId(opId);
	}

	@Override
	@ServiceLog("获取所有的操作信息")
	public List<AuthAction> queryAuthAction() {
		return this.authActionDao.selectAll();
	}
	
	@Override
	@ServiceLog("获取角色的操作权限")
	public List<AuthAction> queryAuthActionByRoleId(Integer roleId){
		return this.authActionDao.selectByRoleId(roleId);
	}
	
	@Override
	@ServiceLog("获取操作列表(分页_ajax)")
	public PageAjax<AuthAction> pageAuthAction(PageAjax<AuthAction> page,
			AuthAction authAction) {
		return this.queryPage(page, authAction);
	}

	
}
