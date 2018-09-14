package com.qyy.jyshop.goods.server.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.goods.server.CommentService;
import com.qyy.jyshop.model.Comment;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
@Service
public class CommentServiceImpl extends AbstratService<Comment> implements CommentService {

	@Override
	public PageAjax<Map<String, Object>> selectGoodsCommentList(Long goodsId, Integer pageNo, Integer pageSize) {
		ParamData params = this.getParamData(pageNo,pageSize);
		params.put("goodsId", goodsId);
		PageAjax<Map<String,Object>> goodsCommentList = this.pageQuery("CommentDao.selectGoodsCommentList", params);
		
		
		List<Map<String, Object>> rows = goodsCommentList.getRows();
		
		for(Map map : rows){
		    String image = (String) map.get("image");
		    if(StringUtils.isNotEmpty(image)){
		        List<String> imageList = Arrays.asList(image.split(","));
		        map.put("imageList", imageList);
		    }
		}
		
		return goodsCommentList;
	}

}
