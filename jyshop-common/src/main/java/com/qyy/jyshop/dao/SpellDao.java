package com.qyy.jyshop.dao;

import com.qyy.jyshop.model.Spell;
import com.qyy.jyshop.supple.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SpellDao  extends MyMapper<Spell> {

    public List<Map<String, Object>> getOngoingByActivityId(@Param("activityId")Long activityId);

    public List<Map<String, Object>> getMyInitiateSpell(@Param("memberId")Long memberId);

    public List<Map<String, Object>> getMyParticipateSpell(@Param("memberStr")String memberStr, @Param("memberId")Long memberId);

    public Map<String, Object> getSpellDetail(@Param("spellId")Long spellId);

    public Integer countMySpellByActivityId(@Param("memberId")Long memberId, @Param("activityId")Long activityId);
}