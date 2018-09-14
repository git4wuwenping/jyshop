package com.qyy.jyshop.admin.floor.service;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.Floor;
import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.pojo.PageAjax;

public interface FloorService {

    PageAjax<Floor> pageFloor(PageAjax<Floor> page, Floor floor);

    String doAddFloor(Floor floor);

    Floor queryByFloorId(Long floorId);

    String doEditFloor(Floor floor, Short floorTypeOld);

    String doDelFloor(Long floorId);

    List<Map<String,Object>> queryFloorGoodscatListByFloorId(Long floorId);

    List<Map<String,Object>> queryFloorGoodsListByFloorId(Long floorId);

    List<Map<String,Object>> queryFloorBrandListByFloorId(Long floorId);

    String saveFloorRel(Integer floorId,Integer [] frIds);

    String delAllFloorRel(Long floorId);

    List<Floor> queryFloorByParentId(Long floorId);

    String saveFloorSon(Map<String, Object> map);

    List<Map<String,Object>> queryGoodsCatListByParentId(Long floorId);

    List<Map<String,Object>> queryGoodsListByParentIds(Long floorId);

    List<Map<String, Object>> queryFloorGoodsCatRel(Long floorId);

    List<Map<String, Object>> queryGoodsCatList();

    List<Map<String, Object>> queryFloorGoodsRel(Long floorId);

    PageAjax<Goods> pageGoods(String currentPage, String gcId, List<Integer> goodsCatIds, String goodsName);
}
