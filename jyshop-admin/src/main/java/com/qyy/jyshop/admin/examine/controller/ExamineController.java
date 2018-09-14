package com.qyy.jyshop.admin.examine.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.examine.service.ExamineService;
import com.qyy.jyshop.admin.goods.service.GoodsGalleryService;
import com.qyy.jyshop.admin.goods.service.GoodsService;
import com.qyy.jyshop.model.ExamineGoods;
import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.model.GoodsGallery;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;
import com.qyy.jyshop.util.DictionaryUtil;
@Controller
@RequestMapping("/admin/examine")
public class ExamineController extends BaseController{
	
	@Autowired
	private ExamineService examineServiceImpl;
	@Autowired
	private GoodsService goodsServiceImpl;
	@Autowired
	private GoodsGalleryService goodsGalleryService;
	
	@Authority(opCode = "150101", opName = "审核管理")
	@RequestMapping("examineMain")
	public String examineMain(Map<String, Object> map){
		return "examine/examine_main";
	}
	
	/**
	 * 查询审核列表
	 */
	@ControllerLog("查询审核列表")
	@RequestMapping("pageExamineAjax")
	@ResponseBody
	@Authority(opCode = "150102", opName = "查询审核列表")
	public PageAjax<Map<String, Object>> pageExamineAjax(PageAjax<Map<String,Object>> page) {
		
		return examineServiceImpl.pageExamineAjax(page);
	}
	
	/**
	 * 审核详细页
	 */

	@Authority(opCode = "15010102", opName = "审核管理")
	@RequestMapping("preExamineDetials/{examineId}")
	public String Examine(@PathVariable("examineId")long examineId,Map<String, Object> map){
		Map<String, Object> examineDetials = examineServiceImpl.selectExamineByexamineId(examineId);
		map.put("examineDetials", examineDetials);
		Long goodsId = (Long) examineDetials.get("goodsId");
		//商品详情
		Goods goods = goodsServiceImpl.queryByGoodsId(goodsId);
        map.put("goods", goods);
        //商品相册列表
        List<GoodsGallery> goodsGalleryList = goodsGalleryService.queryByGoodsId(goodsId);
        map.put("goodsGalleryList", goodsGalleryList);
		return "examine/examine_detials";
	}
	/**
	 * 修改审核信息
	 */
	@ControllerLog("修改审核信息")
    @RequestMapping("editExamineDetials")
    @ResponseBody
    @Authority(opCode = "15010103", opName = "修改审核信息")
    public AjaxResult editExamineDetials(@RequestParam Map<String, Object> map) {
        try {
            return AppUtil.returnObj(examineServiceImpl.updateExamineDetials(map));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
	/**
	 * 审核通过
	 */
	@ControllerLog("审核通过")
    @RequestMapping("examineYes/{examineId}")
    @ResponseBody
    @Authority(opCode = "", opName = "审核通过")
    public AjaxResult examineYes(@PathVariable("examineId") Long examineId) {
        try{
            return AppUtil.returnObj(examineServiceImpl.doGoodsSalesExamine(examineId,
                    Integer.valueOf(DictionaryUtil.getDataValueByName("goods_market_enable", "sales_yes"))));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
	
	
    
    @ControllerLog("审核驳回页面")
    @Authority(opCode = "04010106", opName = "审核驳回页面")
    @RequestMapping("preExamineNo/{examineId}")
    public String preExamineNo(@PathVariable("examineId") Long examineId,Map<String, Object> map) {
    	Map<String, Object> examine = examineServiceImpl.selectExamineByexamineId(examineId);
    	map.put("goodsId", examine.get("goodsId"));
    	return "examine/examine_goods_no";
    }
    
    
    /*@ControllerLog("审核驳回")
    @RequestMapping("examineNo")
    @ResponseBody
    @Authority(opCode = "", opName = "商品驳回")
    public AjaxResult examineNo(@RequestParam Map<String, Object> map) {
    	try {
    		 return AppUtil.returnObj(examineServiceImpl.examineGoodsSalesNo(map));
		} catch (Exception e) {
			 return AppUtil.returnObj(2,e.getMessage());
		}
    }*/
    
}
