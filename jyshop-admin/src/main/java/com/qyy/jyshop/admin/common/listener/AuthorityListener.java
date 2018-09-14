package com.qyy.jyshop.admin.common.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * 权限监听器
 */
//@WebListener(与ServletComponentScan注解一起使用;现在用另一种方式,交由WebConfig统一管理配置)
public class AuthorityListener implements ServletContextListener {
	@Autowired  
    ApplicationContext context;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		//加载自动注入Autowired
//		WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("销毁监听器.....");
	}
}
