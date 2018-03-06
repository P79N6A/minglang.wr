package com.taobao.cun.auge.company;

import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeDto;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeIdentifier;

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
	Result<Boolean> removeVendorEmployee(Long employeeId,Long taobaoUserId,String operator);
	
	
	/**
	 * 新增门店员工
	 * @param companyId
	 * @param employee
	 * @param type
	 * @return
	 */
	Result<Long> addStoreEmployee(Long stationId,CuntaoEmployeeDto employee,CuntaoEmployeeIdentifier type);

	/**
	 * 删除门店员工
	 * @param employeeId
	 * @param operator
	 * @return
	 */
	Result<Boolean> removeStoreEmployee(Long employeeId,Long taobaoUserId,String operator);
	
	/**
	 * 删除门店员工
	 * @param employeeId
	 * @param operator
	 * @return
	 */
	Result<Boolean> removeStoreEmployeeRole(Long employeeId,Long taobaoUserId,String operator,CuntaoEmployeeIdentifier type);

}
