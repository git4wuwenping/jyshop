package com.qyy.jyshop.admin.adminuser.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.admin.adminuser.service.AuthRoleService;
import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.dao.AuthRoleActionDao;
import com.qyy.jyshop.dao.AuthRoleDao;
import com.qyy.jyshop.dao.AuthUserDao;
import com.qyy.jyshop.model.AuthRole;
import com.qyy.jyshop.model.AuthRoleAction;
import com.qyy.jyshop.model.AuthUser;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.StringUtil;



/**
 * 角色
 */
@Service
public class AuthRoleServiceImpl extends AbstratService<AuthRole> implements AuthRoleService{

	@Autowired
	private AuthRoleDao authRoleDao;
	@Autowired
	private AuthRoleActionDao authRoleActionDao;
	@Autowired
	private AuthUserDao authUserDao;
	

	@Override
	@ServiceLog("根据角色Id获取角色信息")
	public AuthRole queryAuthRoleByRoleId(Integer roleId){
		return this.authRoleDao.selectByRoleId(roleId);
	}
	
	
	@Override
	@ServiceLog("获取所有的角色")
	public List<AuthRole> queryAuthRoleByComId(Integer comId) {
	    if(StringUtil.isEmpty(comId))
	        comId=this.getLoginUser().getComId();
		return authRoleDao.selectByComId(comId);
	}

	@Override
	@ServiceLog("获取角色列表(分页_ajax)")
	public PageAjax<AuthRole> pageAuthRole(PageAjax<AuthRole> page,
			AuthRole authRole) {
	    authRole.setComId(this.getLoginUser().getComId());
		return this.queryPage(page, authRole);
	}
	
	@Override
	@Transactional
	@ServiceLog("添加角色")
	public String doAddRole(AuthRole authRole){
		if(authRole==null)
			return "添加的角色数据不能为空...";
		if(StringUtil.isEmpty(authRole.getRoleName()))
			return "角色名称不能为空 ...";
		if(StringUtil.isEmpty(authRole.getCname()))
            return "中文名称不能为空 ...";
		
		synchronized (this) {
			AuthRole role=this.authRoleDao.selectByRoleName(authRole.getRoleName());
			if(role!=null)
				return "角色名己存在,请换一个角色名称添加...";
			authRole.setComId(this.getLoginUser().getComId());
			this.authRoleDao.insert(authRole);
		}
		return null;
	}
	
	@Override
	@Transactional
	@ServiceLog("修改角色")
	public String doEditRole(AuthRole authRole){
		if(authRole==null)
			return "修改的角色数据不能为空...";
		if(StringUtil.isEmpty(authRole.getRoleName()))
			return "角色名称不能为空 ...";
		if(StringUtil.isEmpty(authRole.getCname()))
            return "中文名称不能为空 ...";
		
		AuthRole oldRole=this.authRoleDao.selectByPrimaryKey(authRole.getRoleId());
		if(oldRole==null)
		    return "获取角色信息失败 ...";
		
    	synchronized (this) {
    	    
    	    AuthRole role=this.authRoleDao.selectByRoleName(authRole.getRoleName());
    		if(role!=null && !role.getRoleId().equals(authRole.getRoleId()))
    			return "角色名己存在,请换一个角色名称修改...";
    		if(!oldRole.getComId().equals(this.getLoginUser().getComId()))
    		    return "您没有权限修改该角色信息...";
    		
    		oldRole.setRoleName(authRole.getRoleName());
    		oldRole.setCname(authRole.getCname());
    		this.authRoleDao.updateByPrimaryKeySelective(oldRole);
    	}
		return null;
	}
	
	@Override
	@Transactional
	@ServiceLog("删除角色")
	public String doDelRole(Integer roleId){
		if(StringUtil.isEmpty(roleId))
			return "角色Id不能为空 ...";
		if(roleId.equals(99))
		    return "超级管理员角色不能删除...";
		AuthRole oldRole=this.authRoleDao.selectByPrimaryKey(roleId);
		if(oldRole==null)
            return "获取角色信息失败 ...";
		if(!oldRole.getComId().equals(this.getLoginUser().getComId()))
            return "您没有权限删除该角色信息...";
		
		//删除角色
		this.authRoleDao.deleteByPrimaryKey(roleId);
		//删限关联权限
		this.authRoleActionDao.deleteByRoleId(roleId);
		return null;
	}

	@Override
	@Transactional
	@ServiceLog("用户绑定角色")
	public String doBindUser(int roleId, Integer[] ids) {
		
		//获取该角色原有的用户
		List<AuthUser> authUserList=this.authUserDao.selectByRoleId(roleId);
		//设置为没权限
		if(authUserList!=null && authUserList.size()>0){
			//剔除的用户
			List<Map<String, Integer>> userList = new ArrayList<Map<String, Integer>>();
			Map<String, Integer> user = null;
			//该角色原有的用户
			for(int i=0;i<authUserList.size();i++){
				user = new HashMap<String, Integer>();
				user.put("id", authUserList.get(i).getId());
				user.put("roleId", 0);
				userList.add(user);
			}
			dao.batchUpdate("AuthUserDao.bindUserRole", userList);
		}
		
		//为用户添加角色
		if(ids!=null && ids.length>0){ 
		    //要添加角色的用户
            List<Map<String, Integer>> userList = new ArrayList<Map<String, Integer>>();
            Map<String, Integer> user = null;
            for(int i=0;i<ids.length;i++){
                user = new HashMap<String, Integer>();
                user.put("id", ids[i]);
                user.put("roleId", roleId);
                userList.add(user);
            }
            dao.batchUpdate("AuthUserDao.bindUserRole", userList);
        }
		return null;
	}
	
	@Override
	@Transactional
	@ServiceLog("角色绑定权限")
	public String doBindOperat(int roleId, Integer[] opIds) {
		if(99==roleId)
		    return "不能操作超级管理员权限...";
	    try{
    		if(opIds!=null && opIds.length>0){
    			
    			List<AuthRoleAction> authRoleActionList = new ArrayList<AuthRoleAction>();
    			AuthRoleAction authRoleAction = null;
    			for(int opId: opIds){
    				authRoleAction = new AuthRoleAction();
    				authRoleAction.setRoleId(roleId);
    				authRoleAction.setOpId(opId);
    				authRoleActionList.add(authRoleAction);
    			}
    			authRoleActionDao.batchInsert(authRoleActionList);
    			return null;
    		}
	    }catch(Exception e){
	        e.printStackTrace();
	        return e.getMessage();
	    }
		return "请选择要添加的权限";
	}
	
	@Override
	@Transactional
	@ServiceLog("角色解除权限")
	public String doUnBindOperat(int roleId, Integer[] opIds) {
	    if(99==roleId)
            return "不能操作超级管理员权限...";
	    try{
    		if(opIds!=null && opIds.length>0){
    			List<AuthRoleAction> authRoleActionList = new ArrayList<AuthRoleAction>();
    			AuthRoleAction authRoleAction = null;
    			for(int opId: opIds){
    				authRoleAction = new AuthRoleAction();
    				authRoleAction.setRoleId(roleId);
    				authRoleAction.setOpId(opId);
    				authRoleActionList.add(authRoleAction);
    			}
    			authRoleActionDao.batchDel(authRoleActionList);
    			return null;
    		}
		}catch(Exception e){
            e.printStackTrace();
            return e.getMessage();   
        }
        return "请选择要添加的权限";
	}

}
