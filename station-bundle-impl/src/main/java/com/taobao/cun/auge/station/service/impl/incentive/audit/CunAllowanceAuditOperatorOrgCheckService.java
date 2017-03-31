package com.taobao.cun.auge.station.service.impl.incentive.audit;

import com.taobao.cun.auge.incentive.dto.IncentiveAreaDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.org.service.OrgRangeType;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by xujianhui on 17/2/23.
 *
 * @author xujianhui
 * @date 2017/02/23
 */

@Service("cunAllowanceAuditOperatorOrgCheckService")
class CunAllowanceAuditOperatorOrgCheckService implements AuditOperatorOrgCheckService {

    @Autowired
    private CuntaoOrgServiceClient cuntaoOrgServiceClient;

    /**
     * 村淘补贴由总部运营进行审批
     * @param incentiveAreaList
     * @return
     */
    @Override
    public Long getAuditOperatorOrgId(List<IncentiveAreaDto> incentiveAreaList) {
        if (CollectionUtils.isEmpty(incentiveAreaList) || incentiveAreaList.get(0).getOrgId() == null){
            throw new AugeServiceException("非法的激励区域!");
        }
        Long orgId = incentiveAreaList.get(0).getOrgId();
        return cuntaoOrgServiceClient.getAncestor(orgId, OrgRangeType.HEADQUARTERS).getId();
    }
}
