package com.taobao.cun.auge.station.service.interfaces;

import com.alibaba.fastjson.JSONObject;

import com.taobao.cun.auge.station.dto.PartnerInstanceLevelDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelProcessDto;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;

public interface LevelAuditFlowService {

    public static final String TO_LEVEL = "ToLevel";
    public static final String EVALUATE_LEVEL_INFO = "evaluateInfo";
    public static final String EXPECTED_LEVEL = "expectedLevel";
    public static final String PARTNER_NAME = "partnerName";
    public static final String PROCESS_INSTANCE_ID = "procInstId";


    /**
     * 启动层级审批流程
     * @param levelProcessDto
     */
    void startApproveProcess(PartnerInstanceLevelProcessDto levelProcessDto);
    
    /**
     * 流程流转到审批人时需要获取审批人相关信息(该方法主要根据审批人orgId以及权限Code编码返回)
     */
    String generateApproverOrgIdAndRoleCode(String type, String orgId, String aclRoleCode);
    
    /**
     * 晋升审批消息处理
     * @param partnerInstanceLevelDto 审批晋升小二信息
     * @param approveResult 审批结果
     * @param adjustLevel 晋升到新层级
     */
     void processAuditMessage(PartnerInstanceLevelDto partnerInstanceLevelDto, ProcessApproveResultEnum approveResult, String adjustLevel);

    /**
     * 成功启动内外的审批流程以后要做的一些事情
     * @param
     */
    void afterStartApproveProcessSuccess(JSONObject ob);
}
