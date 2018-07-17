package com.taobao.cun.auge.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.data.partner.dto.PartnerAccessDto;
import com.taobao.cun.auge.data.partner.dto.UnLoginPartnerDto;

public interface PartnerAccessExtMapper {
	List<PartnerAccessDto> queryPartnerAccessList(@Param("statDate") int statDate);
	
	Page<UnLoginPartnerDto> queryUnLoginPartners(@Param("statDate") int statDate, @Param("dayNum") int dayNum);
}
