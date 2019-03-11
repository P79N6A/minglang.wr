package com.taobao.cun.auge.station.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.attachment.service.AttachmentService;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.configuration.TpaGmvCheckConfiguration;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.partner.service.PartnerAssetService;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.PartnerApplyBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.PartnerPeixunBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.StationDecorateBO;
import com.taobao.cun.auge.station.condition.UnionMemberPageCondition;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.ApproveProcessTask;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.AccountMoneyStateEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceIsCurrentEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerPeixunCourseTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.PartnerInstanceExtService;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.auge.station.service.StationDecorateService;
import com.taobao.cun.auge.station.um.UnionMemberQueryService;
import com.taobao.cun.auge.station.um.dto.UnionMemberDto;
import com.taobao.cun.recruit.partner.service.PartnerQualifyApplyService;

@Component("tpsStrategy")
public class TpsStrategy  extends CommonStrategy implements PartnerInstanceStrategy{
	
	private static final Logger logger = LoggerFactory.getLogger(TpStrategy.class);
	
	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	private UnionMemberQueryService unionMemberQueryService;
	
	@Autowired
	PartnerAssetService partnerAssetService;

	@Autowired
	PartnerLifecycleBO partnerLifecycleBO;

	@Autowired
	StationBO stationBO;

	@Autowired
	PartnerBO partnerBO;

	@Autowired
	QuitStationApplyBO quitStationApplyBO;

    @Autowired
    AttachmentService criusAttachmentService;

	@Autowired
	GeneralTaskSubmitService generalTaskSubmitService;

	@Autowired
	AccountMoneyBO accountMoneyBO;

	@Autowired
	PartnerPeixunBO partnerPeixunBO;

	@Autowired
	StationDecorateBO stationDecorateBO;

	@Autowired
	AppResourceService appResourceService;
	
	
	@Autowired
    PartnerInstanceQueryService partnerInstanceQueryService;
	
	@Autowired
	StationDecorateService stationDecorateService;
	
	@Autowired
	PartnerInstanceExtService partnerInstanceExtService;

	@Autowired
	PartnerApplyBO partnerApplyBO;
	
	@Autowired
	TpaGmvCheckConfiguration tpaGmvCheckConfiguration;
	
	@Override
	public void applySettle(PartnerInstanceDto partnerInstanceDto) {
		
		
	}

	@Override
	public void applySettleNewly(PartnerInstanceDto partnerInstanceDto) {
		
		
	}

	@Override
	public void settleSuccess(PartnerInstanceSettleSuccessDto settleSuccessDto, PartnerStationRel rel) {
		
		
	}

	@Override
	public void applyQuit(QuitStationApplyDto quitDto, PartnerInstanceTypeEnum typeEnum) {
		
		
	}

	@Override
	public void handleDifferQuitAuditPass(Long partnerInstanceId) {
		
		
	}

	@Override
	public void quit(PartnerInstanceQuitDto partnerInstanceQuitDto) {
		
		
	}

	@Override
	public void delete(PartnerInstanceDeleteDto partnerInstanceDeleteDto, PartnerStationRel rel) {
		String operator = partnerInstanceDeleteDto.getOperator();
		if (PartnerInstanceIsCurrentEnum.N.getCode().equals(rel.getIsCurrent())) {//历史数据不能删除
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"历史数据不能删除");
		}
		if (!StringUtils.equals(PartnerInstanceStateEnum.TEMP.getCode(), rel.getState())
				&& !StringUtils.equals(PartnerInstanceStateEnum.SETTLE_FAIL.getCode(), rel.getState())
				&& !StringUtils.equals(PartnerInstanceStateEnum.SETTLING.getCode(), rel.getState())) {
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE, "当前状态合伙人信息不能删除");
		}
		// 保证金已经结不能删除
		if (isBondHasFrozen(rel.getId())) {
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"保证金已经结不能删除");
		}
		//该村点只有当前合伙人，直接删除,如果有其他的合伙人（不管是什么状态，都置为已停业）
		Long stationId = rel.getStationId();
		Station station = stationBO.getStationById(stationId);
		if (!StringUtils.equals(StationStatusEnum.TEMP.getCode(), station.getStatus())
				&& !StringUtils.equals(StationStatusEnum.INVALID.getCode(), station.getStatus())
				&& !StringUtils.equals(StationStatusEnum.NEW.getCode(), station.getStatus())) {
			throw new AugeBusinessException(AugeErrorCodes.STATION_BUSINESS_CHECK_ERROR_CODE,"当前状态的服务站信息不能删除");
		}
		
		List<PartnerStationRel> sList = partnerInstanceBO.findPartnerInstances(stationId);
		if (sList != null && sList.size()>1) {
			stationBO.changeState(stationId, StationStatusEnum.valueof(station.getStatus()), StationStatusEnum.CLOSED, operator);
			
			updateIsCurrentForPreInstance(rel, sList);
			
		}else {
			stationBO.deleteStation(stationId, partnerInstanceDeleteDto.getOperator());
		}
		//删除装修记录
		stationDecorateBO.invalidStationDecorate(stationId);
		//删除启航班培训记录和成长营培训记录
		partnerPeixunBO.invalidPeixunRecord(rel.getTaobaoUserId(),
				PartnerPeixunCourseTypeEnum.APPLY_IN, appResourceService
						.queryAppResourceValue("PARTNER_PEIXUN_CODE",
								"APPLY_IN"));
		partnerPeixunBO.invalidPeixunRecord(rel.getTaobaoUserId(),
				PartnerPeixunCourseTypeEnum.UPGRADE, appResourceService
						.queryAppResourceValue("PARTNER_PEIXUN_CODE",
								"UPGRADE"));
		partnerInstanceBO.deletePartnerStationRel(rel.getId(), operator);
		partnerLifecycleBO.deleteLifecycleItems(rel.getId(), operator);
	}
	
	/**
	 * 固点的村点，如果当前人删除，把上一个合伙人设置为当前人（上一个合伙人入驻其他村点，不设置）
	 * @param rel
	 * @param sList
	 */
	private void updateIsCurrentForPreInstance(PartnerStationRel rel,
			List<PartnerStationRel> sList) {
		Long preTaobaoUserId = null;
		Long preInstanceId = null;
		for (PartnerStationRel r : sList) {
			if (r.getId().longValue() != rel.getId().longValue()) {
				preTaobaoUserId = r.getTaobaoUserId();
				preInstanceId = r.getId();
				break;
			}
		}
		
		PartnerStationRel preCurRel = partnerInstanceBO.getCurrentPartnerInstanceByTaobaoUserId(preTaobaoUserId);
		if (preCurRel == null) {//没有入驻其他服务站
			partnerInstanceBO.updateIsCurrentByInstanceId(preInstanceId,PartnerInstanceIsCurrentEnum.Y);
			//还原station信息为上一个合伙人信息
			setStationToPre(preTaobaoUserId);
			
		}
	}
	
    private void  setStationToPre(Long preTaobaoUserId) {
    	PartnerInstanceDto psl = partnerInstanceQueryService.getActivePartnerInstance(preTaobaoUserId);
    	if (psl != null) {
    		PartnerDto pDto = psl.getPartnerDto();
    		StationDto stationDto = psl.getStationDto();
    		stationDto.copyOperatorDto(OperatorDto.defaultOperator());
    		stationDto.setTaobaoNick(pDto.getTaobaoNick());
    		stationDto.setTaobaoUserId(pDto.getTaobaoUserId());
    		stationDto.setState(StationStateEnum.NORMAL);
    		stationDto.setAlipayAccount(pDto.getAlipayAccount());
    		stationBO.updateStation(stationDto);
    	}
    }
	private boolean isBondHasFrozen(Long id) {
		AccountMoneyDto accountMoneyDto = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND,
				AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, id);
		if (null == accountMoneyDto || !AccountMoneyStateEnum.HAS_FROZEN.equals(accountMoneyDto.getState())) {
			return false;
		}
		return true;
	}

	@Override
	public void validateExistChildrenForQuit(PartnerStationRel instance) {
		//人村分离后，老合伙人不再校验淘帮手的状态
		if (PartnerInstanceIsCurrentEnum.N.getCode().equals(instance.getIsCurrent())){
			return;
		}
		List<PartnerInstanceStateEnum> states = PartnerInstanceStateEnum.getPartnerStatusForValidateQuit();
		List<PartnerStationRel> children = partnerInstanceBO.findChildPartners(instance.getId(), states);

		if (CollectionUtils.isEmpty(children)) {
			return;
		}
		for (PartnerStationRel rel : children) {
			if (!StringUtils.equals(PartnerInstanceStateEnum.QUITING.getCode(), rel.getState())) {
				logger.warn("合伙人存在淘帮手");
				throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"该合伙人下存在未退出的淘帮手，请先将其淘帮手退出后，才可以退出合伙人");
			} else {
				PartnerLifecycleItems item = partnerLifecycleBO.getLifecycleItems(rel.getId(), PartnerLifecycleBusinessTypeEnum.QUITING);
				if (null != item && StringUtils.equals(PartnerLifecycleCurrentStepEnum.PROCESSING.getCode(),
						item.getCurrentStep())) {
					if (PartnerLifecycleBondEnum.WAIT_THAW.getCode().equals(item.getBond()) && PartnerLifecycleRoleApproveEnum.AUDIT_PASS.getCode().equals(item.getRoleApprove())) {
						continue;
					}
					logger.warn("合伙人存在淘帮手");
					throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"该合伙人下存在未退出的淘帮手，请先将其淘帮手退出后，才可以退出合伙人");
				}
			}
		}
		
//		UnionMemberPageCondition  con = new UnionMemberPageCondition();
//		con.setOperator(com.taobao.cun.auge.station.enums.OperatorTypeEnum.SYSTEM.getCode());
//		con.setOperatorType(com.taobao.cun.auge.station.enums.OperatorTypeEnum.SYSTEM);
//		con.setParentStationId(instance.getStationId());
//		con.setPageNum(1);
//		con.setPageSize(10);
//		PageDto<UnionMemberDto> umList = unionMemberQueryService.queryByPage(con);
//		if (CollectionUtils.isNotEmpty(umList.getItems())) {
//			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"该村小二下存在优盟，请先删除优盟，才可以停业。");
//		}
	}

	@Override
	public void validateClosePreCondition(PartnerStationRel partnerStationRel) {
		List<PartnerInstanceStateEnum> states = PartnerInstanceStateEnum.getPartnerStatusForValidateClose();
		List<PartnerStationRel> children = partnerInstanceBO.findChildPartners(partnerStationRel.getId(), states);
		
		if (CollectionUtils.isNotEmpty(children)) {
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"该村小二下存在未停业的淘帮手，请先将其淘帮手停业后，才可以停业。");
		}
//		UnionMemberPageCondition  con = new UnionMemberPageCondition();
//		con.setOperator(com.taobao.cun.auge.station.enums.OperatorTypeEnum.SYSTEM.getCode());
//		con.setOperatorType(com.taobao.cun.auge.station.enums.OperatorTypeEnum.SYSTEM);
//		con.setParentStationId(partnerStationRel.getStationId());
//		con.setPageNum(1);
//		con.setPageSize(10);
//		PageDto<UnionMemberDto> umList = unionMemberQueryService.queryByPage(con);
//		if (CollectionUtils.isNotEmpty(umList.getItems())) {
//			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"该村小二下存在优盟，请先删除优盟，才可以停业。");
//		}
		
		//如果是从装修中停业，则需要判断村点是否退出了装修
		if (PartnerInstanceStateEnum.DECORATING.getCode().equals(partnerStationRel.getState())) {
				stationDecorateService.judgeDecorateQuit(partnerStationRel.getStationId());
		}
		//判断培训课程否是已经退款或签到
		partnerPeixunBO.validateQuitable(partnerStationRel.getTaobaoUserId());
	}

	@Override
	public Boolean validateUpdateSettle(Long instanceId) {
		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.SETTLING,
				PartnerLifecycleCurrentStepEnum.PROCESSING);
		if (items != null) {
			return true;
		}
		return false;
	}

	@Override
	public void startClosing(Long instanceId, String stationName, OperatorDto operatorDto) {
		PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);
		Station station = stationBO.getStationById(instance.getStationId());
		Long applyId = findCloseApplyId(instanceId);
		
		ApproveProcessTask processTask = new ApproveProcessTask();
		processTask.setBusiness(ProcessBusinessEnum.stationForcedClosure);
		processTask.setBusinessId(instanceId);
		processTask.setBusinessName(stationName);
		processTask.setBusinessOrgId(station.getApplyOrg());
		processTask.copyOperatorDto(operatorDto);
		Map<String, String> params = new HashMap<String, String>();
		params.put("applyId", String.valueOf(applyId));
		params.put("isInstanceId", "true");
		
		processTask.setParams(params);
		generalTaskSubmitService.submitApproveProcessTask(processTask);
	}

	@Override
	public void startQuiting(Long instanceId, String stationName, OperatorDto operatorDto) {
		PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);
		Station station = stationBO.getStationById(instance.getStationId());
		Long applyId = findQuitApplyId(instanceId);
		
		ApproveProcessTask processTask = new ApproveProcessTask();
		processTask.setBusiness(ProcessBusinessEnum.stationQuitRecord);
		processTask.setBusinessId(instanceId);
		processTask.setBusinessName(stationName);
		processTask.setBusinessOrgId(station.getApplyOrg());
		processTask.copyOperatorDto(operatorDto);
		Map<String, String> params = new HashMap<String, String>();
		params.put("applyId", String.valueOf(applyId));
		params.put("isInstanceId", "true");
		
		processTask.setParams(params);
		generalTaskSubmitService.submitApproveProcessTask(processTask);
	}

	@Override
	public void validateAssetBack(Long instanceId) {
		boolean isBackAsset = partnerAssetService.isBackAsset(instanceId);
		if(!isBackAsset){
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"3件资产尚未回收，请用小二APP回收资产。");
		}
	}

	@Override
	public void validateOtherPartnerQuit(Long instanceId) {
		boolean isOtherPartnerQuit = partnerInstanceQueryService.isOtherPartnerQuit(instanceId);
		if(!isOtherPartnerQuit){
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"村点上存在未退出的合伙人，不能撤点。");
		}
		
	}

	@Override
	public void startService(Long instanceId, Long taobaoUserId, OperatorDto operatorDto) {
		
		
	}

}
