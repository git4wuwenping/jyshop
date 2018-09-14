package com.qyy.jyshop.util.excel.dto;

import java.sql.Timestamp;

import com.qyy.jyshop.util.excel.ExcelResources;

public class ChatForExcel {
	
	private String sender;			//发送人
	private String message;			//发送内容
	private Timestamp createTime;
	private String recipient;		//接收人
	private String sendType;		//发送类型
	private String checkStatus;		//查看状态
	
	@ExcelResources(title = "发送人", order = 1, isCombine = true, isCombineCondition = true)
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	@ExcelResources(title = "发送内容", order = 2, isCombine = true)
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@ExcelResources(title = "创建时间", order = 3, isCombine = true)
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@ExcelResources(title = "接收人", order = 4, isCombine = true)
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	@ExcelResources(title = "发送类型", order = 5, isCombine = true)
	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	@ExcelResources(title = "查看状态", order = 6, isCombine = true)
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	
	
}
