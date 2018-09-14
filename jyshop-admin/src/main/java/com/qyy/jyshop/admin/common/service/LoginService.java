package com.qyy.jyshop.admin.common.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qyy.jyshop.pojo.AjaxResult;



public interface LoginService {
	
	/**
	 * 登陆
	 * @param request
	 * @param response
	 * @return
	 */
	public AjaxResult login(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 退出
	 * @param response
	 * @param request
	 * @return
	 */
	public AjaxResult logout(HttpServletResponse response, HttpServletRequest request);
}
