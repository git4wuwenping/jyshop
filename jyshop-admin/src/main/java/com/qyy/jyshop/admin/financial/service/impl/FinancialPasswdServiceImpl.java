package com.qyy.jyshop.admin.financial.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.admin.common.supper.DataCache;
import com.qyy.jyshop.admin.financial.service.FinancialPasswdService;
import com.qyy.jyshop.conf.Constant;
import com.qyy.jyshop.dao.AuthUserDao;
import com.qyy.jyshop.model.AuthUser;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.redis.RedisDao;
import com.qyy.jyshop.util.CookieUtil;
import com.qyy.jyshop.util.MD5Util;

@Service
public class FinancialPasswdServiceImpl implements FinancialPasswdService {

    @Autowired
    private DataCache dataCache; 
    @Autowired
    private RedisDao redisDao; 
    @Autowired
    private AuthUserDao authUserDao; 
    
    @Override
    public AjaxResult checkFinancialPasswd(String password, HttpServletRequest request) {
        String sessionId = CookieUtil.get(Constant.SESSION_IDENTITY_KEY, request);
        String username = (String) dataCache.getValue(sessionId);
        AuthUser authUser = authUserDao.selectByUsername(username);
        
        try {
            if(authUser != null && MD5Util.checkPwd(password, authUser.getPassword())){
                String key = "FinancialMgr:"+username;
                redisDao.saveObject(key, password,600);
                return new AjaxResult(0,"操作成功",null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AjaxResult(1,"操作失败","密码错误");
    }

    @Override
    public Boolean checkExpire(HttpServletRequest request) {
        String sessionId = CookieUtil.get(Constant.SESSION_IDENTITY_KEY, request);
        String username = (String) dataCache.getValue(sessionId);
        String key = "FinancialMgr:"+username;
        Object object = redisDao.getObject(key);
        if(object != null){
            return true;
        }
        return false;
    }

}
