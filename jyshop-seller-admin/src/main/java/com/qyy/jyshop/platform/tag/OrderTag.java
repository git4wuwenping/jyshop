package com.qyy.jyshop.platform.tag;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.element.AbstractMarkupSubstitutionElementProcessor;

import com.qyy.jyshop.platform.util.SpringContextUtils;
import com.qyy.jyshop.dao.OrderDao;
import com.qyy.jyshop.model.AuthUser;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;

@Component
public class OrderTag extends AbstractMarkupSubstitutionElementProcessor {
    
    public OrderTag() {
        super("odbtn");
    }
    
    @Override
    protected List<Node> getMarkupSubstitutes(Arguments arguments, Element element) {
        
        final List<Node> nodes = new ArrayList<Node>();
        
//        final String orderId = element.getAttributeValue("order_id");
        
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        
        String uri=request.getRequestURI();
        
        String orderId=uri.substring(uri.lastIndexOf("/")+1);
        
        if(!StringUtil.isEmpty(orderId)){
            
            OrderDao orderDao=SpringContextUtils.getBean(OrderDao.class);
            Order order=orderDao.selectByOrderIdNoSplit(Long.valueOf(orderId));
            
            if(order!=null && 
                    !StringUtil.isEmpty(order.getComId()) && (
                    order.getComId().equals(0) ||
                    order.getComId().equals(this.getLoginUser().getComId()))){
                
                //添加发货btn
                if(order.getOrderStatus().equals(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status","order_allocation")))){
                    
                    final Element container = new Element("input");
                    container.setAttribute("type","button");
                    container.setAttribute("id","deliveryBtn");
                    container.setAttribute("class","btn");
                    container.setAttribute("value","发货");
                    container.setAttribute("order_id", order.getOrderId().toString());
                    nodes.add(container); 
                }
                
                //添加作废btn
                if(!order.getOrderStatus().equals(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status","order_return_pay")))
                   && !order.getOrderStatus().equals(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status","order_changed")))
                   && !order.getOrderStatus().equals(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status","order_complete")))
                   && !order.getOrderStatus().equals(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status","order_returnlation")))
                   && !order.getOrderStatus().equals(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status","order_cancel")))){
                    
                    final Element container = new Element("input");
                    container.setAttribute("type","button");
                    container.setAttribute("id","cancleBtn");
                    container.setAttribute("class","btn");
                    container.setAttribute("value","作废");
                    container.setAttribute("order_id", order.getOrderId().toString());
                    nodes.add(container); 
                }
                
            }
        }
        
        return nodes;
    }

    @Override
    public int getPrecedence() {
        return 100;
    }
    
    public AuthUser getLoginUser(){
        return (AuthUser) ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).
                getRequest().getAttribute("loginUser");
    }
}
