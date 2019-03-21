package com.taobao.cun.auge.cuncounty.bo;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDetailDto;

@Component
public class CuntaoCountyQueryBo {
	@Resource
	private CuntaoCountyGovContactBo cuntaoCountyGovContactBo;
	@Resource
	private CuntaoCountyGovContractBo cuntaoCountyGovContractBo;
	@Resource
	private CuntaoCountyOfficeBo cuntaoCountyOfficeBo;
	@Resource
	private CainiaoCountyBo cainiaoCountyBo;
	@Resource
	private CuntaoCountyBo cuntaoCountyBo;
	/**
	 * 查询详情
	 * @param countyId
	 * @return
	 */
	public CuntaoCountyDetailDto getCuntaoCountyDetail(Long countyId) {
		CuntaoCountyDetailDto cuntaoCountyDetailDto = new CuntaoCountyDetailDto();
		//县服务中心基础信息
		cuntaoCountyDetailDto.setCuntaoCountyDto(cuntaoCountyBo.getCuntaoCounty(countyId));
		//签约信息
		cuntaoCountyDetailDto.setCuntaoCountyGovContractDto(cuntaoCountyGovContractBo.getCuntaoCountyGovContract(countyId));
		//政府联系人
		cuntaoCountyDetailDto.setCuntaoCountyGovContacts(cuntaoCountyGovContactBo.getCuntaoCountyGovContacts(countyId));
		//菜鸟县仓
		cuntaoCountyDetailDto.setCainiaoCountyDto(cainiaoCountyBo.getCainiaoCountyDto(countyId));
		//办公场地
		cuntaoCountyDetailDto.setCuntaoCountyOfficeDto(cuntaoCountyOfficeBo.getCuntaoCountyOffice(countyId));
		return cuntaoCountyDetailDto;
	}
}
