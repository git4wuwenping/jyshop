package com.qyy.jyshop.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 系统日志
 * 
 */
@Entity
@Table(name="sys_log")
public class Log implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_id")
	private Long logId; //日志Id
	
	private String username; //操作用户
	
	private Integer type; //操作类型(0操作日志;1异常日志)
	
	private String url; //请求地址
	
	private String method; //执行方法
	
	@Column(name = "request_params")
	private String requestParams; //请求参数
	
	@Column(name = "request_ip")
	private String requestIp; //请求IP
	
	private String description; //操作描述
	
	private String detail; //异常详情
	
	@Column(name = "operat_date")
	private Timestamp operatDate; //操作日期

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Timestamp getOperatDate() {
		return operatDate;
	}

	public void setOperatDate(Timestamp operatDate) {
		this.operatDate = operatDate;
	}
}
