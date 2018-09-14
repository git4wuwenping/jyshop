package com.qyy.jyshop.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shop_member_withdraw_info")
public class MemberWithdrawInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 主键ID

	@Column(name = "member_id")
	private Long memberId; // 用户Id

	@Column(name = "open_bank")
	private String openBank; // 开户行

	@Column(name = "open_bank_city")
	private String openBankCity; // 开户行省市

	@Column(name = "branch_name")
	private String branchName; // 支行名称

	@Column(name = "account_name")
	private String accountName; //  银行卡-开户名 支付宝-真实姓名 微信-真实姓名

	@Column(name = "account_no")
	private String accountNo; //  银行卡-卡号 支付宝-帐号 微信-昵称 

	@Column(name = "draw_type")
	private Integer drawType; // 提现方式 0-支付宝 1-微信 2-银行卡

	@Column(name = "is_binding")
	private Integer isBinding; // 是否绑定 0-是 1-否

	@Column(name = "create_time")
	private Timestamp createTime; // 创建时间

	@Column(name = "is_default")
	private short isDefault; // 是否默认
	
	@Column(name = "card_type")
	private String cardType; // 卡类型
	
	@Column(name = "abbreviation")
	private String abbreviation; // 银行缩写
	
	@Column(name = "bank_image")
	private String bankImage; // 银行缩写
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
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

	public Integer getDrawType() {
		return drawType;
	}

	public void setDrawType(Integer drawType) {
		this.drawType = drawType;
	}

	public Integer getIsBinding() {
		return isBinding;
	}

	public void setIsBinding(Integer isBinding) {
		this.isBinding = isBinding;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public short getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(short isDefault) {
		this.isDefault = isDefault;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getBankImage() {
		return bankImage;
	}

	public void setBankImage(String bankImage) {
		this.bankImage = bankImage;
	}


}
