package com.qyy.jyshop.basics.conf;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.qyy.jyshop.redis.RedisDao;
import com.qyy.jyshop.util.XmlUtil;



@Component  
@Order(value=1)
public class DataDictListener implements CommandLineRunner{
	
	protected static Log logger = LogFactory.getLog( DataDictListener.class);
	
	@Autowired
	private RedisDao redisDao;
	
	@Value("${dictionary-path}")
	private String dictFileLocation;

	/**
	 * 获取xml数据并将其存在redis中
	 * @author hwc
	 * @created 2017年11月20日 下午5:10:08
	 * @param arg0
	 * @throws Exception
	 * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
	 */
	@Override
	public void run(String... arg0) throws Exception {
		logger.info("初始化数据字典数据开始");
        Map dataMap = XmlUtil.parseXmlToDataDict(dictFileLocation);
        if (dataMap == null){
            throw new IllegalStateException("can not load data dict configration, please check the data file!");
        } else{
        	redisDao.saveObject("_dataMap", dataMap);
    		logger.info("初始化数据字典数据结束");
            return;
        }
	}

}
