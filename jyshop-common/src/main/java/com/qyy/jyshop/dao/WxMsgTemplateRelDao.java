package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.WxMsgTemplateRel;
import com.qyy.jyshop.supple.MyMapper;

public interface WxMsgTemplateRelDao  extends MyMapper<WxMsgTemplateRel>{

	void deleteByTplId(Long tplId);
	
	void batchInsert(List<WxMsgTemplateRel> list);
	
	List<Map<String, Object>> selectMemberRelByTplId(Long tplId);

    List<Long> selectMemberIdListByTplId(Long tplId);
	
}
