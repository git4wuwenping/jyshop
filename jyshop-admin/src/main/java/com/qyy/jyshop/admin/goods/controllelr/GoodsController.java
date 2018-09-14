package com.qyy.jyshop.admin.goods.controllelr;


import com.alibaba.fastjson.JSONObject;
import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.company.service.CompanyService;
import com.qyy.jyshop.admin.examine.service.ExamineService;
import com.qyy.jyshop.admin.delivery.service.DlyTypeService;
import com.qyy.jyshop.admin.goods.service.GoodsGalleryService;
import com.qyy.jyshop.admin.goods.service.GoodsService;
import com.qyy.jyshop.admin.goods.service.ProductService;
import com.qyy.jyshop.admin.goodscat.service.GoodsCatService;
import com.qyy.jyshop.admin.goodstype.service.GoodsTypeService;
import com.qyy.jyshop.admin.goodstype.service.SpecService;
import com.qyy.jyshop.admin.shopstore.service.ShopStoreService;
import com.qyy.jyshop.dao.GoodsSpecDao;
import com.qyy.jyshop.model.AuthUser;
import com.qyy.jyshop.model.Company;
import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.model.GoodsGallery;
import com.qyy.jyshop.model.GoodsType;
import com.qyy.jyshop.model.ShopStore;
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
    private CompanyService companyService;
    @Autowired  
    private GoodsGalleryService goodsGalleryService;
    @Autowired
    private ShopStoreService storeService;
    @Autowired
    private SpecService specService;
    @Autowired
    private GoodsSpecDao goodsSpecDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private ExamineService examineService;
    @Autowired
    private DlyTypeService dlyTypeService;
    
    @Authority(opCode = "040101", opName = "自营商品")
    @RequestMapping("dirGoodsMain")
    public String dirGoodsMain(Map<String, Object> map,Integer state) {    
        map.put("comId", 0);
        map.put("state", state);
//        Integer comId = ((AuthUser) BaseController.getRequest().getAttribute("loginUser")).getComId();
        //List<Map<String, Object>> shopStoreList =  goodsService.selectAllShopByComId(comId);
        return "goods/dirGoods_main";
    }
    
    @Authority(opCode = "040106", opName = "商户商品")
    @RequestMapping("comGoodsMain")  
    public String comOrderMain(Map<String, Object> map,Integer state) {
        map.put("comIds", 0);
        map.put("comId", ((AuthUser) BaseController.getRequest().getAttribute("loginUser")).getComId());
        map.put("state", state);
        return "goods/comGoods_main";
    }
    
    
    @Authority(opCode = "040105", opName = "海外商品")
    @RequestMapping("overseasGoodsMain")
    public String overseasGoodsMain(Map<String, Object> map,Integer state) {    
        map.put("comId", 0);
        map.put("state", state);
        return "goods/overseas_goods_main";
    }

    @ControllerLog("查询商品列表")
    @RequestMapping("pageGoodsAjax")
    @ResponseBody
    @Authority(opCode = "04010101", opName = "查询商品列表")
    public PageAjax<Map<String,Object>> pageGoodsAjax(PageAjax<Map<String,Object>> page,Integer state) {
        return goodsService.pageGoods(page,state);
    }
    
    @ControllerLog("添加商品页面")
    @RequestMapping("preAddGoods/{goodsType}")
    @Authority(opCode = "090101", opName = "添加商品页面")
    public String preAddGoods(@PathVariable("goodsType")Integer goodsType,Map<String, Object> map) {
        
        List<TreeNode> treeViewResultList =  goodsCatService.getGoodsCatTreeResult();
        map.put("treeViewResultList", JSONObject.toJSONString(treeViewResultList));
        List<Company> companyList = companyService.queryAllCom();
        map.put("companyList", companyList);
        // 当前登录的用户
        AuthUser loginUser = (AuthUser) BaseController.getRequest().getAttribute("loginUser");
        map.put("goodsTypeList", this.goodsTypeService.queryByComId(loginUser.getComId()));
        map.put("dlyTypeList", this.dlyTypeService.queryByComId(loginUser.getComId()));
        map.put("comId", loginUser.getComId());
        map.put("storeList", storeService.findAllDirShopStores(loginUser.getComId()));
        map.put("goodsType", goodsType);
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
            System.out.println("添加商品出错了www.....");
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
        //店铺列表
        List<ShopStore> shopStores = storeService.findAllDirShopStores(goods.getComId());
        map.put("storeList", shopStores);
        //分类树
        List<TreeNode> treeViewResultList =  goodsCatService.getGoodsCatTreeResult();
        map.put("treeViewResultList", JSONObject.toJSONString(treeViewResultList));
        //供应商列表
        List<Company> companyList = companyService.queryAllCom();
        map.put("companyList", companyList);
        
        map.put("goodsTypeList", this.goodsTypeService.queryByComId(goods.getComId()));
        map.put("dlyTypeList", this.dlyTypeService.queryByComId(null));
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
        map.put("goodsType", goods.getGoodsType()); 
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
            System.out.println("修改商品出错了www.....");
            e.printStackTrace();
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
    
    @ControllerLog("商品上架")
    @RequestMapping("goodsSalesYes/{goodsId}")
    @ResponseBody
    @Authority(opCode = "04010106", opName = "商品上架")
    public AjaxResult goodsSalesYes(@PathVariable("goodsId") Long goodsId) {
        try{
            return AppUtil.returnObj(goodsService.doGoodsSalesExamine(goodsId,
                    Integer.valueOf(DictionaryUtil.getDataValueByName("goods_market_enable", "sales_yes"))));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    @ControllerLog("审核驳回页面")
    @RequestMapping("preGoodsSalesNo/{goodsId}")
    @Authority(opCode = "04010106", opName = "审核驳回页面")
    public String preGoodsSalesNo(@PathVariable("goodsId") Long goodsId,Map<String, Object> map) {
    	map.put("goodsId", goodsId);
    	return "goods/examine/goods_sales_no";
    }
    
    
    @ControllerLog("审核驳回")
    @RequestMapping("examineGoodsSalesNo")
    @ResponseBody
    @Authority(opCode = "04010106", opName = "商品驳回")
    public AjaxResult examineGoodsSalesNo(@RequestParam Map<String, Object> map) {
    	try {
    		 return AppUtil.returnObj(examineService.examineGoodsSalesNo(map));
		} catch (Exception e) {
			 return AppUtil.returnObj(2,e.getMessage());
		}
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
    
    @ControllerLog("删除商品")
    @RequestMapping("delGoods/{id}")
    @ResponseBody
    @Authority(opCode = "04010106", opName = "删除商品")
    public AjaxResult delGoods(@PathVariable("id") Long id) {
        try{
            return AppUtil.returnObj(goodsService.doDelGoods(id));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    @ControllerLog("获取dataTable商品数据")
    @RequestMapping("pageDataTableGoods")
    @ResponseBody
    @Authority(opCode = "04010110", opName = "获取dataTable商品数据")
    public DataTablePage<Map<String,Object>> pageDataTableGoods(@RequestParam Map<String, Object> map) {
        DataTablePage<Map<String,Object>> page = goodsService.pageDataTableGoods(map);
        return page;
    }
 
}
