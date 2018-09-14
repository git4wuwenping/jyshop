package com.qyy.jyshop.admin.giftpackage.service;

import com.qyy.jyshop.model.GiftPackageGallery;

import java.util.List;

public interface GiftPackageGalleryService {
    /**
     * 保存礼包相册
     * @param imgs
     * @param gpId
     */
    void saveGiftPackageGallery(String[] imgs, Long gpId);

    List<GiftPackageGallery> queryByGpId(Long gpId);
}
