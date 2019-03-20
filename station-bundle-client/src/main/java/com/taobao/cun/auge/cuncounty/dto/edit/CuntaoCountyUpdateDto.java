package com.taobao.cun.auge.cuncounty.dto.edit;

import java.util.List;

/**
 * 县服务中心修改
 * 
 * @author chengyu.zhoucy
 *
 */
public class CuntaoCountyUpdateDto {
	/**
	 * 办公场所
	 */
	private CuntaoCountyOfficeEditDto cuntaoCountyOfficeEditDto;
	
	/**
	 * 政府联系人
	 */
	private List<CuntaoCountyGovContactAddDto> cuntaoCountyGovContactAddDtos;
	
	/**
	 * 政府签约信息
	 */
	private CuntaoCountyGovContractEditDto cuntaoCountyGovContractEditDto;
	
	/**
	 * 菜鸟县仓
	 */
	private CainiaoCountyEditDto cainiaoCountyEditDto;
	
	/**
	 * 县服务中心ID
	 */
	private Long countyId;

	public Long getCountyId() {
		return countyId;
	}

	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}

	public CuntaoCountyOfficeEditDto getCuntaoCountyOfficeEditDto() {
		return cuntaoCountyOfficeEditDto;
	}

	public void setCuntaoCountyOfficeEditDto(CuntaoCountyOfficeEditDto cuntaoCountyOfficeEditDto) {
		this.cuntaoCountyOfficeEditDto = cuntaoCountyOfficeEditDto;
	}

	public List<CuntaoCountyGovContactAddDto> getCuntaoCountyGovContactAddDtos() {
		return cuntaoCountyGovContactAddDtos;
	}

	public void setCuntaoCountyGovContactAddDtos(List<CuntaoCountyGovContactAddDto> cuntaoCountyGovContactAddDtos) {
		this.cuntaoCountyGovContactAddDtos = cuntaoCountyGovContactAddDtos;
	}

	public CuntaoCountyGovContractEditDto getCuntaoCountyGovContractEditDto() {
		return cuntaoCountyGovContractEditDto;
	}

	public void setCuntaoCountyGovContractEditDto(CuntaoCountyGovContractEditDto cuntaoCountyGovContractEditDto) {
		this.cuntaoCountyGovContractEditDto = cuntaoCountyGovContractEditDto;
	}

	public CainiaoCountyEditDto getCainiaoCountyEditDto() {
		return cainiaoCountyEditDto;
	}

	public void setCainiaoCountyEditDto(CainiaoCountyEditDto cainiaoCountyEditDto) {
		this.cainiaoCountyEditDto = cainiaoCountyEditDto;
	}
}
