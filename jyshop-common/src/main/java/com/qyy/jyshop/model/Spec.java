package com.qyy.jyshop.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Table(name ="shop_spec")
public class Spec implements Serializable{
	
    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "spec_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long specId; //规格id
    
    @Column(name = "spec_name")
    private String specName; //规格名称
    
    @Column(name = "show_type")
    private Integer showType; //显示方式 0：文字 1：图像 2：文字和图像
    private String remark; //备注
    private Integer sort; //排序
    
    @Column(name = "creation_time")
    private Timestamp creationTime; //创建时间,
    
    @Column(name = "com_id")
    private Integer comId; //供应商Id
    
    @Transient
    private List<SpecValues> specValueList; 
   
    @Transient
    private Integer status;
    
    public Long getSpecId() {
        return specId;
    }

    public void setSpecId(Long specId) {
        this.specId = specId;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public Integer getShowType() {
        return showType;
    }

    public void setShowType(Integer showType) {
        this.showType = showType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public List<SpecValues> getSpecValueList() {
        return specValueList;
    }

    public void setSpecValueList(List<SpecValues> specValueList) {
        this.specValueList = specValueList;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
  
}
