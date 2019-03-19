package com.taobao.cun.auge.cuncounty.service;

import javax.validation.Valid;

import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyAddDto;

/**
 * 县服务中心增删改服务
 * 
 * @author chengyu.zhoucy
 *
 */
public interface CuntaoCountyWriteService {
	/**
	 * 创建一个县服务中心，返回创建的服务中心的ID
	 * @param cuntaoCountyAddDto
	 * @return
	 */
	Long createCuntaoCounty(@Valid CuntaoCountyAddDto cuntaoCountyAddDto);
}
