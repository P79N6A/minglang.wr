package com.taobao.cun.auge.company;

import java.util.List;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.dto.CompanyQueryPageCondition;
import com.taobao.cun.auge.company.dto.CuntaoCompanyDto;

/**
 * 公司查询接口
 * @author zhenhuan.zhangzh
 *
 */
public interface CompanyReadService {

	/**
	 * 分页查询服务商列表
	 */
	Result<PageDto<CuntaoCompanyDto>> queryCompanyByPage(CompanyQueryPageCondition companyQueryPageCondition);
	
	
	/**
	 * 单个查询服务商
	 */
	Result<CuntaoCompanyDto> queryCompanyByID(Long id);
	
	/**
	 * ID批量查询服务商
	 */
	
	Result<List<CuntaoCompanyDto>> queryCompanyByIDS(List<Long> ids);
}
