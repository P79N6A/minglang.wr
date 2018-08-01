package com.taobao.cun.auge.dal.mapper;

import org.apache.ibatis.annotations.Param;

public interface TownBlacknameMapper {
	int countBlackname(@Param("countyName") String countyName, @Param("townName") String townName);
	
	void insertInBlacknamePartnerApply(@Param("partnerApplyId") Long partnerApplyId);
}
