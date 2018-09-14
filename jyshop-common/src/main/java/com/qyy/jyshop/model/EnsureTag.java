package com.qyy.jyshop.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 保障标签表
 */
@Entity
@Table(name = "shop_ensure_tag")
public class EnsureTag implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id; // id

	@Column(name = "tag_order")
	private Integer tagOrder; // 排序

	@Column(name = "tag_name")
	private String tagName; // 标签名称

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTagOrder() {
		return tagOrder;
	}

	public void setTagOrder(Integer tagOrder) {
		this.tagOrder = tagOrder;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

}
