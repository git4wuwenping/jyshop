package com.qyy.jyshop.admin.goods.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;

import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.goods.service.GoodsGalleryService;
import com.qyy.jyshop.admin.goods.service.GoodsService;
import com.qyy.jyshop.admin.goods.service.ProductService;
import com.qyy.jyshop.admin.profit.service.ProfitParamService;
import com.qyy.jyshop.dao.CompanyDao;
import com.qyy.jyshop.dao.DepotDao;
import com.qyy.jyshop.dao.GoodsCatDao;
import com.qyy.jyshop.dao.GoodsDao;
import com.qyy.jyshop.dao.GoodsGalleryDao;
import com.qyy.jyshop.dao.GoodsSpecDao;
import com.qyy.jyshop.dao.ProductDao;
import com.qyy.jyshop.dao.ProductStoreDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.AuthUser;
import com.qyy.jyshop.model.Depot;
import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.model.Product;
import com.qyy.jyshop.model.ProductStore;
import com.qyy.jyshop.pojo.DataTablePage;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.EntityReflectUtil;
import com.qyy.jyshop.util.StringUtil;

@Service
public class GoodsServiceImpl extends AbstratService<Goods> implements GoodsService {
	
	@Autowired
	private ProfitParamService ProfitServiceImpl;
    @Autowired
    private GoodsGalleryDao goodsGalleryDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsCatDao goodsCatDao;
    @Autowired
    private GoodsGalleryService goodsGalleryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private GoodsSpecDao goodsSpecDao;
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private DepotDao depotDao;
    @Autowired
    private ProductStoreDao productStoreDao;
    
    @ServiceLog("查询商品详情")
    @Override
    public Goods queryByGoodsId(Long goodsId){
        return this.queryByID(goodsId);
    }
    
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @ServiceLog("查询商品分页列表")
    @Override
    public PageAjax<Map<String,Object>> pageGoods(PageAjax<Map<String,Object>> page,Integer state) {
         
        ParamData params = this.getParamData(page.getPageNo(),page.getPageSize());
        return this.pageQuery("GoodsDao.selectGoodsByParams", params);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @ServiceLog("添加商品")
    @Override
    public String doAddGoods(Map<String, Object> map) {
        HttpServletRequest request = getRequest();
        Goods goods = EntityReflectUtil.toBean(Goods.class,map);
        if(goods == null)
            throw new RuntimeException("商品为空");
        
        if(!StringUtil.isEmpty(goods.getGoodsSn()) && 
                this.goodsDao.selectByGoodsSn(goods.getGoodsSn())!=null){
            throw new AppErrorException("商品货号已存在...");
        }
        

        String[] imgArray = request.getParameterValues("img");
        String[] specIdArray = request.getParameterValues("spec_ids"); // 规格id数组
        String[] storeArray = request.getParameterValues("stores");//库存数组
        if(imgArray != null && imgArray.length > 0){
            goods.setImage(imgArray[0]);
        }
        goods.setCatName(map.get("catNamePath").toString());
        
        if(StringUtil.isEmpty(goods.getComId())){
            goods.setComId(0);
        }
        if(storeArray!=null && storeArray.length>0){
            Integer totalStore = 0;
            for (String s:storeArray) {
                totalStore += Integer.parseInt(s);
            }
            goods.setStore(totalStore);
        }

        
        synchronized (this) {
            goods.setGoodsSn(String.valueOf(System.currentTimeMillis()));
            //保存商品
            this.insertSelective(goods);
        }
        
        //保存相册
        goodsGalleryService.saveGoodsGallery(imgArray, goods.getGoodsId());
            
            if(specIdArray!=null && specIdArray.length>0){
                String[] productSnArray = request.getParameterValues("productSns");//sn数组
              
                String[] specValueIdArray = request.getParameterValues("spec_value_ids");// 规格值id数组
                String[] specsArray = request.getParameterValues("specs");
                String[] weightArray= request.getParameterValues("weights");//重量数组
                String[] costArray = request.getParameterValues("costs");//成本数组
                String[] priceArray = request.getParameterValues("prices");//价格数组          
                this.productService.doSaveProduct(productSnArray,storeArray, goods, costArray,priceArray, weightArray,
                        specValueIdArray,specsArray, specIdArray,null);
            }else{//+"01"
                Product product=new Product();
                product.setGoodsId(goods.getGoodsId());
                product.setName(goods.getName());
                product.setStore(goods.getStore());
                product.setCost(goods.getCost());
                product.setPrice(goods.getPrice());
                product.setWeight(goods.getWeight());
                product.setComId(goods.getComId());
                
                
                this.productDao.insertSelective(product);
                
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
            }
        
        return null;
    }

    @Transactional
    @ServiceLog("编辑商品")
    @Override
    public String editGoods(Map<String, Object> map) {
        HttpServletRequest request = getRequest();
        Goods goods = EntityReflectUtil.toBean(Goods.class,map);
        if(goods == null)
            throw new RuntimeException("商品为空");
        
        Goods oldGoods=this.goodsDao.selectByGoodsSn(goods.getGoodsSn());
        if(oldGoods!=null && !oldGoods.getGoodsId().equals(goods.getGoodsId())){
            throw new AppErrorException("商品货号已存在...");
        }
        
        String[] imgArray = request.getParameterValues("img");
        String[] specIdArray = request.getParameterValues("spec_ids"); // 规格id数组
        String[] storeArray = request.getParameterValues("stores");//库存数组
        //第一张图片设置为主图
        if(imgArray != null && imgArray.length > 0){
            goods.setImage(imgArray[0]);
        }
        
        if(storeArray!=null && storeArray.length>0){
            Integer totalStore = 0;
            for (String s:storeArray) {
                totalStore += Integer.parseInt(s);
            }
            goods.setStore(totalStore);
        }
        goods.setCatName(map.get("catNamePath").toString());
        goods.setMarketEnable(Integer.valueOf(DictionaryUtil.getDataValueByName("goods_market_enable", "sales_apply")));
        this.goodsDao.updateByPrimaryKeySelective(goods);
        if(StringUtil.isEmpty(goods.getComId())){
            goods.setComId(0);
        }
        //保存相册
        goodsGalleryService.saveGoodsGallery(imgArray, goods.getGoodsId());
        
        List<Long> productIdList=this.productDao.selectOfProductId(goods.getGoodsId());
        if(specIdArray!=null && specIdArray.length>0){
            String[] productSnArray = request.getParameterValues("productSns");//sn数组
            String[] specValueIdArray = request.getParameterValues("spec_value_ids");// 规格值id数组
            String[] specsArray = request.getParameterValues("specs");
            String[] weightArray= request.getParameterValues("weights");//重量数组
            String[] costArray = request.getParameterValues("costs");//成本数组
            String[] priceArray = request.getParameterValues("prices");//价格数组        
            this.productService.doSaveProduct(productSnArray,storeArray, goods, costArray,priceArray, weightArray,
                    specValueIdArray,specsArray, specIdArray,productIdList);
        }else{
            
            if(productIdList!=null && productIdList.size()>0 && productIdList.size()==1){
                Product product=new Product();
                product.setProductId(productIdList.get(0));
                product.setName(goods.getName());
                product.setStore(goods.getStore());
                product.setCost(goods.getCost());
                product.setPrice(goods.getPrice());
                product.setCost(goods.getCost());
                product.setWeight(goods.getWeight());
                        
                this.productDao.updateByPrimaryKeySelective(product);
                productIdList.remove(0);
            }else{
                Product product=new Product();
                product.setGoodsId(goods.getGoodsId());
                if(!StringUtil.isEmpty(goods.getGoodsSn()))
                    product.setSn(goods.getGoodsSn());
                else
                    product.setSn("a"+System.currentTimeMillis());
                product.setName(goods.getName());
                product.setStore(goods.getStore());
                product.setCost(goods.getCost());
                product.setPrice(goods.getPrice());
                product.setCost(goods.getCost());
                product.setWeight(goods.getWeight());
                this.productDao.insertSelective(product);
                
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
            }
            if(productIdList!=null && productIdList.size()>0){
                this.productDao.deleteByProductIds(productIdList);
                this.goodsSpecDao.deleteByGoodsId(goods.getGoodsId());
                this.productStoreDao.deleteByProductIds(productIdList);
            }
        }
        return null;
    }
    
    @Transactional
    @ServiceLog("上下架")
    @Override
    public String doGoodsSalesExamine(Long goodsId,Integer marketEnable){
          this.goodsDao.updateOfMarketEnable(goodsId, marketEnable);
          return null;
    }
    
    @Transactional
    @ServiceLog("删除商品")
    @Override
    public String doDelGoods(Long id) {
        Goods goods = this.queryByID(id);
        if(goods == null)
            return "获取商品信息失败";
        this.delById(id);
        this.goodsGalleryDao.deleteByGoodsId(goods.getGoodsId());
        this.productDao.deleteByGoodsId(goods.getGoodsId());
        return null;
    }

    @ServiceLog("商品分页<DataTablePage>")
	@Override
	public DataTablePage<Map<String,Object>> pageDataTableGoods(Map<String, Object> map) {
        //分页
        Integer start = Integer.parseInt(map.get("start").toString());
        Integer pageSize = Integer.parseInt(map.get("length").toString());
        Integer draw = Integer.parseInt(map.get("draw").toString());
        
        ParamData params = this.getParamData(start/pageSize+1, pageSize);
        if(map.get("search[goodsName]") != null && StringUtils.isNotBlank(map.get("search[goodsName]").toString())){
            params.put("goodsName", map.get("search[goodsName]"));
        }
        if(map.get("search[comName]") != null && StringUtils.isNotBlank(map.get("search[comName]").toString())){
            params.put("comName", map.get("search[comName]"));
        }
        PageAjax<Map<String,Object>> page = this.pageQuery("GoodsDao.selectDataTableGoods", params );
        return new DataTablePage(draw, page.getRows(),page.getTotal(), page.getTotal());
	}
    
    /**
     * 生成商品号
     * @author hwc
     * @created 2018年1月23日 下午5:15:43
     * @param goods
     * @return
     */
    private String createGoodsSn(Goods goods){
        
        StringBuffer goodsSnSb=new StringBuffer("8");
        
        String catCode=this.goodsCatDao.selectOfCatCode(goods.getCatId());
        String comCode=this.companyDao.selectOfComCode(goods.getComId());
        String goodsSn=this.goodsDao.selectOfGoodsSn(goods.getComId(),goods.getCatId());
        
        String str="0001";
        
        if(!StringUtil.isEmpty(goodsSn) && goodsSn.length()==21){
            str=String.valueOf(Integer.valueOf(goodsSn.substring(17)).intValue()+1);
        
            str=StringUtil.strCover(str,4);
        }
        
        goodsSnSb.append(catCode).append(comCode).append(str);
        return goodsSnSb.toString();
    }
     
    
    public static void main(String[] args){
        String sn="8010100120000010001";
        
        
        String str=String.valueOf(Integer.valueOf(sn.substring(15)).intValue()+1);
        
        int strLen =str.length();  
        if (strLen <4) {  
            while (strLen< 4) {  
                StringBuffer sb = new StringBuffer();  
                sb.append("0").append(str);//左补0   
                str= sb.toString();  
                strLen= str.length();  
            }  
        }
        
        System.out.println(str);
    }

	@Override
	public PageAjax<Map<String, Object>> pageStoreAjax(PageAjax<Map<String, Object>> page) {
		ParamData pd = this.getParamData(page.getPageNo(),page.getPageSize());
        return this.pageQuery("GoodsDao.selectGoodsStoreByParams", pd);
	}

}
