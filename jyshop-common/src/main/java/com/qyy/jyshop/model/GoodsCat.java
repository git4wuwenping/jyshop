package com.qyy.jyshop.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "shop_goods_cat")
public class GoodsCat implements Serializable {
    /**
     * 商品分类id
     */
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "cat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer catId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类父id
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 分类路径
     */
    @Column(name = "cat_path")
    private String catPath;
    /**
     * 分类路径名称
     */
    @Column(name = "cat_path_name")
    private String catPathName;

    /**
     * 分类编码
     */
    @Column(name = "cat_code")
    private String catCode;

    /**
     * 编码顺序
     */
    @Column(name = "code_order")
    private Integer codeOrder;

    /**
     * 显示次序
     */
    @Column(name = "cat_order")
    private Integer catOrder;

    /**
     * 类型id
     */
    @Column(name = "type_id")
    private Integer typeId;

    /**
     * 显示到列表
     */
    @Column(name = "list_show")
    private String listShow;

    /**
     * 类别图
     */
    private String image;

    private Integer disable;

    /**
     * 标题
     */
    @Column(name = "page_title")
    private String pageTitle;

    @Column(name = "meta_keywords")
    private String metaKeywords;

    @Column(name = "meta_description")
    private String metaDescription;

    @Column(name = "adv_image")
    private String advImage;

    @Column(name = "auto_id")
    private Integer autoId;

    /**
     * 商品数量
     */
    @Column(name = "goods_count")
    private Integer goodsCount;

    /**
     * 获取商品分类id
     *
     * @return cat_id - 商品分类id
     */
    public Integer getCatId() {
        return catId;
    }

    /**
     * 设置商品分类id
     *
     * @param catId
     *            商品分类id
     */
    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    /**
     * 获取分类名称
     *
     * @return name - 分类名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置分类名称
     *
     * @param name
     *            分类名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取分类父id
     *
     * @return parent_id - 分类父id
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * 设置分类父id
     *
     * @param parentId
     *            分类父id
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取分类路径
     *
     * @return cat_path - 分类路径
     */
    public String getCatPath() {
        return catPath;
    }

    /**
     * 设置分类路径
     *
     * @param catPath
     *            分类路径
     */
    public void setCatPath(String catPath) {
        this.catPath = catPath;
    }

    public String getCatPathName() {
        return catPathName;
    }

    public void setCatPathName(String catPathName) {
        this.catPathName = catPathName;
    }

    /**
     * 获取分类编码
     *
     * @return cat_code - 分类编码
     */
    public String getCatCode() {
        return catCode;
    }

    /**
     * 设置分类编码
     *
     * @param catCode
     *            分类编码
     */
    public void setCatCode(String catCode) {
        this.catCode = catCode;
    }

    /**
     * 获取编码顺序
     *
     * @return code_order - 编码顺序
     */
    public Integer getCodeOrder() {
        return codeOrder;
    }

    /**
     * 设置编码顺序
     *
     * @param codeOrder
     *            编码顺序
     */
    public void setCodeOrder(Integer codeOrder) {
        this.codeOrder = codeOrder;
    }

    /**
     * 获取显示次序
     *
     * @return cat_order - 显示次序
     */
    public Integer getCatOrder() {
        return catOrder;
    }

    /**
     * 设置显示次序
     *
     * @param catOrder
     *            显示次序
     */
    public void setCatOrder(Integer catOrder) {
        this.catOrder = catOrder;
    }

    /**
     * 获取类型id
     *
     * @return type_id - 类型id
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * 设置类型id
     *
     * @param typeId
     *            类型id
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * 获取显示到列表
     *
     * @return list_show - 显示到列表
     */
    public String getListShow() {
        return listShow;
    }

    /**
     * 设置显示到列表
     *
     * @param listShow
     *            显示到列表
     */
    public void setListShow(String listShow) {
        this.listShow = listShow;
    }

    /**
     * 获取类别图
     *
     * @return image - 类别图
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置类别图
     *
     * @param image
     *            类别图
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return disable
     */
    public Integer getDisable() {
        return disable;
    }

    /**
     * @param disable
     */
    public void setDisable(Integer disable) {
        this.disable = disable;
    }

    /**
     * 获取标题
     *
     * @return page_title - 标题
     */
    public String getPageTitle() {
        return pageTitle;
    }

    /**
     * 设置标题
     *
     * @param pageTitle
     *            标题
     */
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    /**
     * @return meta_keywords
     */
    public String getMetaKeywords() {
        return metaKeywords;
    }

    /**
     * @param metaKeywords
     */
    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    /**
     * @return meta_description
     */
    public String getMetaDescription() {
        return metaDescription;
    }

    /**
     * @param metaDescription
     */
    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    /**
     * @return adv_image
     */
    public String getAdvImage() {
        return advImage;
    }

    /**
     * @param advImage
     */
    public void setAdvImage(String advImage) {
        this.advImage = advImage;
    }

    /**
     * @return auto_id
     */
    public Integer getAutoId() {
        return autoId;
    }

    /**
     * @param autoId
     */
    public void setAutoId(Integer autoId) {
        this.autoId = autoId;
    }

    /**
     * 获取商品数量
     *
     * @return goods_count - 商品数量
     */
    public Integer getGoodsCount() {
        return goodsCount;
    }

    /**
     * 设置商品数量
     *
     * @param goodsCount
     *            商品数量
     */
    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }
}