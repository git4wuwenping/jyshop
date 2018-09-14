package com.qyy.jyshop.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name = "shop_goods")
public class Goods implements Serializable {
    

	/**
     * 商品id
     */
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "goods_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goodsId;

    /**
     * 库存报警数
     */
    @Column(name = "alarm_num")
    private Integer alarmNum;

    /**
     * 商品号
     */
    @Column(name = "goods_sn")
    private String goodsSn;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 品牌id
     */
    @Column(name = "brand_id")
    private Integer brandId;

    /**
     * 分类id
     */
    @Column(name = "cat_id")
    private Integer catId;

    @Column(name = "cat_name")
    private String catName; //分类名称
    
    /**
     * 供应商id
     */
    @Column(name = "com_id")
    private Integer comId;

    /**
     * 类型id
     */
    @Column(name = "type_id")
    private Long typeId;

    /**
     * 商品类型
     */
    @Column(name = "goods_type")
    private Integer goodsType;

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
    private Integer marketEnable;

    /**
     * 商品简介
     */
    private String brief;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品成本
     */
    private BigDecimal cost;

    /**
     * 市场价格
     */
    private BigDecimal mktprice;

    /**
     * 是否开启规格
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
     * 是否可用
     */
    private Short disabled;

    /**
     * 库存
     */
    private Integer store;

    /**
     * 黄积分
     *//*
    @Column(name = "yellow_point")
    private BigDecimal yellowPoint;
    *//**
     * 红积分
     *//*
    @Column(name = "redPoint")
    private BigDecimal red_point;*/

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 评价值
     */
    private Integer grade;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 是否是团购
     */
    private Short isgroupbuy;

    /**
     * 商品详情
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
     * 体积
     */
    private BigDecimal volume;
    /**
     * 配送方式id
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
    private String shipAmount;
    
    @Column(name = "dly_type_id")
    private Long dlyTypeId;
    
    /**
     * 图片二维码
     */
    @Column(name = "q_r_code_path")
    private String qRCodePath;
    
    public String getqRCodePath() {
		return qRCodePath;
	}

	public void setqRCodePath(String qRCodePath) {
		this.qRCodePath = qRCodePath;
	}

	/**
     * 店铺
     * @return
     */
    @Column(name = "shop_store_id")
    private Integer shopStoreId;
    public Integer getShopStoreId() {
		return shopStoreId;
	}

	public void setShopStoreId(Integer shopStoreId) {
		this.shopStoreId = shopStoreId;
	}

	@Transient
    private ShopStore shopStore;


	public ShopStore getShopStore() {
		return shopStore;
	}

	public void setShopStore(ShopStore shopStore) {
		this.shopStore = shopStore;
	}

	public String getShipAmount() {
		return shipAmount;
	}

	public void setShipAmount(String shipAmount) {
		this.shipAmount = shipAmount;
	}

	/**
     * 获取商品id
     *
     * @return goods_id - 商品id
     */
    public Long getGoodsId() {
        return goodsId;
    }

    /**
     * 设置商品id
     *
     * @param goodsId 商品id
     */
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
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
     * 获取商品号
     *
     * @return goods_sn - 商品号
     */
    public String getGoodsSn() {
        return goodsSn;
    }

    /**
     * 设置商品号
     *
     * @param goodsSn 商品号
     */
    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
    }

    /**
     * 获取商品名称
     *
     * @return name - 商品名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置商品名称
     *
     * @param name 商品名称
     */
    public void setName(String name) {
        this.name = name;
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
     * 获取分类id
     *
     * @return cat_id - 分类id
     */
    public Integer getCatId() {
        return catId;
    }
    
    /**
     * 设置分类id
     *
     * @param catId 分类id
     */
    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    /**
     * 获取供应商id
     *
     * @return com_id - 供应商id
     */
    public Integer getComId() {
        return comId;
    }

    /**
     * 设置供应商id
     *
     * @param comId 供应商id
     */
    public void setComId(Integer comId) {
        this.comId = comId;
    }

    /**
     * 获取类型id
     *
     * @return type_id - 类型id
     */
    public Long getTypeId() {
        return typeId;
    }

    /**
     * 设置类型id
     *
     * @param typeId 类型id
     */
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    /**
     * 获取商品类型
     *
     * @return goods_type - 商品类型
     */
    public Integer getGoodsType() {
        return goodsType;
    }

    /**
     * 设置商品类型
     *
     * @param goodsType 商品类型
     */
    public void setGoodsType(Integer goodsType) {
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
    public Integer getMarketEnable() {
        return marketEnable;
    }

    /**
     * 设置是否上架
     *
     * @param marketEnable 是否上架
     */
    public void setMarketEnable(Integer marketEnable) {
        this.marketEnable = marketEnable;
    }

    /**
     * 获取商品简介
     *
     * @return brief - 商品简介
     */
    public String getBrief() {
        return brief;
    }

    /**
     * 设置商品简介
     *
     * @param brief 商品简介
     */
    public void setBrief(String brief) {
        this.brief = brief;
    }

    /**
     * 获取商品价格
     *
     * @return price - 商品价格
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置商品价格
     *
     * @param price 商品价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    /*public BigDecimal getYellowPoint() {
		return yellowPoint;
	}

	public void setYellowPoint(BigDecimal yellowPoint) {
		this.yellowPoint = yellowPoint;
	}

	public BigDecimal getRed_point() {
		return red_point;
	}

	public void setRed_point(BigDecimal red_point) {
		this.red_point = red_point;
	}*/
    /**
     * 获取商品成本
     *
     * @return cost - 商品成本
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * 设置商品成本
     *
     * @param cost 商品成本
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
     * 获取是否开启规格
     *
     * @return have_spec - 是否开启规格
     */
    public Short getHaveSpec() {
        return haveSpec;
    }

    /**
     * 设置是否开启规格
     *
     * @param haveSpec 是否开启规格
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
     * 获取是否可用
     *
     * @return disabled - 是否可用
     */
    public Short getDisabled() {
        return disabled;
    }

    /**
     * 设置是否可用
     *
     * @param disabled 是否可用
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
     * 获取商品图片
     *
     * @return image - 商品图片
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置商品图片
     *
     * @param image 商品图片
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
     * 获取商品详情
     *
     * @return intro - 商品详情
     */
    public String getIntro() {
        return intro;
    }

    /**
     * 设置商品详情
     *
     * @param intro 商品详情
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

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public Integer getShipId() {
		return shipId;
	}

	public void setShipId(Integer shipId) {
		this.shipId = shipId;
	}

	public String getShipType() {
		return shipType;
	}

	public void setShipType(String shipType) {
		this.shipType = shipType;
	}

    public Long getDlyTypeId() {
        return dlyTypeId;
    }

    public void setDlyTypeId(Long dlyTypeId) {
        this.dlyTypeId = dlyTypeId;
    }
    
}