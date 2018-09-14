package com.qyy.jyshop.dao;

import com.qyy.jyshop.model.Spec;
import com.qyy.jyshop.supple.MyMapper;
import com.qyy.jyshop.vo.SpecVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpecDao extends MyMapper<Spec>{

    /**
     * 根据规格ID获取规格信息
     * @author hwc
     * @created 2018年1月3日 上午9:30:21
     * @param specId
     * @param comId
     * @return
     */
    Spec selectComSpecBySpecId(@Param("specId")Long specId,@Param("comId")Integer comId);
    
    /**
     * 根据供应商ID获取规格列表
     * @author hwc
     * @created 2018年1月3日 下午4:09:59
     * @param comId
     * @return
     */
    List<Spec> selectByComId(@Param("comId")Integer comId);
    
    /**
     * 根据规格IDS获取规格列表
     * @author hwc
     * @created 2018年1月3日 下午4:04:46
     * @param specIds
     * @return
     */
    List<Spec> selectBySpecIds(Long[] specIds);

    List<SpecVo> selectSpecsByGpId(Long gpId);
    /**
     * 根据商品规格值查询商品
     * @param specValueId
     * @return
     */
	Integer selectGoodBySpecValueId(@Param("specValueId")Long specValueId);
}
