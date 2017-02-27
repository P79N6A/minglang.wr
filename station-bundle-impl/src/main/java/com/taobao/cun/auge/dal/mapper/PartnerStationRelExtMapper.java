package com.taobao.cun.auge.dal.mapper;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.dal.domain.PartnerInstance;
import com.taobao.cun.auge.dal.domain.ProcessedStationStatus;
import com.taobao.cun.auge.dal.example.PartnerInstanceExample;
import com.taobao.cun.auge.station.condition.StationStatisticsCondition;

public interface PartnerStationRelExtMapper {
	
	Page<PartnerInstance> selectPartnerInstancesByExample(PartnerInstanceExample example);
	
	List<ProcessedStationStatus> countProcessedStatus(StationStatisticsCondition condition);
	
	List<ProcessedStationStatus> countProcessingStatus(StationStatisticsCondition condition);
	
	List<ProcessedStationStatus> countCourseStatus(StationStatisticsCondition condition);
	
	List<ProcessedStationStatus> countDecorateStatus(StationStatisticsCondition condition);
	
	/**
	 * 查询待冻结保证金的数据
	 */
	List<Long> getWaitThawMoney(Map param);
	
	/**
	 * 重构过度使用，请正常业务不要使用
	 * @param stationApplyIds
	 * @return
	 */
	List<PartnerInstance> selectPartnerInstancesByStationApplyIds(List<Long> stationApplyIds);
	
	/**
	 * 根据合伙人实例id,批量查询实例
	 * 
	 * @param partnerInstanceIds
	 * @return
	 */
	List<PartnerInstance> selectPartnerInstancesByIds(List<Long> partnerInstanceIds);
}
