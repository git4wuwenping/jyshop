package com.qyy.jyshop.admin.shopstore.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.admin.shopstore.service.ShopStoreService;
import com.qyy.jyshop.dao.ShopStoreDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.ShopStore;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.EntityReflectUtil;
import com.qyy.jyshop.util.StringUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
@Service
public class ShopStoreServiceImpl extends AbstratService<ShopStore> implements ShopStoreService {
	@Autowired
	private ShopStoreDao shopStoreDao;

	@Override
	public List<ShopStore> findAllStores() {
		//List<Map<String,Object>> queryAll = storeDao.findStoreByComId(comId);
		return this.queryAll();
	}


	/**
	 * 根据店铺id 查询店铺
	 */
	@Override
	public ShopStore findStoreByGoodId(int shopStoreId) {
		return this.queryByID(shopStoreId);
	}


	@Override
	public PageAjax<Map<String, Object>> selectShopStoreByParams(PageAjax<ShopStore> page) {
		ParamData params = this.getParamData(page.getPageNo(),page.getPageSize());
        //params.put("state", state);
		PageAjax<Map<String,Object>> pageQuery = this.pageQuery("ShopStoreDao.selectShopStoreByParams", params);
		List<Map<String,Object>> rowsList = pageQuery.getRows();
		for (Map<String, Object> map : rowsList) {
			int shopStoreId = (int) map.get("shopStoreId");
			List<Map<String, Object>> countMap =
					shopStoreDao.selectGoodsAndOrderCountsById(shopStoreId);
			for (Map<String, Object> map2 : countMap) {
				map.put((String) map2.get("type"), map2.get("counts"));
			}
		}
        return pageQuery;
	}


	@Override
	public String addShopStore(Map<String, Object> map) {
		ShopStore shopStore = (ShopStore) EntityReflectUtil.toBean(ShopStore.class,map);
		Timestamp d = new Timestamp(System.currentTimeMillis());
		Integer comId = this.getLoginUserComId();
		shopStore.setCreateTime(d);
		shopStore.setLastModify(d);
		shopStore.setComId(comId);
		shopStoreDao.insert(shopStore);
		return null;
	}


	@Override
	public ShopStore selectShopStoreById(int shopStoreId) {
		return  this.queryByID(shopStoreId);
	}


	@Override
	public String editShopStore(Map<String, Object> map) {
		ShopStore shopStore = EntityReflectUtil.toBean(ShopStore.class,map);
		Timestamp d = new Timestamp(System.currentTimeMillis());
		shopStore.setLastModify(d);
		shopStoreDao.updateStoreById(shopStore);
		return null;
	}


	@Override
	public String deleteShopStore(int shopStoreId) {
		//查询改店铺下是否有商品
		List<Map<String, Object>> goodsList = shopStoreDao.selectGoodListByShopId(shopStoreId);
		if(goodsList.size()>0){
			throw new AppErrorException("该店铺下存在商品不能删除");
		}else{
			Example example = new Example(ShopStore.class);
			Criteria createCriteria = example.createCriteria();
			createCriteria.andEqualTo("shopStoreId", shopStoreId);
			shopStoreDao.deleteByExample(example);
		}
		return null;
	}


	@Override
	public List<ShopStore> findAllDirShopStores(Integer comId) {
		
		return shopStoreDao.findAllDirShopStores(comId);
	}


	@Override
	public String isDisabledExamine(int shopStoreId, Integer valueOf) {
		shopStoreDao.isDisabledExamine(shopStoreId,valueOf);
		return null;
	}


	@Override
	public String selectShopStoreByName(String shopStoreName) {
		if(!StringUtil.isEmpty(shopStoreName)){
			if(shopStoreName.length()<10){
				int count = shopStoreDao.selectShopStoreByName(shopStoreName);
				if(count==0){
					return "false";
				}else{
					throw new AppErrorException("用户名已经被占用");
				}
			}else{
				throw new AppErrorException("店铺名称不能超过10个字");
			}
		}else{
			throw new AppErrorException("店铺名称不能为空");
		}
	}


	@Override
	public String checkShopStoreInformation(String shopStoreInformation) {
		if(StringUtil.isEmpty(shopStoreInformation)){
			throw new AppErrorException("店铺信息不能为空");
		}
		return "false";
	}

}
