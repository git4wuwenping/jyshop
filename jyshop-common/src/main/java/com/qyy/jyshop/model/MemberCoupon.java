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
 * 优惠券关联表
 * 
 * @author jiangbin
 * @created 2018年4月11日 上午8:39:59
 */
@Entity
@Table(name = "shop_member_coupon")
public class MemberCoupon implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // Id

    @Column(name = "cpn_id")
    private Long cpnId; // 优惠劵ID

    @Column(name = "cpn_name")
    private String cpnName; // 优惠券名称

    @Column(name = "cpn_sn")
    private String cpnSn; // 优惠券sn

    @Column(name = "sn")
    private String sn; // 编码

    @Column(name = "member_id")
    private Long memberId; // 用户ID

    @Column(name = "is_use")
    private Integer isUse; // 是否已使用 1是0否

    @Column(name = "is_get")
    private Integer isGet; // 是否已领取 1是 0否

    @Column(name = "get_time")
    private Timestamp getTime; // 领取时间

    @Column(name = "cpn_type")
    private Integer cpnType; // 优惠劵类型：0分类 1指定商品 2指定店铺

    @Column(name = "grant_type")
    private Integer grantType; // 发放类型：0-线上发放1-线下发放2-直接到账3-注册赠送

    @Column(name = "use_type")
    private Integer useType; // 使用类型： 0按日期 1按领取日期有效天数

    @Column(name = "discount_price")
    private BigDecimal discountPrice; // 抵扣金额

    @Column(name = "min_amount")
    private BigDecimal minAmount; // 最低消费金额

    @Column(name = "use_begin_time")
    private Timestamp useBeginTime; // 使用期限起始时间

    @Column(name = "use_end_time")
    private Timestamp useEndTime; // 使用期限结束时间
    
    @Column(name = "effective_date")
    private Integer effectiveDate; // 领取后几天内有效

    @Column(name = "use_time")
    private Timestamp useTime; // 使用时间

    @Column(name = "remark")
    private String remark; // 备注

    @Column(name = "create_time")
    private Timestamp createTime; // 发布时间

    @Column(name = "order_id")
    private Long orderId; // 订单ID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }

    public Integer getIsGet() {
        return isGet;
    }

    public void setIsGet(Integer isGet) {
        this.isGet = isGet;
    }

    public Timestamp getGetTime() {
        return getTime;
    }

    public void setGetTime(Timestamp getTime) {
        this.getTime = getTime;
    }

    public Integer getCpnType() {
        return cpnType;
    }

    public void setCpnType(Integer cpnType) {
        this.cpnType = cpnType;
    }

    public Integer getGrantType() {
        return grantType;
    }

    public void setGrantType(Integer grantType) {
        this.grantType = grantType;
    }

    public Integer getUseType() {
        return useType;
    }

    public void setUseType(Integer useType) {
        this.useType = useType;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
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

    public Timestamp getUseTime() {
        return useTime;
    }

    public void setUseTime(Timestamp useTime) {
        this.useTime = useTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
