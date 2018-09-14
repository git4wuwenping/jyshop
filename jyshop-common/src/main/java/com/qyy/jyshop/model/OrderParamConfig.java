package com.qyy.jyshop.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 订单配置信息
 */
@Entity
@Table(name = "shop_order_param_config")
public class OrderParamConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id; // id

	@Column(name = "hour_order_auto_close")
	private BigDecimal hourOrderAutoClose;// 在线支付未支付订单X小时后自动关闭

	@Column(name = "day_common_auto_confirm")
	private BigDecimal dayCommonAutoConfirm;// 普通商品，发货后X天后买家未操作，系统自动确认收货

	@Column(name = "day_oversea_auto_confirm")
	private BigDecimal dayOverseaAutoConfirm;// 海外商品X天后买家未操作，系统自动确认收货

	@Column(name = "day_legal_auto_close")
	private BigDecimal dayLegalAutoClose;// 已收货订单X天后不发起维权，则关闭维权功能

	@Column(name = "day_refund_auto_agree")
	private BigDecimal dayRefundAutoAgree;// 买家发起退款申请X天后未处理，系统将自动同意维权

	@Column(name = "day_back_auto_agree")
	private BigDecimal dayBackAutoAgree;// 买家发起退货退款申请X天后未处理，系统将自动同意维权

	@Column(name = "day_return_auto_agree")
	private BigDecimal dayReturnAutoAgree;// 买家维权退货X天后未处理，系统将自动同意退款

	@Column(name = "day_deal_legal_limit")
	private BigDecimal dayDealLegalLimit;// 卖家同意退货X天后买家未处理，系统自动关闭维权，不可再次发起

	@Column(name = "day_auto_comment")
	private BigDecimal dayAutoComment;// 已收货订单X天后自动评价，默认为好评，随机上传某条好评内容

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getHourOrderAutoClose() {
		return hourOrderAutoClose;
	}

	public void setHourOrderAutoClose(BigDecimal hourOrderAutoClose) {
		this.hourOrderAutoClose = hourOrderAutoClose;
	}

	public BigDecimal getDayCommonAutoConfirm() {
		return dayCommonAutoConfirm;
	}

	public void setDayCommonAutoConfirm(BigDecimal dayCommonAutoConfirm) {
		this.dayCommonAutoConfirm = dayCommonAutoConfirm;
	}

	public BigDecimal getDayOverseaAutoConfirm() {
		return dayOverseaAutoConfirm;
	}

	public void setDayOverseaAutoConfirm(BigDecimal dayOverseaAutoConfirm) {
		this.dayOverseaAutoConfirm = dayOverseaAutoConfirm;
	}

	public BigDecimal getDayLegalAutoClose() {
		return dayLegalAutoClose;
	}

	public void setDayLegalAutoClose(BigDecimal dayLegalAutoClose) {
		this.dayLegalAutoClose = dayLegalAutoClose;
	}

	public BigDecimal getDayRefundAutoAgree() {
		return dayRefundAutoAgree;
	}

	public void setDayRefundAutoAgree(BigDecimal dayRefundAutoAgree) {
		this.dayRefundAutoAgree = dayRefundAutoAgree;
	}

	public BigDecimal getDayBackAutoAgree() {
		return dayBackAutoAgree;
	}

	public void setDayBackAutoAgree(BigDecimal dayBackAutoAgree) {
		this.dayBackAutoAgree = dayBackAutoAgree;
	}

	public BigDecimal getDayReturnAutoAgree() {
		return dayReturnAutoAgree;
	}

	public void setDayReturnAutoAgree(BigDecimal dayReturnAutoAgree) {
		this.dayReturnAutoAgree = dayReturnAutoAgree;
	}

	public BigDecimal getDayDealLegalLimit() {
		return dayDealLegalLimit;
	}

	public void setDayDealLegalLimit(BigDecimal dayDealLegalLimit) {
		this.dayDealLegalLimit = dayDealLegalLimit;
	}

	public BigDecimal getDayAutoComment() {
		return dayAutoComment;
	}

	public void setDayAutoComment(BigDecimal dayAutoComment) {
		this.dayAutoComment = dayAutoComment;
	}

}
