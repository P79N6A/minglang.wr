package com.taobao.cun.auge.lifecycle.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.configuration.KFCServiceConfig;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.adapter.PaymentAccountQueryAdapter;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PaymentAccountDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.validate.PartnerValidator;
import com.taobao.cun.auge.station.validate.StationValidator;
/**
 * 
 * @author zhenhuan.zhangzh
 *
 */
@Component
public class LifeCycleValidator {
	@Autowired
	PaymentAccountQueryAdapter paymentAccountQueryAdapter;
	
	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
    StationBO stationBO;
	
	@Autowired
	private KFCServiceConfig kfcServiceConfig;
	
	public void validateSettling(PartnerInstanceDto partnerInstanceDto) throws AugeBusinessException {
		ValidateUtils.validateParam(partnerInstanceDto);
		ValidateUtils.notNull(partnerInstanceDto.getStationDto());
		ValidateUtils.notNull(partnerInstanceDto.getPartnerDto());
		ValidateUtils.notNull(partnerInstanceDto.getType());
		StationDto stationDto = partnerInstanceDto.getStationDto();
		PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
		ValidateUtils.notNull(stationDto);
		StationValidator.validateStationInfo(stationDto);
		PartnerValidator.validatePartnerInfo(partnerDto);
		stationModelBusCheck(partnerInstanceDto);
		OperatorDto operator = new OperatorDto();
		operator.copyOperatorDto(partnerInstanceDto);
		PaymentAccountDto paDto = paymentAccountQueryAdapter.queryPaymentAccountByNick(partnerDto.getTaobaoNick(),
				operator);
		if (!partnerDto.getAlipayAccount().equals(paDto.getAlipayId())) {
			throw new AugeBusinessException(AugeErrorCodes.ALIPAY_BUSINESS_CHECK_ERROR_CODE,
					"您录入的支付宝账号与淘宝绑定的不一致，请联系申请人核对:" + partnerDto.getAlipayAccount() + ";" + paDto.getAlipayId());
		}
		if (!partnerDto.getName().equals(paDto.getFullName())
				|| !partnerDto.getIdenNum().equals(paDto.getIdCardNumber())) {
			throw new AugeBusinessException(AugeErrorCodes.ALIPAY_BUSINESS_CHECK_ERROR_CODE,
					"目前支付宝认证的归属人，与您提交的申请人信息不符，请联系申请人核对");
		}

		// 判断淘宝账号是否使用中
		PartnerStationRel existPartnerInstance = partnerInstanceBO.getActivePartnerInstance(paDto.getTaobaoUserId());
		if (null != existPartnerInstance) {
			throw new AugeBusinessException(AugeErrorCodes.DATA_EXISTS_ERROR_CODE, "该账号已经入驻农村淘宝，请核实！");
		}
		// 判断手机号是否已经被使用
		// 逻辑变更只判断入驻中、装修中、服务中，退出中用户
		if (!partnerInstanceBO.judgeMobileUseble(partnerDto.getTaobaoUserId(), null, partnerDto.getMobile())) {
			throw new AugeBusinessException(AugeErrorCodes.DATA_EXISTS_ERROR_CODE, "该手机号已被使用");
		}
		// 入驻老村点，村点状态为已停业
		Long stationId = stationDto.getId();
		if (stationId != null) {
			Station station = stationBO.getStationById(stationId);
			if (station != null && !StationStatusEnum.CLOSED.getCode().equals(station.getStatus())) {
				throw new AugeBusinessException(AugeErrorCodes.STATION_BUSINESS_CHECK_ERROR_CODE, "被入驻的村点状态必须为已停业");
			}
		}
	}
	
	//名称与地址的业务校验KFC、村小二名称
	public void stationModelBusCheck(PartnerInstanceDto ins){
	    StringBuffer sb = new StringBuffer();
	    sb.append(ins.getStationDto().getName());
	    sb.append(ins.getStationDto().getAddress().getAddressDetail());
	    if(kfcServiceConfig.isProhibitedWord(sb.toString())){
	        throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, "名称包含违禁词汇："+kfcServiceConfig.kfcCheck(sb.toString()).get("word"));
	    }if(sb.toString().contains(ins.getPartnerDto().getName())){
	        throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, "村站名称或地址不可以包含村小二名称");
	    }
	}
	
}
