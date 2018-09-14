package com.qyy.jyshop.admin.member.service;

import java.util.List;

import com.qyy.jyshop.model.MemberAddress;

public interface MemberAddressService {

	List<MemberAddress> getAddressListByMemberId(Long memberId);

}
