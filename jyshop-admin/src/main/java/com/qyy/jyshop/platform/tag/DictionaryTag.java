package com.qyy.jyshop.platform.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.dom.Text;
import org.thymeleaf.processor.element.AbstractMarkupSubstitutionElementProcessor;

import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;

/**
 * 数据字典标签
 * @author hwc
 * @created 2017年12月19日 下午4:09:36
 */
@Component
public class DictionaryTag extends AbstractMarkupSubstitutionElementProcessor {
       
    public DictionaryTag() {
        super("dic");
    }
    
    @Override
    protected List<Node> getMarkupSubstitutes(Arguments arguments, Element element) {

        
        final String groupName = element.getAttributeValue("group_name");
        
        final List<Node> nodes = new ArrayList<Node>();
        
        if(!StringUtil.isEmpty(groupName)){
            
            final String id = element.getAttributeValue("id");
            final String name = element.getAttributeValue("name");
            final String value = element.getAttributeValue("value");
            final String type = element.getAttributeValue("type");
            
            if("option".equals(type)){

                Map dataMap=DictionaryUtil.getDataGroupMap(groupName);

                if(dataMap!=null && !StringUtil.isEmpty(dataMap.get("dataItemList"))){
                    
                    JSONArray jsonArray=JSONArray.fromObject(dataMap.get("dataItemList"));
                    for(int i=0;i<jsonArray.size();i++){
                        JSONObject dataJson= jsonArray.getJSONObject(i);
                        final Element container = new Element("option");
                        container.setAttribute("value",String.valueOf(dataJson.get("value")));
                        final Text text = new Text(String.valueOf(dataJson.get("label")));
                        container.addChild(text);
                        nodes.add(container); 
                        
                    }
                }
                
            }
        }
            

        return nodes;
    }

    @Override
    public int getPrecedence() {
        return 99;
    }

   

}
