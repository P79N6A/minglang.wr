package com.taobao.cun.auge.station.service;

import java.util.Date;
import java.util.List;

import com.taobao.cun.auge.station.dto.PartnerCourseScheduleReflectDto;
import com.taobao.cun.auge.station.dto.PartnerCourseScheduleShowDto;


/**
 * 课程表服务接口
 * @author yi.shaoy
 *
 */
public interface PartnerCourseScheduleService{

	/**
	 * 获取协议同意状态
	 */
	public Boolean hasSignCourseScheduleProtocol(Long taobaoUserId);
	/**
	 * 同意协议
	 */
	public void signCourseScheduleProtocol(Long taobaoUserId);
	/**
	 * 按时间获取课程表
	 */
	public List<PartnerCourseScheduleShowDto> getCourseSchedule(Long taobaoUserId,Date gmtStart,Date gmtEnd);
	
	/**
	 * 课程反馈
	 */
	public void reflectCourseSchedule(PartnerCourseScheduleReflectDto dto);

}
