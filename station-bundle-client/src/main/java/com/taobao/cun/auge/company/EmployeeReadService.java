package com.taobao.cun.auge.company;

import java.util.List;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeDto;
import com.taobao.cun.auge.company.dto.EmployeeQueryPageCondition;

/**
 * 工人查询接口
 * @author zhenhuan.zhangzh
 *
 */
public interface EmployeeReadService {


	/**
	 * 分页查找服务商下的工人
	 */
	Result<PageDto<CuntaoEmployeeDto>> queryEmployeeByPage(EmployeeQueryPageCondition employeeQueryPageCondition);

	/**
	 * 单个查找工人
	 */
	Result<CuntaoEmployeeDto> queryEmployeeByID(Long id);
	
	/**
	 * ID批量查找工人
	 */
	
	Result<List<CuntaoEmployeeDto>> queryEmployeeByIDS(List<Long> ids);
}
