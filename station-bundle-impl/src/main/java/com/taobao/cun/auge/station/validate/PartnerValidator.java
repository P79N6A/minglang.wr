package com.taobao.cun.auge.station.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.taobao.common.category.util.StringUtil;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerUpdateServicingDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.PartnerExceptionEnum;

public final class PartnerValidator {

	private PartnerValidator(){
		
	}
	
	public static void validatePartnerInfo(PartnerDto partnerDto) {
		if (StringUtils.isBlank(partnerDto.getTaobaoNick())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_TAOBAONICK_IS_NULL);
		}
		if (StringUtils.isBlank(partnerDto.getAlipayAccount())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_ALIPAYACCOUNT_IS_NULL);
		}
		if (StringUtils.isBlank(partnerDto.getIdenNum())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_IDENNUM_IS_NULL);
		}
		if (StringUtils.isBlank(partnerDto.getName())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_NAME_IS_NULL);
		}

		if (partnerDto.getMobile() == null) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_MOBILE_IS_NULL);
		}
		if (!isMobileNO(partnerDto.getMobile())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_MOBILE_CHECK_FAIL);
		}
	}
	
	public static void validateParnterCanUpdateInfo(PartnerUpdateServicingDto partnerDto) {
		if (partnerDto == null) {
			return;
		}
		if (partnerDto.getMobile() != null) {
			if (!isMobileNO(partnerDto.getMobile())) {
				throw new AugeServiceException(PartnerExceptionEnum.PARTNER_MOBILE_CHECK_FAIL);
			}
		}
	}
	
	public static void validateParnterUpdateInfoByPartner(PartnerUpdateServicingDto partnerDto) {
		if (partnerDto == null) {
			return;
		}
		if (StringUtil.isBlank(partnerDto.getMobile())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_MOBILE_IS_NULL);
		}
		if (!isMobileNO(partnerDto.getMobile())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_MOBILE_CHECK_FAIL);
		}
	}
	
	private static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((1))\\d{10}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

}
