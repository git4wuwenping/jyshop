package com.qyy.jyshop.bargain.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.bargain.service.BargainService;
import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.pojo.PageAjax;

@RestController
public class BargainController extends AppBaseController{
	@Autowired
	private BargainService bargainService;
	
	/**
	 * 砍价商品列表
	 */
	@RequestMapping(value = "anon/bargainGoodsList")
    public Map<String,Object> bargainGoodsList(Integer pageNo,Integer pageSize) {
		PageAjax<Map<String,Object>> bargainGoodsList = bargainService.selectBargainGoodsList(pageNo,pageSize);
        return this.outMessage(0, "获取成功", this.getPageMap(bargainGoodsList));
    }
	/**
	 * 砍价商品列表轮播消息
	 */
	@RequestMapping(value = "anon/bargainCarouselMessage")
    public Map<String,Object> bargainCarouselMessage(Integer pageNo,Integer pageSize) {
		PageAjax<Map<String,Object>> bargainCarouselMessageList = bargainService.selectBargainCarouselMessage(pageNo,pageSize);
        return this.outMessage(0, "获取成功", this.getPageMap(bargainCarouselMessageList));
    }

}
