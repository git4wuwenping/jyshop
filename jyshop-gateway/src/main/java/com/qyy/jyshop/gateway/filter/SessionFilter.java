package com.qyy.jyshop.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.qyy.jyshop.gateway.config.AppAnonymousFilter;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.redis.RedisDao;
import com.qyy.jyshop.util.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SessionFilter extends ZuulFilter  {
	
    private static Logger log = LoggerFactory.getLogger(SessionFilter.class);

    @Autowired
    private RedisDao redisDao;
    /**
     * 过滤器类型
     * pre:请求被路由之前执行
     * routing:在路由请求时被调用
     * post:在routing和error过滤之后被调用
     * error:处理请求时发生错误调用
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤器请求顺序(多个过滤器时依次执行)
     */  
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 判断该过滤器是否执行
     * true:执行
     * false:不执行
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        
        String currentRequestRefer = StringUtils.isEmpty(request.getHeader("origin")) ? request.getHeader("referer") : request.getHeader("origin");
        if (StringUtils.isNotEmpty(currentRequestRefer)) {
            response.addHeader("Access-Control-Allow-Origin", currentRequestRefer);
        }
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with, sid, mycustom, smuser,cookie,Access-Control-Allow-Origin,EX-SysAuthToken,EX-JSESSIONID");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        
        log.info("send {} request to {}", request.getMethod(), request.getRequestURL().toString());
        String url=request.getRequestURI().substring(request.getContextPath().length());
        Object token = request.getParameter("token");
        if(isInclude(url)){
        	return null;
        }else{
        	if(!StringUtil.isEmpty(token)) {
        		log.info("member token ok");
        		Member member=(Member)redisDao.getObject(token.toString());
				if(member!=null && !StringUtil.isEmpty(member.getMemberId())){
					return null;
				}
            }
        	log.warn("member token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            ctx.setResponseBody("{\"res_info\": \"no login\",\"res_data\": null,\"res_code\": -1}");
            return null;
        	
        }
        
//      throw new RuntimeException("aaaaaa...");
    }
    
    /**
	 * 是否需要过滤
	 * 
	 * @param url
	 * @return
	 */
	private boolean isInclude(String url) {
		System.out.println(url);
		if(url.startsWith("/attachment/qrcode/"))
			return true;
		String[] split = url.split("/");
        if(split.length >= 3) {
            String checkUrl = "/" + split[split.length-2];
            if(checkUrl.equals(AppAnonymousFilter.APP_ANONYMOUS_STR)){
                return true;
            }
        }
		
		for (String anonymousUrl : AppAnonymousFilter.APP_ANONYMOUS) {
			if(url.lastIndexOf("/")>=0)
				url=url.substring(url.lastIndexOf("/"));
			if (anonymousUrl.equals(url)) {
				return true;
			}
		}
		return false;
	}

}
