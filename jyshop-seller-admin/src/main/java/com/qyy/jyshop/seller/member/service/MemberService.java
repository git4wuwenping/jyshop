package com.qyy.jyshop.seller.member.service;

import com.qyy.jyshop.model.Member;

public interface MemberService {

	/**
	 * 查询会员详情到用户详情页
	 * @param memberId
	 * @return
	 */
	Member queryByMemberId(Long memberId);

}
