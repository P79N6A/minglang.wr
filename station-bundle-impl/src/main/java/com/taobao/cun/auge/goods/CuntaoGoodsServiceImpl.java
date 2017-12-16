package com.taobao.cun.auge.goods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.taobao.cun.auge.common.result.ErrorInfo;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
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
	

}
