package com.taobao.cun.auge.station.service.impl.incentive;

import com.taobao.cun.auge.incentive.dto.IncentiveAreaDto;
import com.taobao.cun.auge.incentive.dto.IncentiveProgramDto;
import com.taobao.cun.auge.incentive.enums.IncentiveProgramFundsSourcesEnum;
import com.taobao.cun.auge.incentive.service.IncentiveProgramQueryService;
import com.taobao.cun.auge.station.dto.StartProcessDto;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.AlipayTagService;
import com.taobao.cun.auge.station.service.impl.incentive.audit.IncentiveAuditServiceFactory;
import com.taobao.cun.auge.station.service.impl.workflow.ApproverTaskCodeGenerator;
import com.taobao.cun.auge.station.service.interfaces.IncentiveAuditFlowService;
import com.taobao.cun.crius.bpm.dto.CuntaoProcessInstance;
import com.taobao.cun.crius.bpm.enums.AclPermissionEnum;
import com.taobao.cun.crius.bpm.enums.UserTypeEnum;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xujianhui on 17/2/22.
 *
 * @author xujianhui
 * @date 2017/02/22
 */
@Service("incentiveAuditFlowService")
@HSFProvider(serviceInterface= IncentiveAuditFlowService.class)
public class IncentiveAuditFlowServiceImpl implements IncentiveAuditFlowService {

    private static final Logger logger = LoggerFactory.getLogger(IncentiveAuditFlowServiceImpl.class);

    @Autowired
    private IncentiveProgramQueryService incentiveProgramQueryService;

    @Autowired
    private IncentiveAuditServiceFactory incentiveAuditServiceFactory;

    @Autowired
    protected CuntaoWorkFlowService cuntaoWorkFlowService;

    @Override
    public void startProcess(StartProcessDto startProcessDto) {
        validateParams(startProcessDto);
        String orgId = "";
        Map<String, String> initData = initWorkflowVariables(startProcessDto.getBusinessId(), startProcessDto.getOperator(), startProcessDto.getOperatorType());
        ResultModel<CuntaoProcessInstance> rm = cuntaoWorkFlowService.startProcessInstance(
                startProcessDto.getBusiness().getCode(),
                String.valueOf(startProcessDto.getBusinessId()),
                startProcessDto.getOperator(),
                UserTypeEnum.valueof(startProcessDto.getOperatorType().getCode()),
                initData);
        if (!rm.isSuccess()) {
            logger.error("启动审批流程失败。param=" + startProcessDto.getBusinessId(), rm.getException());
            throw new AugeServiceException("启动流程失败。param = " + startProcessDto.getBusinessId(), rm.getException());
        }
    }


    /**
     * 审批流程需要初始化的变量
     * @param incentiveProgramId
     * @return
     */
    private Map<String, String> initWorkflowVariables(Long incentiveProgramId, String submitorCode, OperatorTypeEnum submitorType) {
        Map<String, String> initData = new HashMap<String, String>();
        IncentiveProgramDto incentiveProgramDto = incentiveProgramQueryService.getIncentiveProgramDtoById(incentiveProgramId);

        IncentiveProgramFundsSourcesEnum source = incentiveProgramDto.getFundsSources();
        List<IncentiveAreaDto> areas = incentiveProgramDto.getIncentiveAreaList();
        Long auditOperatorOrgId = incentiveAuditServiceFactory.getAuditOperatorOrgCheckService(source).getAuditOperatorOrgId(areas);
        initData.put("orgId", String.valueOf(auditOperatorOrgId));
        initData.put("submitorCode", submitorCode);
        initData.put("submitorType", String.valueOf(submitorType));
        initData.put("operatorAuditPermission", AclPermissionEnum.incentive_operator_01.getCode());
        initData.put("riskAuditPermission", AclPermissionEnum.incentive_risk_01.getCode());
        initData.put("financeAuditPermission", AclPermissionEnum.incentive_finance_01.getCode());
        return initData;
    }

    @Override
    public String generateTaskCode(String orgId, String aclRoleCode) {
        return ApproverTaskCodeGenerator.generateOrgAclTaskCode(orgId, aclRoleCode);
    }

    @Override
    public void processFinishAuditMessage(Long businessId, ProcessApproveResultEnum result) {

    }

    @Override
    public void taskNodeFinishAuditMessage(Long businessId, Long taskNodeId, ProcessApproveResultEnum result) {

    }

    private void validateParams(StartProcessDto startProcessDto) {
        Assert.notNull(startProcessDto);
        Assert.notNull(startProcessDto.getBusiness());
        Assert.notNull(startProcessDto.getBusinessId());
    }
}
