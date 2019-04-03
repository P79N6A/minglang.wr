package com.taobao.cun.auge.company.bo;

import com.taobao.cun.auge.company.dto.CuntaoServiceVendorDto;

public interface VendorWriteBO {

	/**
	 * 新增合作公司和管理员
	 * @param cuntaoCompanyDto
	 * @return
	 */
	Long addVendor(CuntaoServiceVendorDto cuntaoCompanyDto);

	/**
	 * 新增合作公司和管理员
	 * @param cuntaoCompanyDto
	 * @return
	 */
	Long addNewVendor(CuntaoServiceVendorDto cuntaoCompanyDto);
	
	/**
	 * 更新公司信息
	 * @param cuntaoCompanyDto
	 * @return
	 */
	Boolean updateVendor(CuntaoServiceVendorDto cuntaoCompanyDto);
	
	/**
	 * 删除服务供应商
	 * @param companyId
	 * @param operator
	 * @return
	 */
	Boolean removeVendor(Long companyId,String operator);

	/**
	 * 删除服务供应商
	 * @param companyId
	 * @param operator
	 * @return
	 */
	Boolean removeNewVendor(Long companyId,String operator);
}
