package com.taobao.cun.auge.station.service.impl.incentive.audit;

import java.util.List;

import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.incentive.dto.IncentiveAreaDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.org.service.OrgRangeType;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Created by xujianhui on 17/2/23.
 *
 * @author xujianhui
 * @date 2017/02/23
 */

@Service("governAllowanceAuditOperatorOrgCheckService")
public class GovernAllowanceAuditOperatorOrgCheckService implements AuditOperatorOrgCheckService {

    @Autowired
    private CuntaoOrgServiceClient cuntaoOrgServiceClient;

    /**
     * 政府资金池由激励区域所在大区运营审批
     * @param incentiveAreaList
     * @return
     */
    @Override
    public Long getAuditOperatorOrgId(List<IncentiveAreaDto> incentiveAreaList) {

        if (CollectionUtils.isEmpty(incentiveAreaList) || incentiveAreaList.get(0).getOrgId() == null){
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"非法的激励区域!");
        }
        Long orgId = incentiveAreaList.get(0).getOrgId();
        return cuntaoOrgServiceClient.getAncestor(orgId, OrgRangeType.LARGE_AREA).getId();
    }
}
