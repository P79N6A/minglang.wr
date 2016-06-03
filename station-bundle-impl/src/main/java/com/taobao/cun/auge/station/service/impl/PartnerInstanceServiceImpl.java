package com.taobao.cun.auge.station.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.StationApplySyncEvent;
import com.taobao.cun.auge.event.domain.EventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.station.adapter.PaymentAccountQueryAdapter;
import com.taobao.cun.auge.station.adapter.TradeAdapter;
import com.taobao.cun.auge.station.adapter.UicReadAdapter;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.AttachementBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.bo.ProtocolBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.convert.QuitStationApplyConverter;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.ConfirmCloseDto;
import com.taobao.cun.auge.station.dto.ForcedCloseDto;
import com.taobao.cun.auge.station.dto.OpenStationDto;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.dto.PaymentAccountDto;
import com.taobao.cun.auge.station.dto.QuitDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.AccountMoneyStateEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerForcedCloseReasonEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceIsCurrentEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleConfirmEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleQuitProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.enums.ProtocolTargetBizTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.PartnerExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.cun.crius.event.client.EventDispatcher;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * 
 * 合伙人实例服务接口
 * @author quanzhu.wangqz
 *
 */
@HSFProvider(serviceInterface = PartnerInstanceService.class)
public class PartnerInstanceServiceImpl implements PartnerInstanceService {
	
	private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceService.class);
	public static final String RULE_REGEX = "^[0-9A-Z]+$";

	@Autowired
	ProtocolBO protocolBO;
	
	@Autowired
	PartnerProtocolRelBO partnerProtocolRelBO;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	PartnerInstanceHandler partnerInstanceHandler;

	@Autowired
	PartnerLifecycleBO partnerLifecycleBO;

	@Autowired
	StationBO stationBO;

	@Autowired
	QuitStationApplyBO quitStationApplyBO;

	@Autowired
	StationApplyBO stationApplyBO;

	@Autowired
	Emp360Adapter emp360Adapter;

	@Autowired
	UicReadAdapter uicReadAdapter;

	@Autowired
	PartnerBO partnerBO;

	@Autowired
	TradeAdapter tradeAdapter;
	
	@Autowired
	AttachementBO attachementBO;
	
	@Autowired
	PaymentAccountQueryAdapter paymentAccountQueryAdapter;
	
	@Autowired
	AccountMoneyBO accountMoneyBO;
	
	
	@Override
	public Long saveTemp(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		try {
			ValidateUtils.notNull(partnerInstanceDto);
			Long instanceId = partnerInstanceDto.getId();
			if (instanceId == null) {// 新增
				instanceId = addTemp(partnerInstanceDto);
			} else {// 修改
				updateTemp(partnerInstanceDto, instanceId);
			}
			
			// 同步station_apply
						syncStationApply(SyncStationApplyEnum.ADD, instanceId);
						
			return instanceId;
		} catch (AugeServiceException augeException) {
			String error = getErrorMessage("saveTemp", JSONObject.toJSONString(partnerInstanceDto),augeException.toString());
			logger.error(error,augeException);
			throw augeException;
		}catch (Exception e) {
			String error = getErrorMessage("saveTemp", JSONObject.toJSONString(partnerInstanceDto),e.getMessage());
			logger.error(error,e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	private void updateTemp(PartnerInstanceDto partnerInstanceDto,
			Long instanceId) {
		ValidateUtils.notNull(partnerInstanceDto.getStationDto());
		ValidateUtils.notNull(partnerInstanceDto.getPartnerDto());
		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
		Long stationId = rel.getStationId();
		Long partnerId = rel.getPartnerId();
		
		StationDto stationDto = partnerInstanceDto.getStationDto();
		//判断服务站编号是否使用中
		checkStationNumDuplicate(stationId,stationDto.getStationNum());
		stationDto.setId(stationId);
		stationDto.setState(StationStateEnum.INVALID);
		stationDto.setStatus(StationStatusEnum.TEMP);
		stationBO.updateStation(stationDto);
		attachementBO.modifyAttachementBatch(partnerInstanceDto.getStationDto().getAttachements(), stationId, AttachementBizTypeEnum.CRIUS_STATION);
		
		PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
		partnerDto.setId(partnerId);
		partnerDto.setState(PartnerStateEnum.TEMP);
		partnerBO.updatePartner(partnerInstanceDto.getPartnerDto());
		attachementBO.modifyAttachementBatch(partnerInstanceDto.getStationDto().getAttachements(), partnerId, AttachementBizTypeEnum.PARTNER);
		
		partnerInstanceDto.setStationId(stationId);
		partnerInstanceDto.setPartnerId(partnerId);
		partnerInstanceDto.setState(PartnerInstanceStateEnum.TEMP);
		partnerInstanceDto.setIsCurrent(PartnerInstanceIsCurrentEnum.Y);
		partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);
	}

	private Long addTemp(PartnerInstanceDto partnerInstanceDto) {
		Long instanceId;
		StationDto stationDto = partnerInstanceDto.getStationDto();
		//判断服务站编号是否使用中
		checkStationNumDuplicate(null,stationDto.getStationNum());
		
		stationDto.setState(StationStateEnum.INVALID);
		stationDto.setStatus(StationStatusEnum.TEMP);
		Long stationId = stationBO.addStation(stationDto);
		attachementBO.addAttachementBatch(stationDto.getAttachements(), stationId, AttachementBizTypeEnum.CRIUS_STATION);
		
		PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
		partnerDto.setState(PartnerStateEnum.TEMP);
		Long partnerId = partnerBO.addPartner(partnerDto);
		attachementBO.addAttachementBatch(partnerDto.getAttachements(), partnerId, AttachementBizTypeEnum.PARTNER);
		
		partnerInstanceDto.setStationId(stationId);
		partnerInstanceDto.setPartnerId(partnerId);
		partnerInstanceDto.setState(PartnerInstanceStateEnum.TEMP);
		partnerInstanceDto.setIsCurrent(PartnerInstanceIsCurrentEnum.Y);
		instanceId = partnerInstanceBO.addPartnerStationRel(partnerInstanceDto);
		return instanceId;
	}
	
	private String getErrorMessage(String methodName,String param,String error) {
		StringBuilder sb = new StringBuilder();
		sb.append("PartnerInstanceService-Error|").append(methodName).append("(.param=").append(param).append(").").append("errorMessage:").append(error);
		return sb.toString();
	}
	

	private Long addSubmit(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		try {
			ValidateUtils.notNull(partnerInstanceDto);
			Long taobaoUserId = validateSettlable(partnerInstanceDto);
			StationDto stationDto = partnerInstanceDto.getStationDto();
			//判断服务站编号是否使用中
			checkStationNumDuplicate(null,stationDto.getStationNum());
			
			stationDto.setState(StationStateEnum.INVALID);
			stationDto.setStatus(StationStatusEnum.NEW);
			Long stationId = stationBO.addStation(stationDto);
			attachementBO.addAttachementBatch(stationDto.getAttachements(), stationId, AttachementBizTypeEnum.CRIUS_STATION);
			
			PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
			partnerDto.setState(PartnerStateEnum.TEMP);
			partnerDto.setTaobaoUserId(taobaoUserId);
			Long partnerId = partnerBO.addPartner(partnerDto);
			attachementBO.addAttachementBatch(partnerDto.getAttachements(), partnerId, AttachementBizTypeEnum.PARTNER);
			
			partnerInstanceDto.setStationId(stationId);
			partnerInstanceDto.setPartnerId(partnerId);
			partnerInstanceDto.setState(PartnerInstanceStateEnum.SETTLING);
			partnerInstanceDto.setIsCurrent(PartnerInstanceIsCurrentEnum.Y);
			Long instanceId = partnerInstanceBO.addPartnerStationRel(partnerInstanceDto);
		    
			return instanceId;
		} catch (AugeServiceException augeException) {
			String error = getErrorMessage("addSubmit", JSONObject.toJSONString(partnerInstanceDto),augeException.toString());
			logger.error(error,augeException);
			throw augeException;
		}catch (Exception e) {
			String error = getErrorMessage("addSubmit", JSONObject.toJSONString(partnerInstanceDto),e.getMessage());
			logger.error(error,e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}
	
	
	public static boolean isSpecialStr(String str) {
        Pattern pat = Pattern.compile(RULE_REGEX);
        Matcher mat = pat.matcher(str);
        if (mat.find()) {
            return false;
        } else {
            return true;
        }
    }    
	
	private Long validateSettlable(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		ValidateUtils.notNull(partnerInstanceDto);
		StationDto stationDto = partnerInstanceDto.getStationDto();
		PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
		ValidateUtils.notNull(stationDto);
		
		if(StringUtils.isBlank(stationDto.getName())){
			throw new AugeServiceException(StationExceptionEnum.STATION_NAME_IS_NULL);
		}
		Address address = stationDto.getAddress();
		if(address==null){
			throw new AugeServiceException(StationExceptionEnum.STATION_ADDRESS_IS_NULL);
		}
		String stationName = "";
		if (StringUtils.isNotBlank(address.getCountyDetail())){
			stationName+=address.getCountyDetail();
		}
		stationName+=stationDto.getName();
		try {
			if(stationName.getBytes("UTF-8").length>64){
                throw new AugeServiceException(StationExceptionEnum.CAINIAO_STATION_NAME_TOO_LENGTH);
            }
		} catch (UnsupportedEncodingException e) {
            logger.error("validate:",e);
		}
		
		String stationNum = stationDto.getStationNum();
		if (StringUtils.isEmpty(stationNum)) {
			   throw new AugeServiceException(StationExceptionEnum.STATION_NUM_IS_NULL);
        }

        if (stationNum.length() > 16) {
        	 throw new AugeServiceException(StationExceptionEnum.STATION_NUM_TOO_LENGTH);
        }

        if (isSpecialStr(stationNum)) {
        	 throw new AugeServiceException(StationExceptionEnum.STATION_NUM_ILLEGAL);
        }

		
		if(StringUtils.isBlank(partnerDto.getTaobaoNick())){
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_TAOBAONICK_IS_NULL);
		}
		if(StringUtils.isBlank(partnerDto.getAlipayAccount())){
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_ALIPAYACCOUNT_IS_NULL);
		}
		if(StringUtils.isBlank(partnerDto.getIdenNum())){
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_IDENNUM_IS_NULL);
		}
		if(StringUtils.isBlank(partnerDto.getName())){
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_NAME_IS_NULL);
		}
		
		OperatorDto operator = new OperatorDto();
		operator.copyOperatorDto(partnerInstanceDto);
		PaymentAccountDto  paDto =  paymentAccountQueryAdapter.queryPaymentAccountByNick(partnerDto.getTaobaoNick(),operator);
		if(!partnerDto.getAlipayAccount().equals(paDto.getAlipayId())){
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_ALIPAYACCOUNT_NOTEQUAL);
		}
		if(!partnerDto.getName().equals(paDto.getFullName()) || !partnerDto.getIdenNum().equals(paDto.getIdCardNumber())){
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_PERSION_INFO_NOTEQUAL);
		}
		
		//判断淘宝账号是否使用中
		PartnerStationRel existPartnerInstance = partnerInstanceBO.getActivePartnerInstance(paDto.getTaobaoUserId());
		if (null != existPartnerInstance) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_TAOBAOUSERID_HAS_USED);
		}
		return paDto.getTaobaoUserId();
	}
	
	private void checkStationNumDuplicate(Long stationId,String newStationNum) {
		//判断服务站编号是否使用中
		String oldStationNum = null;
		if (stationId != null) {
			Station oldStation = stationBO.getStationById(stationId);
			oldStationNum = oldStation.getStationNum();
		}
		int count = stationBO.getStationCountByStationNum(newStationNum);
		if (StringUtils.equals(oldStationNum, newStationNum)) {
			if (count > 1) {
				throw new AugeServiceException(StationExceptionEnum.STATION_NUM_IS_DUPLICATE);
			}
		}else {
			if (count > 0) {
				throw new AugeServiceException(StationExceptionEnum.STATION_NUM_IS_DUPLICATE);
			}
		}
	}

	private Long updateSubmit(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		try {
			ValidateUtils.notNull(partnerInstanceDto);
			ValidateUtils.notNull(partnerInstanceDto.getId());
			Long taobaoUserId = validateSettlable(partnerInstanceDto);
			Long instanceId = partnerInstanceDto.getId();
			
			PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
			Long stationId = rel.getStationId();
			Long partnerId = rel.getPartnerId();
			
			StationDto stationDto = partnerInstanceDto.getStationDto();
			//判断服务站编号是否使用中
			checkStationNumDuplicate(stationId,stationDto.getStationNum());
			
			stationDto.setId(stationId);
			stationDto.setState(StationStateEnum.INVALID);
			stationDto.setStatus(StationStatusEnum.NEW);
			stationBO.updateStation(stationDto);
			attachementBO.modifyAttachementBatch(partnerInstanceDto.getStationDto().getAttachements(), 
					stationId, AttachementBizTypeEnum.CRIUS_STATION);
			
			PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
			partnerDto.setId(partnerId);
			partnerDto.setState(PartnerStateEnum.TEMP);
			partnerDto.setTaobaoUserId(taobaoUserId);
			partnerBO.updatePartner(partnerDto);
			attachementBO.modifyAttachementBatch(partnerInstanceDto.getStationDto().getAttachements(), 
					partnerId, AttachementBizTypeEnum.PARTNER);
			
			partnerInstanceDto.setStationId(stationId);
			partnerInstanceDto.setPartnerId(partnerId);
			partnerInstanceDto.setState(PartnerInstanceStateEnum.SETTLING);
			partnerInstanceDto.setIsCurrent(PartnerInstanceIsCurrentEnum.Y);
			partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);
			
			return instanceId;
		} catch (AugeServiceException augeException) {
			String error = getErrorMessage("updateSubmit", JSONObject.toJSONString(partnerInstanceDto),augeException.toString());
			logger.error(error,augeException);
			throw augeException;
		}catch (Exception e) {
			String error = getErrorMessage("updateSubmit", JSONObject.toJSONString(partnerInstanceDto),e.getMessage());
			logger.error(error,e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	@Override
	public boolean update(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void delete(PartnerInstanceDeleteDto partnerInstanceDeleteDto) throws AugeServiceException {
		ValidateUtils.notNull(partnerInstanceDeleteDto);
		Long instanceId = partnerInstanceDeleteDto.getInstanceId();
		ValidateUtils.notNull(instanceId);
		
		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
		if (rel==null || StringUtils.isEmpty(rel.getType())) {
			throw new AugeServiceException(CommonExceptionEnum.RECORD_IS_NULL);
		}
		partnerInstanceHandler.handleDelete(partnerInstanceDeleteDto, rel);
	}

	@Override
	public void signSettledProtocol(Long taobaoUserId, Double waitFrozenMoney) throws AugeServiceException {
		ValidateUtils.notNull(taobaoUserId);
		ValidateUtils.notNull(waitFrozenMoney);
		try {
			Long instanceId = partnerInstanceBO.getInstanceIdByTaobaoUserId(taobaoUserId, PartnerInstanceStateEnum.SETTLING);
			partnerProtocolRelBO.signProtocol(taobaoUserId, ProtocolTypeEnum.SETTLE_PRO, instanceId,
					ProtocolTargetBizTypeEnum.PARTNER_INSTANCE);
			
			addWaitFrozenMoney(instanceId,taobaoUserId,waitFrozenMoney);
			
			PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId,
					PartnerLifecycleBusinessTypeEnum.SETTLING, PartnerLifecycleCurrentStepEnum.SETTLED_PROTOCOL);
			if (items != null) {
				PartnerLifecycleDto param = new PartnerLifecycleDto();
				param.setBond(PartnerLifecycleBondEnum.WAIT_FROZEN);
				param.setCurrentStep(PartnerLifecycleCurrentStepEnum.BOND);
				param.setLifecycleId(items.getId());
				partnerLifecycleBO.updateLifecycle(param);
			}
			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instanceId);
		} catch (AugeServiceException augeException) {
			String error = getErrorMessage("signSettledProtocol", String.valueOf(taobaoUserId),augeException.toString());
			logger.error(error,augeException);
			throw augeException;
		}catch (Exception e) {
			String error = getErrorMessage("signSettledProtocol", String.valueOf(taobaoUserId),e.getMessage());
			logger.error(error,e);
			throw new AugeServiceException(StationExceptionEnum.SYSTEM_ERROR);
		}
	}
	
	private void addWaitFrozenMoney(Long instanceId,Long taobaoUserId,Double waitFrozenMoney) {
		AccountMoneyDto accountMoneyDto = new AccountMoneyDto();
		accountMoneyDto.setMoney(BigDecimal.valueOf(waitFrozenMoney));
		accountMoneyDto.setOperator(String.valueOf(taobaoUserId));
		accountMoneyDto.setObjectId(instanceId);
		accountMoneyDto.setState(AccountMoneyStateEnum.WAIT_FROZEN);
		accountMoneyDto.setTaobaoUserId(String.valueOf(taobaoUserId));
		accountMoneyDto.setTargetType(AccountMoneyTargetTypeEnum.PARTNER_INSTANCE);
		accountMoneyDto.setType(AccountMoneyTypeEnum.PARTNER_BOND);
		accountMoneyBO.addAccountMoney(accountMoneyDto);
	}

	@Override
	public void signManageProtocol(Long taobaoUserId) throws AugeServiceException {
		ValidateUtils.notNull(taobaoUserId);
		try {
			PartnerStationRel partnerStationRel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
			
			if (partnerStationRel == null) {
				throw new AugeServiceException(StationExceptionEnum.SIGN_MANAGE_PROTOCOL_FAIL);
			}
			partnerProtocolRelBO.signProtocol(taobaoUserId, ProtocolTypeEnum.MANAGE_PRO, partnerStationRel.getId(),
					ProtocolTargetBizTypeEnum.PARTNER_INSTANCE);
			
			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_ALL, partnerStationRel.getId());
		}catch (AugeServiceException augeException) {
			String error = getErrorMessage("signManageProtocol", String.valueOf(taobaoUserId),augeException.toString());
			logger.error(error,augeException);
			throw augeException;
		}catch (Exception e) {
			String error = getErrorMessage("signManageProtocol", String.valueOf(taobaoUserId),e.getMessage());
			logger.error(error,e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	@Override
	public boolean freezeBond(Long taobaoUserId, BigDecimal frozenMoney) throws AugeServiceException {
		
		
		// 同步station_apply
//		syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instanceId);
		return false;
	}

	@Override
	public boolean openStation(OpenStationDto openStationDto) throws AugeServiceException {
		ValidateUtils.validateParam(openStationDto);
		ValidateUtils.notNull(openStationDto.getPartnerInstanceId());
		ValidateUtils.notNull(openStationDto.getOpenDate());
		ValidateUtils.notNull(openStationDto.isImme());
		if (openStationDto.isImme()) {// 立即开业
			// TODO:检查开业包
			if (!checkKyPackage()) {
				throw new AugeServiceException(StationExceptionEnum.KAYE_PACKAGE_NOT_EXIST);
			}
			partnerInstanceBO.changeState(openStationDto.getPartnerInstanceId(), PartnerInstanceStateEnum.DECORATING,
					PartnerInstanceStateEnum.SERVICING, openStationDto.getOperator());
			partnerInstanceBO.updateOpenDate(openStationDto.getPartnerInstanceId(), openStationDto.getOpenDate(),
					openStationDto.getOperator());
			// 记录村点状态变化
			EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,
					PartnerInstanceEventConverter.convert(PartnerInstanceStateChangeEnum.START_SERVICING,
							partnerInstanceBO.getPartnerInstanceById(openStationDto.getPartnerInstanceId()),
							openStationDto));
		} else {// 定时开业
			partnerInstanceBO.updateOpenDate(openStationDto.getPartnerInstanceId(), openStationDto.getOpenDate(),
					openStationDto.getOperator());
		}
		// 同步station_apply
		syncStationApply(SyncStationApplyEnum.UPDATE_BASE, openStationDto.getPartnerInstanceId());

		return false;
	}

	@Override
	public boolean checkKyPackage() {
		// TODO:检查开业包
		return true;
	}

	@Override
	public boolean applyCloseByPartner(Long taobaoUserId) throws AugeServiceException {
		try {
			PartnerStationRel partnerInstance = partnerInstanceBO.getPartnerInstanceByTaobaoUserId(taobaoUserId,
					PartnerInstanceStateEnum.SERVICING);
			if (partnerInstance == null) {
				throw new AugeServiceException(PartnerExceptionEnum.NO_RECORD);
			}
			partnerInstanceBO.changeState(partnerInstance.getId(), PartnerInstanceStateEnum.SERVICING,
					PartnerInstanceStateEnum.CLOSING, String.valueOf(taobaoUserId));
			stationBO.changeState(partnerInstance.getId(), StationStatusEnum.SERVICING, StationStatusEnum.CLOSING,
					String.valueOf(taobaoUserId));
			// 插入生命周期扩展表
			PartnerLifecycleDto partnerLifecycle = new PartnerLifecycleDto();
			partnerLifecycle.setPartnerType(PartnerInstanceTypeEnum.valueof(partnerInstance.getType()));
			partnerLifecycle.setBusinessType(PartnerLifecycleBusinessTypeEnum.CLOSING);
			partnerLifecycle.setConfirm(PartnerLifecycleConfirmEnum.WAIT_CONFIRM);
			partnerLifecycle.setQuitProtocol(PartnerLifecycleQuitProtocolEnum.SIGNED);
			partnerLifecycle.setCurrentStep(PartnerLifecycleCurrentStepEnum.CONFIRM);
			partnerLifecycle.setPartnerInstanceId(partnerInstance.getId());
			partnerLifecycleBO.addLifecycle(partnerLifecycle);
			// TODO:插入停业协议
			EventDispatcher.getInstance().dispatch("xxxxx", partnerLifecycle);
			// TODO:发送状态换砖 事件，接受事件里 1记录OPLOG日志 2短信推送 3 状态转换日志
			
			OperatorDto operator = new OperatorDto();
			operator.setOperator(String.valueOf(taobaoUserId));
			operator.setOperatorType(OperatorTypeEnum.HAVANA);

			PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convert(
					PartnerInstanceStateChangeEnum.START_CLOSING,
					partnerInstanceBO.getPartnerInstanceById(partnerInstance.getId()), operator);
			EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
			
			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_ALL, partnerInstance.getId());

			return true;
		} catch (AugeServiceException augeException) {
			String error = getErrorMessage("applyCloseByPartner", String.valueOf(taobaoUserId),augeException.toString());
			logger.error(error,augeException);
			throw augeException;
		}catch (Exception e) {
			String error = getErrorMessage("applyCloseByPartner", String.valueOf(taobaoUserId),e.getMessage());
			logger.error(error,e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	@Override
	public boolean confirmClose(ConfirmCloseDto confirmCloseDto) throws AugeServiceException {
		ValidateUtils.validateParam(confirmCloseDto);
		Long partnerInstanceId = confirmCloseDto.getPartnerInstanceId();
		String employeeId = confirmCloseDto.getOperator();
		boolean isAgree = confirmCloseDto.isAgree();
		try {

			PartnerStationRel partnerInstance = partnerInstanceBO
					.findPartnerInstanceById(confirmCloseDto.getPartnerInstanceId());
			if (partnerInstance == null) {
				throw new AugeServiceException(PartnerExceptionEnum.NO_RECORD);
			}
			// 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
			partnerInstanceHandler.validateExistValidChildren(
					PartnerInstanceTypeEnum.valueof(partnerInstance.getType()), partnerInstanceId);

			Long lifecycleId = partnerLifecycleBO.getLifecycleItemsId(partnerInstance.getId(),
					PartnerLifecycleBusinessTypeEnum.CLOSING, PartnerLifecycleCurrentStepEnum.CONFIRM);
			PartnerLifecycleDto partnerLifecycle = new PartnerLifecycleDto();
			partnerLifecycle.setLifecycleId(lifecycleId);
			partnerLifecycle.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);

			if (isAgree) {
				partnerInstanceBO.changeState(partnerInstanceId, PartnerInstanceStateEnum.CLOSING,
						PartnerInstanceStateEnum.CLOSED, employeeId);
				// 更新服务结束时间
				// 更新服务结束时间
				PartnerInstanceDto instance = new PartnerInstanceDto();
				instance.setServiceEndTime(new Date());
				instance.setId(partnerInstanceId);
				instance.setOperator(employeeId);
				partnerInstanceBO.updatePartnerStationRel(instance);

				stationBO.changeState(partnerInstance.getId(), StationStatusEnum.CLOSING, StationStatusEnum.CLOSED,
						employeeId);
				partnerLifecycle.setConfirm(PartnerLifecycleConfirmEnum.CONFIRM);
				
				PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convert(PartnerInstanceStateChangeEnum.CLOSED,
						partnerInstanceBO.getPartnerInstanceById(partnerInstanceId), confirmCloseDto);
				EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,event);
			} else {
				partnerInstanceBO.changeState(partnerInstanceId, PartnerInstanceStateEnum.CLOSING,
						PartnerInstanceStateEnum.SERVICING, employeeId);
				stationBO.changeState(partnerInstanceId, StationStatusEnum.CLOSING, StationStatusEnum.SERVICING,
						employeeId);
				partnerLifecycle.setConfirm(PartnerLifecycleConfirmEnum.CANCEL);
				
				PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convert(PartnerInstanceStateChangeEnum.CLOSING_REFUSED,
						partnerInstanceBO.getPartnerInstanceById(partnerInstanceId), confirmCloseDto);
				EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,event);
			}
			partnerLifecycleBO.updateLifecycle(partnerLifecycle);
			// TODO:发送状态换砖 事件，接受事件里 1记录OPLOG日志 2短信推送 3 状态转换日志 4,去标
			
			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_BASE, partnerInstance.getId());			return true;
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("partnerInstanceId:").append(partnerInstanceId).append(" employeeId:").append(employeeId)
					.append(" isAgree:").append(isAgree);
			logger.error("confirmClose.error.param:" + sb.toString(), e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	@Override
	public void applyCloseByManager(ForcedCloseDto forcedCloseDto) throws AugeServiceException {
		// 参数校验
		BeanValidator.validateWithThrowable(forcedCloseDto);

		Long instanceId = forcedCloseDto.getInstanceId();
		PartnerStationRel partnerStationRel = partnerInstanceBO.findPartnerInstanceById(instanceId);
		Long stationId = partnerStationRel.getStationId();

		// 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
		partnerInstanceHandler.validateExistValidChildren(PartnerInstanceTypeEnum.valueof(partnerStationRel.getType()),
				instanceId);

		// 合伙人实例停业中
		partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.SERVICING, PartnerInstanceStateEnum.CLOSING,
				forcedCloseDto.getOperator());

		// 村点停业中
		stationBO.changeState(stationId, StationStatusEnum.SERVICING, StationStatusEnum.CLOSING,
				forcedCloseDto.getOperator());

		// 通过事件，定时钟，启动停业流程
		PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convert(
				PartnerInstanceStateChangeEnum.START_CLOSING, partnerInstanceBO.getPartnerInstanceById(instanceId),
				forcedCloseDto);
		
		event.setRemark(PartnerForcedCloseReasonEnum.OTHER.equals(forcedCloseDto.getReason())
				? forcedCloseDto.getRemarks() : forcedCloseDto.getReason().getDesc());

		EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
		
		
		// 同步station_apply
		syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);

		// tair
	}

	@Override
	public void applyQuitByManager(QuitDto quitDto) throws AugeServiceException {
		// 参数校验
		BeanValidator.validateWithThrowable(quitDto);

		Long instanceId = quitDto.getInstanceId();
		String operator = quitDto.getOperator();

		PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);
		Partner partner = partnerBO.getPartnerById(instance.getPartnerId());

		// 校验申请退出的条件
		validateQuitPreCondition(instance, partner);

		// 保存退出申请单
		QuitStationApply quitStationApply = QuitStationApplyConverter.convert(quitDto, instance,
				buildOperatorName(quitDto));
		quitStationApplyBO.saveQuitStationApply(quitStationApply, operator);

		// 合伙人实例退出中
		partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.CLOSED, PartnerInstanceStateEnum.QUITING,
				operator);

		// 村点退出中
		if (quitDto.getIsQuitStation()) {
			stationBO.changeState(instance.getStationId(), StationStatusEnum.CLOSED, StationStatusEnum.QUITING,
					operator);
		}

		// 退出审批流程，由事件监听完成
		// 记录村点状态变化
		PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convert(
				PartnerInstanceStateChangeEnum.START_QUITTING, partnerInstanceBO.getPartnerInstanceById(instanceId),
				quitDto);
		EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);

		// 失效tair
		// tairCache.invalid(TairCache.STATION_APPLY_ID_KEY_DETAIL_VALUE_PRE
		// + quitStationApplyDto.getStationApplyId());
		
		// 同步station_apply
		syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instanceId);
	}

	private void validateQuitPreCondition(PartnerStationRel instance, Partner partner) throws AugeServiceException {
		Long instanceId = instance.getId();
		// 校验是否已经存在退出申请单
		QuitStationApply quitStationApply = quitStationApplyBO.findQuitStationApply(instanceId);
		if (quitStationApply != null) {
			throw new AugeServiceException(StationExceptionEnum.QUIT_STATION_APPLY_EXIST);
		}

		// 校验是否存在未结束的订单
		tradeAdapter.validateNoEndTradeOrders(partner.getTaobaoUserId(), instance.getServiceEndTime());

		// 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
		partnerInstanceHandler.validateExistValidChildren(PartnerInstanceTypeEnum.valueof(instance.getType()),
				instanceId);
	}

	private String buildOperatorName(OperatorDto operatorDto) {
		String operator = operatorDto.getOperator();
		OperatorTypeEnum type = operatorDto.getOperatorType();

		// 小二工号
		if (OperatorTypeEnum.BUC.equals(type)) {
			return emp360Adapter.getName(operator);
		} else if (OperatorTypeEnum.HAVANA.equals(type)) {
			return uicReadAdapter.findTaobaoName(operator);
		}
		return "";
	}

	@Override
	public Long applySettle(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		ValidateUtils.notNull(partnerInstanceDto);
		ValidateUtils.notNull(partnerInstanceDto.getType());
		Long instanceId = partnerInstanceDto.getId();
		if (instanceId == null) {
			// 新增入驻
			instanceId = addSubmit(partnerInstanceDto);
			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.ADD, instanceId);
		}else {
			//暂存后，修改入驻
			updateSubmit(partnerInstanceDto);
			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instanceId);
		}
		//不同类型合伙人，执行不同的生命周期
		partnerInstanceHandler.handleApplySettle(partnerInstanceDto, partnerInstanceDto.getType());
		return instanceId;
		
	}
	
	private void syncStationApply(SyncStationApplyEnum type, Long instanceId) {
		EventDispatcher.getInstance().dispatch(EventConstant.CUNTAO_STATION_APPLY_SYNC_EVENT, new StationApplySyncEvent(type, instanceId));
	}

	@Override
	public void quitPartnerInstance(PartnerInstanceQuitDto partnerInstanceQuitDto) throws AugeServiceException {
		ValidateUtils.notNull(partnerInstanceQuitDto);
		Long instanceId = partnerInstanceQuitDto.getInstanceId();
		ValidateUtils.notNull(instanceId);
		
		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
		if (rel==null || StringUtils.isEmpty(rel.getType())) {
			throw new AugeServiceException(CommonExceptionEnum.RECORD_IS_NULL);
		}
		partnerInstanceHandler.handleQuit(partnerInstanceQuitDto, PartnerInstanceTypeEnum.valueof(rel.getType()));
	}

	@Override
	public Long applySettleNewly(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		/*ValidateUtils.notNull(partnerInstanceDto);
		ValidateUtils.notNull(partnerInstanceDto.getType());
		ValidateUtils.notNull(partnerInstanceDto.getId());
		Long instanceId = partnerInstanceDto.getId();

		updateSubmit(partnerInstanceDto);
		}
		//不同类型合伙人，执行不同的生命周期
		partnerInstanceHandler.handleApplySettle(partnerInstanceDto, partnerInstanceDto.getType());
		return instanceId;*/
		return null;
	}

}
