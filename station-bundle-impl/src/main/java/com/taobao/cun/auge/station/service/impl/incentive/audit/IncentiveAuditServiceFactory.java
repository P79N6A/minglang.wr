package com.taobao.cun.auge.station.service.impl.incentive.audit;

import com.taobao.cun.auge.incentive.enums.IncentiveProgramFundsSourcesEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by xujianhui on 17/2/23.
 *
 * @author xujianhui
 * @date 2017/02/23
 */
@Service("incentiveAuditServiceFactory")
public class IncentiveAuditServiceFactory {

    @Autowired
    @Qualifier("cunAllowanceAuditOperatorOrgCheckService")
    private AuditOperatorOrgCheckService cunAllowanceAuditOperatorOrgCheckService;

    @Autowired
    @Qualifier("governAllowanceAuditOperatorOrgCheckService")
    private AuditOperatorOrgCheckService governAllowanceAuditOperatorOrgCheckService;

    public AuditOperatorOrgCheckService getAuditOperatorOrgCheckService(IncentiveProgramFundsSourcesEnum fundsSource) {
        if (IncentiveProgramFundsSourcesEnum.CUNTAO.equals(fundsSource)) {
            return cunAllowanceAuditOperatorOrgCheckService;
        }else if (IncentiveProgramFundsSourcesEnum.GOV.equals(fundsSource)) {
            return governAllowanceAuditOperatorOrgCheckService;
        }else {
            return governAllowanceAuditOperatorOrgCheckService;
        }


    }
}
