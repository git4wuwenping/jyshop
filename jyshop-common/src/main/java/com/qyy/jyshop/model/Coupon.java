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

/**
 * 优惠券
 * 
 * @author jiangbin
 * @created 2018年4月11日 上午8:40:27
 */
@Entity
@Table(name = "shop_coupon")
public class Coupon implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cpn_id")
    private Long cpnId; // Id

    @Column(name = "cpn_name")
    private String cpnName; // 优惠券名称

    @Column(name = "cpn_sn")
    private String cpnSn; // 优惠券编码

    @Column(name = "grant_type")
    private Integer grantType; // 发放类型：0-线上发放1-线下发放2-直接到账3-注册赠送

    @Column(name = "cpn_img")
    private String cpnImg; // 优惠劵图片

    @Column(name = "discount_price")
    private BigDecimal discountPrice; // 抵扣金额

    @Column(name = "limit_get")
    private Integer limitGet; // 每人限领

    @Column(name = "grant_num")
    private Integer grantNum; // 发放数量

    @Column(name = "get_begin_time")
    private Timestamp getBeginTime; // 领取期限起始时间

    @Column(name = "get_end_time")
    private Timestamp getEndTime; // 领取期限结束时间

    @Column(name = "use_type")
    private Integer useType; // 使用类型： 0按日期 1按领取日期有效天数

    @Column(name = "use_begin_time")
    private Timestamp useBeginTime; // 使用期限起始时间

    @Column(name = "use_end_time")
    private Timestamp useEndTime; // 使用期限结束时间

    @Column(name = "effective_date")
    private Integer effectiveDate; // 领取后几天内有效

    @Column(name = "min_amount")
    private BigDecimal minAmount; // 最低消费金额

    @Column(name = "cpn_type")
    private Integer cpnType; // 优惠劵类型：0分类 1指定商品 2供应商

    @Column(name = "create_time")
    private Timestamp createTime; // 发布时间

    @Column(name = "remark")
    private String remark; // 备注

    public Long getCpnId() {
        return cpnId;
    }

    public void setCpnId(Long cpnId) {
        this.cpnId = cpnId;
    }

    public String getCpnName() {
        return cpnName;
    }

    public void setCpnName(String cpnName) {
        this.cpnName = cpnName;
    }

    public String getCpnSn() {
        return cpnSn;
    }

    public void setCpnSn(String cpnSn) {
        this.cpnSn = cpnSn;
    }

    public Integer getGrantType() {
        return grantType;
    }

    public void setGrantType(Integer grantType) {
        this.grantType = grantType;
    }

    public String getCpnImg() {
        return cpnImg;
    }

    public void setCpnImg(String cpnImg) {
        this.cpnImg = cpnImg;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getLimitGet() {
        return limitGet;
    }

    public void setLimitGet(Integer limitGet) {
        this.limitGet = limitGet;
    }

    public Integer getGrantNum() {
        return grantNum;
    }

    public void setGrantNum(Integer grantNum) {
        this.grantNum = grantNum;
    }

    public Timestamp getGetBeginTime() {
        return getBeginTime;
    }

    public void setGetBeginTime(Timestamp getBeginTime) {
        this.getBeginTime = getBeginTime;
    }

    public Timestamp getGetEndTime() {
        return getEndTime;
    }

    public void setGetEndTime(Timestamp getEndTime) {
        this.getEndTime = getEndTime;
    }

    public Timestamp getUseBeginTime() {
        return useBeginTime;
    }

    public void setUseBeginTime(Timestamp useBeginTime) {
        this.useBeginTime = useBeginTime;
    }

    public Timestamp getUseEndTime() {
        return useEndTime;
    }

    public void setUseEndTime(Timestamp useEndTime) {
        this.useEndTime = useEndTime;
    }

    public Integer getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Integer effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public Integer getCpnType() {
        return cpnType;
    }

    public void setCpnType(Integer cpnType) {
        this.cpnType = cpnType;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getUseType() {
        return useType;
    }

    public void setUseType(Integer useType) {
        this.useType = useType;
    }

}
