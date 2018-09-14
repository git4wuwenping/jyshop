package com.qyy.jyshop.admin.giftpackage.service.impl;

import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.giftpackage.service.GiftPackageGalleryService;
import com.qyy.jyshop.admin.giftpackage.service.GiftPackageProductService;
import com.qyy.jyshop.admin.giftpackage.service.GiftPackageService;
import com.qyy.jyshop.dao.GiftPackageDao;
import com.qyy.jyshop.dao.GiftPackageProductDao;
import com.qyy.jyshop.dao.GiftPackageSpecDao;
import com.qyy.jyshop.model.*;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.EntityReflectUtil;
import com.qyy.jyshop.util.StringUtil;
import com.qyy.jyshop.util.TimestampUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

@Service
public class GiftPackageServiceImpl extends AbstratService<GiftPackage> implements GiftPackageService {

    @Autowired
    private GiftPackageGalleryService giftPackageGalleryService;
    @Autowired
    private GiftPackageProductService giftPackageProductService;
    @Autowired
    private GiftPackageDao giftPackageDao;
    @Autowired
    private GiftPackageProductDao giftPackageProductDao;
    @Autowired
    private GiftPackageSpecDao giftPackageSpecDao;
    
	@Transactional
    @ServiceLog("查询礼包分页列表")
    @Override
    public PageAjax<Map<String,Object>> pageGiftPackage(PageAjax<Map<String,Object>> page) {
        ParamData params = this.getParamData(page.getPageNo(),page.getPageSize());
        return this.pageQuery("GiftPackageDao.selectGiftPackageByParams", params);
    }

    @ServiceLog("添加礼包")
    @Override
    public String doAddGiftPackage(Map<String, Object> map) {
        HttpServletRequest request = getRequest();
        GiftPackage giftPackage = EntityReflectUtil.toBean(GiftPackage.class,map);
        if(giftPackage == null)
            throw new RuntimeException("礼包为空");

        String[] imgArray = request.getParameterValues("img");
        String[] specIdArray = request.getParameterValues("spec_ids"); // 规格id数组
        String[] storeArray = request.getParameterValues("stores");//库存数组
        if(imgArray != null && imgArray.length > 0){
            giftPackage.setImage(imgArray[0]);
        }

        if(StringUtil.isEmpty(giftPackage.getComId())){
            giftPackage.setComId(0);
        }
        if(storeArray!=null && storeArray.length>0){
            Integer totalStore = 0;
            for (String s:storeArray) {
                totalStore += Integer.parseInt(s);
            }
            giftPackage.setStore(totalStore);
        }
        giftPackage.setGpSn("gp"+System.currentTimeMillis());
        giftPackage.setLastModify(TimestampUtil.getNowTime());
        //保存商品
        this.insertSelective(giftPackage);
        //保存礼包相册
        giftPackageGalleryService.saveGiftPackageGallery(imgArray, giftPackage.getGpId());

        if(specIdArray!=null && specIdArray.length>0){
            String[] productSnArray = request.getParameterValues("productSns");//sn数组
            String[] specValueIdArray = request.getParameterValues("spec_value_ids");// 规格值id数组
            String[] specsArray = request.getParameterValues("specs");
            String[] weightArray= request.getParameterValues("weights");//重量数组
            String[] costArray = request.getParameterValues("costs");//成本数组
            String[] priceArray = request.getParameterValues("prices");//价格数组
            this.giftPackageProductService.saveGiftPackageProductInfo(productSnArray,storeArray, giftPackage, costArray,priceArray, weightArray, 
                    specsArray,specValueIdArray, specIdArray,null);
        }else{
            //没有添加规格的情况，默认添加一条货品记录
            GiftPackageProduct product=new GiftPackageProduct();
            product.setSn(giftPackage.getGpSn()+"001");
            product.setGpId(giftPackage.getGpId());
            product.setName(giftPackage.getGpName());
            product.setStore(giftPackage.getStore());
            product.setCost(giftPackage.getCost());
            product.setPrice(giftPackage.getPrice());
            product.setWeight(giftPackage.getWeight());
            this.giftPackageProductDao.insertSelective(product);
            //库存相关暂时注释
        }
        return null;
    }

    @ServiceLog("查询礼包")
    @Override
    public GiftPackage queryByGpId(Long gpId) {
        GiftPackage giftPackage = this.queryByID(gpId);
        return giftPackage;
    }

    @Override
    public String editGiftPackage(Map<String, Object> map) {
        HttpServletRequest request = getRequest();
        GiftPackage giftPackage = EntityReflectUtil.toBean(GiftPackage.class,map);
        if(giftPackage == null)
            throw new RuntimeException("礼包为空");

        String[] imgArray = request.getParameterValues("img");
        String[] specIdArray = request.getParameterValues("spec_ids"); // 规格id数组
        String[] storeArray = request.getParameterValues("stores");//库存数组
        if(imgArray != null && imgArray.length > 0){
            giftPackage.setImage(imgArray[0]);
        }

        if(StringUtil.isEmpty(giftPackage.getComId())){
            giftPackage.setComId(0);
        }
        if(storeArray!=null && storeArray.length>0){
            Integer totalStore = 0;
            for (String s:storeArray) {
                totalStore += Integer.parseInt(s);
            }
            giftPackage.setStore(totalStore);
        }
        giftPackage.setLastModify(TimestampUtil.getNowTime());
        //更新礼包相册
        this.updateByID(giftPackage);
        //保存礼包相册
        giftPackageGalleryService.saveGiftPackageGallery(imgArray, giftPackage.getGpId());
        
        List<Long> productIdList=this.giftPackageProductDao.selectOfGpProductId(giftPackage.getGpId());
        if(specIdArray!=null && specIdArray.length>0){
            String[] productSnArray = request.getParameterValues("productSns");//sn数组
            String[] specValueIdArray = request.getParameterValues("spec_value_ids");// 规格值id数组
            String[] specsArray = request.getParameterValues("specs");
            String[] weightArray= request.getParameterValues("weights");//重量数组
            String[] costArray = request.getParameterValues("costs");//成本数组
            String[] priceArray = request.getParameterValues("prices");//价格数组
            this.giftPackageProductService.saveGiftPackageProductInfo(productSnArray,storeArray, giftPackage, costArray,priceArray, weightArray,specsArray, specValueIdArray, specIdArray,productIdList);
        }else{
            //没有添加规格的情况，默认添加一条货品记录
            GiftPackageProduct product=new GiftPackageProduct();
            product.setSn(giftPackage.getGpSn()+"001");
            product.setGpId(giftPackage.getGpId());
            product.setName(giftPackage.getGpName());
            product.setStore(giftPackage.getStore());
            product.setCost(giftPackage.getCost());
            product.setPrice(giftPackage.getPrice());
            product.setWeight(giftPackage.getWeight());
            this.giftPackageProductDao.insertSelective(product);
            //库存相关暂时注释
            if(productIdList!=null && productIdList.size()>0){
                this.giftPackageProductDao.deleteByGpProductIds(productIdList);
                this.giftPackageSpecDao.deleteByGpId(giftPackage.getGpId());
            }
        }
        
        return null;
    }

    @ServiceLog("删除礼包")
    @Override
    public String doDelGiftPackage(Long gpId) {
        GiftPackage giftPackage = queryByGpId(gpId);
        if(giftPackage == null)
            return "获取礼包信息失败";
        this.delById(gpId);
        return null;
    }

	@Override
	public String doGiftPackageSalesExamine(Long gpId, Integer marketEnable) {
		giftPackageDao.doGiftPackageSalesExamine(gpId,marketEnable);
		return null;
	}

}
