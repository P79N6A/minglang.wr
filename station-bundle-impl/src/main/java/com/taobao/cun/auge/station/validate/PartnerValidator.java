package com.taobao.cun.auge.station.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.taobao.common.category.util.StringUtil;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerUpdateServicingDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import org.apache.commons.lang.StringUtils;

public final class PartnerValidator {

	public static final Pattern P = Pattern.compile("^((1))\\d{10}$");

	private PartnerValidator(){
		
	}
	
	public static void validatePartnerInfo(PartnerDto partnerDto) {
		if (StringUtils.isBlank(partnerDto.getTaobaoNick())) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"申请人淘宝会员名不能为空");
		}
		if (StringUtils.isBlank(partnerDto.getAlipayAccount())) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"申请人支付宝账号不能为空");
		}
		if (StringUtils.isBlank(partnerDto.getIdenNum())) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"身份证号码不能为空");
		}
		if (StringUtils.isBlank(partnerDto.getName())) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"申请人姓名不能为空");
		}

		if (partnerDto.getMobile() == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"手机号不能为空");
		}
		if (!isMobileNO(partnerDto.getMobile())) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"手机号必须是11位数字组成，且以1开头");
		}
	}
	
	public static void validateParnterCanUpdateInfo(PartnerUpdateServicingDto partnerDto) {
		if (partnerDto == null) {
			return;
		}
		if (partnerDto.getMobile() != null) {
			if (!isMobileNO(partnerDto.getMobile())) {
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"手机号必须是11位数字组成，且以1开头");
			}
		}
	}
	
	public static void validateParnterUpdateInfoByPartner(PartnerUpdateServicingDto partnerDto) {
		if (partnerDto == null) {
			return;
		}
		if (StringUtil.isBlank(partnerDto.getMobile())) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"手机号不能为空");
		}
		if (!isMobileNO(partnerDto.getMobile())) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"手机号必须是11位数字组成，且以1开头");
		}
	}
	
	private static boolean isMobileNO(String mobiles) {
		Matcher m = P.matcher(mobiles);
		return m.matches();
	}

}
