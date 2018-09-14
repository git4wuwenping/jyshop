/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 描述
 *
 * @author wu
 * @created 2018/3/28 16:23
 */
@Entity
@Table(name="shop_goods_rejected_ads")
public class GoodsRejectedAds
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String num;
    private String sjr;
    private String tel;
    private String address;
    private String used;
    private Integer comId;

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNum() {
        return this.num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSjr() {
        return this.sjr;
    }

    public void setSjr(String sjr) {
        this.sjr = sjr;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsed() {
        return this.used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public Integer getComId() {
        return this.comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }
}
