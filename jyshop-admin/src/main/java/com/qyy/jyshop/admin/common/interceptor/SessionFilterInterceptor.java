package com.qyy.jyshop.admin.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.redis.RedisDao;
import com.qyy.jyshop.util.StringUtil;



/**
 * app接口拦截器
 */
public class SessionFilterInterceptor extends HandlerInterceptorAdapter{

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(SessionFilterInterceptor.class);
	
	@Autowired
	private RedisDao redisDao;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);
		
		String url = request.getRequestURI().substring(
				request.getContextPath().length());
		logger.info("进来了"+url);
		
		if (isInclude(url)) {
			return super.preHandle(request, response, handler);
		} else {
			String token=request.getParameter("token");
			if(!StringUtil.isEmpty(token)){
				Member member=(Member)redisDao.getObject(token);
				if(member!=null && !StringUtil.isEmpty(member.getMemberId())){
					return super.preHandle(request, response, handler);
				}
			}
			request.getRequestDispatcher("/loginError").forward(request, response);   
			return false;
		}
	}
	
	/**
	 * 是否需要过滤
	 * 
	 * @param url
	 * @return
	 */
	private boolean isInclude(String url) {
		System.out.println(url);
		if(url.startsWith("/attachment/qrcode/"))
			return true;
		return false;
	}
}
