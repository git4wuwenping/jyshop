/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.common.interceptor;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.qyy.jyshop.admin.common.annotation.FinancialMgrAuth;
import com.qyy.jyshop.admin.common.supper.DataCache;
import com.qyy.jyshop.conf.Constant;
import com.qyy.jyshop.dao.AuthUserDao;
import com.qyy.jyshop.model.AuthUser;
import com.qyy.jyshop.redis.RedisDao;
import com.qyy.jyshop.util.CookieUtil;
import com.qyy.jyshop.util.MD5Util;

/**
 * 描述
 * 
 * @author jiangbin
 * @created 2018年3月19日 下午5:33:33
 */
public class FinancialMgrInterceptor extends HandlerInterceptorAdapter {
    
    @Autowired
    private DataCache dataCache;
    @Autowired
    private RedisDao redisDao;
    @Autowired
    private AuthUserDao authUserDao;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 启动支持@Autowired注解
        WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext())
                .getAutowireCapableBeanFactory().autowireBean(this);
        
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            FinancialMgrAuth financialMgrAuth = ((HandlerMethod) handler).getMethodAnnotation(FinancialMgrAuth.class);
            // 没有声明需要权限,或者声明不验证权限
            if (financialMgrAuth == null || financialMgrAuth.validate() == false) {
                return true;
            } else {
                // 在这里实现自己的权限验证逻辑
                if (validateAuthUser(request))// 如果验证成功返回true
                    return true;
                else// 如果验证失败
                {
                    // 返回到输入密码页面
                    String path = URLEncoder.encode(request.getRequestURI());
                    response.sendRedirect("/admin/financialMgr/passwd/passwdMain?path="+path);
                    return false;
                }
            }
        } else
            return true;
    }

    private boolean validateAuthUser(HttpServletRequest request) {
        String sessionId = CookieUtil.get(Constant.SESSION_IDENTITY_KEY, request);
        String username = (String) dataCache.getValue(sessionId);
        String key = "FinancialMgr:"+username;
        String password = (String)redisDao.getObject(key);
        AuthUser authUser = authUserDao.selectByUsername(username);
        try {
            if(authUser != null && MD5Util.checkPwd(password, authUser.getPassword())  ){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
