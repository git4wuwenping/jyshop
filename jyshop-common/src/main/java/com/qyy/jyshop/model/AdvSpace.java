package com.qyy.jyshop.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 广告位
 * @author jiangbin
 * @created 2018年3月15日 下午2:43:32
 */
@Table(name = "shop_adv_space")
public class AdvSpace implements Serializable{
    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 广告位标识
     */
    @Column(name = "name")
    private String name;

    /**
     * 广告位标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 广告类型 0为图片 1为flash
     */
    @Column(name = "type")
    private Short type;

    /**
     * 宽度
     */
    @Column(name = "width")
    private Integer width;

    /**
     * 高度
     */
    @Column(name = "height")
    private Integer height;

    /**
     * 是否可用 1为启用 0为停用
     */
    @Column(name = "used")
    private Short used;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Short getUsed() {
        return used;
    }

    public void setUsed(Short used) {
        this.used = used;
    }

}