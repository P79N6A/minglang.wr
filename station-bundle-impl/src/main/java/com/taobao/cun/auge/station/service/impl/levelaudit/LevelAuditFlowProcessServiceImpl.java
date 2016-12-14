package com.taobao.cun.auge.station.service.impl.levelaudit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.evaluate.dto.PartnerLevelTaskBusinessDataDTO;
import com.taobao.cun.auge.evaluate.enums.LevelTaskDataTypeEnum;
import com.taobao.cun.auge.evaluate.enums.TaskNodeAuditStatus;
import com.taobao.cun.auge.evaluate.service.PartnerLevelTaskBusinessDataService;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.org.service.OrgRangeType;
import com.taobao.cun.auge.questionnaire.QuestionnireDispatchParamDTO;
import com.taobao.cun.auge.questionnaire.dto.QuestionnireForEvaluateEventData;
import com.taobao.cun.auge.questionnaire.enums.QuestionnireEventEnum;
import com.taobao.cun.auge.questionnaire.service.QuestionnireManageService;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelProcessDto;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.interfaces.LevelAuditFlowService;
import com.taobao.cun.auge.station.service.interfaces.LevelAuditMessageService;
import com.taobao.cun.crius.bpm.dto.CuntaoProcessInstance;
import com.taobao.cun.crius.bpm.enums.AclPermissionEnum;
import com.taobao.cun.crius.bpm.enums.UserTypeEnum;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.util.CalendarUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 晋升流程处理服务:S6 S7 S8需要人工流程审核
 * 类LevelAuditProcessStartService.java的实现描述：主要包括启动审核任务流程以及内外审核消息处理服务的获取;
 * @author xujianhui 2016年11月17日 下午3:39:03
 */
@Service("levelAuditFlowService")
@HSFProvider(serviceInterface = LevelAuditFlowService.class, clientTimeout=15000)
public class LevelAuditFlowProcessServiceImpl implements LevelAuditFlowService{

    private static final Logger logger = LoggerFactory.getLogger(LevelAuditFlowProcessServiceImpl.class);
    private static final String SPLITER_CHAR = "#";

    @Autowired
    protected CuntaoWorkFlowService cuntaoWorkFlowService;
    
    @Autowired
    @Qualifier("levelAuditMessageService")
    private LevelAuditMessageService levelAuditMessageService;
    
    @Autowired
    @Qualifier("levelReviewMessageService")
    private LevelAuditMessageService levelReviewMessageService;
    
    @Autowired
    private CuntaoOrgServiceClient cuntaoOrgServiceClient;

    @Autowired
    private PartnerLevelTaskBusinessDataService partnerLevelTaskBusinessDataService;

    @Autowired
    private QuestionnireManageService questionnireManageService;
    
    /**
     * 晋升S6 S7 S8才需要人工审核流程
     * S6的预售层级expectedLevel为空
     * @param expectedLevel
     * @return
     */
    public LevelAuditMessageService getLevelAuditMessageService(PartnerInstanceLevelEnum expectedLevel){
        if(expectedLevel == null){
            return  levelReviewMessageService;
        }else{
            return levelAuditMessageService;
        }
    }
    
    @Override
    public void startApproveProcess(PartnerInstanceLevelProcessDto levelProcessDto) {
        String businessCode = levelProcessDto.getBusinessCode();
        Long businessId = levelProcessDto.getBusinessId();

        String applierId = String.valueOf(levelProcessDto.getEmployeeId());
        OperatorTypeEnum operatorType = OperatorTypeEnum.BUC;

        Map<String, String> initData = new HashMap<String, String>();
        initData.put("countyStationName", levelProcessDto.getCountyStationName());
        initData.put("applyTime", CalendarUtil.formatDate(levelProcessDto.getApplyTime(),CalendarUtil.TIME_PATTERN));
        initData.put("partnerTaobaoUserId", String.valueOf(levelProcessDto.getPartnerTaobaoUserId()));
        initData.put("partnerName", levelProcessDto.getPartnerName());
        initData.put("currentLevel", levelProcessDto.getCurrentLevel().getLevel().toString());
        initData.put("currentLevelDesc", levelProcessDto.getCurrentLevel().getDescription());
        PartnerInstanceLevelEnum expectedLevel = levelProcessDto.getExpectedLevel();
        OrgPermissionHolder approveHolder = getApproversOrgId(expectedLevel, levelProcessDto.getCountyOrgId());
        initData.put("orgId", String.valueOf(approveHolder.getOrgId()));
        initData.put("permission", approveHolder.getPermission());
        
        String evaluateToLevelKey = "ToLevel";
        if(expectedLevel!=null){
            initData.put("expectedLevel", expectedLevel.getLevel().toString());
            initData.put("expectedLevelDesc", expectedLevel.getDescription());
            initData.put(evaluateToLevelKey, expectedLevel.getLevel().toString());
        }else{
            initData.put(evaluateToLevelKey, PartnerInstanceLevelEnum.S_6.getLevel().name().toString());
            initData.put("expectedLevelDesc", PartnerInstanceLevelEnum.S_6.getDescription());
        }
        initData.put("score", levelProcessDto.getScore().toString());
        initData.put("monthlyIncome", levelProcessDto.getMonthlyIncome().toString());
        initData.put("stationId", String.valueOf(levelProcessDto.getStationId()));
        initData.put("stationName", levelProcessDto.getStationName());
        initData.put("employeeName", levelProcessDto.getEmployeeName());
        initData.put("employeeId", levelProcessDto.getEmployeeId());
        initData.put("evaluateInfo", levelProcessDto.getEvaluateInfo());
        ResultModel<CuntaoProcessInstance> rm = cuntaoWorkFlowService.startProcessInstance(businessCode,
                String.valueOf(businessId), applierId, UserTypeEnum.valueof(operatorType.getCode()), initData);
        if (!rm.isSuccess()) {
            logger.error("启动审批流程失败。param=" + JSON.toJSONString(levelProcessDto) , rm.getException());
            throw new AugeServiceException("启动流程失败。param = " + JSON.toJSONString(levelProcessDto),
                    rm.getException());
        }
    }
    
    @Override
    public String generateApproverOrgIdAndRoleCode(String type, String orgId, String aclRoleCode){
        logger.info(String.format("查询流程节点执行人。orgId = %s , roleCode = %s", orgId, aclRoleCode));
        JSONArray result = new JSONArray();
        result.add(0, type + SPLITER_CHAR + orgId + SPLITER_CHAR + aclRoleCode);
        return result.toString();
    }

    @Override
    public void processAuditMessage(PartnerInstanceLevelDto partnerInstanceLevelDto, ProcessApproveResultEnum approveResult, String adjustLevel) {
        if (ProcessApproveResultEnum.APPROVE_PASS.equals(approveResult)) {
            getLevelAuditMessageService(partnerInstanceLevelDto.getExpectedLevel()).handleApprove(partnerInstanceLevelDto, adjustLevel);
        } else {
            getLevelAuditMessageService(partnerInstanceLevelDto.getExpectedLevel()).handleRefuse(partnerInstanceLevelDto, adjustLevel);
        }
    }

    @Override
    public void afterStartApproveProcessSuccess(JSONObject jsonObject) {
        Long processInstanceId = jsonObject.getLong(LevelAuditFlowService.PROCESS_INSTANCE_ID);
        PartnerInstanceLevelDto partnerInstanceLevelDto = JSON.parseObject(jsonObject.getString(LevelAuditFlowService.EVALUATE_LEVEL_INFO), PartnerInstanceLevelDto.class);
        dispatchQuesionnaire(jsonObject, processInstanceId, partnerInstanceLevelDto);
        initReplyMaterial(processInstanceId, partnerInstanceLevelDto);
    }

    /**
     * 初始化答辩材料
     * @param processInstanceId
     * @param partnerInstanceLevelDto
     */
    private void initReplyMaterial(Long processInstanceId, PartnerInstanceLevelDto partnerInstanceLevelDto) {
        PartnerLevelTaskBusinessDataDTO businessDataDTO = new PartnerLevelTaskBusinessDataDTO();
        businessDataDTO.setAuditedPersonId(partnerInstanceLevelDto.getTaobaoUserId());
        businessDataDTO.setProcessInstanceId(processInstanceId);
        businessDataDTO.setInfoType(LevelTaskDataTypeEnum.REPLY_ATTACHMENT);
        businessDataDTO.setAuditStatus(TaskNodeAuditStatus.NOT_AUDIT.name());
        partnerLevelTaskBusinessDataService.saveTaskBusinessData(processInstanceId, null, businessDataDTO);
    }

    /**
     * 分发调查问卷
     * @param jsonObject
     * @param processInstanceId
     * @param partnerInstanceLevelDto
     */
    private void dispatchQuesionnaire(JSONObject jsonObject, Long processInstanceId, PartnerInstanceLevelDto partnerInstanceLevelDto) {
        QuestionnireDispatchParamDTO dispatchParamDTO = new QuestionnireDispatchParamDTO();
        dispatchParamDTO.setQuestionnireEventId(processInstanceId);
        dispatchParamDTO.setInformantId(partnerInstanceLevelDto.getTaobaoUserId());
        dispatchParamDTO.setInformantCountyOrgId(partnerInstanceLevelDto.getCountyOrgId());
        dispatchParamDTO.setType(QuestionnireEventEnum.PARTNER_EVALUATE);

        QuestionnireForEvaluateEventData eventData = new QuestionnireForEvaluateEventData();
        Date evaluateDate = partnerInstanceLevelDto.getEvaluateDate();
        eventData.setEvaluateDate(evaluateDate);
        eventData.setInfomantId(partnerInstanceLevelDto.getTaobaoUserId());
        eventData.setInfomantName(jsonObject.getString(LevelAuditFlowService.PARTNER_NAME));
        eventData.setToLevel(jsonObject.getString(LevelAuditFlowService.TO_LEVEL));
        dispatchParamDTO.setEventDataJson(JSON.toJSONString(eventData));
        questionnireManageService.dispatchQuestionnire(dispatchParamDTO);
    }

    private OrgPermissionHolder getApproversOrgId(PartnerInstanceLevelEnum expectedLevel, Long countyOrgId){
        if(expectedLevel == null || countyOrgId==null){
            return new OrgPermissionHolder(countyOrgId, AclPermissionEnum.cuntao_admin_county_01.getCode());
        }
        OrgRangeType rangeType = OrgRangeType.PROVINCE;
        AclPermissionEnum permision = null;
        if(expectedLevel.getLevel() == PartnerInstanceLevelEnum.PartnerInstanceLevel.S7){
            rangeType = OrgRangeType.PROVINCE;
            permision = AclPermissionEnum.cuntao_dq_admin_01;
        }else if(expectedLevel.getLevel() == PartnerInstanceLevelEnum.PartnerInstanceLevel.S8){
            rangeType = OrgRangeType.LARGE_AREA;
            permision = AclPermissionEnum.cuntao_dqjl_admin_01;
        }
        CuntaoOrgDto orgDto = cuntaoOrgServiceClient.getAncestor(countyOrgId, rangeType);
        if(orgDto!=null){
            return new OrgPermissionHolder(orgDto.getId(), permision.getCode());
        }
        logger.error("orgDto is null, countyOrgId:{}, rangeType:{}, expectLevel:{}", new Object[]{ countyOrgId, rangeType, expectedLevel});
        return null;
    }
    
    static class OrgPermissionHolder {
        private Long orgId;
        private String permission;
        
        public OrgPermissionHolder(Long orgId, String permission) {
            super();
            this.orgId = orgId;
            this.permission = permission;
        }
        
        public Long getOrgId() {
            return orgId;
        }
        
        public String getPermission() {
            return permission;
        }
        
    }

}
