package com.taobao.cun.auge.dal.mapper;

import java.util.List;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.dal.domain.InspectionStatusSummary;
import com.taobao.cun.auge.dal.domain.InspectionStation;
import com.taobao.cun.auge.dal.example.InspectionStationExample;

/**
 * 巡检查询Mapper
 * @author zhenhuan.zhangzh
 *
 */

public interface InspectionStationMapper {

	Page<InspectionStation> selectInspectionStationByExample(InspectionStationExample example);
	
	List<InspectionStatusSummary> countInspectionSummaryByExample(InspectionStationExample example);
}
