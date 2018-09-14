package com.qyy.jyshop.admin.goodstype.service.impl;

import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.goodstype.service.SpecService;
import com.qyy.jyshop.dao.GiftPackageSpecDao;
import com.qyy.jyshop.dao.GoodsSpecDao;
import com.qyy.jyshop.dao.MemberPointExtDao;
import com.qyy.jyshop.dao.SpecDao;
import com.qyy.jyshop.dao.SpecValuesDao;
import com.qyy.jyshop.model.GiftPackageSpec;
import com.qyy.jyshop.model.GoodsSpec;
import com.qyy.jyshop.model.MemberPointExt;
import com.qyy.jyshop.model.Spec;
import com.qyy.jyshop.model.SpecValues;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.EntityReflectUtil;
import com.qyy.jyshop.util.StringUtil;
import com.qyy.jyshop.vo.SpecVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.servlet.http.HttpServletRequest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SpecServiceImpl extends AbstratService<Spec> implements SpecService{

    @Autowired
    private SpecDao specDao;
    @Autowired
    private SpecValuesDao specValuesDao;
    @Autowired
    private GoodsSpecDao goodsSpecDao;
    @Autowired
    private GiftPackageSpecDao giftPackageSpecDao;
    @Autowired
    private MemberPointExtDao memberPointExtDao;
    @Override
    public String selectGoodBySpecValueId(Long specValueId) {
        Integer count = specDao.selectGoodBySpecValueId(specValueId);
        if(count>0){
            return "true";
        }else{
            return "false";
        }
    }

    @ServiceLog("获取规格信息")
    @Override
    public Spec queryBySpecId(Long specId){
        return this.specDao.selectComSpecBySpecId(specId,this.getLoginUserComId());
    }
    
    @Override
    public List<SpecVo> selectSpecsByGpId(Long gpId) {
        return specDao.selectSpecsByGpId(gpId);
    }
    
    @ServiceLog("获取当前管理员管理的所有的规格")
    @Override
    public List<Spec> queryByComId(){
        return this.specDao.selectByComId(this.getLoginUserComId());
    }

    @ServiceLog("根据规格Ids获取规格列表")
    @Override
    public List<Spec> queryBySpecIds(Long[] specIds){
        return this.specDao.selectBySpecIds(specIds);
    }
    
    @ServiceLog("获取规格列表(分页)")
    @Override
    public PageAjax<Spec> pageSpec(PageAjax<Spec> page,Spec spec){
        return this.queryPage(page, spec);
    }
    
    @ServiceLog("添加规格")
    @Override
    @Transactional
    public String doAddSpec(Map<String,Object> specMap) {
        
        if(specMap==null)
            return "添加失败,提交数据为空...";
        if(StringUtil.isEmpty(specMap.get("specName")))
            return "规格名称不能为空...";
        Spec spec=EntityReflectUtil.toBean(Spec.class, specMap);
        Timestamp nowDate=new Timestamp(System.currentTimeMillis());
        spec.setComId(this.getLoginUserComId());
        spec.setCreationTime(nowDate);
        this.specDao.insertSelective(spec);
        
        HttpServletRequest request = getRequest();
        String[] specValue=request.getParameterValues("specValue");
        
        if(!StringUtil.isEmpty(specValue) && specValue.length>0){

            List<SpecValues> specValuesList = new ArrayList<SpecValues>();
            SpecValues specValues = null;
            for(int i=0;i<specValue.length;i++){
                if(!StringUtil.isEmpty(specValue)){
                    specValues = new SpecValues();
                    specValues.setSpecId(spec.getSpecId());
                    specValues.setSpecValue(specValue[i]);
                    specValues.setCreationTime(nowDate);
                    specValuesList.add(specValues);
                }else
                    return "规格值不能为空...";
            }
            if(specValuesList.size()>0)
                this.specValuesDao.batchInsert(specValuesList);
        }
        return null;
    }
    
    @ServiceLog("修改规格")
    @Override
    @Transactional
    public String doEditSpec(Map<String,Object> specMap){
        
        if(specMap==null)
            return "添加失败,提交数据为空...";
        if(StringUtil.isEmpty(specMap.get("specName")))
            return "规格名称不能为空...";
        
        Spec spec=this.queryByID(Long.valueOf(specMap.get("specId").toString()));
        
        if(spec==null)
            return "获取规格信息失败...";
        
        if(!this.getLoginUserComId().equals(0) && !this.getLoginUserComId().equals(spec.getComId()))
            return "您没有权限修改该规格...";
        
        Timestamp nowDate=new Timestamp(System.currentTimeMillis());
        spec.setSpecName(specMap.get("specName").toString());
        this.specDao.updateByPrimaryKeySelective(spec);
        
        //this.specValuesDao.delBySpecId(spec.getSpecId());

        HttpServletRequest request = getRequest();
        String[] specValueIdArray=request.getParameterValues("specValueId");
        String[] specValueArray=request.getParameterValues("specValue");
        List<Long> specValueIdList=this.specValuesDao.selectOfSpecValuesId(spec.getSpecId());
        
        if(!StringUtil.isEmpty(specValueIdArray) && specValueIdArray.length>0){
            List<SpecValues> addSpecValuesList = new ArrayList<SpecValues>();
            SpecValues specValues = null;
            for(int i=0;i<specValueIdArray.length;i++){
                if(StringUtil.isEmpty(specValueArray[i]))
                    return "规格值不能为空...";
                if(StringUtil.isEmpty(specValueIdArray[i])){
                    specValues = new SpecValues();
                    specValues.setSpecId(spec.getSpecId());
                    specValues.setSpecValue(specValueArray[i]);
                    specValues.setCreationTime(nowDate);
                    addSpecValuesList.add(specValues);
                }else{
                    SpecValues oldSpecValues=new SpecValues();
                    oldSpecValues.setSpecValueId(Long.valueOf(specValueIdArray[i]));
                    oldSpecValues.setSpecValue(specValueArray[i]);
                    this.specValuesDao.updateByPrimaryKeySelective(oldSpecValues);
                    
                    if(specValueIdList!=null && specValueIdList.size()>0 &&
                            specValueIdList.contains(Long.valueOf(specValueIdArray[i]))){
                        endReId:for(int k=0;k<specValueIdList.size();k++){
                            if(specValueIdList.get(k).equals(Long.valueOf(specValueIdArray[i]))){
                                specValueIdList.remove(k);
                                break endReId;
                            }
                        }
                    }
                }
            }
            if(addSpecValuesList.size()>0){
                this.specValuesDao.batchInsert(addSpecValuesList);
            }
        }
        if(specValueIdList!=null && specValueIdList.size()>0)
            this.specValuesDao.deleteBySpecValuesIds(specValueIdList);
        return null; 
    }

    @ServiceLog("删除规格")
    @Transactional
    @Override
    public String doDelSpec(Long specId) {
        if(StringUtil.isEmpty(specId))
            return "规格Id不能为空...";
        Example example = new Example(GoodsSpec.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("specId", specId);
//        System.out.println(this.goodsSpecDao.selectCountByExample(example));
        if(this.goodsSpecDao.selectCountByExample(example)>0)
            return "该规格己引用,删除失败...";
        else{
            example = new Example(GiftPackageSpec.class);
            criteria = example.createCriteria();
            criteria.andEqualTo("specId", specId);
            if(giftPackageSpecDao.selectCountByExample(example)>0){
                return "该规格己引用,删除失败...";
            }else{
                this.specDao.deleteByPrimaryKey(specId);
                this.specValuesDao.delBySpecId(specId);
            }
        }
        return null;
    }
	
}
