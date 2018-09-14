package com.qyy.jyshop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shop_member_point_ext")
public class MemberPointExt implements Serializable {

	/**
	 * 描述
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 主键ID

	@Column(name = "mp_id")
	private Long mpId; // 会员积分ID

	@Column(name = "member_id")
	private Long memberId; // 用户Id

	@Column(name = "bill_no")
	private String billNo; // 提现单号

	private Integer style; // 提现方式 0-支付宝 1-微信 2-银行卡

	@Column(name = "open_bank")
	private String openBank; // 开户行

	@Column(name = "open_bank_city")
	private String openBankCity; // 开户行省市

	@Column(name = "branch_name")
	private String branchName; // 支行名称

	@Column(name = "account_name")
	private String accountName; // 银行卡-开户名 支付宝-真实姓名 微信-真实姓名

	@Column(name = "account_no")
	private String accountNo; // 银行卡-卡号 支付宝-帐号 微信-昵称 

	private BigDecimal amount;// 提现金额

	private Integer status; // 积分提现状态 0-待审核 1-待打款 2-已打款 3-已到账 4-已拒绝

	private String mark;// 审核备注

	@Column(name = "create_time")
	private Timestamp createTime; // 创建时间

	@Column(name = "audit_time")
	private Timestamp auditTime; // 审核时间

	@Column(name = "remit_time")
	private Timestamp remitTime; // 打款时间

	@Column(name = "account_time")
	private Timestamp accountTime; // 到账时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMpId() {
		return mpId;
	}

	public void setMpId(Long mpId) {
		this.mpId = mpId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Integer getStyle() {
		return style;
	}

	public void setStyle(Integer style) {
		this.style = style;
	}

	public String getOpenBank() {
		return openBank;
	}

	public void setOpenBank(String openBank) {
		this.openBank = openBank;
	}

	public String getOpenBankCity() {
		return openBankCity;
	}

	public void setOpenBankCity(String openBankCity) {
		this.openBankCity = openBankCity;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Timestamp auditTime) {
		this.auditTime = auditTime;
	}

	public Timestamp getRemitTime() {
		return remitTime;
	}

	public void setRemitTime(Timestamp remitTime) {
		this.remitTime = remitTime;
	}

	public Timestamp getAccountTime() {
		return accountTime;
	}

	public void setAccountTime(Timestamp accountTime) {
		this.accountTime = accountTime;
	}

}
