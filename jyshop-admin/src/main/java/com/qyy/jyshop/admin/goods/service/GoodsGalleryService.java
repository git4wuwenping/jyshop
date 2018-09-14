package com.qyy.jyshop.admin.goods.service;

import java.util.List;

import com.qyy.jyshop.model.GoodsGallery;

public interface GoodsGalleryService {

    List<GoodsGallery> queryByGoodsId(Long goodsId);

    void saveGoodsGallery(String[] imgs, Long goodsId);
}
