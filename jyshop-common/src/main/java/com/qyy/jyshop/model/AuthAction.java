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
 * 权限信息
 */
@Entity
@Table(name="sys_auth_action")
public class AuthAction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "op_id")
	private Integer opId;//主键ID
	@Column(name = "op_code")
	private String opCode;//权限值
	@Column(name = "op_name")
	private String opName;//权限名称
	@Column(name = "op_href")
	private String opHref;//权限操作链接
	@Column(name = "op_seq")
	private Integer opSeq;//显示顺序
	@Column(name = "op_level")
	private Integer opLevel;//显示顺序
	@Column(name = "parent_id")
	private Integer parentId; //父级Id
	private String icon; //权限图标
	
	@Transient
	private List<AuthAction> authActionList; 
	
	public Integer getOpId() {
		return opId;
	}
	public void setOpId(Integer opId) {
		this.opId = opId;
	}
	public String getOpCode() {
		return opCode;
	}
	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}
	public String getOpName() {
		return opName;
	}
	public void setOpName(String opName) {
		this.opName = opName;
	}
	public String getOpHref() {
		return opHref;
	}
	public void setOpHref(String opHref) {
		this.opHref = opHref;
	}
	public Integer getOpSeq() {
		return opSeq;
	}
	public void setOpSeq(Integer opSeq) {
		this.opSeq = opSeq;
	}
	public Integer getOpLevel() {
		return opLevel;
	}
	public void setOpLevel(Integer opLevel) {
		this.opLevel = opLevel;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public List<AuthAction> getAuthActionList() {
		if(authActionList==null)
			authActionList=new ArrayList<AuthAction>();
		return authActionList;
	}
	public void setAuthActionList(List<AuthAction> authActionList) {
		this.authActionList = authActionList;
	}
	
	
}
