package com.taobao.cun.auge.cuncounty.utils;

import java.util.Date;
import java.util.List;

import com.taobao.cun.auge.common.utils.BeanCopy;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyStateEnum;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyWhitenameDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyAddDto;
import com.taobao.cun.auge.dal.domain.CuntaoCounty;
import com.taobao.cun.auge.dal.domain.CuntaoCountyWhitename;

public class BeanConvertUtils {
	
	public static CuntaoCountyWhitenameDto convert(CuntaoCountyWhitename cuntaoCountyWhitename) {
		return BeanCopy.copy(CuntaoCountyWhitenameDto.class, cuntaoCountyWhitename);
	}
	
	public static List<CuntaoCountyWhitenameDto> convert(List<CuntaoCountyWhitename> cuntaoCountyWhitenames) {
		return BeanCopy.copyList(CuntaoCountyWhitenameDto.class, cuntaoCountyWhitenames);
	}
	
	public static CuntaoCounty convert(CuntaoCountyAddDto cuntaoCountyAddDto, Long orgId, CuntaoCountyWhitenameDto cuntaoCountyWhitenameDto) {
		CuntaoCounty cuntaoCounty = BeanCopy.copy(CuntaoCounty.class, cuntaoCountyWhitenameDto);
		cuntaoCounty.setCreator(cuntaoCountyAddDto.getOperator());
		cuntaoCounty.setModifier(cuntaoCountyAddDto.getOperator());
		cuntaoCounty.setGmtCreate(new Date());
		cuntaoCounty.setGmtModified(new Date());
		cuntaoCounty.setIsDeleted("n");
		cuntaoCounty.setName(cuntaoCountyAddDto.getName());
		cuntaoCounty.setOrgId(orgId);
		cuntaoCounty.setState(CuntaoCountyStateEnum.PLANNING.getCode());
		return cuntaoCounty;
	}
}
