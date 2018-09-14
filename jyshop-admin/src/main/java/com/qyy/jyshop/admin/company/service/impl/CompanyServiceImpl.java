package com.qyy.jyshop.admin.company.service.impl;

 
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.company.service.CompanyService;
import com.qyy.jyshop.dao.AuthRoleDao;
import com.qyy.jyshop.dao.AuthUserDao;
import com.qyy.jyshop.dao.CompanyDao;
import com.qyy.jyshop.dao.DepotDao;
import com.qyy.jyshop.dao.ShopStoreDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.AuthRole;
import com.qyy.jyshop.model.AuthUser;
import com.qyy.jyshop.model.Company;
import com.qyy.jyshop.model.Depot;
import com.qyy.jyshop.model.ShopStore;
import com.qyy.jyshop.pojo.DataTablePage;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.EntityReflectUtil;
import com.qyy.jyshop.util.MD5Util;
import com.qyy.jyshop.util.StringUtil;
@Service
public class CompanyServiceImpl  extends AbstratService<Company> implements CompanyService {
	 
	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private AuthUserDao authUserDao;
	@Autowired
	private AuthRoleDao authRoleDao;
	@Autowired
	private ShopStoreDao shopStoreDao;
	@Autowired
	private DepotDao depotDao;

	/**
	 * 分页查询供应商信息
	 */
	@Override
	public PageAjax<Map<String, Object>> pageCompanyAjax(PageAjax<Map<String,Object>> page) {
		ParamData pd = this.getParamData(page.getPageNo(),page.getPageSize());
        return this.pageQuery("CompanyDao.selectComsByParams", pd);
	}

	/**
	 * 根据id查询供应商信息
	 */
	@Override
	public Map<String, Object> queryComById(int comId) {
		List<Map<String, Object>> comMap = companyDao.queryComById(comId);
		return comMap.get(0);
	}

	/**
	 * 查询供应商推荐人
	 */
	@Override
	public Company queryComByParentId(Integer parentId,Integer comId) {
		return companyDao.queryComByParentId(parentId,comId);
	}

	/**
	 * 更新供应商
	 * @throws Exception 
	 */
	@Override
	public String editCompany(Map<String, Object> map) throws Exception {
		Company company  = EntityReflectUtil.toBean(Company.class,map);
		Integer comId = company.getComId();
		//更新店铺
		ShopStore shopStore = EntityReflectUtil.toBean(ShopStore.class,map);
		shopStore.setComId(comId);
		company.setStoreName(shopStore.getShopStoreName());
		this.update(company);
		shopStoreDao.updateStoreById(shopStore);
		if(company.getComType()==0){		//供应商
			AuthUser user  = EntityReflectUtil.toBean(AuthUser.class,map);
			user.setComId(comId);
			user.setPassword(MD5Util.encrypt(user.getPassword()));
			authUserDao.updateUserById(user);
		}
		return null;
	}

	/**
	 * 添加供应商
	 * @throws Exception 
	 */
	@Override
	public String doAddCom(Map<String, Object> map) throws Exception {
		Company company  = EntityReflectUtil.toBean(Company.class,map);
		//this.insert(company);
		//添加店铺
		ShopStore shopStore = EntityReflectUtil.toBean(ShopStore.class,map);
		company.setStoreName(shopStore.getShopStoreName());
		company.setComCode(0L);
		//companyDao.insertCompany(company);	//返回新增主键
		companyDao.insertSelective(company);
		
		Integer comId = company.getComId();
		shopStore.setComId(comId);
		shopStoreDao.insert(shopStore);
		//添加仓库
		Depot depot = new Depot();
		depot.setComId(comId);
		depot.setName(company.getComName()+"的仓库");
		depotDao.insert(depot);
		
		//判断供应商类型
		//Integer companyId = company.getComId();	
		if(comId != 0){	//商户
			//获取所属角色
			String roleName = "company";
			AuthRole role = authRoleDao.selectByRoleName(roleName);
			//获取当前时间
			Timestamp d = new Timestamp(System.currentTimeMillis());
			AuthUser user  = EntityReflectUtil.toBean(AuthUser.class,map);
			
			user.setPassword(MD5Util.encrypt(user.getPassword()));
			user.setComId(comId);
			user.setUseable(0);
			user.setRoleId(role.getRoleId());
			user.setAddTime(d);
			user.setName(user.getUsername());
			authUserDao.insert(user);
			
		}
		return null;
	}

	/**
	 * 停用\启用
	 */
	@Override
	public String isDisabledExamine(int comId,Integer useable) {
			companyDao.isDisabledExamine(comId,useable);
		return null;
	}

	/**
	 * 查询所有供应商
	 */
	@Override
	public List<Company> queryAllCom() {
		return this.queryAll();
	}
	/**
	 * 查询供应商名称是否重复
	 */
	@Override
	public String selectComByName(String comName) {
		if(!StringUtil.isEmpty(comName)){
			int count =companyDao.selectComByName(comName);
			if(count==0){
					return "false";
			}else{
					throw new AppErrorException("供应商名已经被占用");
			}
		}else{
			throw new AppErrorException("供应商名称不能为空");
		}
	}

	//@ServiceLog("供应商分页<DataTablePage>")
    @Override
    public DataTablePage<Map<String, Object>> pageDataTableCom(Map<String, Object> map) {
        //分页
        Integer start = Integer.parseInt(map.get("start").toString());
        Integer pageSize = Integer.parseInt(map.get("length").toString());
        Integer draw = Integer.parseInt(map.get("draw").toString());
        
        ParamData params = this.getParamData(start/pageSize+1, pageSize);
        if(map.get("search[storeName]") != null && StringUtils.isNotBlank(map.get("search[storeName]").toString())){
            params.put("storeName", map.get("search[storeName]"));
        }
        if(map.get("search[comName]") != null && StringUtils.isNotBlank(map.get("search[comName]").toString())){
            params.put("comName", map.get("search[comName]"));
        }
        PageAjax<Map<String,Object>> page = this.pageQuery("CompanyDao.selectDataTableCom", params );
        return new DataTablePage(draw, page.getRows(),page.getTotal(), page.getTotal());
    }
	
}
