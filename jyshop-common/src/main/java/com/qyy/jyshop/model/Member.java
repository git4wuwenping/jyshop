package com.qyy.jyshop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户
 */
@Entity
@Table(name = "shop_member")
public class Member implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long memberId; // 用户id

	@Column(name = "parent_id")
	private Integer parentId; // 推荐会员ID

	@Column(name = "lv_id")
	private Integer lvId; // 会员等级
	private String uname; // 用户名
	private String email; // 邮箱
	private String password; // 密码
	private Timestamp regtime; // 注册时间
	private String name; // 真实姓名
	private Integer sex; // 性别
	private Timestamp birthday; // 生日
	private BigDecimal advance; // 预存款

	@Column(name = "cloud_point")
	private BigDecimal cloudPoint; // 云积分
	@Column(name = "yellow_point")
	private BigDecimal yellowPoint; // 黄积分
	@Column(name = "red_point")
	private BigDecimal redPoint; // 红积分

	@Column(name = "identity_id")
	private Integer identityId; // 会员身份
	private BigDecimal balance; // 余额
	private BigDecimal amount; // 累计交易金额
	@Column(name = "trade_time")
	private Integer tradeTime; // 交易次数

	@Column(name = "lower_count")
	private Integer lowerCount; // 下级数量
	private String remarks; // 备注

	@Column(name = "province_id")
	private Integer provinceId; // 省id

	@Column(name = "city_id")
	private Integer cityId; // 城市id

	@Column(name = "region_id")
	private Integer regionId; // 县id

	private String province; // 省
	private String city; // 城市
	private String region; // 县
	private String address; // 详细地址
	private String zip; // 邮编
	private String mobile; // 手机号
	private String tel; // 电话号码
	private Timestamp lastlogin; // 最近登录时间
	private Integer logincount; // 登录次数
	private String registerip; // 注册ip地址
	private String face; // 头像
	private String nickname; // 昵称
	private Integer state; // 黑名单 0:黑名单 1:白名单

	@Column(name = "q_r_code_path")
	private String qRCodePath; // 微信图片地址

	@Column(name = "open_id")
	private String openId; // 微信openid
	@Column(name = "union_id")
	private String unionId; // 微信unionid

	@Column(name = "weixin_face")
	private String weixinFace; // 微信头像（本地头像为空就读取微信头像）
	private Integer subscribe; // 是否关注

	@Column(name = "subscribe_time")
	private Timestamp subscribeTime; // 关注时间

	@Column(name = "pay_password")
	private String payPassword; // 支付密码

	@Column(name = "profit_time")
	private Timestamp profitTime;

	@Column(name = "is_profit")
	private Integer isProfit; // 1-分润会员

	@Column(name = "become_lower_time")
	private Timestamp becomeLowerTime;

	@Column(name = "become_shopowner_time")
	private Timestamp becomeShopownerTime;
	
	@Column(name = "coin")
	private BigDecimal coin; // 金币

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getLvId() {
		return lvId;
	}

	public void setLvId(Integer lvId) {
		this.lvId = lvId;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getRegtime() {
		return regtime;
	}

	public void setRegtime(Timestamp regtime) {
		this.regtime = regtime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Timestamp getBirthday() {
		return birthday;
	}

	public void setBirthday(Timestamp birthday) {
		this.birthday = birthday;
	}

	public BigDecimal getAdvance() {
		return advance;
	}

	public void setAdvance(BigDecimal advance) {
		this.advance = advance;
	}

	public BigDecimal getCloudPoint() {
		return cloudPoint;
	}

	public void setCloudPoint(BigDecimal cloudPoint) {
		this.cloudPoint = cloudPoint;
	}

	public BigDecimal getYellowPoint() {
		return yellowPoint;
	}

	public void setYellowPoint(BigDecimal yellowPoint) {
		this.yellowPoint = yellowPoint;
	}

	public BigDecimal getRedPoint() {
		return redPoint;
	}

	public void setRedPoint(BigDecimal redPoint) {
		this.redPoint = redPoint;
	}

	public Integer getIdentityId() {
		return identityId;
	}

	public void setIdentityId(Integer identityId) {
		this.identityId = identityId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(Integer tradeTime) {
		this.tradeTime = tradeTime;
	}

	public Integer getLowerCount() {
		return lowerCount;
	}

	public void setLowerCount(Integer lowerCount) {
		this.lowerCount = lowerCount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Timestamp getLastlogin() {
		return lastlogin;
	}

	public void setLastlogin(Timestamp lastlogin) {
		this.lastlogin = lastlogin;
	}

	public Integer getLogincount() {
		return logincount;
	}

	public void setLogincount(Integer logincount) {
		this.logincount = logincount;
	}

	public String getRegisterip() {
		return registerip;
	}

	public void setRegisterip(String registerip) {
		this.registerip = registerip;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public String getWeixinFace() {
		return weixinFace;
	}

	public void setWeixinFace(String weixinFace) {
		this.weixinFace = weixinFace;
	}

	public Integer getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(Integer subscribe) {
		this.subscribe = subscribe;
	}

	public Timestamp getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(Timestamp subscribeTime) {
		this.subscribeTime = subscribeTime;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public Timestamp getProfitTime() {
		return profitTime;
	}

	public void setProfitTime(Timestamp profitTime) {
		this.profitTime = profitTime;
	}

	public Integer getIsProfit() {
		return isProfit;
	}

	public void setIsProfit(Integer isProfit) {
		this.isProfit = isProfit;
	}

	public Timestamp getBecomeLowerTime() {
		return becomeLowerTime;
	}

	public void setBecomeLowerTime(Timestamp becomeLowerTime) {
		this.becomeLowerTime = becomeLowerTime;
	}

	public String getqRCodePath() {
		return qRCodePath;
	}

	public void setqRCodePath(String qRCodePath) {
		this.qRCodePath = qRCodePath;
	}

	public BigDecimal getCoin() {
		return coin;
	}

	public void setCoin(BigDecimal coin) {
		this.coin = coin;
	}
	public Timestamp getBecomeShopownerTime() {
		return becomeShopownerTime;
	}

	public void setBecomeShopownerTime(Timestamp becomeShopownerTime) {
		this.becomeShopownerTime = becomeShopownerTime;
	}

}
