package com.taobao.cun.auge.station.service.impl.incentive.audit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.incentive.dto.IncentiveAreaDto;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.org.service.OrgRangeType;
import com.taobao.cun.auge.station.exception.AugeBusinessException;

/**
 * Created by xujianhui on 17/3/30.
 * 不包含资金的大区运营审批权限逻辑
 * @author xujianhui
 * @date 2017/03/30
 */
@Service("nonFundAuditOperatorOrgCheckService")
public class NonFundAuditOperatorOrgCheckService implements AuditOperatorOrgCheckService{

    @Autowired
    private CuntaoOrgServiceClient cuntaoOrgServiceClient;

    /**
     * 非资金激励的审批逻辑:
     * 1.如果激励区域是大区以及大区以下 由大区运营审批.
     * 2.否则有总部运营审批
     * @param incentiveAreaList
     * @return
     */
    @Override
    public Long getAuditOperatorOrgId(List<IncentiveAreaDto> incentiveAreaList) {

        if (CollectionUtils.isEmpty(incentiveAreaList) || incentiveAreaList.get(0).getOrgId() == null){
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"非法的激励区域!");
        }
        Long orgId = incentiveAreaList.get(0).getOrgId();
        CuntaoOrgDto orgDto = cuntaoOrgServiceClient.getAncestor(orgId, OrgRangeType.LARGE_AREA);
        if (orgDto!=null) {
            return orgDto.getId();
        }else {
            orgDto = cuntaoOrgServiceClient.getAncestor(orgId, OrgRangeType.HEADQUARTERS);
            return orgDto.getId();
        }
    }
}
