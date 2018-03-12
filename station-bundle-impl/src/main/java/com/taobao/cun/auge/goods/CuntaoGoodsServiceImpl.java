package com.taobao.cun.auge.goods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.taobao.cun.auge.common.result.ErrorInfo;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.enums.StationModeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("cuntaoGoodsService")
@HSFProvider(serviceInterface = CuntaoGoodsService.class)
public class CuntaoGoodsServiceImpl implements CuntaoGoodsService {

	@Autowired
	private PartnerProtocolRelBO partnerProtocolRelBO;
	
	@Autowired
	private PartnerInstanceQueryService partnerInstanceQueryService;

	@Autowired
	private DiamondConfiguredProperties diamondConfiguredProperties;
	
	private static final Logger logger = LoggerFactory.getLogger(CuntaoGoodsServiceImpl.class);
	@Override
	public Result<Boolean> confirmSampleGoodsProtocol(Long taobaoUserId) {
		try {
			Result<Boolean> isConfirmResult = isConfirmSampleGoodsProtocol(taobaoUserId);
			if(isConfirmResult.isSuccess() && isConfirmResult.getModule()){
				return isConfirmResult;
			}
			boolean comfirmResult = confirmProtocol(taobaoUserId,ProtocolTypeEnum.SAMPLE_GOODS_AGREEMENT);
			Result<Boolean> result = Result.of(true);
			result.setModule(comfirmResult);
			return result;
		} catch (Exception e) {
			logger.error("confirmSampleGoodsProtocol["+taobaoUserId+"]",e);
			return Result.of(ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, "系统异常"));
		}
	}

	@Override
	public Result<Boolean> confirmSupplyGoodsProtocol(Long taobaoUserId) {
		try {
			Result<Boolean> isConfirmResult = isConfirmSupplyGoodsProtocol(taobaoUserId);
			if(isConfirmResult.isSuccess() && isConfirmResult.getModule()){
				return isConfirmResult;
			}
			boolean comfirmResult = confirmProtocol(taobaoUserId,ProtocolTypeEnum.SUPPLY_GOODS_AGREEMENT);
			Result<Boolean> result = Result.of(true);
			result.setModule(comfirmResult);
			return result;
		} catch (Exception e) {
			logger.error("confirmSupplyGoodsProtocol["+taobaoUserId+"]",e);
			return Result.of(ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, "系统异常"));
		}
	}

	@Override
	public Result<Boolean> isConfirmSampleGoodsProtocol(Long taobaoUserId) {
		try {
			boolean isConfirmed = isConfirmProtocol(taobaoUserId,ProtocolTypeEnum.SAMPLE_GOODS_AGREEMENT);
			Result<Boolean> result = Result.of(true);
			result.setModule(isConfirmed);
			return result;
		} catch (Exception e) {
			logger.error("isConfirmSampleGoodsProtocol["+taobaoUserId+"]",e);
			return Result.of(ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, "系统异常"));
		}
	}

	@Override
	public Result<Boolean> isConfirmSupplyGoodsProtocol(Long taobaoUserId) {
		try {
			boolean isConfirmed = isConfirmProtocol(taobaoUserId,ProtocolTypeEnum.SUPPLY_GOODS_AGREEMENT);
			Result<Boolean> result = Result.of(true);
			result.setModule(isConfirmed);
			return result;
		} catch (Exception e) {
			logger.error("isConfirmSampleGoodsProtocol["+taobaoUserId+"]",e);
			return Result.of(ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, "系统异常"));
		}
	}
	
	private Boolean confirmProtocol(Long taobaoUserId,ProtocolTypeEnum protcolType){
		try {
			Assert.notNull(taobaoUserId);
			PartnerInstanceDto partnerInstance = partnerInstanceQueryService.getActivePartnerInstance(taobaoUserId);
			if(partnerInstance == null){
				 throw new AugeBusinessException(AugeErrorCodes.PARTNER_BUSINESS_CHECK_ERROR_CODE,"合伙人不存在");
			}
			partnerProtocolRelBO.signProtocol(taobaoUserId,protcolType, partnerInstance.getId(), PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
			return true;
		} catch (Exception e) {
			logger.error("confirmProtocol["+taobaoUserId+"]",e);
			throw new AugeBusinessException(e);
		}
		
	}
	
	
	
	private Boolean isConfirmProtocol(Long taobaoUserId,ProtocolTypeEnum protcolType){
		Assert.notNull(taobaoUserId);
		PartnerInstanceDto partnerInstance = partnerInstanceQueryService.getActivePartnerInstance(taobaoUserId);
		if(partnerInstance == null){
			 throw new AugeBusinessException(AugeErrorCodes.PARTNER_BUSINESS_CHECK_ERROR_CODE,"合伙人不存在");
		}
		PartnerProtocolRelDto partnerProtocolRelDto = partnerProtocolRelBO.getPartnerProtocolRelDto(protcolType, partnerInstance.getId(), PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
		if(partnerProtocolRelDto != null){
			return true;
		}
		return false;
	}

	@Override
	public Result<Boolean> confirmStoreSupplyGoodsProtocol(Long taobaoUserId) {
		try {
			Result<Boolean> isConfirmResult = isConfirmStoreSupplyGoodsProtocol(taobaoUserId);
			if(isConfirmResult.isSuccess() && isConfirmResult.getModule()){
				return isConfirmResult;
			}
			boolean comfirmResult = confirmProtocol(taobaoUserId,ProtocolTypeEnum.STORE_SUPPLY_GOODS_AGREEMENT);
			Result<Boolean> result = Result.of(true);
			result.setModule(comfirmResult);
			return result;
		} catch (Exception e) {
			logger.error("confirmStoreSupplyGoodsProtocol["+taobaoUserId+"]",e);
			return Result.of(ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, "系统异常"));
		}
	}

	@Override
	public Result<Boolean> isConfirmStoreSupplyGoodsProtocol(Long taobaoUserId) {
		try {
			boolean isConfirmed = isConfirmProtocol(taobaoUserId,ProtocolTypeEnum.STORE_SUPPLY_GOODS_AGREEMENT);
			Result<Boolean> result = Result.of(true);
			result.setModule(isConfirmed);
			return result;
		} catch (Exception e) {
			logger.error("isConfirmStoreSupplyGoodsProtocol["+taobaoUserId+"]",e);
			return Result.of(ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, "系统异常"));
		}
	}

	@Override
	public Result<Boolean> confirmStationOpeningProtocol(Long taobaoUserId) {
		try {
			Result<Boolean> isConfirmResult = isConfirmStoreSupplyGoodsProtocol(taobaoUserId);
			if(isConfirmResult.isSuccess() && isConfirmResult.getModule()){
				return isConfirmResult;
			}
			boolean comfirmResult = confirmProtocol(taobaoUserId,ProtocolTypeEnum.STATION_OPENING_AGREEMENT);
			Result<Boolean> result = Result.of(true);
			result.setModule(comfirmResult);
			return result;
		} catch (Exception e) {
			logger.error("confirmStoreSupplyGoodsProtocol["+taobaoUserId+"]",e);
			return Result.of(ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, "系统异常"));
		}
	}

	@Override
	public Result<Boolean> isConfirmStationOpeningProtocol(Long taobaoUserId) {
		try {
			boolean isConfirmed = isConfirmProtocol(taobaoUserId,ProtocolTypeEnum.STATION_OPENING_AGREEMENT);
			Result<Boolean> result = Result.of(true);
			result.setModule(isConfirmed);
			return result;
		} catch (Exception e) {
			logger.error("isConfirmStationOpeningProtocol["+taobaoUserId+"]",e);
			return Result.of(ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, "系统异常"));
		}
	}

	@Override
	public Result<Boolean> isSupportGoodsSupply(Long taobaoUserId) {
		try {
			Result<Boolean> result = Result.of(true);
			boolean isConfirmed = isConfirmProtocol(taobaoUserId,ProtocolTypeEnum.STATION_OPENING_AGREEMENT);
			PartnerInstanceDto partnerInstance = partnerInstanceQueryService.getActivePartnerInstance(taobaoUserId);
			if(isConfirmed || (partnerInstance != null && StationModeEnum.V4.getCode().equals(partnerInstance.getMode()) && PartnerInstanceTypeEnum.TP.getCode().equals(partnerInstance.getType().getCode()) )){
				result.setModule(true);
				return result;
			}
			result.setModule(false);
			return result;
		} catch (Exception e) {
			logger.error("isSupportGoodsSupply[" + taobaoUserId + "]", e);
			return Result.of(ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, "系统异常"));
		}
	}

	@Override
	public Result<Boolean> needConfirmStationOpeningProtocol(Long taobaoUserId) {
		try {
			Result<Boolean> result = Result.of(true);
			PartnerInstanceDto partnerInstance = partnerInstanceQueryService.getActivePartnerInstance(taobaoUserId);
			if(partnerInstance != null){
				boolean needConfirmed = this.diamondConfiguredProperties.getCanConfirmStationOpeningProtocolList().contains(partnerInstance.getStationId());
				if(needConfirmed || (StationModeEnum.V4.getCode().equals(partnerInstance.getMode()) && PartnerInstanceTypeEnum.TP.getCode().equals(partnerInstance.getType().getCode()) )){
					result.setModule(true);
					return result;
				}
			}
			result.setModule(false);
			return result;
		} catch (Exception e) {
			logger.error("needConfirmStationOpeningProtocol[" + taobaoUserId + "]", e);
			return Result.of(ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, "系统异常"));
		}
	}

}
