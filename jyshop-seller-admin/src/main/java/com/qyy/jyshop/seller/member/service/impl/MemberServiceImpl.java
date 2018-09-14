package com.qyy.jyshop.seller.member.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.seller.common.annotation.ServiceLog;
import com.qyy.jyshop.seller.member.service.MemberService;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class MemberServiceImpl extends AbstratService<Member> implements MemberService {

	@Autowired
	private MemberDao memberDao;
	
	/**
	 * 查询会员详情到用户详情页
	 */
	@Override
	@ServiceLog("根据用户Id获取用户信息")
	public Member queryByMemberId(Long memId) {
		return this.queryByID(memId);
	}

}
