package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.MemberChat;
import com.qyy.jyshop.supple.MyMapper;

/**
 * 用户咨询记录
 * @author hwc
 * @created 2018年3月12日 下午4:57:07
 */
public interface MemberChatDao extends MyMapper<MemberChat>{

    /**
     * 获取用户最后咨询列表
     * @author hwc
     * @created 2018年3月14日 下午6:19:18
     * @param params
     * @return
     */
	List<Map<String,Object>> selectByLatest(Map<String,Object> params);
	
	/**
	 * 查询用户咨询记录
	 * @author hwc
	 * @created 2018年3月27日 上午9:07:54
	 * @param params
	 * @return
	 */
	List<Map<String,Object>> selectMemberChatByParams(Map<String,Object> params);
	
	/**
	 * 获取用户未读信息
	 * @author hwc
	 * @created 2018年4月3日 上午9:57:42
	 * @param memberId
	 * @return
	 */
	List<MemberChat> selectByUnreadInfo(@Param("memberId")Long memberId);
	
	/**
	 * 导出咨询信息
	 * @author Tonny
	 * @date 2018年4月16日
	 */
	List<Map<String, Object>> queryExportMemberChatData(Map<String, Object> map);
}
