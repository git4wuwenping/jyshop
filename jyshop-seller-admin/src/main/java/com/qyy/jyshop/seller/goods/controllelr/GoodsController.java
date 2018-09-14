package com.qyy.jyshop.seller.goods.controllelr;


import com.alibaba.fastjson.JSONObject;
import com.qyy.jyshop.seller.common.annotation.Authority;
import com.qyy.jyshop.seller.common.annotation.ControllerLog;
import com.qyy.jyshop.seller.common.controller.BaseController;
import com.qyy.jyshop.seller.delivery.service.DlyTypeService;
import com.qyy.jyshop.seller.goods.service.GoodsGalleryService;
import com.qyy.jyshop.seller.goods.service.GoodsService;
import com.qyy.jyshop.seller.goods.service.ProductService;
import com.qyy.jyshop.seller.goodscat.service.GoodsCatService;
import com.qyy.jyshop.seller.goodstype.service.GoodsTypeService;
import com.qyy.jyshop.seller.goodstype.service.SpecService;
import com.qyy.jyshop.seller.examine.service.ExamineService;
import com.qyy.jyshop.dao.GoodsSpecDao;
import com.qyy.jyshop.model.AuthUser;
import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.model.GoodsGallery;
import com.qyy.jyshop.model.GoodsType;
import com.qyy.jyshop.model.Spec;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.DataTablePage;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.TreeNode;
import com.qyy.jyshop.util.AppUtil;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.LongUtil;
import com.qyy.jyshop.util.StringUtil;
import com.qyy.jyshop.vo.SpecVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/goods")
public class GoodsController extends BaseController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsTypeService goodsTypeService;
    @Autowired
    private GoodsCatService goodsCatService;
    @Autowired  
    private GoodsGalleryService goodsGalleryService;
    @Autowired
    private SpecService specService;
    @Autowired
    private GoodsSpecDao goodsSpecDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private ExamineService ExamineService;
    @Autowired
    private DlyTypeService dlyTypeService;
    
    @Authority(opCode = "040106", opName = "商户商品")
    @RequestMapping("comGoodsMain")  
    public String comOrderMain(Map<String, Object> map,Integer state) {
    	map.put("state", state);
    	map.put("comIds", 0);
        map.put("comId", ((AuthUser) BaseController.getRequest().getAttribute("loginUser")).getComId());
        return "goods/comGoods_main";
    }

    @ControllerLog("查询商品列表")
    @RequestMapping("pageGoodsAjax")
    @ResponseBody
    @Authority(opCode = "04010101", opName = "查询商品列表")
    public PageAjax<Map<String,Object>> pageGoodsAjax(PageAjax<Map<String,Object>> page) {
        return goodsService.pageGoods(page);
    }
    
    @ControllerLog("添加商品页面")
    @RequestMapping("preAddGoods/{isDir}")
    @Authority(opCode = "090101", opName = "添加商品页面")
    public String preAddGoods(@PathVariable("isDir") Integer isDir,Map<String, Object> map) {
        
        List<TreeNode> treeViewResultList =  goodsCatService.getGoodsCatTreeResult();
        map.put("treeViewResultList", JSONObject.toJSONString(treeViewResultList));
        // 当前登录的用户
        AuthUser loginUser = (AuthUser) BaseController.getRequest().getAttribute("loginUser");
        map.put("goodsTypeList", this.goodsTypeService.queryByComId(loginUser.getComId()));
        map.put("dlyTypeList", this.dlyTypeService.queryByComId(loginUser.getComId()));
        return "goods/add_goods";
    }
    
    @ControllerLog("添加商品")
    @ResponseBody
    @RequestMapping("addGoods")
    @Authority(opCode = "04010105", opName = "添加商品")
    public AjaxResult addGoods(@RequestParam Map<String, Object> map) {
        try {
            return AppUtil.returnObj(goodsService.doAddGoods(map));
        } catch (Exception e) {
            System.out.println("添加商品出错了www111.....");
            e.printStackTrace();
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    @ControllerLog("编辑商品页面") 
    @RequestMapping("preEditGoods/{goodsId}") 
    @Authority(opCode = "04010102", opName = "编辑商品页面") 
    public String preEditGoods(@PathVariable("goodsId") Long goodsId, Map<String, Object> map) {
        Goods goods = goodsService.queryByGoodsId(goodsId);
        map.put("goods", goods);
        //分类树
        List<TreeNode> treeViewResultList =  goodsCatService.getGoodsCatTreeResult();
        map.put("treeViewResultList", JSONObject.toJSONString(treeViewResultList));
        
        map.put("goodsTypeList", this.goodsTypeService.queryByComId(goods.getComId()));
        
        List<Spec> specList=new ArrayList<Spec>();
        if(!StringUtil.isEmpty(goods.getTypeId()) && goods.getTypeId()>0L){
            GoodsType goodsType=this.goodsTypeService.queryByTypeId(goods.getTypeId());
            if(goodsType!=null && !StringUtil.isEmpty(goodsType.getSpecIds())){
                Long[] specIds=LongUtil.stringArrayToLongArray(goodsType.getSpecIds().split(","));
                specList=this.specService.queryBySpecIds(specIds);
            }
        }
        map.put("specList", specList);
        List<SpecVo> goodsSpecList = goodsSpecDao.selectSpecsByGoodsId(goods.getGoodsId());
        map.put("goodsSpecListJson", JSONObject.toJSONString(goodsSpecList));
        map.put("goodsSpecList", goodsSpecList);
        
        //货品列表
        List<Map<String,Object>> productList = productService.queryByGoodsId(goodsId);
        map.put("productList", productList);
        
        //商品相册列表
        List<GoodsGallery> goodsGalleryList = goodsGalleryService.queryByGoodsId(goodsId);
        map.put("goodsGalleryList", goodsGalleryList);
        map.put("dlyTypeList", this.dlyTypeService.queryByComId(null));
        return "goods/edit_goods";
    }

    @ControllerLog("编辑商品")
    @RequestMapping("editGoods")
    @ResponseBody
    @Authority(opCode = "04010103", opName = "编辑商品")
    public AjaxResult editGoods(@RequestParam Map<String, Object> map) {
        try {
            return AppUtil.returnObj(goodsService.editGoods(map));
        } catch (RuntimeException e) {
            System.out.println("修改商品出错了www111.....");
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    
    @ControllerLog("商品查看页")
    @RequestMapping("preDetailGoods/{goodsId}")
    @Authority(opCode = "04010606", opName = "商品查看页")
    public String preDetailGoods(@PathVariable("goodsId") Long goodsId, Map<String, Object> map) {
        Goods goods = goodsService.queryByGoodsId(goodsId);
        map.put("goods", goods);
        //商品相册列表
        List<GoodsGallery> goodsGalleryList = goodsGalleryService.queryByGoodsId(goodsId);
        map.put("goodsGalleryList", goodsGalleryList);
        return "goods/goods_detail";
    }
    
    @ControllerLog("商品下架")
    @RequestMapping("goodsSalesNo/{goodsId}")
    @ResponseBody
    @Authority(opCode = "04010106", opName = "商品下架")
    public AjaxResult goodsSalesNo(@PathVariable("goodsId") Long goodsId) {
        try{
            return AppUtil.returnObj(goodsService.doGoodsSalesExamine(goodsId,
                    Integer.valueOf(DictionaryUtil.getDataValueByName("goods_market_enable", "sales_no"))));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    @ControllerLog("商品申请上架")
    @RequestMapping("goodsSalesApply/{goodsId}")
    @ResponseBody
    @Authority(opCode = "04010106", opName = "商品申请上架")
    public AjaxResult goodsSalesApply(@PathVariable("goodsId") Long goodsId) {
        try{
            return AppUtil.returnObj(goodsService.doGoodsSalesExamine(goodsId,
                    Integer.valueOf(DictionaryUtil.getDataValueByName("goods_market_enable", "sales_apply"))));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    
    @ControllerLog("查看驳回原因")
    @RequestMapping("checkDismissal/{goodsId}")
    @Authority(opCode = "04010111", opName = "查看驳回原因")
    public String checkDismissal(@PathVariable("goodsId") Long goodsId, Map<String, Object> map) {
    	List<Map<String,Object>> examineList = ExamineService.selectExamineByGoodsIs(goodsId);
    	map.put("examine", examineList.get(0));
        return "goods/examine/dismissal";
    }
    
    @ControllerLog("获取dataTable商品数据")
    @RequestMapping("pageDataTableGoods")
    @ResponseBody
    @Authority(opCode = "04010110", opName = "获取dataTable商品数据")
    public DataTablePage<Goods> pageDataTableGoods(@RequestParam Map<String, Object> map) {
        DataTablePage<Goods> page = goodsService.pageDataTableGoods(map);
        return page;
    }
 
}
