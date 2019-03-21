package com.taobao.cun.auge.cuncounty.dto;

import java.util.List;

/**
 * 县服务中心详情
 * 
 * @author chengyu.zhoucy
 *
 */
public class CuntaoCountyDetailDto {
	/**
	 * 县服务中心
	 */
	private CuntaoCountyDto cuntaoCountyDto;
	
	/**
	 * 办公场地信息
	 */
	private CuntaoCountyOfficeDto cuntaoCountyOfficeDto;
	
	/**
	 * 政府签约信息
	 */
	private CuntaoCountyGovContractDto cuntaoCountyGovContractDto;
	
	/**
	 * 县政府联系人
	 */
	private List<CuntaoCountyGovContactDto> cuntaoCountyGovContacts;
	
	/**
	 * 菜鸟县仓
	 */
	private CainiaoCountyDto cainiaoCountyDto;

	public CuntaoCountyDto getCuntaoCountyDto() {
		return cuntaoCountyDto;
	}

	public void setCuntaoCountyDto(CuntaoCountyDto cuntaoCountyDto) {
		this.cuntaoCountyDto = cuntaoCountyDto;
	}

	public CuntaoCountyOfficeDto getCuntaoCountyOfficeDto() {
		return cuntaoCountyOfficeDto;
	}

	public void setCuntaoCountyOfficeDto(CuntaoCountyOfficeDto cuntaoCountyOfficeDto) {
		this.cuntaoCountyOfficeDto = cuntaoCountyOfficeDto;
	}

	public CuntaoCountyGovContractDto getCuntaoCountyGovContractDto() {
		return cuntaoCountyGovContractDto;
	}

	public void setCuntaoCountyGovContractDto(CuntaoCountyGovContractDto cuntaoCountyGovContractDto) {
		this.cuntaoCountyGovContractDto = cuntaoCountyGovContractDto;
	}

	public List<CuntaoCountyGovContactDto> getCuntaoCountyGovContacts() {
		return cuntaoCountyGovContacts;
	}

	public void setCuntaoCountyGovContacts(List<CuntaoCountyGovContactDto> cuntaoCountyGovContacts) {
		this.cuntaoCountyGovContacts = cuntaoCountyGovContacts;
	}

	public CainiaoCountyDto getCainiaoCountyDto() {
		return cainiaoCountyDto;
	}

	public void setCainiaoCountyDto(CainiaoCountyDto cainiaoCountyDto) {
		this.cainiaoCountyDto = cainiaoCountyDto;
	}
}
