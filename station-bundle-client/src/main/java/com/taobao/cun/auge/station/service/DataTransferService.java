package com.taobao.cun.auge.station.service;

import java.util.List;

import com.taobao.cun.auge.station.dto.PartnerCourseRecordDto;

/**
 * 数据迁移专用
 * @author yi.shaoy
 *
 */
public interface DataTransferService {

	/**
	 * 根据状态获取培训记录
	 * @param status
	 * @return
	 */
	public List<PartnerCourseRecordDto> getAllRecords(String status,String courseCode);
	
	/**
	 * 为培训记录重新下单
	 * @param dto
	 * @return
	 */
	public Boolean createOrder(PartnerCourseRecordDto dto);
	
	/**
	 * 订单转款
	 */
	public Boolean transferMoney(PartnerCourseRecordDto dto);
	
	/**
	 * 签到
	 */
	public Boolean sign(PartnerCourseRecordDto dto);
}
