package com.qyy.jyshop.seller.chat.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.qyy.jyshop.pojo.ExcelData;
import com.qyy.jyshop.pojo.PageAjax;

public interface MemberChatService {
	
    /**
     * 获取用户当天咨询信息
     * @author hwc
     * @created 2018年4月8日 上午9:07:44
     * @param params
     * @return
     */
    public List<Map<String,Object>> queryTodayChatList(Map<String,Object> params);
    
	/**
	 * 获取用户最新咨询信息列表(分页)
	 * @param memberId 用户 Id
	 * @return
	 */
	public PageAjax<Map<String, Object>> pageByLatest(PageAjax<Map<String,Object>> page);
	
	/**
	 * 查询用户咨询列表(分页)
	 * @param page
	 * @return
	 */
	public PageAjax<Map<String, Object>> pageMemberChatByParams(PageAjax<Map<String,Object>> page);
	/**
	 * 导出咨询信息
	 * @author Tonny
	 * @throws Exception 
	 * @date 2018年4月16日
	 */
	public ExcelData getExportData(Map<String, Object> map, HttpServletResponse response) throws Exception;
	
}
