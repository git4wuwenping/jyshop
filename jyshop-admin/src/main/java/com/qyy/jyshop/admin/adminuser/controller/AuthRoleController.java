package com.qyy.jyshop.admin.adminuser.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.adminuser.service.AuthActionService;
import com.qyy.jyshop.admin.adminuser.service.AuthRoleService;
import com.qyy.jyshop.admin.adminuser.service.AuthUserService;
import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.model.AuthAction;
import com.qyy.jyshop.model.AuthRole;
import com.qyy.jyshop.model.AuthUser;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;
import com.qyy.jyshop.util.StringUtil;



@Controller
@RequestMapping("/admin/role")
public class AuthRoleController extends BaseController{

	@Autowired
	private AuthRoleService authRoleService;
	@Autowired
	private AuthUserService authUserService;
	@Autowired
	private AuthActionService authActionService;
	
	@Authority(opCode = "020102", opName = "角色管理界面")
	@RequestMapping("roleMain")
	public String roleMain() {
		return "adminuser/role/role_main";
	}

	@ControllerLog("查询角色列表")
	@RequestMapping("pageRoleAjax")
	@ResponseBody
	@Authority(opCode = "02010201", opName = "查询角色列表")
	public PageAjax<AuthRole> pageRoleAjax(PageAjax<AuthRole> page, AuthRole role) {
		return authRoleService.pageAuthRole(page, role);
	}
	
	@ControllerLog("添加角色页面")
	@Authority(opCode = "02010202", opName = "添加角色页面")
	@RequestMapping("preAddRole")
	public String preAddRole() {
		return "adminuser/role/add_role";
	}

	@ControllerLog("添加角色")
	@RequestMapping("addRole")
	@ResponseBody
	@Authority(opCode = "02010203", opName = "添加角色")
	public AjaxResult addRole(AuthRole authRole) {
		try{
			return AppUtil.returnObj(authRoleService.doAddRole(authRole));
		}catch (Exception e) {
			return AppUtil.returnObj(2,e.getMessage());
		}
	}

	@ControllerLog("更新角色页面")
	@Authority(opCode = "02010204", opName = "更新角色页面")
	@RequestMapping("preEditRole/{roleId}")
	public String preEditRole(@PathVariable("roleId") Integer roleId, Map<String, Object> map) {
		AuthRole role = authRoleService.queryAuthRoleByRoleId(roleId);
		map.put("role", role);
		return "adminuser/role/edit_role";
	}

	@ControllerLog("修改角色")
	@RequestMapping("editRole")
	@ResponseBody
	@Authority(opCode = "02010205", opName = "修改角色")
	public AjaxResult editRole(AuthRole role) {
		try{
			return AppUtil.returnObj(authRoleService.doEditRole(role));
		}catch (Exception e) {
			return AppUtil.returnObj(2,e.getMessage());
		}
	}

	@ControllerLog("删除角色")
	@RequestMapping("delRole/{roleId}")
	@ResponseBody
	@Authority(opCode = "02010206", opName = "删除角色")
	public AjaxResult delRole(@PathVariable("roleId") int roleId) {
		try{
			return AppUtil.returnObj(authRoleService.doDelRole(roleId));
		}catch (Exception e) {
			return AppUtil.returnObj(2,e.getMessage());
		}
	}
	
	@ControllerLog("角色用户管理页面")
	@Authority(opCode = "02010207", opName = "角色用户管理页面")
	@RequestMapping("preBindUser/{roleId}")
	public String preBindUser(@PathVariable("roleId")Integer roleId, Map<String, Object> map) {
		AuthRole role = authRoleService.queryAuthRoleByRoleId(roleId);
        map.put("role", role);

        List<AuthUser> users = authUserService.queryAuthUserByComId(null);
        List<AuthUser> hasUsers = authUserService.queryAuthUserByRoleId(roleId);
        List<String> usernames = new ArrayList<String>();
        for(AuthUser user: hasUsers){
        	usernames.add(user.getUsername());
        }
        for (AuthUser user : users) {
            if (usernames.contains(user.getUsername())) {
                user.setStatus(1);
            }
        }
        map.put("userList", users);
		return "adminuser/role/role_user";
	}
	
	@ControllerLog("角色绑定用户")
	@RequestMapping("bindUser")
	@ResponseBody
	@Authority(opCode = "02010208", opName = "角色绑定用户")
	public AjaxResult bindUser(int roleId, Integer[] ids) {
		try{
			return AppUtil.returnObj(authRoleService.doBindUser(roleId,ids));
		}catch (Exception e) {
			return AppUtil.returnObj(2,e.getMessage());
		}
	}
	
	@ControllerLog("角色权限管理页面")
	@Authority(opCode = "02010209", opName = "角色权限管理页面")
	@RequestMapping("preBindOperat/{roleId}")
	public String preBindOperat(@PathVariable("roleId")int roleId, Map<String, Object> map) {
		
	    List<AuthRole> roleList = authRoleService.queryAuthRoleByComId(null);
		map.put("roleList", roleList);
		
		// 当前登录的用户
		AuthUser loginUser = (AuthUser) BaseController.getRequest().getAttribute("loginUser");
		// 当前用户拥有的权限
		List<AuthAction> authActionList = this.authActionService.queryAuthActionByRoleId(loginUser.getRoleId());
		map.put("authActionList", authActionList);
		
		//角色拥有的权限
		List<AuthAction> hasActionList = null;
		AuthRole role = authRoleService.queryAuthRoleByRoleId(roleId);
		if (role != null) {
			hasActionList = this.authActionService.queryAuthActionByRoleId(role.getRoleId());
		}

		// 角色没有的权限
		List<AuthAction> noActionList = new ArrayList<AuthAction>();
		for (AuthAction authAction : authActionList) {
			if (!hasActionList.contains(authAction)) {
				noActionList.add(authAction);
			}
		}
		map.put("hasCount", hasActionList.size());
		map.put("noCount", noActionList.size());
		map.put("roleId", roleId);
		
		return "adminuser/role/role_action";
	}

	@ControllerLog("角色已绑定权限")
	@Authority(opCode = "02010210", opName = "角色已绑定权限")
    @RequestMapping("hasOperat/{roleId}")
    public String hasOperat(Map<String, Object> map, @PathVariable("roleId") int roleId, @RequestParam(defaultValue = "") String opName) {
		List<AuthAction> hasActionList = new ArrayList<AuthAction>();
		AuthRole role = authRoleService.queryAuthRoleByRoleId(roleId);
		if (role != null) {
			//选择角色拥有的权限
			List<AuthAction> authActionList = this.authActionService.queryAuthActionByRoleId(role.getRoleId());
			if (authActionList != null) {
				// 筛选条件
				if (!StringUtil.isEmpty(opName)) {
					for (AuthAction authAction : authActionList) {
						if (authAction.getOpName().contains(opName) || authAction.getOpCode().contains(opName)) {
							hasActionList.add(authAction);
						}
					}
				} else {
					hasActionList = authActionList;
				}
			}
		}
		//排序
		Collections.sort(hasActionList, new Comparator<AuthAction>() {
			@Override
			public int compare(AuthAction o1, AuthAction o2) {
				return o1.getOpCode().compareTo(o2.getOpCode());
			}
		});
		map.put("hasActionList", hasActionList);
        map.put("roleId", roleId);
        map.put("opName", opName);
        map.put("hasActionCount", hasActionList==null?0:hasActionList.size());
        return "adminuser/role/action_has";
    }
	
	@ControllerLog("绑定角色权限")
	@RequestMapping("bindUserOperat")
	@ResponseBody
	@Authority(opCode = "02010213", opName = "绑定角色权限")
	public AjaxResult bindUserOperat(int roleId, Integer[] opIds) {
		try{
			return AppUtil.returnObj(authRoleService.doBindOperat(roleId, opIds));
		}catch (Exception e) {
			return AppUtil.returnObj(2,e.getMessage());
		}
	}
	
	@ControllerLog("角色未绑定权限")
	@Authority(opCode = "02010212", opName = "角色未绑定权限")
	@RequestMapping("noOperat/{roleId}")
	public String noOperat(Map<String, Object> map, @PathVariable("roleId") int roleId, @RequestParam(defaultValue = "") String opName) {

		// 当前登录的用户
		AuthUser loginUser = (AuthUser) BaseController.getRequest().getAttribute("loginUser");
		// 当前用户拥有的权限
		List<AuthAction> authActionList = this.authActionService.queryAuthActionByRoleId(loginUser.getRoleId());
		map.put("authActionList", authActionList);
		
		//选择角色拥有的权限
		AuthRole role = authRoleService.queryAuthRoleByRoleId(roleId);
		List<String> operatCode = new ArrayList<String>();
		if (role != null) {
			List<AuthAction> hasActionList = this.authActionService.queryAuthActionByRoleId(role.getRoleId());
			for(AuthAction hasAction : hasActionList){
				operatCode.add(hasAction.getOpCode());
			
			}
		}
		
		List<AuthAction> noActionList = new ArrayList<AuthAction>();
		//剔除角色拥有的权限
		for (AuthAction authAction : authActionList) {
		    
			if (authAction!=null) {
			    if(!operatCode.contains(authAction.getOpCode())){
    				// 搜索条件
    				if (StringUtils.isNotEmpty(opName)) {
    					if (authAction.getOpName().contains(opName) || authAction.getOpCode().contains(opName)) {
    						noActionList.add(authAction);
    					}
    				} else {
    					noActionList.add(authAction);
    				}
			    }
			}else{
			    noActionList.add(authAction);
			}
		}
		//排序
		Collections.sort(noActionList, new Comparator<AuthAction>() {
			@Override
			public int compare(AuthAction o1, AuthAction o2) {
			    if(o1!=null && o2!=null){
			        System.out.println(o1.getOpCode().compareTo(o2.getOpCode()));
			        return o1.getOpCode().compareTo(o2.getOpCode());
			    }else
			        return 0;
			}
		});
		map.put("noActionList", noActionList);
        map.put("roleId", roleId);
        map.put("opName", opName);
        map.put("noActionCount", noActionList==null?0:noActionList.size());
		return "adminuser/role/action_no";
	}
	
	@ControllerLog("解除角色权限")
	@RequestMapping("unBindUserOperat")
	@ResponseBody
	@Authority(opCode = "02010206", opName = "解除角色权限")
	public AjaxResult unBindUserOperat(int roleId, Integer[] opIds) {
		try{
			return AppUtil.returnObj(authRoleService.doUnBindOperat(roleId, opIds));
		}catch (Exception e) {
			return AppUtil.returnObj(2,e.getMessage());
		}
	}

}
