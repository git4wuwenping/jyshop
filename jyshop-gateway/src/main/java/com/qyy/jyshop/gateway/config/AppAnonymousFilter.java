package com.qyy.jyshop.gateway.config;

public class AppAnonymousFilter {

	/**
	 * 匿名可访问的APP资源
	 */
	public static final String[] APP_ANONYMOUS =new String[]{
			"/sendCode",
			"/queryAddress",
			"/homeGoodsList",					//首页商品列表
			"/hotGoods",						//首页热门商品
			"/getGoodsCatData",					//商品类别
			"/orderGoods",						//商品列表排序
			"/goodsDetails",					//商品详情
			"/shopStoreInfo",					//店铺信息以及店铺下商品信息
			"/getGoodsByCatId",					//根据商品类别查询商品列表
			"/payReturn",   
			"/appPayReturn",
			"/appOrderPayReturn",
			"/appGiftOrderPayReturn",           //店长礼包回调
			"/appAlipayOrderPayReturn",
			"/toLogiReturn",
			"/mobileCheck",						//手机验证码发送
			"/checkCode",						//验证码校验
			"/shopownerGoods",					//店长礼包
			"/isBindingMobile",					//是否绑定手机
			"/LogisticsInfo",					//物流信息
			"/wxLogin",                   
			"/wxCode",                   
			"/h5OrderPayReturn",
			"/h5GiftOrderPayReturn", 
			"/h5PayReturn",
			"/getGiftPackageList",				//店长礼包列表
			"/giftPackageDetails",				//店长礼包详情
			"/selectGoodsByParams",				//根据条件查询商品
			"/selectGoodsByFirCatId",			//查询一级分类下的所有商品
			"/examineState",
			"/memberRegister",					//手机注册
			"/goodsCommentList",				//商品详情评价列表
			"/queryAllUser",
			"/sendInfo",
			"/onLogin"
	};
	
	public static final String APP_ANONYMOUS_STR ="/anon";
}
