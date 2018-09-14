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
@Table(name = "shop_point_param")
public class PointParam implements Serializable {

	/**
	 * 描述
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 主键ID

	@Column(name = "red_point_scale")
	private BigDecimal redPointScale; // 红积分比例

	@Column(name = "yellow_point_scale")
	private BigDecimal yellowPointScale; // 黄积分比例

	@Column(name = "withdraw_deposit_min")
	private BigDecimal withdrawDepositMin; // 申请提现门槛

	@Column(name = "yellow_convert_begin")
	private Timestamp yellowConvertBegin; // 黄积分兑换开始时间

	@Column(name = "yellow_convert_end")
	private Timestamp yellowConvertEnd; // 黄积分兑换结束时间

	@Column(name = "cloud_point_scale")
	private BigDecimal cloudPointScale; // 云积分比例

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getRedPointScale() {
		return redPointScale;
	}

	public void setRedPointScale(BigDecimal redPointScale) {
		this.redPointScale = redPointScale;
	}

	public BigDecimal getYellowPointScale() {
		return yellowPointScale;
	}

	public void setYellowPointScale(BigDecimal yellowPointScale) {
		this.yellowPointScale = yellowPointScale;
	}

	public BigDecimal getWithdrawDepositMin() {
		return withdrawDepositMin;
	}

	public void setWithdrawDepositMin(BigDecimal withdrawDepositMin) {
		this.withdrawDepositMin = withdrawDepositMin;
	}

	public Timestamp getYellowConvertBegin() {
		return yellowConvertBegin;
	}

	public void setYellowConvertBegin(Timestamp yellowConvertBegin) {
		this.yellowConvertBegin = yellowConvertBegin;
	}

	public Timestamp getYellowConvertEnd() {
		return yellowConvertEnd;
	}

	public void setYellowConvertEnd(Timestamp yellowConvertEnd) {
		this.yellowConvertEnd = yellowConvertEnd;
	}

	public BigDecimal getCloudPointScale() {
		return cloudPointScale;
	}

	public void setCloudPointScale(BigDecimal cloudPointScale) {
		this.cloudPointScale = cloudPointScale;
	}

}
