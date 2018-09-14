package com.qyy.jyshop.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 拼团活动
 */
@Entity
@Table(name = "shop_spell_activity")
public class SpellActivity implements Serializable {

    /**
     * 拼团活动ID
     */
    @Id
    @Column(name = "activity_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activityId;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Timestamp createDate;

    /**
     * 最后修改时间
     */
    @Column(name = "last_modify")
    private Timestamp lastModify;

    /**
     * 供应商ID
     */
    @Column(name = "com_id")
    private Long comId;

    /**
     * 店铺ID
     */
    @Column(name = "shop_store_id")
    private Long shopStoreId;

    /**
     * 活动开始时间
     */
    @Column(name = "start_date")
    private Timestamp startDate;

    /**
     * 活动结束时间
     */
    @Column(name = "end_date")
    private Timestamp endDate;

    /**
     * 成团周期
     */
    @Column(name = "cycle")
    private Integer cycle;

    /**
     * 拼团活动名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 商品ID
     */
    @Column(name = "goods_id")
    private Long goodsId;

    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 商品标题
     */
    @Column(name = "goods_title")
    private String goodsTitle;

    /**
     * 拼团类型（0：普通团，1：夺宝团，2：阶梯团，3：试用团，4：免费团）
     */
    @Column(name = "spell_type")
    private Integer spellType;

    /**
     * 活动图片1
     */
    @Column(name = "image1")
    private String image1;

    /**
     * 活动图片2
     */
    @Column(name = "image2")
    private String image2;

    /**
     * 商品原价
     */
    @Column(name = "goods_price")
    private BigDecimal goodsPrice;

    /**
     * 拼团价
     */
    @Column(name = "spell_price")
    private BigDecimal spellPrice;

    /**
     * 库存
     */
    @Column(name = "store")
    private Integer store;

    /**
     * 拼团人数
     */
    @Column(name = "num")
    private Integer num;

    /**
     * 商品描述
     */
    @Column(name = "goods_describe")
    private String goodsDescribe;

    /**
     * 邮费模板ID
     */
    @Column(name = "dly_type_id")
    private Long dlyTypeId;

    /**
     * 限制次数
     */
    @Column(name = "limited")
    private Integer limited;

    /**
     * 分享标题
     */
    @Column(name = "share_title")
    private String shareTitle;

    /**
     * 分享描述
     */
    @Column(name = "share_describe")
    private String shareDescribe;

    /**
     * 分享图片
     */
    @Column(name = "share_image")
    private String shareImage;

    /**
     * 活动规则
     */
    @Column(name = "rules")
    private String rules;

    /**
     * 已拼件数
     */
    @Column(name = "already_num")
    private Integer alreadyNum;

    /**
     * 真实件数
     */
    @Column(name = "real_num")
    private Integer realNum;

    /**
     * 成团件数
     */
    @Column(name = "complete_num")
    private Integer completeNum;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort;

    /**
     * 状态（0：未开始，1：进行中，2：已结束）
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 是否推荐
     */
    @Column(name = "recommended")
    private Boolean recommended;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getLastModify() {
        return lastModify;
    }

    public void setLastModify(Timestamp lastModify) {
        this.lastModify = lastModify;
    }

    public Long getComId() {
        return comId;
    }

    public void setComId(Long comId) {
        this.comId = comId;
    }

    public Long getShopStoreId() {
        return shopStoreId;
    }

    public void setShopStoreId(Long shopStoreId) {
        this.shopStoreId = shopStoreId;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public Integer getSpellType() {
        return spellType;
    }

    public void setSpellType(Integer spellType) {
        this.spellType = spellType;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getSpellPrice() {
        return spellPrice;
    }

    public void setSpellPrice(BigDecimal spellPrice) {
        this.spellPrice = spellPrice;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getGoodsDescribe() {
        return goodsDescribe;
    }

    public void setGoodsDescribe(String goodsDescribe) {
        this.goodsDescribe = goodsDescribe;
    }

    public Long getDlyTypeId() {
        return dlyTypeId;
    }

    public void setDlyTypeId(Long dlyTypeId) {
        this.dlyTypeId = dlyTypeId;
    }

    public Integer getLimited() {
        return limited;
    }

    public void setLimited(Integer limited) {
        this.limited = limited;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareDescribe() {
        return shareDescribe;
    }

    public void setShareDescribe(String shareDescribe) {
        this.shareDescribe = shareDescribe;
    }

    public String getShareImage() {
        return shareImage;
    }

    public void setShareImage(String shareImage) {
        this.shareImage = shareImage;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public Integer getAlreadyNum() {
        return alreadyNum;
    }

    public void setAlreadyNum(Integer alreadyNum) {
        this.alreadyNum = alreadyNum;
    }

    public Integer getRealNum() {
        return realNum;
    }

    public void setRealNum(Integer realNum) {
        this.realNum = realNum;
    }

    public Integer getCompleteNum() {
        return completeNum;
    }

    public void setCompleteNum(Integer completeNum) {
        this.completeNum = completeNum;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getRecommended() {
        return recommended;
    }

    public void setRecommended(Boolean recommended) {
        this.recommended = recommended;
    }
}