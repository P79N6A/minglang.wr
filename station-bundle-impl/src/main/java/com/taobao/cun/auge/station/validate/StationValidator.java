package com.taobao.cun.auge.station.validate;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.dto.StationUpdateServicingDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;

public final class StationValidator {
	
	private static final Logger logger = LoggerFactory.getLogger(StationValidator.class);
	
	public static final String RULE_REGEX = "^[0-9A-Z]+$";
	
	public static final String RULE_REGEX_ADDRESS = "[`~!@#$%^&*+=|{}':;',\\[\\].<>/?~！@#￥%……&*——+|{}【】‘；：”“’。，、？]";
	
	private StationValidator(){
		
	}
	
	public static void validateStationUpdateInfoByPartner(StationUpdateServicingDto stationDto) {
		if (stationDto == null) {
			return;
		}
		Address address = stationDto.getAddress();
		if (address == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"服务站地址不能为空");
		}
	}
	
	public static void validateStationCanUpdateInfo(StationUpdateServicingDto stationDto) {
		if (stationDto == null) {
			return;
		}
		if (StringUtils.isEmpty(stationDto.getName())) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"服务点名称不能为空");
		}
		Address address = stationDto.getAddress();
		if (address == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"服务站地址不能为空");
		}
		if (address.getAddressDetail().length() > 25) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"村服务站地址长度不超过25位");
        }
		if (isSpecialStr(address.getAddressDetail(),RULE_REGEX_ADDRESS)) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"村服务站地址不可含有特殊字符");
        }
		String stationName = "";
		if (StringUtils.isNotBlank(address.getCountyDetail())) {
			stationName += address.getCountyDetail();
		}
		stationName += stationDto.getName();
		try {
			if (stationName.getBytes("UTF-8").length > 64) {
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"村服务点名称超过了菜鸟驿站要求的长度");
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("validate:", e);
		}

		String stationNum = stationDto.getStationNum();
		if (StringUtils.isEmpty(stationNum)) {

			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"服务站编号不能为空");
		}
		stationNum = stationNum.toUpperCase();
		if (stationNum.length() > 16) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"村服务站编号长度0-16位");
		}

		if (isSpecialStr(stationNum,RULE_REGEX)) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"村服务站编号不能含有特殊字符");
		}
	}
	
	public static void validateStationInfo(StationDto stationDto) {
		if (StringUtils.isBlank(stationDto.getName())) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"服务点名称不能为空");
		}
		Address address = stationDto.getAddress();
		if (address == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"服务站地址不能为空");
		}
	
        if (address.getAddressDetail().length() > 25) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"村服务站地址长度不超过25位");
        }
        
        if (isSpecialStr(address.getAddressDetail(),RULE_REGEX_ADDRESS)) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"村服务站地址不可含有特殊字符");
        }
        
		String stationName = "";
		if (StringUtils.isNotBlank(address.getCountyDetail())) {
			stationName += address.getCountyDetail();
		}
		stationName += stationDto.getName();
		try {
			if (stationName.getBytes("UTF-8").length > 64) {
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"村服务点名称超过了菜鸟驿站要求的长度");
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("validate:", e);
		}

		String stationNum = stationDto.getStationNum();
		if (StringUtils.isEmpty(stationNum)) {

			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"服务站编号不能为空");
		}
		stationNum = stationNum.toUpperCase();
		if (stationNum.length() > 16) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"村服务站编号长度0-16位");
		}

		if (isSpecialStr(stationNum,RULE_REGEX)) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"村服务站编号不能含有特殊字符");
		}
	}
	
	private static boolean isSpecialStr(String str,String rule) {
		Pattern pat = Pattern.compile(rule);
		Matcher mat = pat.matcher(str);
		if (mat.find()) {
			return false;
		} else {
			return true;
		}
	}

}
