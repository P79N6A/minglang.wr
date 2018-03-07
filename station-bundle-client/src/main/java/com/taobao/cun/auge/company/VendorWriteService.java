package com.taobao.cun.auge.company;

import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.dto.CuntaoServiceVendorDto;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;

/**
 * 公司管理服务接口
 * @author zhenhuan.zhangzh
 *
 */
public interface VendorWriteService {

	/**
	 * 新增合作公司和管理员
	 * @param serviceVendor
	 * @return
	 */
	Result<Long> addVendor(CuntaoServiceVendorDto cuntaoCompanyDto);
	
	/**
	 * 更新公司信息
	 * @param cuntaoCompanyDto
	 * @return
	 */
	Result<Boolean> updateVendor(CuntaoServiceVendorDto cuntaoCompanyDto);
	
	/**
	 * 删除服务供应商
	 * @param serviceVendorId
	 * @return
	 */
	Result<Boolean> removeVendor(Long companyId,String operator);
	
	/**
	 * 确认服务商协议
	 * @param taobaoUserId
	 * @param protocol
	 * @return
	 */
	Result<Boolean> confirmVendorProtocol(Long taobaoUserId,ProtocolTypeEnum protocol);
	
	
	/**
	 * 是否确认服务商协议
	 * @param taobaoUserId
	 * @param protocol
	 * @return
	 */
	Result<Boolean> isConfirmVendorProtocol(Long taobaoUserId,ProtocolTypeEnum protocol);
	
}
