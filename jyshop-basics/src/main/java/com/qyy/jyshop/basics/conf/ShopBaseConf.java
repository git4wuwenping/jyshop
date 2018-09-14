package com.qyy.jyshop.basics.conf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.qyy.jyshop.dao.CoinParamConfigDao;
import com.qyy.jyshop.dao.OrderParamConfigDao;
import com.qyy.jyshop.dao.ProfitParamDao;
import com.qyy.jyshop.model.CoinParamConfig;
import com.qyy.jyshop.model.OrderParamConfig;
import com.qyy.jyshop.model.ProfitParam;
import com.qyy.jyshop.redis.RedisDao;

@Component
@Order(value = 0)
public class ShopBaseConf implements CommandLineRunner {

	private final Log logger = LogFactory.getLog(ShopBaseConf.class);
	@Autowired
	private CoinParamConfigDao coinParamConfigDao;
	@Autowired
	private OrderParamConfigDao orderParamConfigDao;
	@Autowired
	private ProfitParamDao profitParamDao;

	@Autowired
	private RedisDao redisDao;

	/**
	 * 获取分润配置，订单配置，金币配置
	 */
	@Override
	public void run(String... arg0) throws Exception {
		logger.info("开始加载商城基础参数配置...");

		CoinParamConfig coinParamConfig = coinParamConfigDao.selectAll().get(0);
		redisDao.saveObject("shopParam_coinParamConfig", coinParamConfig);
		OrderParamConfig orderParamConfig = orderParamConfigDao.selectAll().get(0);
		redisDao.saveObject("shopParam_orderParamConfig", orderParamConfig);
		ProfitParam profitParam = profitParamDao.selectAll().get(0);
		redisDao.saveObject("shopParam_profitParam", profitParam);

		logger.info("完成加载商城基础参数配置...");
	}

	@SuppressWarnings("unchecked")
	public <T> T getParamConfig(Class<T> clazz, String key) {
		Object object = redisDao.getObject(key);
		try{
			T cast = clazz.cast(object);
			if(cast != null)
                return cast;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(clazz.getSimpleName().equals("CoinParamConfig")){
			CoinParamConfig coinParamConfig = coinParamConfigDao.selectAll().get(0);
			redisDao.saveObject("shopParam_coinParamConfig", coinParamConfig);
			return (T) coinParamConfig;
		}else if(clazz.getSimpleName().equals("OrderParamConfig")){
			OrderParamConfig orderParamConfig = orderParamConfigDao.selectAll().get(0);
			redisDao.saveObject("shopParam_orderParamConfig", orderParamConfig);
			return (T) orderParamConfig;
		}else if(clazz.getSimpleName().equals("ProfitParam")){
			ProfitParam profitParam = profitParamDao.selectAll().get(0);
			redisDao.saveObject("shopParam_profitParam", profitParam);
			return (T) profitParam;
		}
		return null;
	}

}
