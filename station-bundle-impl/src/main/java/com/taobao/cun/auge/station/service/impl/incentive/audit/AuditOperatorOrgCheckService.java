package com.taobao.cun.auge.station.service.impl.incentive.audit;

import com.taobao.cun.auge.incentive.dto.IncentiveAreaDto;

import java.util.List;

/**
 * Created by xujianhui on 17/2/23.
 * 计算 审批激励方案的运营人员 组织的服务:不同的资金类型和激励区域来确定审批运营组织.
 * @author xujianhui
 * @date 2017/02/23
 */
public interface AuditOperatorOrgCheckService {
    Long getAuditOperatorOrgId(List<IncentiveAreaDto> incentiveAreaList);
}
