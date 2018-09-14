package com.qyy.jyshop.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 拼团
 */
@Entity
@Table(name = "shop_spell")
public class Spell implements Serializable {

    /**
     * 拼团ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spell_id")
    private Long spellId;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    public Timestamp createDate;

    /**
     * 拼团活动ID
     */
    @Column(name = "activity_Id")
    private Long activityId;

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
     * 图片
     */
    @Column(name = "image2")
    private String image2;

    /**
     * 发起人ID
     */
    @Column(name = "originator_id")
    private Long originatorId;

    /**
     * 发起人名字
     */
    @Column(name = "originator_name")
    private String originatorName;

    /**
     * 发起人头像
     */
    @Column(name = "originator_face")
    private String originatorFace;

    /**
     * 拼团开始时间
     */
    @Column(name = "start_date")
    private Timestamp startDate;

    /**
     * 拼团结束时间
     */
    @Column(name = "end_date")
    private Timestamp endDate;

    /**
     * 拼团类型（0：普通团，1：夺宝团，2：阶梯团，3：试用团，4：免费团）
     */
    @Column(name = "spell_type")
    private Integer spellType;

    /**
     * 活动名称
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
     * 商品原价
     */
    @Column(name = "goods_price")
    private BigDecimal goodsPrice;

    /**
     * 拼团价
     */
    @Column(name = "spell_price")
    private BigDecimal spellPrice;

//    /**
//     * 货品ID
//     */
//    @Column(name = "product_id")
//    private Long productId;
//
//    /**
//     * 货品规格
//     */
//    @Column(name = "product_specs")
//    private String productSpecs;

    /**
     * 成团人数
     */
    @Column(name = "complete_num")
    private Integer completeNum;

    /**
     * 参与人数
     */
    @Column(name = "participate_num")
    private Integer participateNum;

    /**
     * 参与人员信息
     */
    @Column(name = "participate_details")
    private String participateDetails;

    /**
     * 中奖用户
     */
    @Column(name = "winning")
    private Long winning;

    /**
     * 成团时间
     */
    @Column(name = "complete_date")
    private Timestamp completeDate;

    /**
     * 状态（0:未开始，1：正在拼团，2：拼团成功，3：拼团失败，4：取消拼团）
     */
    @Column(name = "status")
    private Integer status;

    public Long getSpellId() {
        return spellId;
    }

    public void setSpellId(Long spellId) {
        this.spellId = spellId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
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

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public Long getOriginatorId() {
        return originatorId;
    }

    public void setOriginatorId(Long originatorId) {
        this.originatorId = originatorId;
    }

    public String getOriginatorName() {
        return originatorName;
    }

    public void setOriginatorName(String originatorName) {
        this.originatorName = originatorName;
    }

    public String getOriginatorFace() {
        return originatorFace;
    }

    public void setOriginatorFace(String originatorFace) {
        this.originatorFace = originatorFace;
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

    public Integer getSpellType() {
        return spellType;
    }

    public void setSpellType(Integer spellType) {
        this.spellType = spellType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
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

//    public Long getProductId() {
//        return productId;
//    }
//
//    public void setProductId(Long productId) {
//        this.productId = productId;
//    }
//
//    public String getProductSpecs() {
//        return productSpecs;
//    }
//
//    public void setProductSpecs(String productSpecs) {
//        this.productSpecs = productSpecs;
//    }

    public Integer getCompleteNum() {
        return completeNum;
    }

    public void setCompleteNum(Integer completeNum) {
        this.completeNum = completeNum;
    }

    public Integer getParticipateNum() {
        return participateNum;
    }

    public void setParticipateNum(Integer participateNum) {
        this.participateNum = participateNum;
    }

    public String getParticipateDetails() {
        return participateDetails;
    }

    public void setParticipateDetails(String participateDetails) {
        this.participateDetails = participateDetails;
    }

    public Long getWinning() {
        return winning;
    }

    public void setWinning(Long winning) {
        this.winning = winning;
    }

    public Timestamp getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Timestamp completeDate) {
        this.completeDate = completeDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
