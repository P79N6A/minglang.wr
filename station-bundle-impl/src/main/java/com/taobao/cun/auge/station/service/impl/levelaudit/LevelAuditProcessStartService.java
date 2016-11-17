package com.taobao.cun.auge.station.service.impl.levelaudit;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.org.service.OrgRangeType;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelProcessDto;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.interfaces.LevelAuditMessageService;
import com.taobao.cun.crius.bpm.dto.CuntaoProcessInstance;
import com.taobao.cun.crius.bpm.enums.UserTypeEnum;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.util.CalendarUtil;

/**
 * 晋升流程启动服务:S6 S7 S8需要人工流程审核
 * 类LevelAuditProcessStartService.java的实现描述：主要包括启动审核任务流程以及内外审核消息处理服务的获取;
 * @author xujianhui 2016年11月17日 下午3:39:03
 */
@Service("levelAuditProcessStartService")
public class LevelAuditProcessStartService {

    private static final Logger logger = LoggerFactory.getLogger(LevelAuditProcessStartService.class);

    @Autowired
    protected CuntaoWorkFlowService cuntaoWorkFlowService;
    
    @Autowired
    private LevelAuditMessageService levelAuditMessageService;
    
    @Autowired
    private LevelAuditMessageService levelReviewMessageService;
    
    @Autowired
    private CuntaoOrgServiceClient cuntaoOrgServiceClient;

    
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
    
    public final void startApproveProcess(PartnerInstanceLevelProcessDto levelProcessDto) {
        String businessCode = levelProcessDto.getBusinessCode();
        Long businessId = levelProcessDto.getBusinessId();

        String applierId = String.valueOf(levelProcessDto.getEmployeeId());
        OperatorTypeEnum operatorType = OperatorTypeEnum.BUC;

        //  创建村点任务流程
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
        if(expectedLevel!=null){
            initData.put("expectedLevel", expectedLevel.getLevel().toString());
            initData.put("expectedLevelDesc", expectedLevel.getDescription());
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
    
    public static final String SPLITER_CHAR = "#";
    public String generateApproverOrgIdAndRoleCode(String type, String orgId, String aclRoleCode){
        logger.info(String.format("查询流程节点执行人。orgId = %s , roleCode = %s", orgId, aclRoleCode));
        JSONArray result = new JSONArray();
        result.add(0, type + SPLITER_CHAR + orgId + SPLITER_CHAR + aclRoleCode);
        return result.toString();
    }

    private OrgPermissionHolder getApproversOrgId(PartnerInstanceLevelEnum expectedLevel, Long countyOrgId){
        if(expectedLevel == null || countyOrgId==null){
            return new OrgPermissionHolder(countyOrgId, "cuntao_dq_admin_01");
        }
        OrgRangeType rangeType = OrgRangeType.PROVINCE;
        String permision = "";
        if(expectedLevel.getLevel() == PartnerInstanceLevelEnum.PartnerInstanceLevel.S7){
            rangeType = OrgRangeType.PROVINCE;
        }else if(expectedLevel.getLevel() == PartnerInstanceLevelEnum.PartnerInstanceLevel.S8){
            rangeType = OrgRangeType.LARGE_AREA;
        }
        CuntaoOrgDto orgDto = cuntaoOrgServiceClient.getAncestor(countyOrgId, rangeType);
        if(orgDto!=null){
            return new OrgPermissionHolder(orgDto.getId(), permision);
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
