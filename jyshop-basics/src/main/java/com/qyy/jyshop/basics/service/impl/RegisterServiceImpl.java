package com.qyy.jyshop.basics.service.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.basics.service.RegisterService;
import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.util.StringUtil;
@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private MemberDao memberDao;
	@Override
	public void memberRegister(String uname, String password) {
		Member member = new Member();
		member.setUname(uname);
		member.setPassword(StringUtil.md5(password));
		member.setMobile(uname);
		Timestamp d = new Timestamp(System.currentTimeMillis()); 
		member.setRegtime(d);
		memberDao.insert(member);
	}

}
