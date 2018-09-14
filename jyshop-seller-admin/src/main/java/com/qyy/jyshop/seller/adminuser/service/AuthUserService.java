package com.qyy.jyshop.seller.adminuser.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qyy.jyshop.model.AuthUser;
import com.qyy.jyshop.pojo.PageAjax;

public interface AuthUserService {

	/**
	 * 根据用户Id获取用户信息
	 * @param id
	 * @return
	 */
	public AuthUser queryAuthUserById(Integer id);


	/**
	 * 根据供应商Id获取用户信息
	 * @author hwc
	 * @created 2017年11月21日 下午2:58:49
	 * @param comId
	 * @return
	 */
	public List<AuthUser> queryAuthUserByComId(Integer comId);
	
	/**
	 * 根据角色Id获取用户
	 * @param roleId 角色Id
	 * @return 用户信息
	 */
	public List<AuthUser> queryAuthUserByRoleId(Integer roleId);
	
	/**
	 * 获取管理员列表(分页_ajax)
	 * @param page
	 * @param authUser
	 * @return
	 */
	public PageAjax<AuthUser> pageAuthUser(PageAjax<AuthUser> page, AuthUser authUser);
	
	/**
	 * 添加管理员
	 * @param authUser
	 */
	public String doAddAuthUser(AuthUser authUser)throws Exception;
	
	/**
	 * 修改用户
	 * @param authUser
	 * @return
	 */
	public String doEditUser(AuthUser authUser);
	
	/**
	 * 修改用户密码
	 * @param response
	 * @param request
	 * @param id
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	public String doEditPassword(HttpServletResponse response, HttpServletRequest request,Integer id, String oldPwd, String newPwd)throws Exception;
	
	/**
	 * 重置管理员密码为123456
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String doResetPwd(Integer id)throws Exception;
	
	/**
	 * 删除管理员
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String doDelUser(Integer id);
}
