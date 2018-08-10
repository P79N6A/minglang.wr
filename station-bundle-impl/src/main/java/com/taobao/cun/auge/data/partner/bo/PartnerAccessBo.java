package com.taobao.cun.auge.data.partner.bo;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.dal.mapper.PartnerAccessExtMapper;
import com.taobao.cun.auge.data.partner.dto.PartnerAccessDto;
import com.taobao.cun.auge.data.partner.dto.UnLoginPartnerDto;

@Component
public class PartnerAccessBo {
	@Resource
	private PartnerAccessExtMapper partnerAccessExtMapper;
	
	public List<PartnerAccessDto> queryPartnerAccessList(int day){
		Date statDate = DateUtils.addDays(new Date(), (day+1) * -1);
		return partnerAccessExtMapper.queryPartnerAccessList(Integer.valueOf(DateFormatUtils.format(statDate, "yyyyMMdd")));
	}
	
	public PageDto<UnLoginPartnerDto> queryUnLoginPartners(String fullIdPath, int day, int pageNum) {
		Date statDate = DateUtils.addDays(new Date(), (day+1) * -1);
		PageHelper.startPage(pageNum, 15);
        Page<UnLoginPartnerDto> page = partnerAccessExtMapper.queryUnLoginPartners(fullIdPath, Integer.valueOf(DateFormatUtils.format(statDate, "yyyyMMdd")), day);

        return PageDtoUtil.success(page, page.getResult());
	}
}
