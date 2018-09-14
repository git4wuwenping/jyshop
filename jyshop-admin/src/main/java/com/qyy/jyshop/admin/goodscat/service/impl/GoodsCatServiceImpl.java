package com.qyy.jyshop.admin.goodscat.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.goodscat.service.GoodsCatService;
import com.qyy.jyshop.dao.GoodsCatDao;
import com.qyy.jyshop.model.GoodsCat;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.pojo.TreeNode;
import com.qyy.jyshop.supple.AbstratService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class GoodsCatServiceImpl extends AbstratService<GoodsCat> implements GoodsCatService {

	@Autowired
	private GoodsCatDao goodsCatDao;

	@Override
	public List<GoodsCat> queryGoodsCatList() {
		return this.queryAll();
	}

	@ServiceLog("获取商品分类列表(分页_ajax)")
	@Override
	public PageAjax<Map<String, Object>> pageGoodsCat(PageAjax<GoodsCat> page,Integer catLevel) {

		ParamData params = this.getParamData(page.getPageNo(), page.getPageSize());
		params.put("catLevel", catLevel);
		return this.pageQuery("GoodsCatDao.selectGoodsCatList", params);
	}

	@ServiceLog("添加商品分类")
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
			if(!isWork){
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
		String parentCatCode = "";
		if(parentGoodsCat == null) { //顶级分类
			parentCatPath = "0|";
		}else {
			parentCatPath = parentGoodsCat.getCatPath();
			parentCatCode = parentGoodsCat.getCatCode();
			if(parentCatCode.length() > 6){
				return false;
			}
		}
		Example example = new Example(GoodsCat.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("parentId",parentId);
		List<GoodsCat> queryByExample = this.queryByExample(example);
		int size = queryByExample.size();
		
		goodsCat.setCatPath(parentCatPath+goodsCat.getCatId()+"|");
		String catCode = "";
		
		if(size < 10) {
			catCode = parentCatCode + "00" + size;
		}else if(size < 100) {
			catCode = parentCatCode + "0" + size;
		}else {
			catCode = parentCatCode + size;
		}
		
		goodsCat.setCatCode(catCode);
		return true;
	}

	@ServiceLog("删除商品分类")
	@Override
	public String delGoodsCat(Integer id) {
		GoodsCat goodsCat = this.queryByID(id);
		if (goodsCat == null)
			return "获取分类信息失败";
		this.delById(id);
		return null;
	}

	@ServiceLog("查询商品分类")
	@Override
	public GoodsCat queryGoodsCatByCatId(Integer id) {
		GoodsCat goodsCat = this.queryByID(id);
		return goodsCat;
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
			criteria.andNotEqualTo("catId", goodsCat.getCatId());
			criteria.andEqualTo("parentId", goodsCat.getParentId());
			List<GoodsCat> list = this.queryByExample(example);
			if (list != null && list.size() > 0) {
				return "分类名称己存在";
			}
			this.updateByID(goodsCat);
		}
		return null;
	}

	@ServiceLog("获取商品分类树结果")
	@Override
	public List<TreeNode> getGoodsCatTreeResult() {

		List<TreeNode> nodeList = new ArrayList<TreeNode>();

		List<GoodsCat> goodsCatList = this.goodsCatDao.selectGoodsCatList();

		if (goodsCatList != null && goodsCatList.size() > 0) {
			TreeNode node = null;
			for (GoodsCat goodsCat : goodsCatList) {
				if (goodsCat.getParentId().equals(0)) {
					node = new TreeNode();
					node.setId(goodsCat.getCatId());
					node.setText(goodsCat.getName());
					node.setData(goodsCat.getImage());
					node.setNodes(this.getChildCat(goodsCatList, goodsCat.getCatId(),true));
					nodeList.add(node);
				}
			}
		}
		return nodeList;
	}

	private List<TreeNode> getChildCat(List<GoodsCat> goodsCatList, Integer catId, boolean hasChild) {
		List<TreeNode> nodeList = new ArrayList<TreeNode>();
		//boolean endAdd=false;
		
		for (GoodsCat goodsCat : goodsCatList) {
			TreeNode node = null;

			if (goodsCat.getParentId().equals(catId)) {
				node = new TreeNode();
				node.setId(goodsCat.getCatId());
				node.setText(goodsCat.getName());
				node.setData(goodsCat.getImage());
				if (hasChild){
					node.setNodes(this.getChildCat(goodsCatList, goodsCat.getCatId(), false));
				}
				nodeList.add(node);
				//endAdd=true;
			}else{
			    //if(endAdd)
			    //    break endAdd;
			}
		}

		return nodeList;
	}

    


}
