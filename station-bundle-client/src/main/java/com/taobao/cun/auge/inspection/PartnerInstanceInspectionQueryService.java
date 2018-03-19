package com.taobao.cun.auge.inspection;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.inspection.condition.PartnerInstanceInspectionPagedCondition;
import com.taobao.cun.auge.inspection.dto.PartnerInstanceInspectionDto;

/**
 * 门店巡检列表查询服务
 * @author zhenhuan.zhangzh
 *
 */
public interface PartnerInstanceInspectionQueryService {

	/**
	 * 门店巡检列表查询
	 * @param condition
	 * @return
	 */
	public PageDto<PartnerInstanceInspectionDto> queryByPage(PartnerInstanceInspectionPagedCondition condition);
}
