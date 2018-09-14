package com.qyy.jyshop.seller.chat.service.impl;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.qyy.jyshop.dao.MemberChatDao;
import com.qyy.jyshop.model.MemberChat;
import com.qyy.jyshop.pojo.ExcelData;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.seller.chat.service.MemberChatService;
import com.qyy.jyshop.seller.common.annotation.ServiceLog;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DateUtil;
import com.qyy.jyshop.util.EntityReflectUtil;
import com.qyy.jyshop.util.excel.ExcelUtil;
import com.qyy.jyshop.util.excel.dto.ChatForExcel;

@Service
public class MemberChatServiceImpl extends AbstratService<MemberChat> implements MemberChatService {
	
    @Autowired
    private MemberChatDao memberChatDao;
    
    @ServiceLog("获取用户当天咨询列表")
    @Override
    public List<Map<String,Object>> queryTodayChatList(Map<String,Object> params){
        params.put("nickname", params.get("nickname")==null?"":params.get("nickname").toString().trim());
        params.put("createTime", DateUtil.getCurDate());
        params.put("comId",this.getLoginUserComId());
        return this.memberChatDao.selectMemberChatByParams(params);
    }
    
	@ServiceLog("获取用户最新咨询信息列表(分页)")
	@Override
	public PageAjax<Map<String, Object>> pageByLatest(PageAjax<Map<String,Object>> page){
		
		ParamData pd = this.getParamData(page.getPageNo(),page.getPageSize());
		pd.put("comId",this.getLoginUserComId());
        return this.pageQuery("MemberChatDao.selectByLatest", pd);
	}
	
	@ServiceLog("查询用户咨询列表(分页)")
	@Override
	public PageAjax<Map<String, Object>> pageMemberChatByParams(PageAjax<Map<String,Object>> page){
	 	ParamData pd = this.getParamData(page.getPageNo(),page.getPageSize());
	 	pd.put("comId",this.getLoginUserComId());
        return this.pageQuery("MemberChatDao.selectMemberChatByParams", pd);
	}

	@Override
	public ExcelData getExportData(Map<String, Object> map, HttpServletResponse response) throws Exception {
	    if(map==null)
	        map=new HashMap<String,Object>();
	    map.put("comId", this.getLoginUserComId());
		List<Map<String,Object>> memberChatList = memberChatDao.queryExportMemberChatData(map);;//必须按合并列排序
        List<ChatForExcel> list = new ArrayList<ChatForExcel>();
        for(Map<String,Object> data:memberChatList) {
        	ChatForExcel chatForExcel = EntityReflectUtil.toBean(ChatForExcel.class, data);
            list.add(chatForExcel);
        }
        
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("title", "咨询信息表");
        String xlsType="咨询信息";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
        ExcelUtil.getInstance().exportObj2ExcelByTemplate(response,datas, "template.xls", new FileOutputStream("E:/"+df.format(new Date())+"咨询信息导出.xls"),
                list, ChatForExcel.class, true,2,xlsType);
		return null;
	}
	
	
	
}
