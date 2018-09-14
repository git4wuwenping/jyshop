package com.qyy.jyshop.seller.adminuser.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.seller.adminuser.service.AuthRoleService;
import com.qyy.jyshop.seller.adminuser.service.AuthUserService;
import com.qyy.jyshop.seller.common.annotation.Authority;
import com.qyy.jyshop.seller.common.annotation.ControllerLog;
import com.qyy.jyshop.seller.common.controller.BaseController;
import com.qyy.jyshop.model.AuthRole;
import com.qyy.jyshop.model.AuthUser;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;


@Controller
@RequestMapping("/admin/user")
public class AuthUserController extends BaseController{

	@Autowired
	private AuthRoleService authRoleService;
	@Autowired
	private AuthUserService authUserService;
	
	@Authority(opCode = "020101", opName = "用户管理界面")
	@RequestMapping("userMain")
	public String userMain(Map<String, Object> map) {
	    
		List<AuthRole> roleList = authRoleService.queryAuthRoleByComId(null);
		map.put("roleList", roleList);
		return "adminuser/user/user_main";
	}
	
	@ControllerLog("查询用户列表")
	@RequestMapping("pageUserAjax")
	@ResponseBody
	@Authority(opCode = "02010101", opName = "查询用户列表")
	public PageAjax<AuthUser> pageUserAjax(PageAjax<AuthUser> page, AuthUser user) {
		return authUserService.pageAuthUser(page, user);
	}
	
	@ControllerLog("添加用户页面")
	@RequestMapping("preAddUser")
	@Authority(opCode = "02010102", opName = "添加用户页面")
	public String preAddUser(Map<String, Object> map) {
		List<AuthRole> roleList = authRoleService.queryAuthRoleByComId(null);
		map.put("roleList", roleList);
		return "adminuser/user/add_user";
	}

	@ControllerLog("添加用户")
	@RequestMapping("addUser")
	@ResponseBody
	@Authority(opCode = "02010103", opName = "添加用户")
	public AjaxResult addUser(AuthUser user) {
		try{
			return AppUtil.returnObj(authUserService.doAddAuthUser(user));
		}catch (Exception e) {
			return AppUtil.returnObj(2,e.getMessage());
		}
	}
	
	@ControllerLog("更新用户页面")
	@RequestMapping("preEditUser/{id}")
	@Authority(opCode = "02010104", opName = "更新用户页面")
	public String preEditUser(@PathVariable("id") int id,Map<String, Object> map) {
		List<AuthRole> roleList = authRoleService.queryAuthRoleByComId(null);
		map.put("roleList", roleList);
		map.put("user", this.authUserService.queryAuthUserById(id));
		return "adminuser/user/edit_user";
	}
	
	@ControllerLog("修改用户")
	@RequestMapping("editUser")
	@ResponseBody
	@Authority(opCode = "02010105", opName = "修改用户")
	public AjaxResult editUser(AuthUser user) {
		try{
			return AppUtil.returnObj(authUserService.doEditUser(user));
		}catch (Exception e) {
			return AppUtil.returnObj(2,e.getMessage());
		}
	}
	
	@ControllerLog("修改个人密码页面")
	@RequestMapping("preEditPassword/{id}")
	@Authority(opCode = "02010110", opName = "修改个人密码页面")
	public String preEditPassword(@PathVariable("id") int id,Map<String, Object> map) {
		map.put("id", id);
		return "adminuser/user/edit_password";
	}
	
	@ControllerLog("修改个人密码")
	@RequestMapping("editPassword")
	@ResponseBody
	@Authority(opCode = "02010109", opName = "修改个人密码")
	public AjaxResult editPassword(HttpServletResponse response, HttpServletRequest request, int id, String oldPwd, String newPwd) {
		try{
			return AppUtil.returnObj(authUserService.doEditPassword(response, request, id, oldPwd, newPwd));
		}catch (Exception e) {
			return AppUtil.returnObj(2,e.getMessage());
		}
	}
	
	@ControllerLog("重置用户密码")
	@RequestMapping("editResetPwd/{id}")
	@ResponseBody
	@Authority(opCode = "02010108", opName = "重置用户密码")
	public AjaxResult editResetPwd(@PathVariable("id") int id) {
		try{
			return AppUtil.returnObj(authUserService.doResetPwd(id));
		}catch (Exception e) {
			return AppUtil.returnObj(2,e.getMessage());
		}
	}
	
	@ControllerLog("删除用户")
	@RequestMapping("delUser/{id}")
	@ResponseBody
	@Authority(opCode = "02010107", opName = "删除用户")
	public AjaxResult delUser(@PathVariable("id") int id) {
		try{
			return AppUtil.returnObj(authUserService.doDelUser(id));
		}catch (Exception e) {
			return AppUtil.returnObj(2,e.getMessage());
		}
	}
	
}
