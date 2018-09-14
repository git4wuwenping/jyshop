package com.qyy.jyshop.util.excel.dto;

import java.sql.Timestamp;

import com.qyy.jyshop.util.excel.ExcelResources;

public class CommentForExcel {
	private int commentId;
	private Long memberId;
	private Long  goodsId;
	private String name;
	private Timestamp commentTime;
	private String uname;
	private String orderId;
	private String score;
	private String content;
	
	@ExcelResources(title = "评价ID", order = 1, isCombine = true, isCombineCondition = true)
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	@ExcelResources(title = "用户D", order = 2, isCombine = true)
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	@ExcelResources(title = "商品ID", order = 3, isCombine = true)
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	@ExcelResources(title = "商品名称", order = 4, isCombine = true)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@ExcelResources(title = "评价时间", order = 5, isCombine = true)
	public Timestamp getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(Timestamp commentTime) {
		this.commentTime = commentTime;
	}
	@ExcelResources(title = "会员昵称", order = 6, isCombine = true)
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	@ExcelResources(title = "订单编号", order = 7, isCombine = true)
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	@ExcelResources(title = "评价级别", order = 8, isCombine = true)
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	@ExcelResources(title = "评价内容", order = 9, isCombine = true)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
