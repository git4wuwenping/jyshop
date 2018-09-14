package com.qyy.jyshop.giftpackage.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.dao.GiftPackageProductDao;
import com.qyy.jyshop.giftpackage.service.GiftPackageProductService;
import com.qyy.jyshop.model.GiftPackageProduct;
import com.qyy.jyshop.supple.AbstratService;
@Service
public class GiftPackageProductServiceImpl extends AbstratService<GiftPackageProduct> implements GiftPackageProductService {
	@Autowired
	private GiftPackageProductDao giftPackageProductDao;
	@Override
	public List<Map<String, Object>> queryByGpId(Long gpId) {
		 List<Map<String, Object>> products = giftPackageProductDao.selectGiftPackageProductByGpProductId(gpId);
		 for (Map<String, Object> map : products) {
			String specValueIds = (String) map.get("specValueIds");
			if(specValueIds!=null){
			String[] strings = specValueIds.split(",");
			 int[] arr = new int[strings.length];  
		        for(int i =0;i<strings.length;i++){
		        	arr[i]= Integer.parseInt(strings[i]);
		        }  
		        for(int i =0;i<arr.length-1;i++) {  
		                for(int j=i+1;j<arr.length;j++) {  
		                    if(arr[i]>arr[j])  {
		                    int temp = arr[i];  
		                    arr[i] = arr[j];  
		                    arr[j]= temp;   
		                }  
				}
		    }
		        StringBuilder sb = new StringBuilder();
		        for(int i =0;i<arr.length;i++)  
		        {  
		            if(i!=arr.length-1)  
		            {  
		                sb.append(arr[i]+",");  
		            }  
		            if(i==arr.length-1)  
		            {  
		                sb.append(arr[i]+"");  
		            }  
		        }  
		        map.put("specValueIds",sb);
		}
    }
    	return products;
	}
	
}
