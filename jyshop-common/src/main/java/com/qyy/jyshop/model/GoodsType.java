package com.qyy.jyshop.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shop_goods_type")
public class GoodsType implements Serializable{
    
    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeId; //主键id
    
    @Column(name = "type_name")
    private String typeName; //类型名字
    
    @Column(name = "type_props")
    private String typeProps; //类型属性json字符串保存
    
    @Column(name = "spec_ids")
    private String specIds; //规格IDS
    
    @Column(name = "parent_id")
    private Long parentId; //父类型id
    
    @Column(name = "creation_time")
    private Timestamp creationTime; //创建时间
    
    @Column(name = "com_id")
    private Integer comId; //供应商id

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeProps() {
        return typeProps;
    }

    public void setTypeProps(String typeProps) {
        this.typeProps = typeProps;
    }

    public String getSpecIds() {
        return specIds;
    }

    public void setSpecIds(String specIds) {
        this.specIds = specIds;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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
     
}
