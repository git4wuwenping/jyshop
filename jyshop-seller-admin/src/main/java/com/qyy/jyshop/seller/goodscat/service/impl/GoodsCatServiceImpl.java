package com.qyy.jyshop.seller.goodscat.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.seller.common.annotation.ServiceLog;
import com.qyy.jyshop.seller.goodscat.service.GoodsCatService;
import com.qyy.jyshop.dao.GoodsCatDao;
import com.qyy.jyshop.model.GoodsCat;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.pojo.TreeNode;
import com.qyy.jyshop.supple.AbstratService;

import tk.mybatis.mapper.entity.Example;

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
    public PageAjax<Map<String, Object>> pageGoodsCat(PageAjax<GoodsCat> page) {
    	
    	 ParamData params = this.getParamData(page.getPageNo(),page.getPageSize());
         return this.pageQuery("GoodsCatDao.selectGoodsCatList", params);
    }

    @ServiceLog("添加商品分类")
    @Override
    public String addGoodsCat(GoodsCat goodsCat) {
        if(goodsCat == null)
            return "获取分类信息失败";
        synchronized (this) {
            Example example = new Example(GoodsCat.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("name", goodsCat.getName());
            List<GoodsCat> list = this.queryByExample(example);
            if(list != null && list.size() > 0){
                return "分类名称己存在";
            }
        }
        this.insert(goodsCat);
        return null;
    }

    @ServiceLog("删除商品分类")
	@Override
	public String delGoodsCat(Integer id) {
		 GoodsCat goodsCat = this.queryByID(id);
	        if(goodsCat == null)
	            return "获取分类信息失败";
	        this.delById(id);
	        return null;	}

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
        if(goodsCat == null)
            return "获取分类信息失败";
        this.updateByID(goodsCat);
        return null;
    }

    /*@ServiceLog("获取商品分类树结果")
    @Override
    public List<TreeNode> getGoodsCatTreeResult() {

        List<TreeNode> nodeList = new ArrayList<TreeNode>();

        List<GoodsCat> goodsCatList = this.goodsCatDao.selectGoodsCatList();

        if (goodsCatList != null && goodsCatList.size() > 0) {
            TreeNode node = null;
            endAdd: for (GoodsCat goodsCat : goodsCatList) {
                if (goodsCat.getParentId().equals(0)) {
                    node = new TreeNode();
                    node.setId(goodsCat.getCatId());
                    node.setText(goodsCat.getName());
                    node.setNodes(this.getChildCat(goodsCatList, goodsCat.getCatId(),true));
                    nodeList.add(node);
                } else
                    break endAdd;
            }
        }
        return nodeList;
    }

    private List<TreeNode> getChildCat(List<GoodsCat> goodsCatList, Integer catId, boolean isChild) {
        List<TreeNode> nodeList = new ArrayList<TreeNode>();
        boolean endAdd=false;
        
        endAdd: for (GoodsCat goodsCat : goodsCatList) {
            TreeNode node = null;

            if (goodsCat.getParentId().equals(catId)) {
                node = new TreeNode();
                node.setId(goodsCat.getCatId());
                node.setText(goodsCat.getName());
                if (isChild){
                    node.setNodes(this.getChildCat(goodsCatList, goodsCat.getCatId(), false));
                }
                nodeList.add(node);
                endAdd=true;
            }else{
                if(endAdd)
                    break endAdd;
            }
        }

        return nodeList;
    }*/
    
    
    
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
