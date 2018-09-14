/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 描述
 * 
 * @author jiangbin
 * @created 2018年3月19日 下午4:14:45
 */

@Entity
@Table(name = "shop_old_member_point")
public class OldMemberPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "member_id")
    private Long memberId;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "point")
    private BigDecimal point;
    @Column(name = "already")
    private BigDecimal already;
    @Column(name = "residue")
    private BigDecimal residue;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public BigDecimal getPoint() {
        return point;
    }

    public void setPoint(BigDecimal point) {
        this.point = point;
    }

    public BigDecimal getAlready() {
        return already;
    }

    public void setAlready(BigDecimal already) {
        this.already = already;
    }

    public BigDecimal getResidue() {
        return residue;
    }

    public void setResidue(BigDecimal residue) {
        this.residue = residue;
    }

}
