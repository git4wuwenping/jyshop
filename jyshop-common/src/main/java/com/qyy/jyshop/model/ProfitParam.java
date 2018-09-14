package com.qyy.jyshop.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shop_profit_param")
public class ProfitParam implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profit_id")
    private Integer profitId; // id
    private BigDecimal tax; // 税收
    private BigDecimal management; // 管理费
    private BigDecimal operation; // 运营费
    private BigDecimal gain; // 利润
    private BigDecimal presenter; // 推荐者
    @Column(name = "service_center")
    private BigDecimal serviceCenter;// 运营服务中心
    private BigDecimal referee; // 推荐人
    private BigDecimal operator; // 经办人
    @Column(name = "profit_member_first")
    private BigDecimal profitMemberFirst;// 有分润会员首次购买分润比例
    @Column(name = "profit_member_sec")
    private BigDecimal profitMemberSec;// 有分润会员重复购买分润比例
    @Column(name = "profit_shopowner_first")
    private BigDecimal profitShopownerFirst;// 店长推荐首次购买分润比例
    @Column(name = "profit_shopowner_sec")
    private BigDecimal profitShopownerSec;// 店长推荐重复购买分润比例
    @Column(name = "profit_shopowner_sub")
    private BigDecimal profitShopownerSub;// 店长下级分润比例

    @Column(name = "profit_member_first_real")
    private BigDecimal profitMemberFirstReal;// 有分润会员首次购买分润实际比例
    @Column(name = "profit_member_sec_real")
    private BigDecimal profitMemberSecReal;// 有分润会员重复购买分润实际比例
    @Column(name = "profit_shopowner_first_real")
    private BigDecimal profitShopownerFirstReal;// 店长推荐首次购买分润实际比例
    @Column(name = "profit_shopowner_sec_real")
    private BigDecimal profitShopownerSecReal;// 店长推荐重复购买分润实际比例
    @Column(name = "profit_shopowner_sub_real")
    private BigDecimal profitShopownerSubReal;// 店长下级分润实际比例

    public Integer getProfitId() {
        return profitId;
    }

    public void setProfitId(Integer profitId) {
        this.profitId = profitId;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getManagement() {
        return management;
    }

    public void setManagement(BigDecimal management) {
        this.management = management;
    }

    public BigDecimal getOperation() {
        return operation;
    }

    public void setOperation(BigDecimal operation) {
        this.operation = operation;
    }

    public BigDecimal getGain() {
        return gain;
    }

    public void setGain(BigDecimal gain) {
        this.gain = gain;
    }

    public BigDecimal getPresenter() {
        return presenter;
    }

    public void setPresenter(BigDecimal presenter) {
        this.presenter = presenter;
    }

    public BigDecimal getServiceCenter() {
        return serviceCenter;
    }

    public void setServiceCenter(BigDecimal serviceCenter) {
        this.serviceCenter = serviceCenter;
    }

    public BigDecimal getReferee() {
        return referee;
    }

    public void setReferee(BigDecimal referee) {
        this.referee = referee;
    }

    public BigDecimal getOperator() {
        return operator;
    }

    public void setOperator(BigDecimal operator) {
        this.operator = operator;
    }

    public BigDecimal getProfitMemberFirst() {
        return profitMemberFirst;
    }

    public void setProfitMemberFirst(BigDecimal profitMemberFirst) {
        this.profitMemberFirst = profitMemberFirst;
    }

    public BigDecimal getProfitMemberSec() {
        return profitMemberSec;
    }

    public void setProfitMemberSec(BigDecimal profitMemberSec) {
        this.profitMemberSec = profitMemberSec;
    }

    public BigDecimal getProfitShopownerFirst() {
        return profitShopownerFirst;
    }

    public void setProfitShopownerFirst(BigDecimal profitShopownerFirst) {
        this.profitShopownerFirst = profitShopownerFirst;
    }

    public BigDecimal getProfitShopownerSec() {
        return profitShopownerSec;
    }

    public void setProfitShopownerSec(BigDecimal profitShopownerSec) {
        this.profitShopownerSec = profitShopownerSec;
    }

    public BigDecimal getProfitShopownerSub() {
        return profitShopownerSub;
    }

    public void setProfitShopownerSub(BigDecimal profitShopownerSub) {
        this.profitShopownerSub = profitShopownerSub;
    }

    public BigDecimal getProfitMemberFirstReal() {
        return profitMemberFirstReal;
    }

    public void setProfitMemberFirstReal(BigDecimal profitMemberFirstReal) {
        this.profitMemberFirstReal = profitMemberFirstReal;
    }

    public BigDecimal getProfitMemberSecReal() {
        return profitMemberSecReal;
    }

    public void setProfitMemberSecReal(BigDecimal profitMemberSecReal) {
        this.profitMemberSecReal = profitMemberSecReal;
    }

    public BigDecimal getProfitShopownerFirstReal() {
        return profitShopownerFirstReal;
    }

    public void setProfitShopownerFirstReal(BigDecimal profitShopownerFirstReal) {
        this.profitShopownerFirstReal = profitShopownerFirstReal;
    }

    public BigDecimal getProfitShopownerSecReal() {
        return profitShopownerSecReal;
    }

    public void setProfitShopownerSecReal(BigDecimal profitShopownerSecReal) {
        this.profitShopownerSecReal = profitShopownerSecReal;
    }

    public BigDecimal getProfitShopownerSubReal() {
        return profitShopownerSubReal;
    }

    public void setProfitShopownerSubReal(BigDecimal profitShopownerSubReal) {
        this.profitShopownerSubReal = profitShopownerSubReal;
    }

}
