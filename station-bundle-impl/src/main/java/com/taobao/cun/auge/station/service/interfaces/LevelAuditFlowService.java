package com.taobao.cun.auge.station.service.interfaces;

import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelProcessDto;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;

public interface LevelAuditFlowService {

    /**
     * 启动层级审批流程
     * @param startProcessDto
     */
    void startApproveProcess(PartnerInstanceLevelProcessDto levelProcessDto);
    
    /**
     * 流程流转到审批人时需要获取审批人相关信息(该方法主要根据审批人orgId以及权限Code编码返回)
     */
    String generateApproverOrgIdAndRoleCode(String type, String orgId, String aclRoleCode);
    
    /**
     * 晋升审批消息处理
     * @param ob
     * @param approveResult 审批结果
     */
     void processAuditMessage(JSONObject ob, ProcessApproveResultEnum approveResult);
}
