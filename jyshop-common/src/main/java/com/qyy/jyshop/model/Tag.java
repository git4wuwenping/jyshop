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
 * 商品标签
 * 
 */
@Entity
@Table(name = "shop_tag")
public class Tag implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId; // Id

    @Column(name = "tag_name")
    private String tagName; // 标签名称

    @Column(name = "tag_order")
    private Integer tagOrder; // 排序

    @Column(name = "create_time")
    private Timestamp createTime; // 创建时间

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Integer getTagOrder() {
        return tagOrder;
    }

    public void setTagOrder(Integer tagOrder) {
        this.tagOrder = tagOrder;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

}
