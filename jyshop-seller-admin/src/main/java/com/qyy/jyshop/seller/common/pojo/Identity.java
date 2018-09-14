package com.qyy.jyshop.seller.common.pojo;

import java.util.List;

import com.qyy.jyshop.model.AuthAction;
import com.qyy.jyshop.model.AuthUser;


/**
 * 封装Session
 */
public class Identity {
	
	private String sessionId;
	private String loginIp;
	private AuthUser loginUser;
	private List<AuthAction> authActionList;
	private List<AuthAction> authActionMenu;
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public AuthUser getLoginUser() {
		return loginUser;
	}
	public void setLoginUser(AuthUser loginUser) {
		this.loginUser = loginUser;
	}
	public List<AuthAction> getAuthActionList() {
		return authActionList;
	}
	public void setAuthActionList(List<AuthAction> authActionList) {
		this.authActionList = authActionList;
	}

    public List<AuthAction> getAuthActionMenu() {
		return authActionMenu;
	}
	public void setAuthActionMenu(List<AuthAction> authActionMenu) {
		this.authActionMenu = authActionMenu;
	}
	
}
