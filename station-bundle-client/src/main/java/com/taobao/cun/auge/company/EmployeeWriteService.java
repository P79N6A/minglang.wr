package com.taobao.cun.auge.company;

import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeIdentifier;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeDto;

/**
 * 村淘员工管理服务
 * @author zhenhuan.zhangzh
 *
 */
public interface EmployeeWriteService{

	/**
	 * 新增公司员工
	 * @param companyId
	 * @param employee
	 * @param type
	 * @return
	 */
	Result<Long> addVendorEmployee(Long vendorId,CuntaoEmployeeDto employee,CuntaoEmployeeIdentifier identifier);
	
	/**
	 * 新增公司员工
	 * @param companyId
	 * @param employee
	 * @param type
	 * @return
	 */
	Result<Long> addVendorEmployeeByEmployeeId(Long vendorId,Long employeeId,CuntaoEmployeeIdentifier identifier);
	
	
	/**
	 * 更新员工信息
	 * @param employee
	 * @return
	 */
	Result<Boolean> updateVendorEmployee(CuntaoEmployeeDto employee);
	
	/**
	 * 删除员工
	 * @param employeeId
	 * @return
	 */
	Result<Boolean> removeVendorEmployee(Long employeeId);
	
	
	/**
	 * 新增门店员工
	 * @param companyId
	 * @param employee
	 * @param type
	 * @return
	 */
	Result<Long> addStoreEmployee(Long stationId,CuntaoEmployeeDto employee,CuntaoEmployeeIdentifier type);
}
