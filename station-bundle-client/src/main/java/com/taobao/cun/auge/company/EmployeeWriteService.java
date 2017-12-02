package com.taobao.cun.auge.company;

import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.dto.CuntaoCompanyEmployeeType;
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
	Result<Long> addCompanyEmployee(Long companyId,CuntaoEmployeeDto employee,CuntaoCompanyEmployeeType type);
	
	/**
	 * 更新员工信息
	 * @param employee
	 * @return
	 */
	Result<Boolean> updateCompanyEmployee(CuntaoEmployeeDto employee);
	
	/**
	 * 删除员工
	 * @param employeeId
	 * @return
	 */
	Result<Boolean> removeCompanyEmployee(Long employeeId);
}
