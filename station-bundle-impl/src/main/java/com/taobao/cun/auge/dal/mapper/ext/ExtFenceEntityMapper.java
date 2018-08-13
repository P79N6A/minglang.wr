package com.taobao.cun.auge.dal.mapper.ext;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taobao.cun.auge.dal.domain.FenceEntity;

public interface ExtFenceEntityMapper {
	List<FenceEntity> selectStationQuitedFenceEntities();
	
	List<FenceEntity> selectTypeChangedDefaultFenceEntities(@Param("type")String type, @Param("templateIds")List<Long> templateIds);
}
