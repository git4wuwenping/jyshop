package com.qyy.jyshop.admin.coin.service;

import com.qyy.jyshop.model.CoinParamConfig;

public interface CoinParamConfigService {

	CoinParamConfig querycoinParamConfig();

	String edit(CoinParamConfig config);

}
