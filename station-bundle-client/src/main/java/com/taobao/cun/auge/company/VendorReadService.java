package com.taobao.cun.auge.company;

import java.util.List;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.dto.VendorQueryPageCondition;
import com.taobao.cun.auge.company.dto.CuntaoServiceVendorDto;

/**
 * 服务商查询接口
 * @author zhenhuan.zhangzh
 *
 */
public interface VendorReadService {

	/**
	 * 分页查询服务商列表
	 */
	Result<PageDto<CuntaoServiceVendorDto>> queryVendorByPage(VendorQueryPageCondition companyQueryPageCondition);
	
	
	/**
	 * 单个查询服务商
	 */
	Result<CuntaoServiceVendorDto> queryVendorByID(Long id);
	
	/**
	 * ID批量查询服务商
	 */
	
	Result<List<CuntaoServiceVendorDto>> queryVendorByIDS(List<Long> ids);
}
