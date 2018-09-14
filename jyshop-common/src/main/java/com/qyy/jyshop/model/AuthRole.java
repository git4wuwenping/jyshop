package com.qyy.jyshop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 角色信息
 *
 */
@Entity
@Table(name = "sys_auth_role")
public class AuthRole implements Serializable{
	
	/**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Integer roleId;

	@Column(name = "role_name")
	private String roleName; //角色名称

	private String cname; //中文名
	
	
	@Column(name = "com_id")
    private Integer comId; //供应商Id
	
	/**
	 * 角色权限
	 */
	@Transient
	private List<AuthAction> authActionList = new ArrayList<AuthAction>();

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public List<AuthAction> getAuthActionList() {
		return authActionList;
	}

	public void setAuthActionList(List<AuthAction> authActionList) {
		this.authActionList = authActionList;
	}
	
}