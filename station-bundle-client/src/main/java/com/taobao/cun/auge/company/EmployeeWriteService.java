package com.taobao.cun.auge.company;

import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.dto.CuntaoVendorEmployeeType;
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
	Result<Long> addEmployee(Long companyId,CuntaoEmployeeDto employee,CuntaoVendorEmployeeType type);
	
	/**
	 * 更新员工信息
	 * @param employee
	 * @return
	 */
	Result<Boolean> updateEmployee(CuntaoEmployeeDto employee);
	
	/**
	 * 删除员工
	 * @param employeeId
	 * @return
	 */
	Result<Boolean> removeEmployee(Long employeeId);
}
