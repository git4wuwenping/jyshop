package com.qyy.jyshop.admin.examine.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.admin.examine.service.ExamineService;
import com.qyy.jyshop.admin.goods.service.GoodsService;
import com.qyy.jyshop.dao.ExamineGoodsDao;
import com.qyy.jyshop.dao.GoodsDao;
import com.qyy.jyshop.model.ExamineGoods;
import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.EntityReflectUtil;
import com.qyy.jyshop.util.TimestampUtil;
@Service
public class ExamineServiceImpl  extends AbstratService<ExamineGoods> implements ExamineService {
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private ExamineGoodsDao examineGoodsDao;
	@Autowired
	private GoodsService goodsServiceImpl;

	@Override
	public String examineGoodsSalesNo(Map<String, Object> map) {
		
		 ExamineGoods examine = EntityReflectUtil.toBean(ExamineGoods.class,map);
		 Goods goods = EntityReflectUtil.toBean(Goods.class,map);
		 examine.setGoodsId(goods.getGoodsId());
		 examine.setState((short)1);
		 examine.setUserId(this.getLoginUser().getId());
		 examine.setOperateTime(TimestampUtil.getNowTime());
		 examine.setCreateTime(TimestampUtil.getNowTime());
		 List<Map<String, Object>> examineList = 
				 examineGoodsDao.selectExamineByGoodsId(goods.getGoodsId());	//商品修改驳回
		 if(examineList.get(0)!=null && examineList.size()>0){
			 examine.setExamineId((Long) examineList.get(0).get("examineId"));
			 this.update(examine);		//更新审核数据
		 }else{
			 this.insert(examine);		//商品上架插入
		 }
	 	 
   	 	 goodsDao.updateOfMarketEnable(goods.getGoodsId(),
	 			Integer.valueOf(DictionaryUtil.getDataValueByName("goods_market_enable", "examine_no")));
	 	 
		return null;
	}

	@Override
	public PageAjax<Map<String, Object>> pageExamineAjax(PageAjax<Map<String, Object>> page) {
		ParamData pd = this.getParamData(page.getPageNo(),page.getPageSize());
		return this.pageQuery("ExamineGoodsDao.selectExamineByParams", pd);
	}

	@Override
	public Map<String, Object> selectExamineByexamineId(long examineId) {
		List<Map<String, Object>> list = examineGoodsDao.selectExamineByexamineId(examineId);
		
		return list.get(0);
	}

	@Override
	public String updateExamineDetials(Map<String, Object> map) {
		ExamineGoods examine = EntityReflectUtil.toBean(ExamineGoods.class,map);
		examine.setCreateTime(TimestampUtil.getNowTime());
		this.update(examine);
		return null;
	}

	@Override
	public String doGoodsSalesExamine(Long examineId, Integer state) {
		ExamineGoods examineGoods = this.queryByID(examineId);
		goodsServiceImpl.doGoodsSalesExamine(examineGoods.getGoodsId(), state);
		return null;
	}
	
	
	
	
}
