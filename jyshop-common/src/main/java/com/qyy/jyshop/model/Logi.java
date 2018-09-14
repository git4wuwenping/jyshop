package com.qyy.jyshop.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 物流公司
 * @author hwc
 * @created 2017年12月21日 上午10:23:29
 */
@Entity
@Table(name="shop_logi")
public class Logi implements Serializable {

    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logi_id")
    private Integer logiId; //主键ID
    
    @Column(name = "logi_code")
    private String logiCode; //物流代码
    
    @Column(name = "logi_name")
    private String logiName; //物流名称

    public Integer getLogiId() {
        return logiId;
    }

    public void setLogiId(Integer logiId) {
        this.logiId = logiId;
    }

    public String getLogiCode() {
        return logiCode;
    }

    public void setLogiCode(String logiCode) {
        this.logiCode = logiCode;
    }

    public String getLogiName() {
        return logiName;
    }

    public void setLogiName(String logiName) {
        this.logiName = logiName;
    }
    
}
