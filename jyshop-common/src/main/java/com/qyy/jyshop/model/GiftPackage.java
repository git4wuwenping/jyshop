package com.qyy.jyshop.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "shop_giftpackage")
public class GiftPackage {
    /**
     * 礼包id
     */
    @Id
    @Column(name = "gp_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gpId;

    /**
     * 库存报警数
     */
    @Column(name = "alarm_num")
    private Integer alarmNum;

    /**
     * 礼包sn
     */
    @Column(name = "gp_sn")
    private String gpSn;

    /**
     * 礼包名称
     */
    @Column(name = "gp_name")
    private String gpName;

    /**
     * 品牌id
     */
    @Column(name = "brand_id")
    private Integer brandId;

    /**
     * 供应商id 0:自营
     */
    @Column(name = "com_id")
    private Integer comId;

    /**
     * 类型id
     */
    @Column(name = "type_id")
    private Integer typeId;

    /**
     * 商品类型 0:普通商品 1:店长礼包
     */
    @Column(name = "goods_type")
    private Short goodsType;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 商品重量
     */
    private BigDecimal weight;

    /**
     * 是否上架
     */
    @Column(name = "market_enable")
    private Short marketEnable;

    /**
     * 礼包简介
     */
    private String brief;

    /**
     * 礼包价格
     */
    private BigDecimal price;

    /**
     * 礼包成本
     */
    private BigDecimal cost;

    /**
     * 市场价格
     */
    private BigDecimal mktprice;

    /**
     * 是否开启规格 0:没开启 1:开启
     */
    @Column(name = "have_spec")
    private Short haveSpec;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 最近一次修改时间
     */
    @Column(name = "last_modify")
    private Date lastModify;

    /**
     * 访问次数
     */
    @Column(name = "view_count")
    private Integer viewCount;

    /**
     * 购买次数
     */
    @Column(name = "buy_count")
    private Integer buyCount;

    /**
     * 是否可用 1:可用 0:不可用
     */
    private Short disabled;

    /**
     * 库存
     */
    private Integer store;

    /**
     * 积分
     */
    private Integer point;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 评价值
     */
    private Integer grade;

    /**
     * 礼包图片
     */
    private String image;

    /**
     * 是否是团购
     */
    private Short isgroupbuy;

    /**
     * 体积
     */
    private BigDecimal volume;

    /**
     * 配送方式ID
     */
    @Column(name = "SHIP_ID")
    private Integer shipId;

    /**
     * 配送方式
     */
    @Column(name = "SHIP_TYPE")
    private String shipType;

    /**
     * 配送费用
     */
    @Column(name = "SHIP_AMOUNT")
    private BigDecimal shipAmount;

    /**
     * 店铺id
     */
    @Column(name = "shop_store_id")
    private Integer shopStoreId;

    /**
     * 礼包详情
     */
    private String intro;

    /**
     * 参数字串
     */
    private String params;

    /**
     * 规格字串
     */
    private String specs;

    /**
     * 获取礼包id
     *
     * @return gp_id - 礼包id
     */
    public Long getGpId() {
        return gpId;
    }

    /**
     * 设置礼包id
     *
     * @param gpId 礼包id
     */
    public void setGpId(Long gpId) {
        this.gpId = gpId;
    }

    /**
     * 获取库存报警数
     *
     * @return alarm_num - 库存报警数
     */
    public Integer getAlarmNum() {
        return alarmNum;
    }

    /**
     * 设置库存报警数
     *
     * @param alarmNum 库存报警数
     */
    public void setAlarmNum(Integer alarmNum) {
        this.alarmNum = alarmNum;
    }

    /**
     * 获取礼包sn
     *
     * @return gp_sn - 礼包sn
     */
    public String getGpSn() {
        return gpSn;
    }

    /**
     * 设置礼包sn
     *
     * @param gpSn 礼包sn
     */
    public void setGpSn(String gpSn) {
        this.gpSn = gpSn;
    }

    /**
     * 获取礼包名称
     *
     * @return gp_name - 礼包名称
     */
    public String getGpName() {
        return gpName;
    }

    /**
     * 设置礼包名称
     *
     * @param gpName 礼包名称
     */
    public void setGpName(String gpName) {
        this.gpName = gpName;
    }

    /**
     * 获取品牌id
     *
     * @return brand_id - 品牌id
     */
    public Integer getBrandId() {
        return brandId;
    }

    /**
     * 设置品牌id
     *
     * @param brandId 品牌id
     */
    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    /**
     * 获取供应商id 0:自营
     *
     * @return com_id - 供应商id 0:自营
     */
    public Integer getComId() {
        return comId;
    }

    /**
     * 设置供应商id 0:自营
     *
     * @param comId 供应商id 0:自营
     */
    public void setComId(Integer comId) {
        this.comId = comId;
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
     * @param typeId 类型id
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * 获取商品类型 0:普通商品 1:店长礼包
     *
     * @return goods_type - 商品类型 0:普通商品 1:店长礼包
     */
    public Short getGoodsType() {
        return goodsType;
    }

    /**
     * 设置商品类型 0:普通商品 1:店长礼包
     *
     * @param goodsType 商品类型 0:普通商品 1:店长礼包
     */
    public void setGoodsType(Short goodsType) {
        this.goodsType = goodsType;
    }

    /**
     * 获取计量单位
     *
     * @return unit - 计量单位
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 设置计量单位
     *
     * @param unit 计量单位
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * 获取商品重量
     *
     * @return weight - 商品重量
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * 设置商品重量
     *
     * @param weight 商品重量
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /**
     * 获取是否上架
     *
     * @return market_enable - 是否上架
     */
    public Short getMarketEnable() {
        return marketEnable;
    }

    /**
     * 设置是否上架
     *
     * @param marketEnable 是否上架
     */
    public void setMarketEnable(Short marketEnable) {
        this.marketEnable = marketEnable;
    }

    /**
     * 获取礼包简介
     *
     * @return brief - 礼包简介
     */
    public String getBrief() {
        return brief;
    }

    /**
     * 设置礼包简介
     *
     * @param brief 礼包简介
     */
    public void setBrief(String brief) {
        this.brief = brief;
    }

    /**
     * 获取礼包价格
     *
     * @return price - 礼包价格
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置礼包价格
     *
     * @param price 礼包价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取礼包成本
     *
     * @return cost - 礼包成本
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * 设置礼包成本
     *
     * @param cost 礼包成本
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * 获取市场价格
     *
     * @return mktprice - 市场价格
     */
    public BigDecimal getMktprice() {
        return mktprice;
    }

    /**
     * 设置市场价格
     *
     * @param mktprice 市场价格
     */
    public void setMktprice(BigDecimal mktprice) {
        this.mktprice = mktprice;
    }

    /**
     * 获取是否开启规格 0:没开启 1:开启
     *
     * @return have_spec - 是否开启规格 0:没开启 1:开启
     */
    public Short getHaveSpec() {
        return haveSpec;
    }

    /**
     * 设置是否开启规格 0:没开启 1:开启
     *
     * @param haveSpec 是否开启规格 0:没开启 1:开启
     */
    public void setHaveSpec(Short haveSpec) {
        this.haveSpec = haveSpec;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取最近一次修改时间
     *
     * @return last_modify - 最近一次修改时间
     */
    public Date getLastModify() {
        return lastModify;
    }

    /**
     * 设置最近一次修改时间
     *
     * @param lastModify 最近一次修改时间
     */
    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    /**
     * 获取访问次数
     *
     * @return view_count - 访问次数
     */
    public Integer getViewCount() {
        return viewCount;
    }

    /**
     * 设置访问次数
     *
     * @param viewCount 访问次数
     */
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    /**
     * 获取购买次数
     *
     * @return buy_count - 购买次数
     */
    public Integer getBuyCount() {
        return buyCount;
    }

    /**
     * 设置购买次数
     *
     * @param buyCount 购买次数
     */
    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    /**
     * 获取是否可用 1:可用 0:不可用
     *
     * @return disabled - 是否可用 1:可用 0:不可用
     */
    public Short getDisabled() {
        return disabled;
    }

    /**
     * 设置是否可用 1:可用 0:不可用
     *
     * @param disabled 是否可用 1:可用 0:不可用
     */
    public void setDisabled(Short disabled) {
        this.disabled = disabled;
    }

    /**
     * 获取库存
     *
     * @return store - 库存
     */
    public Integer getStore() {
        return store;
    }

    /**
     * 设置库存
     *
     * @param store 库存
     */
    public void setStore(Integer store) {
        this.store = store;
    }

    /**
     * 获取积分
     *
     * @return point - 积分
     */
    public Integer getPoint() {
        return point;
    }

    /**
     * 设置积分
     *
     * @param point 积分
     */
    public void setPoint(Integer point) {
        this.point = point;
    }

    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取评价值
     *
     * @return grade - 评价值
     */
    public Integer getGrade() {
        return grade;
    }

    /**
     * 设置评价值
     *
     * @param grade 评价值
     */
    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    /**
     * 获取礼包图片
     *
     * @return image - 礼包图片
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置礼包图片
     *
     * @param image 礼包图片
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * 获取是否是团购
     *
     * @return isgroupbuy - 是否是团购
     */
    public Short getIsgroupbuy() {
        return isgroupbuy;
    }

    /**
     * 设置是否是团购
     *
     * @param isgroupbuy 是否是团购
     */
    public void setIsgroupbuy(Short isgroupbuy) {
        this.isgroupbuy = isgroupbuy;
    }

    /**
     * 获取体积
     *
     * @return volume - 体积
     */
    public BigDecimal getVolume() {
        return volume;
    }

    /**
     * 设置体积
     *
     * @param volume 体积
     */
    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    /**
     * 获取配送方式ID
     *
     * @return SHIP_ID - 配送方式ID
     */
    public Integer getShipId() {
        return shipId;
    }

    /**
     * 设置配送方式ID
     *
     * @param shipId 配送方式ID
     */
    public void setShipId(Integer shipId) {
        this.shipId = shipId;
    }

    /**
     * 获取配送方式
     *
     * @return SHIP_TYPE - 配送方式
     */
    public String getShipType() {
        return shipType;
    }

    /**
     * 设置配送方式
     *
     * @param shipType 配送方式
     */
    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    /**
     * 获取配送费用
     *
     * @return SHIP_AMOUNT - 配送费用
     */
    public BigDecimal getShipAmount() {
        return shipAmount;
    }

    /**
     * 设置配送费用
     *
     * @param shipAmount 配送费用
     */
    public void setShipAmount(BigDecimal shipAmount) {
        this.shipAmount = shipAmount;
    }

    /**
     * 获取店铺id
     *
     * @return shop_store_id - 店铺id
     */
    public Integer getShopStoreId() {
        return shopStoreId;
    }

    /**
     * 设置店铺id
     *
     * @param shopStoreId 店铺id
     */
    public void setShopStoreId(Integer shopStoreId) {
        this.shopStoreId = shopStoreId;
    }

    /**
     * 获取礼包详情
     *
     * @return intro - 礼包详情
     */
    public String getIntro() {
        return intro;
    }

    /**
     * 设置礼包详情
     *
     * @param intro 礼包详情
     */
    public void setIntro(String intro) {
        this.intro = intro;
    }

    /**
     * 获取参数字串
     *
     * @return params - 参数字串
     */
    public String getParams() {
        return params;
    }

    /**
     * 设置参数字串
     *
     * @param params 参数字串
     */
    public void setParams(String params) {
        this.params = params;
    }

    /**
     * 获取规格字串
     *
     * @return specs - 规格字串
     */
    public String getSpecs() {
        return specs;
    }

    /**
     * 设置规格字串
     *
     * @param specs 规格字串
     */
    public void setSpecs(String specs) {
        this.specs = specs;
    }
}