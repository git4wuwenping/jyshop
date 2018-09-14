package com.qyy.jyshop.admin.common.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.conf.Constant;
import com.qyy.jyshop.dao.AuthActionDao;
import com.qyy.jyshop.dao.AuthRoleDao;
import com.qyy.jyshop.dao.AuthUserDao;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.redis.RedisDao;
import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.common.pojo.Identity;
import com.qyy.jyshop.admin.common.service.LoginService;
import com.qyy.jyshop.admin.common.supper.DataCache;
import com.qyy.jyshop.model.AuthAction;
import com.qyy.jyshop.model.AuthRole;
import com.qyy.jyshop.model.AuthUser;
import com.qyy.jyshop.util.AppUtil;
import com.qyy.jyshop.util.CookieUtil;
import com.qyy.jyshop.util.IPUtil;
import com.qyy.jyshop.util.MD5Util;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthUserDao authUserDao;
    @Autowired
    private AuthRoleDao authRoleDao;
    @Autowired
    private AuthActionDao authActionDao;
    @Autowired
    private DataCache dataCache;
    @Autowired
    private RedisDao redisDao;

    @Override
    @ServiceLog("登录")
    public AjaxResult login(HttpServletRequest request, HttpServletResponse response) {
        String verifyCode = (String) request.getSession().getAttribute(Constant.VERIFY_CODE);
        String result = null;
        ParamData params = new ParamData();
        String vcode = params.getString("vcode");
        // params.containsKey("vcode") && (StringUtils.isEmpty(verifyCode) ||
        // !verifyCode.equalsIgnoreCase(vcode))
        if (false) {
            result = "验证码错误";
        } else {
            String username = params.getString("username");
            String loginIp = params.getString("loginIp");
            String key = username + loginIp + Constant.LOGIN_ERROR_TIMES;
            AuthUser user = authUserDao.selectByUsername(username);

            if (user == null || !user.getPassword().equals(params.getString("password"))) {
                int errTimes = dataCache.getInt(key);
                // 记录密码错误次数,达到3次则需要输出验证码
                dataCache.setValue(key, errTimes += 1);
                result = "用户名或密码错误|" + errTimes;
            } else {
                
                if(!user.getUseable().equals(0))
                    result = "帐号己禁用...";
                if(!user.getComId().equals(0))
                    result = "只能使用自营帐号登陆...";
                
                AuthRole authRole = this.authRoleDao.selectByRoleId(user.getRoleId());
                user.setRole(authRole);

                if (user.getRole() == null || Constant.ROLE_ANONYMOUS.equals(user.getRole().getRoleName())) {
                    result = "用户未分组,无法登录";
                } else {
                    // 更新登录IP和登录时间
                    user.setLoginIp(loginIp);
                    user.setLoginTime(new Timestamp(System.currentTimeMillis()));
                    this.authUserDao.updateByPrimaryKey(user);
                    Identity identity = new Identity();

                    List<AuthAction> authActionList = this.authActionDao.selectByRoleId(authRole.getRoleId());
                    // 组装权限列表
                    if (authActionList != null && authActionList.size() > 0) {
                        List<AuthAction> authActionTop = new ArrayList<AuthAction>();
                        List<AuthAction> authActionMenu = new ArrayList<AuthAction>();
                        for (AuthAction authAction : authActionList) {
                            if(authAction!=null){
                                if(authAction.getOpLevel().equals(1)){
                                    authActionTop.add(authAction);
                                }else if (authAction.getOpLevel().equals(2)) {
                                    authActionMenu.add(authAction);
                                } else {
                                    addAction: for (int i = 0; i < authActionMenu.size(); i++) {
                                        if (authActionMenu.get(i).getOpId().equals(authAction.getParentId())) {
                                            authActionMenu.get(i).getAuthActionList().add(authAction);
                                            break addAction;
                                        }
                                    }
                                }
                            }
                        }
                        
                        for(AuthAction authAction :authActionMenu){
                            addAction: for (int i = 0; i < authActionTop.size(); i++) {
                                if (authActionTop.get(i).getOpId().equals(authAction.getParentId())) {
                                    authActionTop.get(i).getAuthActionList().add(authAction);
                                    break addAction;
                                }
                            }
                        }

                        identity.setAuthActionMenu(authActionTop);
                    }else{
                        result = "该用户未分配权限";
                    }
                    identity.setAuthActionList(authActionList);
                    identity.setLoginUser(user);
                    identity.setLoginIp(loginIp);
                    String sessionId = getSessionId(username, identity.getLoginIp());
                    identity.setSessionId(sessionId);
                    dataCache.setValue(username + loginIp, identity);
                    dataCache.setValue(sessionId, username);
                    dataCache.remove(key);
                    CookieUtil.set(Constant.SESSION_IDENTITY_KEY, sessionId, response);
                }
            }
        }
        return AppUtil.returnObj(result);
    }

    @Override
    @ServiceLog("退出")
    public AjaxResult logout(HttpServletResponse response, HttpServletRequest request) {
        String sessionId = CookieUtil.get(Constant.SESSION_IDENTITY_KEY, request);

        if (StringUtils.isNotEmpty(sessionId)) {
            String userName = (String) dataCache.getValue(sessionId);
            if (StringUtils.isNotEmpty(userName)) {
                dataCache.remove(userName + IPUtil.getIpAdd(request));
                //将财务密码从Redis缓存中删除
                String key = "FinancialMgr:"+userName;
                redisDao.delObject(key);
            }
            dataCache.remove(sessionId);
            CookieUtil.delete(Constant.SESSION_IDENTITY_KEY, request, response);
        }
        
        return AppUtil.returnObj(null);
    }

    private String getSessionId(String userName, String ip) {
        String str = userName + "_" + System.currentTimeMillis() + "_" + ip;
        try {
            return MD5Util.encrypt(str);
        } catch (Exception e) {
            return "生成token错误";
        }
    }
}
