package com.qyy.jyshop.model;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * 店铺实体
 * @author 22132
 *
 */
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name = "shop_store")
public class ShopStore implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="shop_store_id")
	private Integer shopStoreId;	//店铺id
	
	@Column(name="shop_store_name")
	private String shopStoreName;	//店铺名称
	
	@Column(name="shop_store_information")
	private String shopStoreInformation; //店铺信息
	
	@Column(name="shop_store_logo")
	private String shopStoreLogo;		//店铺logo
	
	@Column(name="shop_store_theme")
	private String shopStoreTheme;		//店铺主题
	
	@Column(name="com_id")
	private Integer comId;			//供应商id
	
	@Column(name="state")
	private Integer state;			//状态 0:可用 1:不可用
	
	@Column(name="CREATE_TIME")
	private Timestamp createTime;			//创建时间
	
	@Column(name="last_modify")
	private Timestamp lastModify;			//创建时间
	
	/*@Column(name="goods_id")
	private Integer goodsId;*/			//商品id
	@Transient
	private List<Goods> goodsList;			//商品集
	
	public List<Goods> getGoods() {
		return goodsList;
	}
	public void setGoods(List<Goods> goodsList) {
		this.goodsList = goodsList;
	}
	public Integer getShopStoreId() {
		return shopStoreId;
	}
	public void setShopStoreId(Integer shopStoreId) {
		this.shopStoreId = shopStoreId;
	}
	public String getShopStoreName() {
		return shopStoreName;
	}
	public void setShopStoreName(String shopStoreName) {
		this.shopStoreName = shopStoreName;
	}
	public String getShopStoreInformation() {
		return shopStoreInformation;
	}
	public void setShopStoreInformation(String shopStoreInformation) {
		this.shopStoreInformation = shopStoreInformation;
	}
	public String getShopStoreLogo() {
		return shopStoreLogo;
	}
	public void setShopStoreLogo(String shopStoreLogo) {
		this.shopStoreLogo = shopStoreLogo;
	}
	public String getShopStoreTheme() {
		return shopStoreTheme;
	}
	public void setShopStoreTheme(String shopStoreTheme) {
		this.shopStoreTheme = shopStoreTheme;
	}
	public Integer getComId() {
		return comId;
	}
	public void setComId(Integer comId) {
		this.comId = comId;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getLastModify() {
		return lastModify;
	}
	public void setLastModify(Timestamp lastModify) {
		this.lastModify = lastModify;
	}
	
	
}
