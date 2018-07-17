package com.taobao.cun.auge.data.partner.bo;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.mapper.PartnerAccessExtMapper;
import com.taobao.cun.auge.data.partner.dto.PartnerAccessDto;

@Component
public class PartnerAccessBo {
	@Resource
	private PartnerAccessExtMapper partnerAccessExtMapper;
	
	public List<PartnerAccessDto> queryPartnerAccessList(int day){
		Date statDate = DateUtils.addDays(new Date(), day * -1);
		return partnerAccessExtMapper.queryPartnerAccessList(Integer.valueOf(DateFormatUtils.format(statDate, "yyyyMMdd")));
	}
}
