package com.qyy.jyshop.vo;

import java.sql.Timestamp;

import com.qyy.jyshop.model.MemberPointExt;

public class MemberPointExtVo extends MemberPointExt {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5044706553448996696L;

	private Timestamp createTimeBegin;
	private Timestamp createTimeEnd;

	private Integer conditionType;
	private String conditionVal;
	private String memberNo;
	private String phone;

	public Timestamp getCreateTimeBegin() {
		return createTimeBegin;
	}

	public void setCreateTimeBegin(Timestamp createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public Timestamp getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Timestamp createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public Integer getConditionType() {
		return conditionType;
	}

	public void setConditionType(Integer conditionType) {
		this.conditionType = conditionType;
	}

	public String getConditionVal() {
		return conditionVal;
	}

	public void setConditionVal(String conditionVal) {
		this.conditionVal = conditionVal;
	}

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
