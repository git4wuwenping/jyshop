package com.qyy.jyshop.admin.adminuser.service.impl;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.admin.adminuser.service.AuthUserService;
import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.common.supper.DataCache;
import com.qyy.jyshop.conf.Constant;
import com.qyy.jyshop.dao.AuthUserDao;
import com.qyy.jyshop.model.AuthUser;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.CookieUtil;
import com.qyy.jyshop.util.IPUtil;
import com.qyy.jyshop.util.MD5Util;
import com.qyy.jyshop.util.StringUtil;


/**
 * 管理员
 */
@Service
public class AuthUserServiceImpl extends AbstratService<AuthUser> implements AuthUserService{

	@Autowired
	private AuthUserDao authUserDao;
	@Autowired
	private DataCache dataCache;
	
	@Override
	@ServiceLog("根据用户Id获取用户信息")
	public AuthUser queryAuthUserById(Integer id){
		return this.queryByID(id);
	}
	
	@Override
	@ServiceLog("根据供应商Id获取用户信息")
	public List<AuthUser> queryAuthUserByComId(Integer comId){
	    if(StringUtil.isEmpty(comId))
	        comId=this.getLoginUser().getComId();
		return this.authUserDao.selectByComId(comId);
	}
	
	@Override
	@ServiceLog("根据角色Id获取用户")
	public List<AuthUser> queryAuthUserByRoleId(Integer roleId){
	    AuthUser authUser=new AuthUser();
	    authUser.setRoleId(roleId);
	    authUser.setComId(this.getLoginUser().getComId());
		return this.authUserDao.select(authUser);
	}
	
	@Override
	@ServiceLog("获取管理员列表(分页_ajax)")
	public PageAjax<AuthUser> pageAuthUser(PageAjax<AuthUser> page,
			AuthUser authUser) {
	    AuthUser loginUser = this.getLoginUser();
	    authUser.setComId(loginUser.getComId());
		return this.queryPage(page, authUser);
	}
	
	@Override
	@Transactional
	@ServiceLog("添加管理员")
	public String doAddAuthUser(AuthUser authUser)throws Exception{
		if(authUser==null)
			return "获取注册信息失败";	
		if(StringUtil.isEmpty(authUser.getName()))
			return "用户名不能为空";
		if(StringUtil.isEmpty(authUser.getUsername()))
			return "账号不能为空";
		if(StringUtil.isEmpty(authUser.getPassword()))
			return "密码不能为空";
		authUser.setPassword(MD5Util.encrypt(authUser.getPassword()));
		authUser.setAddTime(new Timestamp(System.currentTimeMillis()));
		authUser.setComId(this.getLoginUser().getComId());
		synchronized (this) {
			AuthUser user=this.authUserDao.selectByUsername(authUser.getUsername());
			if(user!=null)
				return "用户名己存在,请换一个用户注册...";
			this.authUserDao.insert(authUser);
		}
		return null;
	}
	
	@Override
	@Transactional
	@ServiceLog("修改管理员")
	public String doEditUser(AuthUser authUser){
		if(authUser==null)
			return "获取修改信息失败";
		if(authUser==null)
			return "获取注册信息失败";	
		if(StringUtil.isEmpty(authUser.getName()))
			return "用户名不能为空";
		if(StringUtil.isEmpty(authUser.getUsername()))
			return "账号不能为空";
		if(StringUtil.isEmpty(authUser.getPassword()))
			return "密码不能为空";
		AuthUser oldAuthUser=this.authUserDao.selectByPrimaryKey(authUser.getId());
		if(oldAuthUser==null)
		    return "获取用户信息失败";
		
		if(oldAuthUser.getComId().equals(this.getLoginUser().getComId())){
		    this.authUserDao.updateByPrimaryKeySelective(authUser);
		}else{
		    return "你没有权限修改该用户...";
		}
		return null;
	}
	
	@Override
	@Transactional
	@ServiceLog("修改密码")
	public String doEditPassword(HttpServletResponse response, HttpServletRequest request, Integer id, String oldPwd, String newPwd)throws Exception{
		
		if(StringUtil.isEmpty(newPwd))
			return "新密码不能为空...";
		if(!this.getLoginUser().getId().equals(id))
		    return "与当前登陆用户不匹配,修改密码失败...";
		
		if(!StringUtil.isEmpty(oldPwd)){
			AuthUser user = this.queryByID(id);
			if(!MD5Util.encrypt(oldPwd).equals(user.getPassword())){
				return "旧密码不正确";
			}
			//修改密码
			user.setPassword(MD5Util.encrypt(newPwd));
			this.update(user);
			String sessionId = CookieUtil.get(Constant.SESSION_IDENTITY_KEY, request);
			if (StringUtils.isNotEmpty(sessionId)) {
				dataCache.remove(sessionId);
				String userName = (String) dataCache.getValue(sessionId);
				if (StringUtils.isNotEmpty(userName)) {
					dataCache.remove(userName + IPUtil.getIpAdd(request));
				}
				CookieUtil.delete(Constant.SESSION_IDENTITY_KEY, request, response);
			}
		}else{
			return "旧密码不能为空...";
		}

		return null;
	}
	
	@Override
	@Transactional
	@ServiceLog("重置默认密码")
	public String doResetPwd(Integer id)throws Exception{
		if(StringUtil.isEmpty(id))
			return "获取用户ID失败";
		AuthUser user=this.queryByID(id);
		if(user==null)
			return "获取用户失败,重置密码失败...";
		if(user.getComId().equals(this.getLoginUser().getComId())){
		    user.setPassword(MD5Util.encrypt("123456"));
		    this.updateByID(user);
		}else{
		    return "你没有权限重置该用户密码...";
		}
		return null;
	}
	
	@Override
	@Transactional
	@ServiceLog("删除用户")
	public String doDelUser(Integer id){
		if(StringUtil.isEmpty(id))
			return "获取用户ID失败";
		AuthUser oldAuthUser=this.authUserDao.selectByPrimaryKey(id);
        if(oldAuthUser==null)
            return "获取用户信息失败";
        
        if(oldAuthUser.getComId().equals(this.getLoginUser().getComId())){
            this.authUserDao.deleteByPrimaryKey(id);
        }else{
            return "你没有权限删除该用户...";
        }
		return null;
	}
}
