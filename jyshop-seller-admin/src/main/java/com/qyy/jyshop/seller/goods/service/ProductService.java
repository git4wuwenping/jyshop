package com.qyy.jyshop.seller.goods.service;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.Goods;

public interface ProductService {
    
    /**
     * 根据商品ID获取货品列表
     * @author hwc
     * @created 2018年1月4日 上午10:05:20
     * @param goodsId
     * @return
     */
   public List<Map<String,Object>> queryByGoodsId(Long goodsId);
    

   /**
    * 保存货品
    * @author hwc
    * @created 2018年1月4日 下午7:22:19
    * @param productSnArray
    * @param storeArray
    * @param goods
    * @param costArray
    * @param priceArray
    * @param weightArray
    * @param specValueIdArray
    * @param specIdArray
    * @param productIdList
    */
   public void doSaveProduct(String[] productSnArray,String[] storeArray,
           Goods goods,  String[] costArray,String[] priceArray, String[] weightArray, String[] specValueIdArray,
           String[] specsArray,String[] specIdArray,List<Long> productIdList);
}
