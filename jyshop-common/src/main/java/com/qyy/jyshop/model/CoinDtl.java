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
@Table(name = "shop_coin_dtl")
public class CoinDtl implements Serializable {

	/**
	 * 描述
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 主键id

	@Column(name = "member_id")
	private Long memberId; // 会员ID

	@Column(name = "coin_type")
	private Short coinType; // 类型 0-邀请好友、1-关注公众号、2-注册、3-兑换、4-抽奖

	@Column(name = "coin_num")
	private Integer coinNum;// 数量

	@Column(name = "create_time")
	private Timestamp createTime;// 创建时间

	@Column(name = "order_id")
	private Long orderId; // 关联订单id

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

	public Short getCoinType() {
		return coinType;
	}

	public void setCoinType(Short coinType) {
		this.coinType = coinType;
	}

	public Integer getCoinNum() {
		return coinNum;
	}

	public void setCoinNum(Integer coinNum) {
		this.coinNum = coinNum;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

}
