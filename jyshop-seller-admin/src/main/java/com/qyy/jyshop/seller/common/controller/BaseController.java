package com.qyy.jyshop.seller.common.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.qyy.jyshop.exception.AjaxLoginException;
import com.qyy.jyshop.exception.AjaxPermissionException;
import com.qyy.jyshop.exception.LoginException;
import com.qyy.jyshop.exception.MalciousException;
import com.qyy.jyshop.exception.PermissionException;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.util.IPUtil;


@Controller
public class BaseController {
 
	/**日志*/
	public static Logger logger = LoggerFactory.getLogger(BaseController.class);

	/**
	 * 失败返回
	 * @param retmsg
	 * @return
	 */
	public AjaxResult returnFailed(String retmsg) {
		return new AjaxResult(retmsg);
	}

	/**
	 * 得到request对象
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/** ajax登录异常处理 **/
	@ExceptionHandler({ AjaxLoginException.class })
	@ResponseBody
	public AjaxResult ajaxLoginExceptionHandler(AjaxLoginException e) {
		logger.error("登录请求发生异常:", e);
		return new AjaxResult(e.getKey(), e.getMessage());
	}

	/** 普通登录异常处理 **/
	@ExceptionHandler({ LoginException.class })
	public String loginExceptionHandler(LoginException e, HttpServletRequest request) {
		logger.error("登录请求发生异常:", e);
		request.setAttribute("err", e.getMessage());
		return "forward:/";
	}

	/** 普通权限异常处理 **/
	@ExceptionHandler({ PermissionException.class })
	public String permissonExceptionHandler(PermissionException e) {
		return "common/no_permisson";
	}

	/** ajax权限异常处理 **/
	@ExceptionHandler({ AjaxPermissionException.class })
	@ResponseBody
	public AjaxResult ajaxPermissionExceptionHandler(AjaxPermissionException e) {
		return new AjaxResult(e.getKey(), e.getMessage());
	}
	
	/** 频繁请求异常处理 **/
	@ExceptionHandler({ MalciousException.class })
	public String malExceptionHandler(MalciousException e) {
		return "common/mal_request";
	}

	/** 公共异常处理 **/
	@ExceptionHandler({ Exception.class })
	public Object exceptionHandler(Exception e, HttpServletRequest request) {
		ParamData params = new ParamData();
		logger.info("");
		StringBuilder sb = new StringBuilder(params.getString("loginIp")).append(request.getRequestURI()).append("请求发生异常:")
				.append(request.getServletPath()).append(":").append(params);
		logger.error(sb.toString(), e);
		return "common/500";
	}

	public void logBefore(String desc) {
		HttpServletRequest request = getRequest();
		logger.error("");
		StringBuilder sb = new StringBuilder(IPUtil.getIpAdd(request)).append(desc).append(":").append(request.getServletPath());
		logger.error(sb.toString());
	}
	
	
}
