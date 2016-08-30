package com.taobao.cun.auge.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.github.pagehelper.Page;
import com.taobao.cun.auge.dal.domain.PartnerInstance;
import com.taobao.cun.auge.dal.domain.ProcessedStationStatus;
import com.taobao.cun.auge.dal.example.PartnerInstanceExample;

public interface PartnerStationRelExtMapper {
	
	Page<PartnerInstance> selectPartnerInstancesByExample(PartnerInstanceExample example);
	
	List<ProcessedStationStatus> countProcessedStatus(@Param("orgIdPath") String orgIdPath);
	
	List<ProcessedStationStatus> countProcessingStatus(@Param("orgIdPath") String orgIdPath);
	
	List<ProcessedStationStatus> countCourseStatus(@Param("orgIdPath") String orgIdPath);
	
	List<ProcessedStationStatus> countDecorateStatus(@Param("orgIdPath") String orgIdPath);
	
	Integer test();
	
	/**
	 * 查询待冻结保证金的数据
	 */
	List<Long> getWaitThawMoney(Map param);
}