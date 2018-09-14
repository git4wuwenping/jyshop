package com.qyy.jyshop.seller.goods.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.seller.common.annotation.ServiceLog;
import com.qyy.jyshop.seller.goods.service.GoodsGalleryService;
import com.qyy.jyshop.seller.goods.service.GoodsService;
import com.qyy.jyshop.seller.goods.service.ProductService;
import com.qyy.jyshop.dao.CompanyDao;
import com.qyy.jyshop.dao.DepotDao;
import com.qyy.jyshop.dao.ExamineGoodsDao;
import com.qyy.jyshop.dao.GoodsCatDao;
import com.qyy.jyshop.dao.GoodsDao;
import com.qyy.jyshop.dao.GoodsGalleryDao;
import com.qyy.jyshop.dao.GoodsSpecDao;
import com.qyy.jyshop.dao.ProductDao;
import com.qyy.jyshop.dao.ProductStoreDao;
import com.qyy.jyshop.model.AuthUser;
import com.qyy.jyshop.model.Depot;
import com.qyy.jyshop.model.ExamineGoods;
import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.model.Product;
import com.qyy.jyshop.model.ProductStore;
import com.qyy.jyshop.pojo.DataTablePage;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.EntityReflectUtil;
import com.qyy.jyshop.util.StringUtil;
import com.qyy.jyshop.util.TimestampUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.servlet.http.HttpServletRequest;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl extends AbstratService<Goods> implements GoodsService {
	
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
    @Autowired
    private ExamineGoodsDao examineGoodsDao;
    
    @ServiceLog("查询商品详情")
    @Override
    public Goods queryByGoodsId(Long goodsId){
        return this.queryByID(goodsId);
    }
    
    @Transactional
    @ServiceLog("查询商品分页列表")
    @Override
    public PageAjax<Map<String,Object>> pageGoods(PageAjax<Map<String,Object>> page) {
    	
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
        
        String[] imgArray = request.getParameterValues("img");
        String[] specIdArray = request.getParameterValues("spec_ids"); // 规格id数组
        String[] storeArray = request.getParameterValues("stores");//库存数组
        if(imgArray != null && imgArray.length > 0){
            goods.setImage(imgArray[0]);
        }
        goods.setCatName(map.get("catNamePath").toString());
        goods.setComId(this.getLoginUserComId());
      
        if(storeArray!=null && storeArray.length>0){
            Integer totalStore = 0;
            for (String s:storeArray) {
                totalStore += Integer.parseInt(s);
            }
            goods.setStore(totalStore);
        }
        synchronized (goods.getCatId()) {
            goods.setGoodsSn(String.valueOf(System.currentTimeMillis()));
            //保存商品
            this.insertSelective(goods);
            //this.goodsDao.insertGoods(goods);
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
                this.productService.doSaveProduct(productSnArray,storeArray, goods, costArray,priceArray, 
                        weightArray, specValueIdArray,specsArray, specIdArray,null);
            }else{
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
        
        Short state = Short.valueOf(DictionaryUtil.getDataValueByName("examine_status", "wait_examine")) ;
        Short type = 1; 
        examineGoodsDao.updateExamineBygoodsId(goods.getGoodsId(),state,type,TimestampUtil.getNowTime());
        goods.setComId(this.getLoginUserComId());
        
        //保存相册
        goodsGalleryService.saveGoodsGallery(imgArray, goods.getGoodsId());
        
        List<Long> productIdList=this.productDao.selectOfProductId(goods.getGoodsId());
        if(specIdArray!=null && specIdArray.length>0){
            String[] productSnArray = request.getParameterValues("productSns");//sn数组
            String[] specValueIdArray = request.getParameterValues("spec_value_ids");// 规格值id数组
            String[] specsArray = request.getParameterValues("specs");// 规格值id数组
            String[] weightArray= request.getParameterValues("weights");//重量数组
            String[] costArray = request.getParameterValues("costs");//成本数组
            String[] priceArray = request.getParameterValues("prices");//价格数组        
            this.productService.doSaveProduct(productSnArray,storeArray, goods, costArray,priceArray, 
                    weightArray, specValueIdArray,specsArray,specIdArray,productIdList);
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
                product.setSn(goods.getGoodsSn()+"001");
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
    	  if(Integer.valueOf(DictionaryUtil.getDataValueByName("goods_market_enable", "sales_apply"))==marketEnable){	//上架待审核
    		  List<Map<String,Object>> list = examineGoodsDao.selectExamineByGoodsId(goodsId);
    		  if(list!=null&&list.size()>0){
    			  short state = (short)0;
    			  short type = (short)1;
    			  examineGoodsDao.updateExamineBygoodsId(goodsId, state, type, TimestampUtil.getNowTime());
    		  }
    		  //插入待审核数据
              ExamineGoods examineGoods = new ExamineGoods();
              examineGoods.setGoodsId(goodsId);
              examineGoods.setState(Short.valueOf(DictionaryUtil.getDataValueByName("examine_status", "wait_examine")));
              examineGoods.setCreateTime(TimestampUtil.getNowTime());
              examineGoods.setType((short)0);
              examineGoodsDao.insert(examineGoods);
    	  }
          this.goodsDao.updateOfMarketEnable(goodsId, marketEnable);
          return null;
    }
    

    @ServiceLog("商品分页<DataTablePage>")
	@Override
	public DataTablePage<Goods> pageDataTableGoods(Map<String, Object> map) {
		Example example = new Example(Goods.class);
        Example.Criteria criteria = example.createCriteria();
        //分页
        Integer start = Integer.parseInt(map.get("start").toString());
        Integer pageSize = Integer.parseInt(map.get("length").toString());
        Integer draw = Integer.parseInt(map.get("draw").toString());
        if(map.get("search[value]") != null && StringUtils.isNotBlank(map.get("search[value]").toString())){
            criteria.andLike("name", "%"+map.get("search[value]").toString()+"%");
        }
        AuthUser authUser = this.getLoginUser();
        if(authUser!=null && authUser.getRoleId() != 99){
            criteria.andEqualTo("comId", authUser.getComId());
        }
        
        PageAjax<Goods> page = this.queryPageListByExample(start/pageSize+1,pageSize, example);
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
        AuthUser authUser=this.getLoginUser();
        String comCode=this.companyDao.selectOfComCode(authUser.getComId());
        String goodsSn=this.goodsDao.selectOfGoodsSn(goods.getComId(),goods.getCatId());
        
        String str="0001";
        
        if(!StringUtil.isEmpty(goodsSn) && goodsSn.length()==21){
            str=String.valueOf(Integer.valueOf(goodsSn.substring(17)).intValue()+1);
        
            str=StringUtil.strCover(str,4);
        }
        
        goodsSnSb.append(catCode).append(comCode).append(str);
        return goodsSnSb.toString();
    }
    
    @Override
	public PageAjax<Map<String, Object>> pageStoreAjax(PageAjax<Map<String, Object>> page) {
		ParamData pd = this.getParamData(page.getPageNo(),page.getPageSize());
		Integer comId = this.getLoginUserComId();
		pd.put("comIds", comId);
        return this.pageQuery("GoodsDao.selectGoodsStoreByParams", pd);
	}

}
