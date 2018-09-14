package com.qyy.jyshop.basics.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.basics.conf.ShopBaseConf;
import com.qyy.jyshop.basics.conf.WeixinAuthToken;
import com.qyy.jyshop.basics.conf.WeixinConfig;
import com.qyy.jyshop.basics.service.WxService;
import com.qyy.jyshop.basics.util.WechatSignUtil;
import com.qyy.jyshop.basics.util.WeixinUrlUtil;
import com.qyy.jyshop.dao.CoinDtlDao;
import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.CoinDtl;
import com.qyy.jyshop.model.CoinParamConfig;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.MemberWithdrawInfo;
import com.qyy.jyshop.redis.RedisDao;
import com.qyy.jyshop.util.IPUtil;
import com.qyy.jyshop.util.StringUtil;
import com.qyy.jyshop.util.TimestampUtil;
import com.qyy.jyshop.util.TokenUtil;

import net.sf.json.JSONObject;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@SuppressWarnings("ALL")
@Service
public class WxServiceImpl implements WxService {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private RedisDao redisDao;
    @Autowired
	private ShopBaseConf shopBaseConf;
    @Autowired
    private CoinDtlDao coinDtlDao;
    /**
     * 微信授权免登陆(待修改)
     * 
     * @author hwc
     * @created 2018年1月19日 上午11:32:19
     * @param code
     * @return
     * @see com.qyy.jyshop.basics.service.WxService # wxLogin(java.lang.String)
     */
    @Override
    @Transactional
    public Map<String, Object> wxLogin(String code,Integer reqType, Integer parentId) {
        
        if(StringUtil.isEmpty(code))
            throw new AppErrorException("code不能为空...");
        if(StringUtil.isEmpty(reqType))
            throw new AppErrorException("请求类型不能为空...");
        
        WeixinAuthToken weixinAuthToken = null;
        if(reqType.equals(0))
            weixinAuthToken=WeixinUrlUtil.getWeixinOpenidJson(code,
                    WeixinConfig.APP_ID,
                    WeixinConfig.APP_SECRET);
        else if(reqType.equals(1))
            weixinAuthToken=WeixinUrlUtil.getWeixinOpenidJson(code,
                    WeixinConfig.IOS_APP_ID,
                    WeixinConfig.IOS_APP_SECRET);
        else
            throw new AppErrorException("无效的请求类型...");
        
        if (weixinAuthToken != null) {
            String openId = weixinAuthToken.getOpenId();
            String unionId=weixinAuthToken.getUnionId();
            System.out.println("openId:"+openId);
            String accessToken = (String)redisDao.getObject("WEIXIN_ACCESS_TOKEN");
            if(StringUtils.isBlank(accessToken)){
                accessToken = WeixinUrlUtil.getAccessToken();
                redisDao.saveObject("WEIXIN_ACCESS_TOKEN", accessToken, 7200);
            }
            JSONObject jsonObject = null;
            if(reqType.equals(0)){
                jsonObject=WeixinUrlUtil.getUnionID(accessToken, openId);
            } else if(reqType.equals(1)){
                jsonObject=WeixinUrlUtil.getUnionIDForApp(weixinAuthToken.getAccessToken(), openId);
            }
            Map result = (Map) JSONObject.toBean(JSONObject.fromObject(jsonObject), Map.class);
            if (result.get("errmsg") != null || result.get("errcode") != null) {
                return null;
            }
            System.out.println("获取用户基本信息----" + result);
            if(StringUtil.isEmpty(unionId)){
                if (jsonObject != null) {
                    unionId = result.get("unionid").toString();
                }
            }
            String nickname = result.get("nickname") == null ? "微信用户" : result.get("nickname").toString();
            String weixinFace = result.get("headimgurl") == null ? "" : result.get("headimgurl").toString();
            String sex = result.get("sex") == null ? "0" : result.get("sex").toString();

            if (!StringUtil.isEmpty(unionId)) {

                Member member = this.memberDao.selectByUnionId(unionId);

                if (member == null) {
                    member = new Member();
                    CoinParamConfig coinParamConfig = shopBaseConf.getParamConfig(CoinParamConfig.class, "shopParam_coinParamConfig");
                    //member.setUname(openId);
                    member.setNickname(nickname);
                    //member.setPassword(StringUtil.md5("123456"));
                    member.setWeixinFace(weixinFace);
                    member.setSex(Integer.parseInt(sex));
                    member.setLvId(1);
                    member.setOpenId(openId);
                    member.setUnionId(unionId);
                    member.setPayPassword(StringUtil.md5(""));
                    member.setCoin( new BigDecimal(coinParamConfig.getCoinRegisterGet()));			//新用户注册 赠送新用户金币数量
                    member.setRegisterip(IPUtil.getIpAdd());
                    member.setRegtime(TimestampUtil.getNowTime());
                    if(!StringUtil.isEmpty(parentId) && parentId > 0){
                        //判断推荐人是否存在
                        Member parentMember = this.memberDao.selectByPrimaryKey(Long.valueOf(parentId.toString()));
                        //老用户推荐 赠送老用户金币数量
                        if(parentMember!=null){
                        	parentMember.setCoin(parentMember.getCoin().add(new BigDecimal(coinParamConfig.getCoinInviteGet())));
                        	member.setBecomeLowerTime(TimestampUtil.getNowTime());
                        	Example example = new Example(MemberWithdrawInfo.class);
                    		Criteria createCriteria = example.createCriteria();
                    		createCriteria.andEqualTo("memberId", parentMember.getMemberId());
                        	memberDao.updateByExample(parentMember, example);
                            member.setParentId(parentId);
                            //封装获取金币明细表
                            CoinDtl parentCoinDtl = new CoinDtl();
                            parentCoinDtl.setMemberId(parentMember.getMemberId());
                            parentCoinDtl.setCoinNum(coinParamConfig.getCoinInviteGet());
                            parentCoinDtl.setCreateTime(TimestampUtil.getNowTime());
                            parentCoinDtl.setCoinType((short)4);
                            coinDtlDao.insert(parentCoinDtl);		
                        }
                    }
                    this.memberDao.insertSelective(member);
                    //Long memberIdReturn = this.memberDao.insertMemberReturnId(member);
                    //封装获取金币明细表
                    CoinDtl coinDtl = new CoinDtl();
                    coinDtl.setMemberId(member.getMemberId());
                    coinDtl.setCoinNum(coinParamConfig.getCoinRegisterGet());
                    coinDtl.setCreateTime(TimestampUtil.getNowTime());
                    coinDtl.setCoinType((short)2);
                    coinDtlDao.insert(coinDtl);
                } else {
                    if(StringUtil.isEmpty(member.getUname()))
                    member.setUname(openId);
                    member.setNickname(nickname);
                    member.setWeixinFace(weixinFace);
                    member.setOpenId(openId);
                    member.setUnionId(unionId);
                    if(!StringUtil.isEmpty(parentId) && parentId > 0 && member.getParentId() <= 0 ){
                        member.setParentId(parentId);
                        member.setBecomeLowerTime(TimestampUtil.getNowTime());
                    }
                    this.memberDao.updateByPrimaryKeySelective(member);
                }

                String token = TokenUtil.generateTokeCode(member.getMemberId());
                this.redisDao.saveObject(token, member, 3600 * 24 * 30);
                Map<String, Object> memberMap = new HashMap<String, Object>();
                memberMap.put("token", token);
                memberMap.put("member", member);
                return memberMap;
            }
        }

        return null;
    }
    @Override
    public Map<String, Object> wxSign(String url) {
        String accessToken = (String)redisDao.getObject("WEIXIN_ACCESS_TOKEN");
        if(StringUtils.isBlank(accessToken)){
            accessToken = WeixinUrlUtil.getAccessToken();
            redisDao.saveObject("WEIXIN_ACCESS_TOKEN", accessToken, 7200);
        }
        
        String jsapiTicket = (String) redisDao.getObject("WEIXIN_JSAPI_TICKET");
        if (StringUtils.isBlank(jsapiTicket)) {
            jsapiTicket = WeixinUrlUtil.getJsapiTicket(accessToken);
            redisDao.saveObject("WEIXIN_JSAPI_TICKET", jsapiTicket, 7200);
        }
        
        
        Map<String,Object> retMap = new HashMap<String, Object>();
        retMap.put("sign_info", WechatSignUtil.getSign(jsapiTicket,url));
        return retMap;
    }
}
