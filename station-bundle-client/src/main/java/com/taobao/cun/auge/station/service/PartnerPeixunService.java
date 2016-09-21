package com.taobao.cun.auge.station.service;

import java.util.List;

import com.taobao.cun.auge.station.dto.PartnerOnlinePeixunDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunDto;
import com.taobao.cun.auge.station.enums.PartnerPeixunCourseTypeEnum;

/**
 * 合伙人培训服务
 * @author yi.shaoy
 *
 */
public interface PartnerPeixunService {

	
	public List<PartnerPeixunDto> queryBatchPeixunPocess(List<Long> userIds,String courseType,String courseCode);
	
	/**
	 * 在线培训状态查询
	 * 可包含连带考试状态，也可不包含
	 */
	public PartnerOnlinePeixunDto queryOnlinePeixunProcess(Long userId,String courseType);
	
	/**
	 * 线下训状态查询
	 */
	public PartnerPeixunDto queryOfflinePeixunProcess(Long userId,String courseCode,PartnerPeixunCourseTypeEnum courseType);
}
