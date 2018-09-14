package com.qyy.jyshop.admin.goodscat.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.goodscat.service.GoodsCatNewService;
import com.qyy.jyshop.dao.GoodsCatDao;
import com.qyy.jyshop.model.GoodsCat;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class GoodsCatNewServiceImpl extends AbstratService<GoodsCat> implements GoodsCatNewService {

	@Autowired
	private GoodsCatDao goodsCatDao;

	@Override
	public List<GoodsCat> listGoodsCatByParentId(Long parentId) {
		Example example = new Example(GoodsCat.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("parentId", parentId);
		example.setOrderByClause("cat_order,cat_id asc");
		List<GoodsCat> list = this.queryByExample(example);
		return list;
	}

	@Override
	@Transactional
	public String addGoodsCat(GoodsCat goodsCat) {
		if (goodsCat == null)
			return "获取分类信息失败";
		goodsCat.setName(goodsCat.getName().trim());
		synchronized (this) {
			Example example = new Example(GoodsCat.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andEqualTo("name", goodsCat.getName());
			criteria.andEqualTo("parentId", goodsCat.getParentId());
			List<GoodsCat> list = this.queryByExample(example);
			if (list != null && list.size() > 0) {
				return "分类名称己存在";
			}
			this.insert(goodsCat);
			Boolean isWork = this.getCatCodeByCatPath(goodsCat);
			if (!isWork) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return "父类不能是三级分类";
			}
			this.updateByID(goodsCat);

		}
		return null;
	}

	private Boolean getCatCodeByCatPath(GoodsCat goodsCat) {
		Integer parentId = goodsCat.getParentId();
		GoodsCat parentGoodsCat = this.queryByID(parentId);
		String parentCatPath = "";
		String parentCatPathName = "";
		String parentCatCode = "";
		if (parentGoodsCat == null) { // 顶级分类
			parentCatPath = "0|";
		} else {
			parentCatPath = parentGoodsCat.getCatPath();
			parentCatCode = parentGoodsCat.getCatCode();
			parentCatPathName = parentGoodsCat.getCatPathName() + "->";
			if (parentCatCode.length() > 6) {
				return false;
			}
		}
		Example example = new Example(GoodsCat.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("parentId", parentId);
		createCriteria.andNotEqualTo("catId", goodsCat.getCatId());
		example.setOrderByClause("cat_code desc");
		List<GoodsCat> queryByExample = this.queryByExample(example);
		
		int size = 1;
		if(queryByExample!=null && queryByExample.size() > 0){
		    String catCode = queryByExample.get(0).getCatCode();
		    size = Integer.parseInt(catCode.substring(catCode.length()-3, catCode.length())) + 1;
		}
		
		
		
		//int size = queryByExample.size();

		goodsCat.setCatPath(parentCatPath + goodsCat.getCatId() + "|");
		goodsCat.setCatPathName(parentCatPathName + goodsCat.getName());
		String catCode = "";

		if (size < 10) {
			catCode = parentCatCode + "00" + size;
		} else if (size < 100) {
			catCode = parentCatCode + "0" + size;
		} else {
			catCode = parentCatCode + size;
		}

		goodsCat.setCatCode(catCode);
		return true;
	}

	
	@Override
	public Map<String,Object> queryGoodsCatByCatId(Integer id) {
		Map<String,Object> returnMap = this.goodsCatDao.queryGoodsCatByCatId(id);
		return returnMap;
	}
	
	
	@Transactional
	@ServiceLog("修改商品分类")
	@Override
	public String editGoodsCat(GoodsCat goodsCat) {
		if (goodsCat == null)
			return "获取分类信息失败";
		goodsCat.setName(goodsCat.getName().trim());
		
		
		synchronized (this) {
			Example example = new Example(GoodsCat.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andEqualTo("name", goodsCat.getName());
			criteria.andEqualTo("parentId", goodsCat.getParentId());
			criteria.andNotEqualTo("catId", goodsCat.getCatId());
			List<GoodsCat> list = this.queryByExample(example);
			if (list != null && list.size() > 0) {
				return "分类名称己存在";
			}
			this.updateByID(goodsCat);
		}
		return null;
	}

	@Override
	@Transactional
	public String editGoodsCatDisable(Integer id, Integer disable) {
		GoodsCat goodscat = new GoodsCat();
		goodscat.setCatId(id);
		goodscat.setDisable(disable);
		this.updateByID(goodscat);
		return null;
	}

    @Override
    public List<GoodsCat> listAll() {
        Example example = new Example(GoodsCat.class);
        example.setOrderByClause("cat_code asc");
        return goodsCatDao.selectByExample(example);
    }
    
    
    @Override
    public List<Integer> selectAllSonCat(int catId) {
        List<GoodsCat> goodsCatList = this.goodsCatDao.selectGoodsCatList();
        //List<GoodsCat> allList = this.goodsCatDao.selectGoodsCatList();
        List<Integer> returnList = new ArrayList<Integer>();
        
        setSonCat(returnList,goodsCatList,catId);
        return returnList;
    }
    
    private void setSonCat(List<Integer> returnList,List<GoodsCat> goodsCatList,Integer catId){
        for(GoodsCat goodsCat:goodsCatList){
            if(goodsCat.getParentId().compareTo(catId) == 0){
                returnList.add(goodsCat.getCatId());
                setSonCat(returnList,goodsCatList,goodsCat.getCatId());
            }
        }
    }
}
