package com.qyy.jyshop.admin.store.service.Impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.qyy.jyshop.admin.store.service.StoreService;
import com.qyy.jyshop.dao.ProductStoreDao;
import com.qyy.jyshop.dao.SpecValuesDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.ProductStore;
import com.qyy.jyshop.model.SpecValues;
import com.qyy.jyshop.util.StringUtil;
import com.qyy.jyshop.util.TimestampUtil;

@Service
public class StoreServiceImpl  implements StoreService {
	@Autowired
	private ProductStoreDao productstoreDao;
	@Autowired
	private SpecValuesDao specValuesDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> selectProductStoreByGoodsId(Long goodsId) {
		List<Map<String, Object>> productStoreList = 
				productstoreDao.selectProductStoreByGoodsId(goodsId);
		for (int j = 0; j < productStoreList.size(); j++) {
			String specValueIds = (String) productStoreList.get(j).get("specValueId");
			String[] specValueIdsArr = specValueIds.split(",");
			String string ="";
			for (int i = 0;i<specValueIdsArr.length;i++) {
				SpecValues specValues = this.specValuesDao.selectByPrimaryKey(Long.parseLong(specValueIdsArr[i]));
				if(i<specValueIdsArr.length-1){
					string+=specValues.getSpecValue()+",";
				}else{
					string+=specValues.getSpecValue();
				}
			}
			productStoreList.get(j).put("specValue",string);
		}
		return productStoreList;
	}

	@Override
	public String editStore(String dataJson) {
		JSONArray fromObject = JSONArray.parseArray(dataJson);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> jsonList = (List<Map<String, Object>>) JSON.parse(dataJson);
		if(jsonList.size()>0 && jsonList!=null){
			Map<String, Object> goodsIdmap = (Map<String, Object>)jsonList.get(jsonList.size()-1);	//商品id
			Long goodsId = Long.parseLong(goodsIdmap.get("goodsId").toString());
			List<ProductStore> productStoreList = productstoreDao.selectProductStoreAndStoreId(goodsId);	//根据商品id查找有多少数据
			if(productStoreList!=null && productStoreList.size()>0){
				int num = productStoreList.size();
				for(Map<String, Object> mapList : jsonList){ 
					//String storeValue = mapList.get("storeId").toString();
					//String usableValue = mapList.get("usableStore").toString();
					if(!StringUtil.isEmpty(mapList.get("storeId")) 
							&& !StringUtil.isEmpty(mapList.get("usableStore"))){
						Long storeId = Long.parseLong(mapList.get("storeId").toString());
						int usableStore = Integer.parseInt(mapList.get("usableStore").toString());
						System.out.println(storeId+"---"+usableStore);
							for (int i = 0; i<num; i++) {
								//设置一个布尔值，如果变动字段设置为false,如果不变动则设置为true
								if(usableStore >= 0 && usableStore < 9999){
									boolean bool = true; 	
									//判断哪些库存更改过
									if(  storeId.equals(productStoreList.get(i).getStoreId()) 
											&& usableStore!=productStoreList.get(i).getUsableStore()){	
										bool=false;
									}
									if(bool == false){		//库存有修改
										productstoreDao.updateProductStoreByStoreId(storeId,usableStore,TimestampUtil.getNowTime());
									}
									
								}else{
									 throw new RuntimeException("库存值在0到9999之间");
								}
							}
					}
				}  	
			}else{
				new AppErrorException("该商品下不存在货品请联系管理员或者运营部门");
			}
		}else{
			new AppErrorException("数据错误。。。");
		}
		return null;
	}
	
}
