package com.qyy.jyshop.goods.server.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.dao.ProductDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.goods.server.ProductService;
import com.qyy.jyshop.model.Product;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.StringUtil;

@Service
public class ProductServiceImpl extends AbstratService<Product> implements ProductService{

    @Autowired
    private ProductDao productDao;
    
    @Override
    public List<Map<String, Object>> queryByGoodsId(Long goodsId) {
        if(StringUtil.isEmpty(goodsId))
            throw new AppErrorException("商品ID不能为空...");
    	List<Map<String, Object>> products = this.productDao.selectByGoodsId(goodsId);
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
