package com.qyy.jyshop.model;

import java.io.Serializable;

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
@Table(name = "shop_coin_param_config")
public class CoinParamConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id; // id

	@Column(name = "coin_worth")
	private Integer coinWorth;// 金币价值，多少金币抵1人民币

	@Column(name = "coin_signin_get")
	private Integer coinSigninGet;// 签到一次所得金币数量

	@Column(name = "coin_register_get")
	private Integer coinRegisterGet;// 新人注册所得金币数量

	@Column(name = "coin_invite_get")
	private Integer coinInviteGet;// 邀请好友所得金币数量

	@Column(name = "coin_subscribe_get")
	private Integer coinSubscribeGet;// 关注公众号所得金币数量

	@Column(name = "coin_lottery_cost")
	private Integer coinLotteryCost;// 单次抽奖消耗金币数量

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCoinWorth() {
		return coinWorth;
	}

	public void setCoinWorth(Integer coinWorth) {
		this.coinWorth = coinWorth;
	}

	public Integer getCoinSigninGet() {
		return coinSigninGet;
	}

	public void setCoinSigninGet(Integer coinSigninGet) {
		this.coinSigninGet = coinSigninGet;
	}

	public Integer getCoinRegisterGet() {
		return coinRegisterGet;
	}

	public void setCoinRegisterGet(Integer coinRegisterGet) {
		this.coinRegisterGet = coinRegisterGet;
	}

	public Integer getCoinInviteGet() {
		return coinInviteGet;
	}

	public void setCoinInviteGet(Integer coinInviteGet) {
		this.coinInviteGet = coinInviteGet;
	}

	public Integer getCoinSubscribeGet() {
		return coinSubscribeGet;
	}

	public void setCoinSubscribeGet(Integer coinSubscribeGet) {
		this.coinSubscribeGet = coinSubscribeGet;
	}

	public Integer getCoinLotteryCost() {
		return coinLotteryCost;
	}

	public void setCoinLotteryCost(Integer coinLotteryCost) {
		this.coinLotteryCost = coinLotteryCost;
	}

}
