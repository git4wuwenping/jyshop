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
@Table(name = "shop_comment")
public class Comment implements Serializable {

	/**
	 * 描述
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "comment_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long commentId; // 主键id

	@Column(name = "goods_id")
	private Long goodsId; // 商品id
	@Column(name = "member_id")
	private Long memberId; // 会员id
	@Column(name = "order_id")
	private Long orderId; // 订单id
	@Column(name = "item_id")
	private Long itemId;
	@Column(name = "content")
	private String content; // 平价内容
	@Column(name = "anon_flag")
	private Short anonFlag; // 平价内容
	@Column(name = "image")
	private String image; // 平价图片

	/**
	 * 评价得分，0：好评；1：中评；2：差评
	 */
	@Column(name = "score")
	private Integer score;

	/**
	 * 评价时间
	 */
	@Column(name = "comment_time")
	private Timestamp commentTime;

	/**
	 * 评价ip地址
	 */
	@Column(name = "ip")
	private String ip;

	/**
	 * 状态 0-未审核，隐藏 1-审核通过，展示
	 */
	@Column(name = "status")
	private Short status;

	@Column(name = "auto_flag")
	private Short autoFlag;// 是否为自动好评 0-否 1-是

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
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

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Timestamp getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(Timestamp commentTime) {
		this.commentTime = commentTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Short getAutoFlag() {
		return autoFlag;
	}

	public void setAutoFlag(Short autoFlag) {
		this.autoFlag = autoFlag;
	}

	public Short getAnonFlag() {
		return anonFlag;
	}

	public void setAnonFlag(Short anonFlag) {
		this.anonFlag = anonFlag;
	}

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
