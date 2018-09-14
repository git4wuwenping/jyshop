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
@Table(name = "shop_member_bargain")
public class MemberBargain implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "member_bargain_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberBargainId; //主键ID
    
    private Long bargainId; //砍价活动ID
    private Long goodsId; //商品ID
    private Long memberId; //用户ID
    private String nickname; //昵称
    private String face; //头像
    private Long orderId; //订单ID
    private String orderSn; //订单号
    private BigDecimal bargainPrice; //砍价金额
    private Integer num; //砍价次数
    private Timestamp createDate; //创建时间
    public Long getMemberBargainId() {
        return memberBargainId;
    }
    public void setMemberBargainId(Long memberBargainId) {
        this.memberBargainId = memberBargainId;
    }
    public Long getBargainId() {
        return bargainId;
    }
    public void setBargainId(Long bargainId) {
        this.bargainId = bargainId;
    }
    public Long getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getFace() {
        return face;
    }
    public void setFace(String face) {
        this.face = face;
    }
    public Long getMemberId() {
        return memberId;
    }
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
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
    public BigDecimal getBargainPrice() {
        return bargainPrice;
    }
    public void setBargainPrice(BigDecimal bargainPrice) {
        this.bargainPrice = bargainPrice;
    }
    public Integer getNum() {
        return num;
    }
    public void setNum(Integer num) {
        this.num = num;
    }
    public Timestamp getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
    
}
