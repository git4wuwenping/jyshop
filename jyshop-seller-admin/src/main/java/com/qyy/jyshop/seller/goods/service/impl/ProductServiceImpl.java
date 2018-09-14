package com.qyy.jyshop.seller.goods.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.seller.common.annotation.ServiceLog;
import com.qyy.jyshop.seller.goods.service.ProductService;
import com.qyy.jyshop.dao.DepotDao;
import com.qyy.jyshop.dao.GoodsSpecDao;
import com.qyy.jyshop.dao.ProductDao;
import com.qyy.jyshop.dao.ProductStoreDao;
import com.qyy.jyshop.dao.SpecValuesDao;
import com.qyy.jyshop.model.Depot;
import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.model.GoodsSpec;
import com.qyy.jyshop.model.Product;
import com.qyy.jyshop.model.ProductStore;
import com.qyy.jyshop.model.SpecValues;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.StringUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl extends AbstratService<Product> implements ProductService {

    @Autowired
    private DepotDao depotDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductStoreDao productStoreDao;
    @Autowired
    private GoodsSpecDao goodsSpecDao;
    @Autowired
    private SpecValuesDao specValuesDao;

    @ServiceLog("获取货品列表")
    @Override
    public List<Map<String,Object>> queryByGoodsId(Long goodsId){
        List<Map<String,Object>> mapList = productDao.selectByGoodsId(goodsId);
        for (Map<String,Object> map:mapList) {

            GoodsSpec goodsSpec = new GoodsSpec();
            goodsSpec.setProductId(Long.parseLong(map.get("productId").toString()));
            goodsSpec.setGoodsId(Long.parseLong(map.get("goodsId").toString()));
            List<GoodsSpec> goodsSpecList =  goodsSpecDao.select(goodsSpec);
            List<SpecValues> specValuesList = new ArrayList<>();
            if(!StringUtil.isEmpty(map.get("specValueIds"))){
                for (String s: map.get("specValueIds").toString().split(",")) {
                    specValuesList.add(specValuesDao.selectByPrimaryKey(Long.valueOf(s)));
                }
            }
            map.put("goodsSpecList", goodsSpecList);
            map.put("specValuesList", specValuesList);
        }
        return mapList;
    }

    @Transactional
    @Override
    public void doSaveProduct(String[] productSnArray,String[] storeArray, Goods goods, 
            String[] costArray,String[] priceArray, String[] weightArray, String[] specValueIdArray, 
            String[] specsArray,String[] specIdArray,List<Long> productIdList){
        
        if(productSnArray != null && productSnArray.length > 0){
            
            Integer sn=000;
            
            for (String productSn : productSnArray) {
                if(productSn!=null && productSn.length()==24){
    
                    Integer s=Integer.valueOf(productSn.substring(21)).intValue();
                    if(s>sn)
                        sn=s;
                }
            }
            
            //保存货品
            for (int i = 0; i < productSnArray.length; i++) {

                Product product = null;
                if(!StringUtil.isEmpty(productSnArray[i])){
                    Product p=new Product();
                    p.setSn(productSnArray[i]);
                    product = productDao.selectOne(p);
                }
                
                if(product == null){
                    //添加货品表
                    product = new Product();
                    product.setStore(Integer.parseInt(storeArray[i]));
                    product.setCost(new BigDecimal(costArray[i]));
                    product.setWeight(new BigDecimal(weightArray[i]));
                    product.setGoodsId(goods.getGoodsId());
                    product.setName(goods.getName());
                    product.setPrice(new BigDecimal(priceArray[i]).setScale(2, BigDecimal.ROUND_DOWN));
                    product.setSpecIds(specIdArray[i]);
                    product.setSpecValueIds(specValueIdArray[i]);
                    product.setComId(goods.getComId());
                    if(!StringUtil.isEmpty(specsArray[i]))
                        product.setSpecs(specsArray[i].substring(0,specsArray[i].length()-1));
                    productDao.insertSelective(product);
                    String[] specValueIds = specValueIdArray[i].split(",");
                    String[] specIds = specIdArray[i].split(",");
                    //添加商品规格表
                    for (int j = 0; j < specIds.length; j++) {
                        GoodsSpec goodsSpec = new GoodsSpec();
                        goodsSpec.setGoodsId(goods.getGoodsId());
                        goodsSpec.setProductId(product.getProductId());
                        goodsSpec.setSpecId(Long.valueOf(specIds[j]));
                        goodsSpec.setSpecValueId(Long.valueOf(specValueIds[j]));
                        goodsSpecDao.insertSelective(goodsSpec);
                    }
                    
                    Depot depot=this.depotDao.selectByComId(goods.getComId());                     
                    ProductStore productStore=new ProductStore();
                    productStore.setGoodsId(goods.getGoodsId());
                    productStore.setGoodsSn(goods.getGoodsSn());
                    productStore.setComId(goods.getComId());
                    productStore.setProductId(product.getProductId());
                    productStore.setProductSn(product.getSn());
                    productStore.setStore(product.getStore());
                    productStore.setUsableStore(product.getStore());
                    productStore.setDepotId(depot.getId());
                    productStore.setDepotCode(depot.getCode());
                    productStore.setDepotName(depot.getName());
                    productStore.setCreationTime(new Timestamp(System.currentTimeMillis()));
                    this.productStoreDao.insertSelective(productStore);
                } else {
                    //更新货品表
                    product.setStore(Integer.parseInt(storeArray[i]));
                    product.setCost(new BigDecimal(costArray[i]));
                    product.setWeight(new BigDecimal(weightArray[i]));
                    product.setGoodsId(goods.getGoodsId());
                    product.setName(goods.getName());
                    product.setPrice(new BigDecimal(priceArray[i]).setScale(2, BigDecimal.ROUND_DOWN));
                    if(!StringUtil.isEmpty(specsArray[i]))
                        product.setSpecs(specsArray[i].substring(0,specsArray[i].length()-1));
                    this.productDao.updateByPrimaryKeySelective(product);
                    String[] specValueIds = specValueIdArray[i].split(",");
                    String[] specIds = specIdArray[i].split(",");
                    //更新商品规格表
                    for (int j = 0; j < specIds.length; j++) {
                        GoodsSpec goodsSpec = new GoodsSpec();
                        goodsSpec.setGoodsId(goods.getGoodsId());
                        goodsSpec.setProductId(product.getProductId());
                        goodsSpec.setSpecId(Long.valueOf(specIds[j]));
                        goodsSpec.setSpecValueId(Long.valueOf(specValueIds[j]));
                        goodsSpecDao.updateByPrimaryKey(goodsSpec);
                    }
                    
                    if(productIdList!=null && productIdList.size()>0 &&
                            productIdList.contains(product.getProductId())){
                        endReId:for(int k=0;k<productIdList.size();k++){
                            if(product.getProductId().equals(productIdList.get(k))){
                                productIdList.remove(k);
                                break endReId;
                            }
                        }
                    }
                }
            }
        }
        
        if(productIdList!=null && productIdList.size()>0){
            this.productDao.deleteByProductIds(productIdList);
            this.goodsSpecDao.deleteByProductIds(productIdList);
            this.productStoreDao.deleteByProductIds(productIdList);
        }
    }
}
