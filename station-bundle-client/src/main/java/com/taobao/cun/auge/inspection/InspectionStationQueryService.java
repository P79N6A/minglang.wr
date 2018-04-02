package com.taobao.cun.auge.inspection;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.inspection.condition.InspectionPagedCondition;
import com.taobao.cun.auge.inspection.dto.InspectionStationDto;
import com.taobao.cun.auge.inspection.dto.InspectionStatusSummaryDto;

/**
 * 门店巡检列表查询服务
 * @author zhenhuan.zhangzh
 *
 */
public interface InspectionStationQueryService {

	/**
	 * 门店巡检列表查询
	 * @param condition
	 * @return
	 */
	public PageDto<InspectionStationDto> queryByPage(InspectionPagedCondition condition);

	/**
	 * 巡检数据汇总
	 * @param condition
	 * @return
	 */
	InspectionStatusSummaryDto countInspectionStatusSummary(InspectionPagedCondition condition);

}
