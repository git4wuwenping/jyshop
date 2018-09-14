package com.qyy.jyshop.admin.giftpackage.service;

import com.qyy.jyshop.model.GiftPackage;

import java.util.List;
import java.util.Map;

public interface GiftPackageProductService {

    void saveGiftPackageProductInfo(String[] productSnArr, String[] storeArr, GiftPackage giftPackage, String[]priceArray,String[] costArr, String[] weightArr, 
            String[] specsArr,String[] specValueIdArr, String[] specIdArr,List<Long> productIdList);

    List<Map> queryByGpId(Long gpId);
}
