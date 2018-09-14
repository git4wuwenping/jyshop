package com.qyy.jyshop.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 审核类实体
 * @author 22132
 *
 */
@Entity
@Table(name = "shop_examine_goods")
public class ExamineGoods implements Serializable{
	 private static final long serialVersionUID = 1L;

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "examine_id")
	    private Long examineId; // 主键id

	    @Column(name = "goods_id")
	    private Long goodsId; // 商品id
	    
	    private String dismissal;	//驳回原因
	    
	    private String suggestion;	//处理意见
	    
	    private short state;		//审核状态	0，通过；1，驳回'
	    
	    private String remarks;		//备注
	    
	    @Column(name = "user_id")
	    private int userId;			//用户id
	    
	    private short type;			//审核标识
	    
	    @Column(name = "operate_time")
	    private Timestamp operateTime;			//经办时间
	    
	    @Column(name = "create_time")
	    private Timestamp createTime;				//创建时间
	    

		public Long getExamineId() {
			return examineId;
		}

		public void setExamineId(Long examineId) {
			this.examineId = examineId;
		}

		public Long getGoodsId() {
			return goodsId;
		}

		public void setGoodsId(Long goodsId) {
			this.goodsId = goodsId;
		}

		public String getDismissal() {
			return dismissal;
		}

		public void setDismissal(String dismissal) {
			this.dismissal = dismissal;
		}

		public String getSuggestion() {
			return suggestion;
		}

		public void setSuggestion(String suggestion) {
			this.suggestion = suggestion;
		}

		public short getState() {
			return state;
		}

		public void setState(short state) {
			this.state = state;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}


		public Timestamp getOperateTime() {
			return operateTime;
		}

		public void setOperateTime(Timestamp operateTime) {
			this.operateTime = operateTime;
		}

		public Timestamp getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Timestamp createTime) {
			this.createTime = createTime;
		}

		public short getType() {
			return type;
		}

		public void setType(short type) {
			this.type = type;
		}

	    
}
