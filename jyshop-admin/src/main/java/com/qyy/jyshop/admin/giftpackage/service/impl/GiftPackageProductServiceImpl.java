package com.qyy.jyshop.admin.giftpackage.service.impl;

import com.qyy.jyshop.admin.giftpackage.service.GiftPackageProductService;
import com.qyy.jyshop.dao.GiftPackageDao;
import com.qyy.jyshop.dao.GiftPackageProductDao;
import com.qyy.jyshop.dao.GiftPackageSpecDao;
import com.qyy.jyshop.dao.SpecValuesDao;
import com.qyy.jyshop.model.*;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class GiftPackageProductServiceImpl extends AbstratService<GiftPackageProduct> implements GiftPackageProductService {

    @Autowired
    private GiftPackageProductDao giftPackageProductDao;
    @Autowired
    private GiftPackageSpecDao giftPackageSpecDao;
    @Autowired
    private GiftPackageDao giftPackageDao;
    @Autowired
    private SpecValuesDao specValuesDao;

    @Override
    public void saveGiftPackageProductInfo(String[] productSnArr, String[] storeArr, GiftPackage giftPackage,   String[] costArr,String[] priceArray, String[] weightArr,String[]specsArr,String[] specValueIdArr, String[] specIdArr,List<Long> productIdList) {

        if (productSnArr != null && productSnArr.length > 0) {
            GiftPackageProduct pro = new GiftPackageProduct();
            pro.setGpId(giftPackage.getGpId());
            List<GiftPackageProduct> list = giftPackageProductDao.select(pro);
            //先删除
            for (GiftPackageProduct p : list) {
                if (!Arrays.asList(productSnArr).contains(p.getSn())) {
                    //删除货品
                    giftPackageProductDao.delete(p);
                    //删除商品规格表
                    Example giftPackageSpecExample = new Example(GiftPackageSpec.class);
                    Example.Criteria criteria = giftPackageSpecExample.createCriteria();
                    criteria.andEqualTo("gpProductId", p.getGpProductId());
                    giftPackageSpecDao.deleteByExample(giftPackageSpecExample);
                }
            }
            //保存货品
            for (int i = 0; i < productSnArr.length; i++) {
                GiftPackageProduct p = new GiftPackageProduct();
                p.setSn(productSnArr[i]);
                GiftPackageProduct giftPackageProduct = giftPackageProductDao.selectOne(p);
                if (giftPackageProduct == null) {
                    //添加货品表
                    giftPackageProduct = new GiftPackageProduct();
                    giftPackageProduct.setSn(giftPackage.getGpId() + "-" + System.currentTimeMillis());
                    giftPackageProduct.setStore(Integer.parseInt(storeArr[i]));
                    giftPackageProduct.setCost(new BigDecimal(costArr[i]));
                    giftPackageProduct.setWeight(new BigDecimal(weightArr[i]));
                    giftPackageProduct.setAlarmNum((short) 0);
                    giftPackageProduct.setGpId(giftPackage.getGpId());
                    giftPackageProduct.setName(giftPackage.getGpName());
                    giftPackageProduct.setPrice(new BigDecimal(priceArray[i]).setScale(2, BigDecimal.ROUND_DOWN));
                    giftPackageProduct.setIsUsable((byte) 1);
                    giftPackageProduct.setSpecIds(specIdArr[i]);
                    giftPackageProduct.setSpecValueIds(specValueIdArr[i]);
                    if(!StringUtil.isEmpty(specsArr[i]))
                        giftPackageProduct.setSpecs(specsArr[i].substring(0,specsArr[i].length()-1));
                    giftPackageProductDao.insertSelective(giftPackageProduct);
                    String[] specValueIds = specValueIdArr[i].split(",");
                    String[] specIds = specIdArr[i].split(",");
                    //添加商品规格表
                    for (int j = 0; j < specIds.length; j++) {
                        GiftPackageSpec giftPackageSpec = new GiftPackageSpec();
                        giftPackageSpec.setGpId(giftPackage.getGpId());
                        giftPackageSpec.setGpProductId(giftPackageProduct.getGpProductId());
                        giftPackageSpec.setSpecId(Long.valueOf(specIds[j]));
                        giftPackageSpec.setSpecValueId(Long.valueOf(specValueIds[j]));
                        giftPackageSpecDao.insertSelective(giftPackageSpec);
                    }
                } else {
                    //更新货品表
                    giftPackageProduct.setGpProductId(giftPackageProduct.getGpProductId());
                    giftPackageProduct.setStore(Integer.parseInt(storeArr[i]));
                    giftPackageProduct.setCost(new BigDecimal(costArr[i]));
                    giftPackageProduct.setWeight(new BigDecimal(weightArr[i]));
                    giftPackageProduct.setAlarmNum((short) 0);
                    giftPackageProduct.setGpId(giftPackage.getGpId());
                    giftPackageProduct.setName(giftPackage.getGpName());
                    giftPackageProduct.setPrice(new BigDecimal(priceArray[i]).setScale(2, BigDecimal.ROUND_DOWN));
                    giftPackageProduct.setIsUsable((byte) 1);
                    if(!StringUtil.isEmpty(specsArr[i]))
                        giftPackageProduct.setSpecs(specsArr[i].substring(0,specsArr[i].length()-1));
//                    String specs = specValuesService.getSpecValueNamesBySpecValueIds(specValueIdArr[i]);
//                    product.setSpecs(specs);
                    this.updateByID(giftPackageProduct);
                }
            }
        }
    }

    @Override
    public List<Map> queryByGpId(Long gpId) {
        List<Map> mapList = giftPackageProductDao.selectByGpId(gpId);
        for (Map<String,Object> map:mapList) {
            GiftPackageSpec giftPackageSpec = new GiftPackageSpec();
            giftPackageSpec.setGpProductId(Long.parseLong(map.get("gpProductId").toString()));
            giftPackageSpec.setGpId(Long.parseLong(map.get("gpId").toString()));
            List<GiftPackageSpec> giftPackageSpecList =  giftPackageSpecDao.select(giftPackageSpec);
            List<SpecValues> specValuesList = new ArrayList<>();
            if(!StringUtil.isEmpty(map.get("specValueIds"))){
                for (String s: map.get("specValueIds").toString().split(",")) {
                    specValuesList.add(specValuesDao.selectByPrimaryKey(Long.valueOf(s)));
                }
            }
            map.put("giftPackageSpecList", giftPackageSpecList);
            map.put("specValuesList", specValuesList);
        }
        return mapList;
    }
}
