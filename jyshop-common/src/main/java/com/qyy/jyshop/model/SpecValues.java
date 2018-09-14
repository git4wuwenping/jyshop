package com.qyy.jyshop.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name ="shop_spec_values")
public class SpecValues implements Serializable{
    
    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spec_value_id")
    private Long specValueId; //规格值id
    
    @Column(name = "spec_id")
    private Long specId; //规格id
    
    @Column(name = "spec_value")
    private String specValue; //规格值
    
    @Column(name = "spec_image")
    private String specImage; //规格图片
    private Integer sort; //排序值
    
    @Column(name = "creation_time")
    private Timestamp creationTime; //创建时间

    public Long getSpecValueId() {
        return specValueId;
    }

    public void setSpecValueId(Long specValueId) {
        this.specValueId = specValueId;
    }

    public Long getSpecId() {
        return specId;
    }

    public void setSpecId(Long specId) {
        this.specId = specId;
    }

    public String getSpecValue() {
        return specValue;
    }

    public void setSpecValue(String specValue) {
        this.specValue = specValue;
    }

    public String getSpecImage() {
        return specImage;
    }

    public void setSpecImage(String specImage) {
        this.specImage = specImage;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }
    
}

