package com.qyy.jyshop.seller.goods.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.seller.common.annotation.ServiceLog;
import com.qyy.jyshop.seller.goods.service.GoodsGalleryService;
import com.qyy.jyshop.dao.GoodsGalleryDao;
import com.qyy.jyshop.model.GoodsGallery;
import com.qyy.jyshop.supple.AbstratService;

import java.util.Arrays;
import java.util.List;

@Service
public class GoodsGalleryServiceImpl extends AbstratService<GoodsGallery> implements GoodsGalleryService {

    @Autowired
    private GoodsGalleryDao goodsGalleryDao;

    @ServiceLog("根据商品id查询相册列表")
    @Override
    public List<GoodsGallery> queryByGoodsId(Long goodsId) {
        GoodsGallery goodsGallery = new GoodsGallery();
        goodsGallery.setGoodsId(goodsId);
        return this.queryList(goodsGallery);
    }
    
    @ServiceLog("保存相册")
    @Transactional
    @Override
    public void saveGoodsGallery(String[] imgs, Long goodsId) {
        if(imgs != null && imgs.length > 0){
            GoodsGallery goodsGallery = new GoodsGallery();
            goodsGallery.setGoodsId(goodsId);
            List<GoodsGallery> list = goodsGalleryDao.select(goodsGallery);
            //先删除
            for (GoodsGallery gallery: list) {
                if(!Arrays.asList(imgs).contains(gallery.getImage())){
                    goodsGalleryDao.delete(gallery);
                }
            }
            for (String img:imgs) {
                GoodsGallery gallery = new GoodsGallery();
                gallery.setGoodsId(goodsId);
                gallery.setImage(img);
                GoodsGallery gal = goodsGalleryDao.selectOne(gallery);
                if(gal == null){
                    goodsGalleryDao.insertSelective(gallery);
                }
            }
        }
    }
}
