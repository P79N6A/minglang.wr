package com.taobao.cun.auge.cuncounty.utils;

import java.util.Date;
import java.util.List;

import com.taobao.cun.auge.common.utils.BeanCopy;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyStateEnum;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyWhitenameDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyAddDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyGovContactAddDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyGovProtocolAddDto;
import com.taobao.cun.auge.dal.domain.CuntaoCounty;
import com.taobao.cun.auge.dal.domain.CuntaoCountyGovContact;
import com.taobao.cun.auge.dal.domain.CuntaoCountyGovProtocol;

public class BeanConvertUtils {
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
	
	public static CuntaoCountyGovContact convert(CuntaoCountyGovContactAddDto cuntaoCountyGovContactAddDto) {
		CuntaoCountyGovContact cuntaoCountyGovContact = BeanCopy.copy(CuntaoCountyGovContact.class, cuntaoCountyGovContactAddDto);
		cuntaoCountyGovContact.setGmtCreate(new Date());
		cuntaoCountyGovContact.setGmtModified(new Date());
		cuntaoCountyGovContact.setCreator(cuntaoCountyGovContactAddDto.getOperator());
		cuntaoCountyGovContact.setModifier(cuntaoCountyGovContactAddDto.getOperator());
		return cuntaoCountyGovContact;
	}
	
	public static CuntaoCountyGovProtocol convert(CuntaoCountyGovProtocolAddDto cuntaoCountyGovProtocolAddDto) {
		CuntaoCountyGovProtocol cuntaoCountyGovProtocol = BeanCopy.copy(CuntaoCountyGovProtocol.class, cuntaoCountyGovProtocolAddDto);
		cuntaoCountyGovProtocol.setGmtCreate(new Date());
		cuntaoCountyGovProtocol.setGmtModified(new Date());
		cuntaoCountyGovProtocol.setCreator(cuntaoCountyGovProtocolAddDto.getOperator());
		cuntaoCountyGovProtocol.setModifier(cuntaoCountyGovProtocolAddDto.getOperator());
		return cuntaoCountyGovProtocol;
	}
	
	public static <T, S> List<T> listConvert(Class<T> targetClass, List<S> sources){
		return BeanCopy.copyList(targetClass, sources);
	}
	
	public static <T, S> T convert(Class<T> targetClass, S source){
		return BeanCopy.copy(targetClass, source);
	}
}
