package com.qyy.jyshop.admin.spell.service.impl;

import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.spell.service.SpellRecomGoodsService;
import com.qyy.jyshop.dao.SpellRecomGoodsDao;
import com.qyy.jyshop.model.SpellRecomGoods;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SpellRecomGoodsServiceImpl extends AbstratService<SpellRecomGoods> implements SpellRecomGoodsService {

    @Autowired
    private SpellRecomGoodsDao spellRecomGoodsDao;

    @ServiceLog("推荐商品列表(分页_ajax)")
    public PageAjax<Map<String, Object>> recommendPage(PageAjax<SpellRecomGoods> page) {
        ParamData params = getParamData(Integer.valueOf(page.getPageNo()), Integer.valueOf(page.getPageSize()));
        return pageQuery("SpellRecomGoodsDao.pageList", params);
    }

    @Override
    @Transactional
    public String recommendAdd(Map<String, String> map) {
        String goodsIds = map.get("goodsIds");
        System.out.println("==========================================================");
        System.out.println("goodsIds = " + goodsIds);
        List<String> addList = Arrays.asList(goodsIds.split("\\|"));
        if (CollectionUtils.isEmpty(addList)) {
            return null;
        }
        List<String> nowList = new ArrayList<>();
        List<SpellRecomGoods> relList = spellRecomGoodsDao.selectAll();
        relList.forEach(rel -> {
            nowList.add(rel.getGoodsId().toString());
        });
        addList = addList.stream().filter(t -> !nowList.contains(t)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(addList)) {
            List<SpellRecomGoods> list = new ArrayList<>();
            for (String goodIdStr : addList) {
                SpellRecomGoods rel = new SpellRecomGoods();
                rel.setGoodsId(Long.parseLong(goodIdStr));
                list.add(rel);
            }
            spellRecomGoodsDao.batchInsert(list);
        }
        return null;
    }
}
