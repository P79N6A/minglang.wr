package com.taobao.cun.auge.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taobao.cun.auge.data.partner.dto.PartnerAccessDto;

public interface PartnerAccessExtMapper {
	List<PartnerAccessDto> queryPartnerAccessList(@Param("statDate") int statDate);
}
