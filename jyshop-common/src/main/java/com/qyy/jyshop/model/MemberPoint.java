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
@Table(name = "shop_member_point")
public class MemberPoint implements Serializable {

	/**
	 * 描述
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 主键ID

	@Column(name = "order_id")
	private Long orderId; // 订单ID

	@Column(name = "order_sn")
	private String orderSn; // 订单号

	@Column(name = "order_type")
	private Integer orderType; // 订单类型 0-普通订单 1-礼包订单

	private Integer type; // 类型 0:获取 1:消费 2:提现
	private BigDecimal point; // 积分

	@Column(name = "point_type")
	private Integer pointType; // 积分类型 0:云积分 1:黄积分 2:红积分

	@Column(name = "member_id")
	private Long memberId; // 用户Id

	@Column(name = "get_type")
	private Integer getType; //获取类型：0-本人首购，1-本人次购，2-下级购买，3-白积分转换 4-旧商城迁移数据

	@Column(name = "point_status")
	private Integer pointStatus; // 积分消费状态：0-待确认，1-已确认 | 积分提现状态 0-待审核 1-待打款
									// 2-已打款 3-已到账 4-已拒绝

	@Column(name = "create_time")
	private Timestamp createTime; // 创建时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getPoint() {
		return point;
	}

	public void setPoint(BigDecimal point) {
		this.point = point;
	}

	public Integer getPointType() {
		return pointType;
	}

	public void setPointType(Integer pointType) {
		this.pointType = pointType;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getGetType() {
		return getType;
	}

	public void setGetType(Integer getType) {
		this.getType = getType;
	}

	public Integer getPointStatus() {
		return pointStatus;
	}

	public void setPointStatus(Integer pointStatus) {
		this.pointStatus = pointStatus;
	}

}
