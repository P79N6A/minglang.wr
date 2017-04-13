package com.taobao.cun.auge.incentive;

/**
 * Created by xujianhui on 17/3/13.
 *
 * @author xujianhui
 * @date 2017/03/13
 */

import com.taobao.cun.auge.station.dto.ApproveProcessTask;
import com.taobao.cun.auge.station.dto.StartProcessDto;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;

/**
 * Created by xujianhui on 17/2/22.
 * 激励方案审批流程的启动,相关节点权限生成逻辑,以及审批流程结束的处理逻辑.
 * @author xujianhui
 * @date 2017/02/22
 */
public interface IncentiveAuditFlowService {

    void submitStartAuditTask(ApproveProcessTask processTask);

    /**
     * 启动一个激励方案审批流程
     * 满足幂等
     * @param startProcessDto
     */
    void startProcess(StartProcessDto startProcessDto);


    /**
     * 生成组织 acl权限组成的taskCode,后面需要根据这个字符串判断一个用户是否具有该任务节点的审批权限
     */
    String generateTaskCode(String orgId, String aclRoleCode);


    /**
     * 整个流程实例(不是某个节点)完成审批消息的处理
     * @param businessId
     */
    void processFinishAuditMessage(String processInstanceId, Long businessId, ProcessApproveResultEnum result, String financeRemarks);

    /**
     * 某个流程节点审批结束的消息处理
     * @param businessId
     * @param taskNodeId
     * @param result
     */
    void taskNodeFinishAuditMessage(Long businessId, Long taskNodeId, ProcessApproveResultEnum result);

    /**
     * 结束一个审批流程
     * @param incentiveId
     * @return
     */
    boolean terminateProcess(Long incentiveId, String operator);

    /**
     * 审批完成的消息
     * @param objectId
     * @param resultEnum
     * @return
     */
    boolean handProcessFinish(Long objectId, ProcessApproveResultEnum resultEnum);
}

