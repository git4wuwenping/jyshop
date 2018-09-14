package com.qyy.jyshop.controller;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.exception.AppLoginException;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.StringUtil;

public abstract class AppBaseController {
	
    /**日志*/
    public static Logger logger = LoggerFactory.getLogger(AppBaseController.class);
    
	public Map<String, Object> outMessage(Integer redCode, String resInfo,Map<String, Object> resData) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("res_code", redCode);
		resultMap.put("res_info", resInfo);
		resultMap.put("res_data", resData);
		return resultMap;
	}
	
	public Map<String, Object> outErrorMessage() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("res_code", "1");
		resultMap.put("res_info", "服务己关闭");
		resultMap.put("res_data", null);
		return resultMap;
	}
	
	public Map<String,Object> getPageMap(PageAjax<Map<String,Object>> page){
        Map<String,Object> pageMap=new HashMap<String, Object>();
        //页码
        pageMap.put("pageNo", page.getPageNo());
        //页数
        pageMap.put("pageSize", page.getPageSize());
        //第一页页码
        pageMap.put("firstPage", page.getFirstPage());
        //最后页页码
        pageMap.put("lastPage", page.getLastPage());
        //下一页页码(0:没有下一页)
        pageMap.put("nextPage", page.getNextPage());
        //总页码
        pageMap.put("pages", page.getPages());
        //当前页数据数
        pageMap.put("size", page.getSize());
        //当前页开始数据行
        pageMap.put("startRow", page.getStartRow());
        //数据总数
        pageMap.put("total", page.getTotal());
        //上一页页码
        pageMap.put("prePage", page.getPrePage());
        //数据
        pageMap.put("dataList", page.getRows());
        
        return pageMap;
    }
	
	public <T> Map<String,Object> getPageEntity(PageAjax<T> page){
        Map<String,Object> pageMap=new HashMap<String, Object>();
        //页码
        pageMap.put("pageNo", page.getPageNo());
        //页数
        pageMap.put("pageSize", page.getPageSize());
        //第一页页码
        pageMap.put("firstPage", page.getFirstPage());
        //最后页页码
        pageMap.put("lastPage", page.getLastPage());
        //下一页页码(0:没有下一页)
        pageMap.put("nextPage", page.getNextPage());
        //总页码
        pageMap.put("pages", page.getPages());
        //当前页数据数
        pageMap.put("size", page.getSize());
        //当前页开始数据行
        pageMap.put("startRow", page.getStartRow());
        //数据总数
        pageMap.put("total", page.getTotal());
        //上一页页码
        pageMap.put("prePage", page.getPrePage());
        //数据
        pageMap.put("dataList", page.getRows());
        
        return pageMap;
    }
	
	@ExceptionHandler({ AppErrorException.class })
    @ResponseBody
    public Map<String,Object> AppPromptExceptionHandler(AppErrorException e) {
        logger.error("请求接口发生异常:", e);
        return this.outMessage(1, e.getMessage(), null);
    }
	
	@ExceptionHandler({ AppLoginException.class })
    @ResponseBody
    public Map<String,Object> AppLoginExceptionHandler(AppLoginException e) {
        logger.error("登陆接口发生异常:", e);
        return this.outMessage(1, StringUtil.isEmpty(e.getMessage())?"请先登陆...":e.getMessage(), null);
    }
	
	@ExceptionHandler({ HystrixRuntimeException.class })
    @ResponseBody
    public Map<String,Object> HystrixRuntimeExceptionHandler(HystrixRuntimeException e) {
        logger.error("请求接口发生异常:", e);

        if(e.getCause()!=null ){
            if(!StringUtil.isEmpty(e.getCause().getMessage()) &&
                    e.getCause().getMessage().indexOf("content:")>=0){
                JSONObject jsStr = JSONObject.fromObject(e.getCause().getMessage().substring(e.getCause().getMessage().indexOf("content:")+8));
                return this.outMessage(1, String.valueOf(jsStr.get("message")), null);
            }
        }
        
        return this.outMessage(1, e.getMessage(), null);
    }
	
	
}
