package com.qyy.jyshop.admin.giftpackage.service.impl;

import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.giftpackage.service.GiftPackageGalleryService;
import com.qyy.jyshop.dao.GiftPackageGalleryDao;
import com.qyy.jyshop.model.GiftPackageGallery;
import com.qyy.jyshop.supple.AbstratService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class GiftPackageGalleryServiceImpl extends AbstratService<GiftPackageGallery> implements GiftPackageGalleryService {

    @Autowired
    private GiftPackageGalleryDao giftPackageGalleryDao;

    @ServiceLog("保存礼包相册")
    @Transactional
    @Override
    public void saveGiftPackageGallery(String[] imgs, Long gpId) {
        if(imgs != null && imgs.length > 0){
            GiftPackageGallery giftPackageGallery = new GiftPackageGallery();
            giftPackageGallery.setGpId(gpId);
            List<GiftPackageGallery> list = giftPackageGalleryDao.select(giftPackageGallery);
            //先删除
            for (GiftPackageGallery gallery: list) {
                if(!Arrays.asList(imgs).contains(gallery.getImage())){
                    giftPackageGalleryDao.delete(gallery);
                }
            }
            for (String img:imgs) {
                GiftPackageGallery gallery = new GiftPackageGallery();
                gallery.setGpId(gpId);
                gallery.setImage(img);
                GiftPackageGallery gal = giftPackageGalleryDao.selectOne(gallery);
                if(gal == null){
                    giftPackageGalleryDao.insertSelective(gallery);
                }
            }
        }
    }

    @Override
    public List<GiftPackageGallery> queryByGpId(Long gpId) {
        GiftPackageGallery gallery = new GiftPackageGallery();
        gallery.setGpId(gpId);
        return this.queryList(gallery);
    }
}
