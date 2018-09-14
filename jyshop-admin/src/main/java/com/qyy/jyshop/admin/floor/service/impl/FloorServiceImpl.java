package com.qyy.jyshop.admin.floor.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.github.pagehelper.page.PageMethod;
import com.qyy.jyshop.admin.floor.service.FloorService;
import com.qyy.jyshop.admin.goodscat.service.GoodsCatNewService;
import com.qyy.jyshop.dao.FloorDao;
import com.qyy.jyshop.dao.FloorRelDao;
import com.qyy.jyshop.dao.GoodsDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.Floor;
import com.qyy.jyshop.model.FloorRel;
import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.StringUtil;

@Service
public class FloorServiceImpl extends AbstratService<Floor> implements FloorService {

    @Autowired
    private FloorDao floorDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private FloorRelDao floorRelDao;

    @Override
    public PageAjax<Floor> pageFloor(PageAjax<Floor> page, Floor floor) {
        PageMethod.startPage(page.getPageNo(), page.getPageSize());
        Example example = new Example(Floor.class);
        Criteria createCriteria = example.createCriteria();
        if(floor!= null && !StringUtil.isEmpty(floor.getFloorName())){
            createCriteria.andLike("floorName", "%"+floor.getFloorName()+"%");
        }
        createCriteria.andEqualTo("parentId", 0);
        example.setOrderByClause("floor_order asc");
        List<Floor> list = this.queryByExample(example);
        return new PageAjax<Floor>(list);
    }

    @Override
    @Transactional
    public String doAddFloor(Floor floor) {
        if (floor == null) {
            return "专题信息为空";
        }
        Floor record = new Floor();
        record.setFloorName(floor.getFloorName());
        record.setParentId((long)0);
        Floor selectOne = floorDao.selectOne(record);
        if (selectOne != null) {
            return "专题名称重复";
        }
        floor.setParentId((long)0);
        floor.setHorizontalSize(4);
        floor.setVerticalSize(2);
        floor.setDisable((short)0);
        floor.setFloorType((short)0);
        floor.setFloorOrder((short)0);
        this.insert(floor);
        return null;
    }

    @Override
    public Floor queryByFloorId(Long floorId) {
        return this.floorDao.selectByPrimaryKey(floorId);
    }

    @Override
    @Transactional
    public String doEditFloor(Floor floor,Short floorTypeOld) {
        if (floor == null) {
            return "专题信息为空";
        }
        Example example = new Example(Floor.class);
        Criteria createCriteria = example.createCriteria();
        createCriteria.andEqualTo("floorName", floor.getFloorName());
        createCriteria.andNotEqualTo("floorId", floor.getFloorId());
        createCriteria.andEqualTo("parentId", 0);
        List<Floor> queryByExample = this.queryByExample(example);
        if (queryByExample.size() > 0) {
            return "专题名称重复";
        }
        
        this.update(floor);
        return null;
    }

    @Override
    @Transactional
    public String doDelFloor(Long floorId) {
        this.deleteByID(floorId);
        
        Example example =new Example(Floor.class);
        Criteria createCriteria = example.createCriteria();
        createCriteria.andEqualTo("parentId", floorId);
        floorDao.deleteByExample(example);
        
        return null;
    }

    @Override
    public List<Map<String, Object>> queryFloorGoodscatListByFloorId(Long floorId) {
        return floorRelDao.queryFloorGoodscatListByFloorId(floorId);
    }

    @Override
    public List<Map<String, Object>> queryFloorGoodsListByFloorId(Long floorId) {
        return floorRelDao.queryFloorGoodsListByFloorId(floorId);
    }

    @Override
    public List<Map<String, Object>> queryFloorBrandListByFloorId(Long floorId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional
    public String saveFloorRel(Integer floorId, Integer[] frIds) {
        // 删除旧数据
        floorRelDao.deleteByFloorId(floorId.longValue());
        // 添加新数据
        List<FloorRel> floorRelList = new ArrayList<FloorRel>();
        if(frIds.length>0){
            for (int i = 0; i < frIds.length; i++) {
                FloorRel floorRel = new FloorRel();
                floorRel.setFloorId(floorId.longValue());
                floorRel.setFrOrder(Short.parseShort(i + 1 + ""));
                floorRel.setRelId(frIds[i].longValue());
                floorRel.setDesc("");
                floorRelList.add(floorRel);
            }
            floorRelDao.batchInsert(floorRelList);
        }
        return null;
    }

    @Override
    @Transactional
    public String delAllFloorRel(Long floorId) {
        floorRelDao.deleteByFloorId(floorId);
        return null;
    }

    @Override
    public List<Floor> queryFloorByParentId(Long floorId) {
        Example example = new Example(Floor.class);
        Criteria createCriteria = example.createCriteria();
        createCriteria.andEqualTo("parentId", floorId);
        example.setOrderByClause("floor_order asc");
       return this.queryByExample(example);
    }

    @Override
    @Transactional
    public String saveFloorSon(Map<String, Object> map) {
        String fatherFloorId = (String) map.get("fatherFloorId");
        if(StringUtils.isEmpty(fatherFloorId)){
            throw new AppErrorException("系统出错了");
        }
        HttpServletRequest request = getRequest();
        String [] floorIds = request.getParameterValues("floorId[]");
        
        if(floorIds != null && floorIds.length > 0){
            String [] floorNames = request.getParameterValues("floorName[]");
            String [] floorTypes = request.getParameterValues("floorType[]");
            String [] horizontalSizes = request.getParameterValues("horizontalSize[]");
            String [] verticalSizes = request.getParameterValues("verticalSize[]");
            String [] floorOrders = request.getParameterValues("floorOrder[]");
            String [] disables = request.getParameterValues("disable[]");
            List<Floor> newFloorList = new ArrayList<Floor>();
            List<Long> floorIdList = new ArrayList<Long>();
            List<Floor> oldFloorList1 = new ArrayList<Floor>();
            for(int i =0 ;i<floorIds.length;i++){
                Floor floor = new Floor();
                floor.setFloorName(floorNames[i]);
                floor.setFloorType(Short.parseShort(floorTypes[i]));
                floor.setHorizontalSize(Integer.parseInt(horizontalSizes[i]));
                floor.setVerticalSize(Integer.parseInt(verticalSizes[i]));
                floor.setFloorOrder(Short.parseShort(floorOrders[i]));
                floor.setDisable(Short.parseShort(disables[i]));
                floor.setParentId(Long.parseLong(fatherFloorId));
                if(floorIds[i].equals("***")){
                    newFloorList.add(floor);
                }else{
                    floorIdList.add(Long.parseLong(floorIds[i]));
                    floor.setFloorId(Long.parseLong(floorIds[i]));
                    oldFloorList1.add(floor);
                }
            }
            
            Example example =new Example(Floor.class);
            Criteria createCriteria = example.createCriteria();
            createCriteria.andEqualTo("parentId", Long.parseLong(fatherFloorId));
            List<Floor> oldFloorList = floorDao.selectByExample(example);
            List<Long> oldFloorIdList = oldFloorList.stream().map(e->e.getFloorId()).collect(Collectors.toList());
            oldFloorIdList.removeAll(floorIdList);
            if(oldFloorIdList.size()>0){//删除旧数据
                floorDao.deleteByFloorIds(oldFloorIdList);
                //关联删除rel表
                floorRelDao.deleteByFloorIds(oldFloorIdList);
                
            }
            if(newFloorList.size()>0){//新增新数据
                floorDao.batchInsert(newFloorList);
            }
            if(oldFloorList1.size()>0){//更新旧数据
                oldFloorList1.forEach(x -> {
                    oldFloorList.forEach(y->{
                        if(y.getFloorId().compareTo(x.getFloorId()) == 0){
                            if(! x.equals(y)){
                                floorDao.updateByPrimaryKey(x);
                            }
                        }
                    });
                });
            }
        }else{
            Example example =new Example(Floor.class);
            Criteria createCriteria = example.createCriteria();
            createCriteria.andEqualTo("parentId", Long.parseLong(fatherFloorId));
            floorDao.deleteByExample(example);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> queryGoodsCatListByParentId(Long floorId) {
        return floorRelDao.queryGoodsCatListByParentId(floorId);
    }

    @Override
    public List<Map<String, Object>> queryGoodsListByParentIds(Long floorId) {
        return floorRelDao.queryGoodsListByParentIds(floorId);
    }

    @Override
    public List<Map<String, Object>> queryFloorGoodsCatRel(Long floorId) {
        return floorRelDao.queryFloorGoodsCatRel(floorId);
    }

    @Override
    public List<Map<String, Object>> queryGoodsCatList() {
        return floorRelDao.queryGoodsCatList();
    }

    @Override
    public List<Map<String, Object>> queryFloorGoodsRel(Long floorId) {
        return floorRelDao.queryFloorGoodsRel(floorId);
    }

    @Override
    public PageAjax<Goods> pageGoods(String currentPage, String gcId, List<Integer> goodsCatIds, String goodsName) {
        if(StringUtils.isEmpty(currentPage)){
            currentPage = "0";
        }
        PageMethod.startPage(Integer.parseInt(currentPage), 12);
        Example example = new Example(Goods.class);
        Criteria createCriteria = example.createCriteria();
        if(StringUtils.isNotEmpty(gcId)){
            createCriteria.andIn("catId", goodsCatIds);
        }
        if(!StringUtil.isEmpty(goodsName)){
            createCriteria.andLike("name", "%"+goodsName+"%");
        }
        List<Goods> list = goodsDao.selectByExample(example);
        return new PageAjax<Goods>(list);
    }
}
