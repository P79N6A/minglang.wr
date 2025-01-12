package com.taobao.cun.auge.station.notify.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.taobao.common.category.util.StringUtil;
import com.taobao.cun.auge.asset.service.AssetService;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.CuntaoFlowRecord;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.StationBundleEventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.flowRecord.enums.CuntaoFlowRecordTargetTypeEnum;
import com.taobao.cun.auge.incentive.IncentiveAuditFlowService;
import com.taobao.cun.auge.lifecycle.event.LifeCyclePhaseEvent;
import com.taobao.cun.auge.lifecycle.event.LifeCyclePhaseEventBuilder;
import com.taobao.cun.auge.lifecycle.statemachine.StateMachineEvent;
import com.taobao.cun.auge.lifecycle.statemachine.StateMachineService;
import com.taobao.cun.auge.platform.service.BusiWorkBaseInfoService;
import com.taobao.cun.auge.station.bo.*;
import com.taobao.cun.auge.station.convert.PartnerInstanceConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.dto.*;
import com.taobao.cun.auge.station.enums.*;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.NewRevenueCommunicationService;
import com.taobao.cun.auge.station.service.StationDecorateService;
import com.taobao.cun.auge.station.service.StationService;
import com.taobao.cun.auge.station.service.interfaces.LevelAuditFlowService;
import com.taobao.cun.crius.bpm.dto.CuntaoTask;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.recruit.ability.dto.ServiceAbilityApplyAuditDto;
import com.taobao.cun.recruit.ability.enums.ServiceAbilityApplyStateEnum;
import com.taobao.cun.recruit.ability.service.ServiceAbilityApplyService;
import com.taobao.cun.recruit.partner.dto.AddressInfoDecisionAuditDto;
import com.taobao.cun.recruit.partner.dto.AddressInfoDecisionDto;
import com.taobao.cun.recruit.partner.dto.PartnerQualifyApplyAuditDto;
import com.taobao.cun.recruit.partner.enums.AddressInfoDecisionStatusEnum;
import com.taobao.cun.recruit.partner.enums.PartnerQualifyApplyStatus;
import com.taobao.cun.recruit.partner.service.AddressInfoDecisionService;
import com.taobao.cun.recruit.partner.service.PartnerQualifyApplyService;
import com.taobao.notify.message.StringMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Component("processProcessor")
public class ProcessProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ProcessProcessor.class);
    private static final String ERROR_MSG = "ProcessProcessor-ERROR ";

    @Autowired
    PartnerInstanceBO partnerInstanceBO;

    @Autowired
    StationBO stationBO;

    @Autowired
    QuitStationApplyBO quitStationApplyBO;

    @Autowired
    CloseStationApplyBO closeStationApplyBO;

    @Autowired
    PartnerInstanceHandler partnerInstanceHandler;

    @Autowired
    PartnerLifecycleBO partnerLifecycleBO;

    @Autowired
    GeneralTaskSubmitService generalTaskSubmitService;

    @Autowired
    BusiWorkBaseInfoService busiWorkBaseInfoService;

    @Autowired
    StationService stationService;

    @Autowired
    CuntaoFlowRecordBO cuntaoFlowRecordBO;

    @Autowired
    LevelAuditFlowService levelAuditFlowService;

    @Autowired
    PeixunPurchaseBO peixunPurchaseBO;
    @Autowired
    PartnerBO partnerBO;

    @Autowired
    IncentiveAuditFlowService incentiveAuditFlowService;

    @Autowired
    PartnerPeixunBO partnerPeixunBO;

    @Autowired
    private StateMachineService stateMachineService;

    @Autowired
    private PartnerQualifyApplyService partnerQualifyApplyService;

    @Autowired
    private StationModifyApplyBO stationModifyApplyBO;

    @Autowired
    AssetService assetService;

    @Autowired
    StationDecorateService stationDecorateService;

    @Autowired
    private AddressInfoDecisionService addressInfoDecisionService;
    @Autowired
    private DiamondConfiguredProperties diamondConfiguredProperties;
    @Autowired
    private CuntaoWorkFlowService cuntaoWorkFlowService;

    @Autowired
    private ServiceAbilityApplyService serviceAbilityApplyService;

    @Autowired
    private NewRevenueCommunicationService newRevenueCommunicationService;

    @Autowired
    private StationDecorateBO stationDecorateBO;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void handleProcessMsg(StringMessage strMessage, JSONObject ob) throws Exception {
        String msgType = strMessage.getMessageType();
        String businessCode = ob.getString("businessCode");
        String objectId = ob.getString("objectId");
        Long businessId = Long.valueOf(objectId);
        // 监听流程实例结束
        if (ProcessMsgTypeEnum.PROC_INST_FINISH.getCode().equals(msgType)) {
            JSONObject instanceStatus = ob.getJSONObject("instanceStatus");
            String resultCode = instanceStatus.getString("code");
            if (ProcessBusinessEnum.TPV_QUIT.getCode().equals(businessCode) || ProcessBusinessEnum.TPV_CLOSE.getCode()
                .equals(businessCode)) {
                return;
            }
            // 村点强制停业
            if (ProcessBusinessEnum.stationForcedClosure.getCode().equals(businessCode)) {
                closeApprove(businessId, ProcessApproveResultEnum.valueof(resultCode));
                // 合伙人退出
            } else if (ProcessBusinessEnum.stationQuitRecord.getCode().equals(businessCode)) {
                quitApprove(businessId, ProcessApproveResultEnum.valueof(resultCode));
                //村点撤点
            } else if (ProcessBusinessEnum.SHUT_DOWN_STATION.getCode().equals(businessCode)) {
                stationService.auditQuitStation(businessId, ProcessApproveResultEnum.valueof(resultCode));
            } else if (ProcessBusinessEnum.partnerInstanceLevelAudit.getCode().equals(businessCode)) {
                try {
                    logger.info("monitorLevelApprove, JSONObject :" + ob.toJSONString());
                    /**
                     * 启动审批流程时塞进来的数据
                     */
                    PartnerInstanceLevelDto dto = JSON.parseObject(ob.getString("evaluateInfo"),
                        PartnerInstanceLevelDto.class);
                    /**
                     * cuntaobops 审批通过的level
                     */
                    String adjustLevel = ob.getString("adjustLevel");
                    levelAuditFlowService.processAuditMessage(dto, ProcessApproveResultEnum.valueof(resultCode),
                        adjustLevel);
                } catch (Exception e) {
                    logger.error("LevelAuditFlowProcessServiceImpl processAuditMessage error  ", e);
                    throw e;
                }
            } else if (ProcessBusinessEnum.partnerFlowerNameApply.getCode().equals(businessCode)) {
                handleFlowerNameApply(objectId, resultCode);
            } else if (ProcessBusinessEnum.peixunRefund.getCode().equals(businessCode)) {
                handlePeixunRefund(objectId, resultCode);
            } else if (ProcessBusinessEnum.incentiveProgramAudit.getCode().equals(businessCode)) {
                String financeRemarks = ob.getString("financeRemarks");
                String processInstanceId = ob.getString(LevelAuditFlowService.PROCESS_INSTANCE_ID);
                incentiveAuditFlowService.processFinishAuditMessage(processInstanceId, businessId,
                    ProcessApproveResultEnum.valueof(resultCode), financeRemarks);
            } else if (ProcessBusinessEnum.assetTransfer.getCode().equals(businessCode)) {
                assetService.processAuditAssetTransfer(businessId, ProcessApproveResultEnum.valueof(resultCode));
            } else if (ProcessBusinessEnum.partnerQualifyAudit.getCode().equals(businessCode)) {
                PartnerQualifyApplyAuditDto pqaDto = new PartnerQualifyApplyAuditDto();
                if (ProcessApproveResultEnum.APPROVE_PASS.getCode().equals(resultCode)) {
                    pqaDto.setStatus(PartnerQualifyApplyStatus.AUDIT_PASS);
                } else if (ProcessApproveResultEnum.APPROVE_REFUSE.getCode().equals(resultCode)) {
                    pqaDto.setStatus(PartnerQualifyApplyStatus.AUDIT_NOT_PASS);
                }
                pqaDto.setId(businessId);
                pqaDto.copyOperatorDto(com.taobao.cun.common.operator.OperatorDto.defaultOperator());
                partnerQualifyApplyService.auditPartnerQualifyApply(pqaDto);
            } else if (ProcessBusinessEnum.serviceAbilityDecision.getCode().equals(businessCode)
                || ProcessBusinessEnum.serviceAbilitySHRHDecision.getCode().equals(businessCode)) {
                ServiceAbilityApplyAuditDto auditDto = new ServiceAbilityApplyAuditDto();
                if (ProcessApproveResultEnum.APPROVE_PASS.getCode().equals(resultCode)) {
                    auditDto.setState(ServiceAbilityApplyStateEnum.AUDIT_PASS);
                } else if (ProcessApproveResultEnum.APPROVE_REFUSE.getCode().equals(resultCode)) {
                    auditDto.setState(ServiceAbilityApplyStateEnum.AUDIT_UNPASS);
                }
                auditDto.setAuditOpinion(ob.getString("taskRemark"));
                auditDto.setId(businessId);
                auditDto.setOperator("system");
                serviceAbilityApplyService.auditApply(auditDto);
            }else if (ProcessBusinessEnum.addressInfoDecision.getCode().equals(businessCode)) {
                AddressInfoDecisionAuditDto aidDto = new AddressInfoDecisionAuditDto();
                if (ProcessApproveResultEnum.APPROVE_PASS.getCode().equals(resultCode)) {
                    aidDto.setStatus(AddressInfoDecisionStatusEnum.AUDIT_PASS);
                } else if (ProcessApproveResultEnum.APPROVE_REFUSE.getCode().equals(resultCode)) {
                    aidDto.setStatus(AddressInfoDecisionStatusEnum.AUDIT_NOT_PASS);
                }
                aidDto.setId(businessId);
                aidDto.copyOperatorDto(com.taobao.cun.common.operator.OperatorDto.defaultOperator());
                addressInfoDecisionService.audit(aidDto);
            } else if (ProcessBusinessEnum.stationInfoApply.getCode().equals(businessCode)) {
                if (ProcessApproveResultEnum.APPROVE_PASS.getCode().equals(resultCode)) {
                    stationModifyApplyBO.auditForNameAndAddress(businessId, StationModifyApplyStatusEnum.AUDIT_PASS);
                } else if (ProcessApproveResultEnum.APPROVE_REFUSE.getCode().equals(resultCode)) {
                    stationModifyApplyBO.auditForNameAndAddress(businessId,
                        StationModifyApplyStatusEnum.AUDIT_NOT_PASS);
                }
            } else if (ProcessBusinessEnum.decorationFeedback.getCode().equals(businessCode)) {
                StationDecorateAuditDto stationDecorateAuditDto = new StationDecorateAuditDto();
                if (ProcessApproveResultEnum.APPROVE_PASS.getCode().equals(resultCode)) {
                    stationDecorateAuditDto.setIsAgree(true);
                } else if (ProcessApproveResultEnum.APPROVE_REFUSE.getCode().equals(resultCode)) {
                    stationDecorateAuditDto.setIsAgree(false);
                }
                stationDecorateAuditDto.setId(businessId);
                stationDecorateAuditDto.copyOperatorDto(OperatorDto.defaultOperator());
                stationDecorateService.audit(stationDecorateAuditDto);
            }else if(ProcessBusinessEnum.decorationCheckAudit.getCode().equals(businessCode)){
                //装修审核流程结束
                StationDecorateDto stationDecrateDto = stationDecorateService.getInfoById(businessId);
                if(ProcessApproveResultEnum.APPROVE_PASS.getCode().equals(resultCode)){
                    stationDecorateService.auditStationDecorateCheck(stationDecrateDto.getStationId(),ProcessApproveResultEnum.APPROVE_PASS,stationDecrateDto.getAuditOpinion());
                }else{
                    //不通过
                    stationDecorateBO.auditStationDecorateCheck(stationDecrateDto.getStationId(),ProcessApproveResultEnum.APPROVE_REFUSE,stationDecrateDto.getAuditOpinion());
                }
            }
            else if(ProcessBusinessEnum.stationTransHandOverInviteAudit.getCode().equals(businessCode)){

                String inviteType = ob.getString("inviteType");
                NewRevenueCommunicationDto newRevenueCommunicationDto=newRevenueCommunicationService.getProcessNewRevenueCommunication(inviteType,businessId.toString());
                if(newRevenueCommunicationDto!=null){
                    newRevenueCommunicationDto.setAuditStatus(resultCode);
                    newRevenueCommunicationService.auditNewRevenueCommunication(newRevenueCommunicationDto);
                }
            }

            // 节点被激活
        }else if (ProcessMsgTypeEnum.ACT_INST_START.getCode().equals(msgType)) {
            // 任务被激活
        } else if (ProcessMsgTypeEnum.TASK_ACTIVATED.getCode().equals(msgType)) {
            // 村点强制停业
            if (ProcessBusinessEnum.stationForcedClosure.getCode().equals(businessCode)) {
                monitorTaskStarted(businessId, PartnerLifecycleBusinessTypeEnum.CLOSING);
                // 村点退出
            } else if (ProcessBusinessEnum.stationQuitRecord.getCode().equals(businessCode)) {
                monitorTaskStarted(businessId, PartnerLifecycleBusinessTypeEnum.QUITING);
            }
            //任务完成
        } else if (ProcessMsgTypeEnum.TASK_COMPLETED.getCode().equals(msgType)) {
            recordCloseQuitApprove(ob, businessCode, businessId);
            //流程启动
            if (ProcessBusinessEnum.peixunPurchase.getCode().equals(businessCode)) {
                //培训集采
                String resultCode = ob.getString("result");
                String audit = ob.getString("approver");
                String auditName = ob.getString("approverName");
                String desc = ob.getString("taskRemark");
                handlePeixunPurchase(objectId, audit, auditName, desc, resultCode);
            } else if (ProcessBusinessEnum.decorationFeedback.getCode().equals(businessCode)) {
                String auditOpinion = ob.getString("taskRemark");
                StationDecorateDto sdd = new StationDecorateDto();
                sdd.setAuditOpinion(auditOpinion);
                sdd.setId(businessId);
                sdd.copyOperatorDto(OperatorDto.defaultOperator());
                stationDecorateService.updateStationDecorate(sdd);
            } else if (ProcessBusinessEnum.decorationInfoDecision.getCode().equals(businessCode)) {
                String auditOpinion = ob.getString("taskRemark");
                DecorationInfoDecisionDto sdd = new DecorationInfoDecisionDto();
                sdd.setAuditOpinion(auditOpinion);
                sdd.setId(businessId);
                sdd.copyOperatorDto(OperatorDto.defaultOperator());
                stationDecorateService.updateDecorationDecision(sdd);
            } else if (ProcessBusinessEnum.addressInfoDecision.getCode().equals(businessCode)) {
                String auditOpinion = ob.getString("taskRemark");
                AddressInfoDecisionDto sdd = new AddressInfoDecisionDto();
                sdd.setMemo(auditOpinion);
                sdd.setId(businessId);
                sdd.copyOperatorDto(com.taobao.cun.common.operator.OperatorDto.defaultOperator());
                addressInfoDecisionService.updateAddressInfoMemo(sdd);
            } else if (ProcessBusinessEnum.serviceAbilityDecision.getCode().equals(businessCode)
                || ProcessBusinessEnum.serviceAbilitySHRHDecision.getCode().equals(businessCode)) {
                ServiceAbilityApplyAuditDto auditDto = new ServiceAbilityApplyAuditDto();
                auditDto.setAuditOpinion(ob.getString("taskRemark"));
                auditDto.setId(businessId);
                auditDto.setOperator("system");
                serviceAbilityApplyService.saveApplyAuditOpinion(auditDto);
            }else if (ProcessBusinessEnum.decorationDesignAudit.getCode().equals(businessCode)) {
                StationDecorateDto stationDecrateDto = stationDecorateService.getInfoById(businessId);
                String taskId = ob.getString("taskId");
                CuntaoTask task = cuntaoWorkFlowService.getCuntaoTask(taskId);
                String resultCode = ob.getString("result");
                String desc = ob.getString("taskRemark");
                ProcessApproveResultEnum decorationDesignAuditResult = null;
                if ("拒绝".equals(resultCode)) {
                    decorationDesignAuditResult = ProcessApproveResultEnum.APPROVE_REFUSE;
                } else {
                    decorationDesignAuditResult = ProcessApproveResultEnum.APPROVE_PASS;
                }
                if (diamondConfiguredProperties.getDecorateDesignCountyAuditActivityId().equals(task.getActivityId())
                    || "sid-5121cfcd-77dc-18f4-6bd1-039075a2b442".equals(task.getActivityId())) {
                    //为了兼容老数据，activityId写死，待老数据处理完，将该硬编码删除
                    //县小二审核
                    stationDecorateService.auditStationDecorateDesignByCounty(stationDecrateDto.getStationId(),
                        decorationDesignAuditResult, desc);
                } else {
                    //运营中台审核
                    stationDecorateService.auditStationDecorateDesign(stationDecrateDto.getStationId(),
                        decorationDesignAuditResult, desc);
                }
            } else if (ProcessBusinessEnum.decorationCheckAudit.getCode().equals(businessCode)) {

                StationDecorateDto stationDecrateDto = stationDecorateService.getInfoById(businessId);
                String resultCode = ob.getString("result");
                String desc = ob.getString("taskRemark");

                ProcessApproveResultEnum decorationCheckAuditResult = null;
                if ("拒绝".equals(resultCode)) {
                    decorationCheckAuditResult = ProcessApproveResultEnum.APPROVE_REFUSE;
                } else {
                    decorationCheckAuditResult = ProcessApproveResultEnum.APPROVE_PASS;
                }
                //只保留意见,为流程结束的意见获取推送
                stationDecorateService.auditStationDecorateAfterNodeFish(stationDecrateDto.getStationId(),
                        decorationCheckAuditResult, desc);
            }
        } else if (ProcessMsgTypeEnum.PROC_INST_START.getCode().equals(msgType)) {
            if (ProcessBusinessEnum.partnerInstanceLevelAudit.getCode().equals(businessCode)) {
                levelAuditFlowService.afterStartApproveProcessSuccess(ob);
            }
        }
    }

    // 停业、退出打印日志
    private void recordCloseQuitApprove(JSONObject ob, String businessCode, Long businessId) {
        if (ProcessBusinessEnum.stationForcedClosure.getCode().equals(businessCode)
            || ProcessBusinessEnum.TPV_CLOSE.getCode().equals(businessCode)
            || ProcessBusinessEnum.stationQuitRecord.getCode().equals(businessCode)
            || ProcessBusinessEnum.TPV_QUIT.getCode().equals(businessCode)) {
            try {
                PartnerInstanceDto partnerStationRel = partnerInstanceBO.getPartnerInstanceById(businessId);

                CuntaoFlowRecord cuntaoFlowRecord = new CuntaoFlowRecord();

                cuntaoFlowRecord.setTargetId(partnerStationRel.getStationId());
                cuntaoFlowRecord.setTargetType(CuntaoFlowRecordTargetTypeEnum.STATION.getCode());

                if (StringUtil.isNotBlank(ob.getString("result"))) {
                    cuntaoFlowRecord.setNodeTitle("审批(" + ob.getString("result") + ")");
                } else {
                    cuntaoFlowRecord.setNodeTitle("审批");
                }
                cuntaoFlowRecord.setOperatorName(ob.getString("approverName"));
                cuntaoFlowRecord.setOperatorWorkid(ob.getString("approver"));
                cuntaoFlowRecord.setOperateTime(new Date());
                cuntaoFlowRecord.setOperateOpinion(ob.getString("result"));
                cuntaoFlowRecord.setRemarks(ob.getString("taskRemark"));
                cuntaoFlowRecordBO.addRecord(cuntaoFlowRecord);
            } catch (Exception e) {
                logger.error("Failed to log close quit record.JSONObject=" + ob.toJSONString(), e);
            }
        }
    }

    private void monitorHomepageShowApprove(String objectId, String businessCode,
                                            ProcessApproveResultEnum approveResult) {
        busiWorkBaseInfoService.updateHomepageShowApproveResult(Long.parseLong(objectId), businessCode,
            ProcessApproveResultEnum.APPROVE_PASS.equals(approveResult));

    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void closeApprove(Long instanceId, ProcessApproveResultEnum approveResult) throws Exception {
        try {
            PartnerStationRel partnerStationRel = partnerInstanceBO.findPartnerInstanceById(instanceId);

            //Long stationId = partnerStationRel.getStationId();

            OperatorDto operatorDto = OperatorDto.defaultOperator();
            //String operator = operatorDto.getOperator();

            if (ProcessApproveResultEnum.APPROVE_PASS.equals(approveResult)) {
                PartnerInstanceDto partnerInstanceDto = PartnerInstanceConverter.convert(partnerStationRel);
                partnerInstanceDto.copyOperatorDto(operatorDto);
                LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(partnerInstanceDto,
                    StateMachineEvent.CLOSED_EVENT);
                stateMachineService.executePhase(phaseEvent);
                /*
				// 合伙人实例已停业, 更新服务结束时间
				PartnerInstanceDto instance = new PartnerInstanceDto();
				instance.setServiceEndTime(new Date());
				instance.setState(PartnerInstanceStateEnum.CLOSED);
				instance.setId(instanceId);
				instance.copyOperatorDto(operatorDto);
				// instance.setVersion(partnerStationRel.getVersion());这里不需要乐观锁
				partnerInstanceBO.updatePartnerStationRel(instance);

				// 村点已停业
				stationBO.changeState(stationId, StationStatusEnum.CLOSING, StationStatusEnum.CLOSED, operator);

				// 更新生命周期表
				updatePartnerLifecycle(instanceId, PartnerLifecycleRoleApproveEnum.AUDIT_PASS);

				// 同步station_apply状态和服务结束时间
				stationApplySyncBO.updateStationApply(instanceId, SyncStationApplyEnum.UPDATE_BASE);

				// 记录村点状态变化
				// 去标，通过事件实现
				// 短信推送
				// 通知admin，合伙人退出。让他们监听村点状态变更事件
				dispatchInstStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.CLOSED, operatorDto);
			*/
            } else {
                //获取停业申请单
				/*CloseStationApplyDto closeStationApplyDto = closeStationApplyBO.getCloseStationApply(instanceId);
				PartnerInstanceStateEnum sourceInstanceState = closeStationApplyDto.getInstanceState();

				// 合伙人实例已停业
				partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.CLOSING, sourceInstanceState,
				operator);

				// 村点已停业
				if (PartnerInstanceStateEnum.SERVICING.equals(sourceInstanceState)) {
					stationBO.changeState(stationId, StationStatusEnum.CLOSING, StationStatusEnum.SERVICING, operator);
				} else if (PartnerInstanceStateEnum.DECORATING.equals(sourceInstanceState)) {
					stationBO.changeState(stationId, StationStatusEnum.CLOSING, StationStatusEnum.DECORATING,
					operator);
				} else {
					throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"partner
					 state is not decorating or servicing.");
				}

				// 删除停业申请表
				closeStationApplyBO.deleteCloseStationApply(instanceId, operator);

				// 更新生命周期表
				updatePartnerLifecycle(instanceId, PartnerLifecycleRoleApproveEnum.AUDIT_NOPASS);

				// 同步station_apply，只更新状态
				stationApplySyncBO.updateStationApply(instanceId, SyncStationApplyEnum.UPDATE_STATE);

				// 记录村点状态变化
				dispatchInstStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.CLOSING_REFUSED,
				operatorDto);*/
                CloseStationApplyDto closeStationApplyDto = closeStationApplyBO.getCloseStationApply(instanceId);
                PartnerInstanceStateEnum sourceInstanceState = closeStationApplyDto.getInstanceState();

                PartnerInstanceDto partnerInstanceDto = PartnerInstanceConverter.convert(partnerStationRel);
                partnerInstanceDto.copyOperatorDto(operatorDto);
                if (PartnerInstanceStateEnum.SERVICING.equals(sourceInstanceState)) {
                    LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(partnerInstanceDto,
                        StateMachineEvent.SERVICING_EVENT);
                    stateMachineService.executePhase(phaseEvent);
                } else if (PartnerInstanceStateEnum.DECORATING.equals(sourceInstanceState)) {
                    LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(partnerInstanceDto,
                        StateMachineEvent.DECORATING_EVENT);
                    stateMachineService.executePhase(phaseEvent);
                }

            }
        } catch (Exception e) {
            logger.error(ERROR_MSG + "monitorCloseApprove", e);
            throw e;
        }
    }

    /**
     * 更新生命周期表，流程审批结果
     *
     * @param instanceId
     * @param approveResult
     */
    private void updatePartnerLifecycle(Long instanceId, PartnerLifecycleRoleApproveEnum approveResult) {
        PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId,
            PartnerLifecycleBusinessTypeEnum.CLOSING,
            PartnerLifecycleCurrentStepEnum.PROCESSING);

        PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();

        partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
        partnerLifecycleDto.setRoleApprove(approveResult);
        partnerLifecycleDto.setPartnerInstanceId(instanceId);
        partnerLifecycleDto.copyOperatorDto(OperatorDto.defaultOperator());
        partnerLifecycleDto.setLifecycleId(items.getId());

        partnerLifecycleBO.updateLifecycle(partnerLifecycleDto);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void quitApprove(Long instanceId, ProcessApproveResultEnum approveResult) throws Exception {
        OperatorDto operatorDto = OperatorDto.defaultOperator();
        //String operator = operatorDto.getOperator();

        PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);
        if (PartnerInstanceTypeEnum.TPV.getCode().equals(instance.getType())) {
            return;
        }
        //Long stationId = instance.getStationId();

        // 校验退出申请单是否存在
        //QuitStationApply quitApply = quitStationApplyBO.findQuitStationApply(instanceId);

        // 审批通过
        if (ProcessApproveResultEnum.APPROVE_PASS.equals(approveResult)) {
            PartnerInstanceDto partnerInstanceDto = PartnerInstanceConverter.convert(instance);
            partnerInstanceDto.copyOperatorDto(operatorDto);
            Map<String, Object> extensionInfos = Maps.newHashMap();
            extensionInfos.put("fromAuditflow", true);
            LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(partnerInstanceDto,
                StateMachineEvent.QUIT_EVENT, extensionInfos);
            stateMachineService.executePhase(phaseEvent);

				/*
				// 村点已撤点
				if (quitApply.getIsQuitStation() == null || "y".equals(quitApply.getIsQuitStation())) {
					stationBO.changeState(stationId, StationStatusEnum.QUITING, StationStatusEnum.QUIT, operator);
				}

				// 处理合伙人、淘帮手、村拍档不一样的业务
				partnerInstanceHandler.handleDifferQuitAuditPass(instanceId, PartnerInstanceTypeEnum.valueof(instance
				.getType()));

				generalTaskSubmitService.submitQuitApprovedTask(instanceId, stationId, instance.getTaobaoUserId(),
						quitApply.getIsQuitStation());
			*/
        } else {

            PartnerInstanceDto partnerInstanceDto = PartnerInstanceConverter.convert(instance);
            partnerInstanceDto.copyOperatorDto(operatorDto);
            LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(partnerInstanceDto,
                StateMachineEvent.CLOSED_EVENT);
            stateMachineService.executePhase(phaseEvent);

				/*
				// 合伙人实例已停业
				partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.QUITING, PartnerInstanceStateEnum
				.CLOSED, operator);
				// 村点已停业
				if (quitApply.getIsQuitStation() == null || "y".equals(quitApply.getIsQuitStation())) {
					stationBO.changeState(stationId, StationStatusEnum.QUITING, StationStatusEnum.CLOSED, operator);
				}

				// 删除退出申请单
				quitStationApplyBO.deleteQuitStationApply(instanceId, operator);

				// 更新什么周期表
				PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId,
				PartnerLifecycleBusinessTypeEnum.QUITING,
						PartnerLifecycleCurrentStepEnum.PROCESSING);

				PartnerLifecycleDto param = new PartnerLifecycleDto();
				param.setRoleApprove(PartnerLifecycleRoleApproveEnum.AUDIT_NOPASS);
				param.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
				param.setLifecycleId(items.getId());
				partnerLifecycleBO.updateLifecycle(param);

				// 同步station_apply
				stationApplySyncBO.updateStationApply(instanceId, SyncStationApplyEnum.UPDATE_STATE);

				// 发送合伙人实例状态变化事件
				dispatchInstStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.QUITTING_REFUSED, operatorDto);
			*/
        }
    }

    /**
     * 监听任务已启动,修改生命周期表，流程中心任务已启动
     *
     * @param instanceId
     * @param businessType
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void monitorTaskStarted(Long instanceId, PartnerLifecycleBusinessTypeEnum businessType) {

        PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId, businessType,
            PartnerLifecycleCurrentStepEnum.PROCESSING);

        if (null == items || PartnerLifecycleRoleApproveEnum.TO_AUDIT.getCode().equals(items.getRoleApprove())) {
            return;
        }

        PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();

        partnerLifecycleDto.setRoleApprove(PartnerLifecycleRoleApproveEnum.TO_AUDIT);
        partnerLifecycleDto.setLifecycleId(items.getId());
        partnerLifecycleDto.copyOperatorDto(OperatorDto.defaultOperator());

        partnerLifecycleBO.updateLifecycle(partnerLifecycleDto);
    }

    private void dispatchInstStateChangeEvent(Long instanceId, PartnerInstanceStateChangeEnum stateChange,
                                              OperatorDto operator) {
        PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
        PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convertStateChangeEvent(stateChange,
            partnerInstanceDto,
            operator);
        EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
    }

    private void handlePeixunPurchase(String id, String audit, String auditName, String desc, String result) {
        peixunPurchaseBO.audit(new Long(id), audit, auditName, desc, !"拒绝".equals(result));
    }

    private void handleFlowerNameApply(String id, String result) {
        partnerBO.auditFlowerNameApply(new Long(id), ProcessApproveResultEnum.APPROVE_PASS.getCode().equals(result));
    }

    private void handlePeixunRefund(String id, String result) {
        partnerPeixunBO.refundAuditExecute(new Long(id),
            ProcessApproveResultEnum.APPROVE_PASS.getCode().equals(result));
    }

}
