package com.taobao.cun.auge.company;

import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.dto.CuntaoCompanyDto;

/**
 * 公司管理服务接口
 * @author zhenhuan.zhangzh
 *
 */
public interface CompanyWriteService {

	/**
	 * 新增合作公司和管理员
	 * @param serviceVendor
	 * @return
	 */
	Result<Long> addCompany(CuntaoCompanyDto cuntaoCompanyDto);
	
	/**
	 * 更新公司信息
	 * @param cuntaoCompanyDto
	 * @return
	 */
	Result<Boolean> updateCompany(CuntaoCompanyDto cuntaoCompanyDto);
	
	/**
	 * 删除服务供应商
	 * @param serviceVendorId
	 * @return
	 */
	Result<Boolean> removeCompany(Long companyId);
	
	
}
