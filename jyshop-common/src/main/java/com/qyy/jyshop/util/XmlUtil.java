package com.qyy.jyshop.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.qyy.jyshop.conf.Constant;


/**
 * xml操作类
 * @author hwc
 * @created 2015-8-3 上午10:51:13
 */
public class XmlUtil {

    private static final Logger LOGGER = Logger.getLogger(XmlUtil.class);

    /**
     * 
     *  利用dom4j将xml文件转换为dom对象
     * @author hwc
     * @created 2015-8-3 上午11:01:12
     * @param reader
     * @param xmlFilePath xml文件路径
     * @return dom对象
     */
    public static final Document xmlToDocument(SAXReader reader, String xmlFilePath) {
        Document doc = null;
        InputStream inputStream = XmlUtil.class.getResourceAsStream(xmlFilePath);
        try {
            doc = reader.read(inputStream);
        } catch (DocumentException e) {
            LOGGER.error("classpath不存在文件(" + xmlFilePath + ")：", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ie) {
                LOGGER.error("关闭流发生错误：", ie);
            }
        }
        return doc;
    }


    /**
     * 
     * 将数据字典配置文件读取到map中
     * @author hwc
     * @created 2015-8-3 上午11:00:29
     * @param dictFile 数据字典文件路径
     * @return Map形式的数据集合
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static final Map parseXmlToDataDict(String dictFile) {
        SAXReader reader = new SAXReader();
        Document doc = xmlToDocument(reader, dictFile);
        if (doc == null) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("数据字典文件读取失败！");
            return null;
        }
        Element root = doc.getRootElement();
        Map dictMap = new HashMap();
        for (Iterator it = root.elementIterator(); it.hasNext();) {
            Element groupElement = (Element) it.next();
            Map groupMap = new HashMap();
            String groupName = groupElement.attributeValue(Constant.NAME);
            groupMap.put(Constant.NAME, groupName);
            groupMap.put(Constant.DESCRIPTION, groupElement.attributeValue(Constant.DESCRIPTION));
            List itemList = new ArrayList();
            for (Iterator itg = groupElement.elementIterator(); itg.hasNext();) {
                Element itemElement = (Element) itg.next();
                Map dataMap = new HashMap();
                String itemName = itemElement.attributeValue(Constant.NAME);
                dataMap.put(Constant.NAME, itemName);
                dataMap.put(Constant.VALUE, itemElement.attributeValue(Constant.VALUE));
                dataMap.put(Constant.LABEL, itemElement.attributeValue(Constant.LABEL));
                itemList.add(dataMap);
                groupMap.put(itemName, dataMap);
            }
            groupMap.put(Constant.DATA_ITEM_LIST, itemList);
            dictMap.put(groupName, groupMap);
        }
        return dictMap;
    }
}
