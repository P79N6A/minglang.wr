package com.taobao.cun.auge.station.service;

import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.PartnerPeixunQueryCondition;
import com.taobao.cun.auge.station.dto.PartnerOnlinePeixunDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunListDetailDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunStatusCountDto;
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
	 * 批量查询在线培训状态 不包括考试
	 */
	public Map<String,PartnerOnlinePeixunDto> queryBatchOnlinePeixunProcess(Long userId,List<String> courseCodes);
	
	
	/**
	 * 线下训状态查询
	 */
	public PartnerPeixunDto queryOfflinePeixunProcess(Long userId,String courseCode,PartnerPeixunCourseTypeEnum courseType);
	
	/**
	 * 提供根据条件查询培训数量
	 * @return
	 */
	public List<PartnerPeixunStatusCountDto> queryPeixunCountByCondition(PartnerPeixunQueryCondition condition);

   /**
    * 提供分页查询培训列表 
    */
	public PageDto<PartnerPeixunListDetailDto> queryPeixunList(PartnerPeixunQueryCondition condition);
	
	/**
	 * 提供给村学习视频播放验权服务
	 */
	public Boolean checkCourseViewPermission(Long taobaoUserId,String courseCode);
	
}
