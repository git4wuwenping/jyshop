package com.qyy.jyshop.basics.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.model.CoinParamConfig;
import com.qyy.jyshop.model.OrderParamConfig;
import com.qyy.jyshop.model.ProfitParam;
import com.qyy.jyshop.redis.RedisDao;

@RestController
public class ShopBaseConfController extends AppBaseController {

    private static final String String = null;
    @Autowired
    private RedisDao redisDao;

    /**
     * @author jiangbin
     */
    @RequestMapping(value = "/anon/saveShopBaseConf")
    public Map<String, Object> saveShopBaseConf(@RequestParam Map<String, Object> map) {
        String code = (String) map.get("code");
        if (code == null || !code.equals("CHEERTEA")) {
            return this.outMessage(-1, "校验码不正确", null);
        }
        String type = (String) map.get("type");
        if (type == null || type.equals("")) {
            return this.outMessage(-1, "类型为空", null);
        }
        try {
            if (type.equals("profit")) {
                ProfitParam profitParam = JSON.parseObject((String) map.get("profitParam"), ProfitParam.class);
                if (profitParam == null) {
                    return this.outMessage(-1, "保存数据为空", null);
                }
                redisDao.saveObject("shopParam_profitParam", profitParam);
            } else if (type.equals("coin")) {
                CoinParamConfig coinParamConfig = JSON.parseObject((String) map.get("coinParamConfig"),
                        CoinParamConfig.class);
                if (coinParamConfig == null) {
                    return this.outMessage(-1, "保存数据为空", null);
                }
                redisDao.saveObject("shopParam_coinParamConfig", coinParamConfig);
            } else if (type.equals("order")) {
                OrderParamConfig orderParamConfig = JSON.parseObject((String) map.get("orderParamConfig"),
                        OrderParamConfig.class);
                if (orderParamConfig == null) {
                    return this.outMessage(-1, "保存数据为空", null);
                }
                redisDao.saveObject("shopParam_orderParamConfig", orderParamConfig);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return this.outMessage(-1, "异常错误", null);
        }
        return this.outMessage(0, "保存成功", null);
    }
}
