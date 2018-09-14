package com.qyy.jyshop.model;

import java.io.Serializable;

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
 * @created 2018年4月11日 上午8:40:15
 */
@Entity
@Table(name = "shop_coupon_rel")
public class CouponRel implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // Id

    @Column(name = "rel_id")
    private Long relId; // 关联Id

    @Column(name = "rel_type")
    private Integer relType; // 关联类型：0-分类 1-商品 2-供应商

    @Column(name = "cpn_sn")
    private String cpnSn; // 优惠券sn

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRelId() {
        return relId;
    }

    public void setRelId(Long relId) {
        this.relId = relId;
    }

    public Integer getRelType() {
        return relType;
    }

    public void setRelType(Integer relType) {
        this.relType = relType;
    }

    public String getCpnSn() {
        return cpnSn;
    }

    public void setCpnSn(String cpnSn) {
        this.cpnSn = cpnSn;
    }

}
