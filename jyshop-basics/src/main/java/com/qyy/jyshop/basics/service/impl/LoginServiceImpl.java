package com.qyy.jyshop.basics.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.basics.service.LoginService;
import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.redis.RedisDao;
import com.qyy.jyshop.util.StringUtil;
import com.qyy.jyshop.util.TokenUtil;

@Service
public class LoginServiceImpl implements LoginService{

    @Autowired
    private RedisDao redisDao;
    @Autowired
    private MemberDao memberDao;
    
    @Override
    public Map<String,Object> doMemberLogin(String uname, String password){
        
        if(StringUtil.isEmpty(uname))
            throw new AppErrorException("用户名不能为空...");
        if(StringUtil.isEmpty(password))
            throw new AppErrorException("密码不能为空...");
        
        password=StringUtil.md5(password);
        Member member=this.memberDao.queryByUnameAndPassword(uname,password);
        if(member!=null && !StringUtil.isEmpty(member)){
            Object tokenOb=this.redisDao.getObject(member.getUname());
            tokenOb=null;
            Map<String,Object> memberMap=new HashMap<String, Object>();
            memberMap.put("member", member);
            if(!StringUtil.isEmpty(tokenOb)){
                this.redisDao.saveObject(tokenOb.toString(), member,3600*24*30);
                memberMap.put("token", tokenOb);
            }else{
                String token=TokenUtil.generateTokeCode(member.getMemberId());
                if(!StringUtil.isEmpty(token)){
                    this.redisDao.saveObject(token, member,3600*24*30);
                    this.redisDao.saveObject(member.getUname(), token,3600*24*30);
                    this.redisDao.saveObject(member.getMemberId()+"", member,3600*24*30);
                    memberMap.put("token", token);
                }
            }
            return memberMap;
        }
        return null;
    }

}
