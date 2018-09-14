package com.qyy.jyshop.basics.conf;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.qyy.jyshop.basics.service.AddressService;
import com.qyy.jyshop.model.Address;
import com.qyy.jyshop.redis.RedisDao;





@Component  
@Order(value=2)
public class AddressBaseData implements CommandLineRunner{

	protected static Log logger = LogFactory.getLog(AddressBaseData.class);
	
	@Autowired
	private RedisDao redisDao;
	@Autowired
	private AddressService addressService;
	
	@Override
	public void run(String... args) throws Exception {
		logger.info("初始化地址数据开始");
		List<Address> addressList=addressService.queryAddressList();
		redisDao.saveObject("addressList", addressList);
		logger.info("初始化地址数据结束");
	}

}
