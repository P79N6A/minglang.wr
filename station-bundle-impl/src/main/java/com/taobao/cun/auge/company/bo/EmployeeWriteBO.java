package com.taobao.cun.auge.company.bo;

import com.taobao.cun.auge.company.dto.CuntaoEmployeeDto;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeIdentifier;

public interface EmployeeWriteBO {

	/**
	 * 新增公司员工
	 * @param companyId
	 * @param employee
	 * @param type
	 * @return
	 */
	Long addVendorEmployee(Long vendorId,CuntaoEmployeeDto employee,CuntaoEmployeeIdentifier identifier);
	
	/**
	 * 新增公司员工
	 * @param vendorId
	 * @param employeeId
	 * @param identifier
	 * @return
	 */
	Long addVendorEmployeeByEmployeeId(Long vendorId,Long employeeId,CuntaoEmployeeIdentifier identifier);
	/**
	 * 更新员工信息
	 * @param employee
	 * @return
	 */
	Boolean updateVendorEmployee(CuntaoEmployeeDto employee);
	
	/**
	 * 删除员工
	 * @param employeeId
	 * @return
	 */
	Boolean removeVendorEmployee(Long employeeId);
	
	
	/**
	 * 新增门店员工
	 * @param companyId
	 * @param employee
	 * @param type
	 * @return
	 */
	Long addStoreEmployee(Long stationId,CuntaoEmployeeDto employee,CuntaoEmployeeIdentifier type);
}
