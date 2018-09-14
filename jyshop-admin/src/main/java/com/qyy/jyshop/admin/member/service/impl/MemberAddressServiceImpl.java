package com.qyy.jyshop.admin.member.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qyy.jyshop.admin.member.service.MemberAddressService;
import com.qyy.jyshop.model.MemberAddress;
import com.qyy.jyshop.supple.AbstratService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class MemberAddressServiceImpl extends AbstratService<MemberAddress> implements MemberAddressService {

	@Override
	public List<MemberAddress> getAddressListByMemberId(Long memberId) {
		Example example = new Example(MemberAddress.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("memberId", memberId);
		return this.queryByExample(example );
	}

}
