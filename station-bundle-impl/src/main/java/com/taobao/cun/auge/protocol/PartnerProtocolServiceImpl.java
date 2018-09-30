package com.taobao.cun.auge.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.taobao.cun.auge.common.result.ErrorInfo;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.dto.CuntaoServiceVendorDto;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("partnerProtocolService")
@HSFProvider(serviceInterface = PartnerProtocolService.class)
public class PartnerProtocolServiceImpl implements PartnerProtocolService {

	@Autowired
	private PartnerProtocolRelBO partnerProtocolRelBO;
	
	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	
	private static final Logger logger = LoggerFactory.getLogger(PartnerProtocolServiceImpl.class);

	@Override
	public Result<Boolean> confirmPartnerProtocol(Long taobaoUserId, ProtocolTypeEnum protocol) {
		try {
			Result<Boolean> isConfirmResult = isConfirmPartnerProtocol(taobaoUserId,protocol);
			if(isConfirmResult.isSuccess() && isConfirmResult.getModule()){
				return isConfirmResult;
			}
			boolean comfirmResult = confirmProtocol(taobaoUserId,protocol);
			Result<Boolean> result = Result.of(true);
			result.setModule(comfirmResult);
			return result;
		} catch (Exception e) {
			logger.error("confirmPartnerProtocol["+taobaoUserId+"]",e);
			return Result.of(ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, "系统异常"));
		}
	}


	@Override
	public Result<Boolean> isConfirmPartnerProtocol(Long taobaoUserId, ProtocolTypeEnum protocol) {
		try {
			boolean isConfirmed = isConfirmProtocol(taobaoUserId,protocol);
			Result<Boolean> result = Result.of(true);
			result.setModule(isConfirmed);
			return result;
		} catch (Exception e) {
			logger.error("isConfirmPartnerProtocol["+taobaoUserId+"]",e);
			return Result.of(ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, "系统异常"));
		}
	}
	
	private Boolean isConfirmProtocol(Long taobaoUserId,ProtocolTypeEnum protcolType){
		Assert.notNull(taobaoUserId);
		Assert.notNull(protcolType);
		PartnerStationRel instance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		if(instance == null){
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"合伙人不存在");
		}
		PartnerProtocolRelDto partnerProtocolRelDto = partnerProtocolRelBO.getPartnerProtocolRelDto(protcolType, instance.getId(), PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
		if(partnerProtocolRelDto != null){
			return true;
		}
		return false;
	}
	
	
	private Boolean confirmProtocol(Long taobaoUserId,ProtocolTypeEnum protcolType){
		try {
			Assert.notNull(taobaoUserId);
			Assert.notNull(protcolType);
			PartnerStationRel instance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
			if(instance == null){
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"合伙人不存在");
			}
			partnerProtocolRelBO.signProtocol(taobaoUserId,protcolType, instance.getId(), PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
			return true;
		} catch (Exception e) {
			logger.error("confirmProtocol["+taobaoUserId+"]",e);
			throw new AugeBusinessException(e);
		}
		
	}

}
