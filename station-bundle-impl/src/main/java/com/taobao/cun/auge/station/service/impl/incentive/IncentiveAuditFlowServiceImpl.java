package com.taobao.cun.auge.station.service.impl.incentive;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.incentive.IncentiveAuditFlowService;
import com.taobao.cun.auge.incentive.dto.IncentiveAreaDto;
import com.taobao.cun.auge.incentive.dto.IncentiveProgramAuditDto;
import com.taobao.cun.auge.incentive.dto.IncentiveProgramDto;
import com.taobao.cun.auge.incentive.enums.IncentiveProgramFundsSourcesEnum;
import com.taobao.cun.auge.incentive.enums.IncentiveProgramIncentiveTypeEnum;
import com.taobao.cun.auge.incentive.enums.IncentiveProgramStateEnum;
import com.taobao.cun.auge.incentive.service.IncentiveProgramQueryService;
import com.taobao.cun.auge.incentive.service.IncentiveProgramService;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.station.dto.ApproveProcessTask;
import com.taobao.cun.auge.station.dto.StartProcessDto;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.impl.incentive.audit.IncentiveAuditServiceFactory;
import com.taobao.cun.auge.station.service.impl.workflow.ApproverTaskCodeGenerator;
import com.taobao.cun.crius.bpm.dto.CuntaoProcessInstance;
import com.taobao.cun.crius.bpm.dto.StartProcessInstanceDto;
import com.taobao.cun.crius.bpm.enums.AclPermissionEnum;
import com.taobao.cun.crius.bpm.enums.UserTypeEnum;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * Created by xujianhui on 17/2/22.
 *
 * @author xujianhui
 * @date 2017/02/22
 */
@Service("incentiveAuditFlowService")
@HSFProvider(serviceInterface= IncentiveAuditFlowService.class, clientTimeout = 15000)
public class IncentiveAuditFlowServiceImpl implements IncentiveAuditFlowService {

    private static final Logger logger = LoggerFactory.getLogger(IncentiveAuditFlowServiceImpl.class);

    private static final String INCENTIVE_AUDIT_BUSINESS_CODE = "incentive_program_audit";

    @Autowired
    private IncentiveProgramQueryService incentiveProgramQueryService;

    @Autowired
    private IncentiveProgramService incentiveProgramService;

    @Autowired
    private IncentiveAuditServiceFactory incentiveAuditServiceFactory;

    @Autowired
    protected CuntaoWorkFlowService cuntaoWorkFlowService;

    @Autowired
    private CuntaoOrgServiceClient cuntaoOrgServiceClient;

    @Autowired
    Emp360Adapter emp360Adapter;

    @Autowired
    private GeneralTaskSubmitService generalTaskSubmitService;

    @Override
    public void submitStartAuditTask(ApproveProcessTask processTask) {
        generalTaskSubmitService.submitIncentiveProgramAuditTask(processTask);
    }

    @Override
    public void startProcess(StartProcessDto startProcessDto) {
        validateParams(startProcessDto);
        Map<String, String> initData = initWorkflowVariables(startProcessDto.getBusinessId(), startProcessDto.getOperator(), startProcessDto.getOperatorType());
        
		StartProcessInstanceDto startDto = new StartProcessInstanceDto();

		startDto.setBusinessCode(startProcessDto.getBusiness().getCode());
		startDto.setBusinessId(String.valueOf(startProcessDto.getBusinessId()));

		startDto.setApplierId(startProcessDto.getOperator());
		startDto.setApplierUserType(UserTypeEnum.valueof(startProcessDto.getOperatorType().getCode()));
		startDto.setInitData(initData);

		ResultModel<Boolean> rm = cuntaoWorkFlowService.startProcessInstance(startDto);
        if (!rm.isSuccess()) {
            logger.error("启动审批流程失败。param=" + startProcessDto.getBusinessId(), rm.getException());
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"启动流程失败。param = " + startProcessDto.getBusinessId(), rm.getException());
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
        initData.put("headquarterOrgId", String.valueOf(cuntaoOrgServiceClient.getRoot().getId()));

        initData.put("need_finance_audit", isFundIncentive(incentiveProgramDto.getIncentiveTypeList()));
        initData.put("submitorCode", submitorCode);
        initData.put("submitorType", String.valueOf(submitorType));
        initData.put("applierName", buildOperatorName(submitorCode, submitorType));
        initData.put("applyDate", DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(Calendar.getInstance()));
        initData.put("operatorAuditPermission", AclPermissionEnum.incentive_operator_01.getCode());
        initData.put("riskAuditPermission", AclPermissionEnum.incentive_risk_01.getCode());
        initData.put("financeAuditPermission", AclPermissionEnum.incentive_finance_01.getCode());
        initData.put("dtoJson", JSON.toJSONString(incentiveProgramDto));
        return initData;
    }

    /**
     * 激励方案是否包含资金:这里决定是否需要财务审批,流程中有一个属性依赖这个值
     * @param incentiveTypes
     * @return
     */
    private String isFundIncentive(List<IncentiveProgramIncentiveTypeEnum> incentiveTypes) {
        if (incentiveTypes != null) {
            for (IncentiveProgramIncentiveTypeEnum type : incentiveTypes) {
                if (IncentiveProgramIncentiveTypeEnum.MONEY.getCode().equals(type.getCode())) {
                    return "TRUE";
                }
            }
        }
        return "FALSE";
    }

    @Override
    public String generateTaskCode(String orgId, String aclRoleCode) {
        return ApproverTaskCodeGenerator.generateOrgAclTaskCode(orgId, aclRoleCode);
    }

    @Override
    public void processFinishAuditMessage(String processInstanceId, Long businessId, ProcessApproveResultEnum result, String financeRemarks) {
        if (businessId == null || result == null) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"激励审批非法消息");
        }
        IncentiveProgramAuditDto auditDto = new IncentiveProgramAuditDto();
        auditDto.setOperatorType(OperatorTypeEnum.BUC);
        auditDto.setOperator("financeAuditor");
        auditDto.setOperatorOrgId(cuntaoOrgServiceClient.getRoot().getId());
        auditDto.setId(businessId);
        auditDto.setAuditInstanceId(processInstanceId);
        auditDto.setFinanceAuditRemarks(financeRemarks);
        if (ProcessApproveResultEnum.APPROVE_PASS.equals(result)) {
            auditDto.setStatEnum(IncentiveProgramStateEnum.AUDIT_PASS);
        }else if (ProcessApproveResultEnum.APPROVE_REFUSE.equals(result)) {
            auditDto.setStatEnum(IncentiveProgramStateEnum.AUDIT_NOT_PASS);
        }
        incentiveProgramService.auditIncentiveProgram(auditDto);
    }

    @Override
    public void taskNodeFinishAuditMessage(Long businessId, Long taskNodeId, ProcessApproveResultEnum result) {

    }

    @Override
    public boolean terminateProcess(Long incentiveId, String operator) {
        if (incentiveId == null) {
            return false;
        }
        try {
            ResultModel<CuntaoProcessInstance> resultModel = cuntaoWorkFlowService.findRunningProcessInstance(
                INCENTIVE_AUDIT_BUSINESS_CODE, incentiveId.toString());
            if (resultModel != null && resultModel.getResult() != null) {
                String processInstanceId = resultModel.getResult().getProcessInstanceId();
                cuntaoWorkFlowService.teminateProcessInstance(processInstanceId, operator);
                return true;
            }
        }catch(Exception e) {
            logger.error("terminate incentive audit process error:" + incentiveId, e);
        }
        return false;
    }

    @Override
    public boolean handProcessFinish(Long objectId, ProcessApproveResultEnum resultEnum) {
        return false;
    }

    private String buildOperatorName(String operator, OperatorTypeEnum type) {
        if (OperatorTypeEnum.BUC.equals(type)) {
            return emp360Adapter.getName(operator);
        }
        return operator;
    }

    private void validateParams(StartProcessDto startProcessDto) {
        Assert.notNull(startProcessDto);
        Assert.notNull(startProcessDto.getBusiness());
        Assert.notNull(startProcessDto.getBusinessId());
    }
}
