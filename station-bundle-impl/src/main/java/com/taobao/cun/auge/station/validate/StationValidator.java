package com.taobao.cun.auge.station.validate;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.dto.StationUpdateServicingDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;

public final class StationValidator {
	
	private static final Logger logger = LoggerFactory.getLogger(StationValidator.class);
	
	public static final String RULE_REGEX = "^[0-9A-Z]+$";
	
	private StationValidator(){
		
	}
	
	public static void validateStationUpdateInfoByPartner(StationUpdateServicingDto stationDto) {
		if (stationDto == null) {
			return;
		}
		Address address = stationDto.getAddress();
		if (address == null) {
			throw new AugeServiceException(StationExceptionEnum.STATION_ADDRESS_IS_NULL);
		}
	}
	
	public static void validateStationCanUpdateInfo(StationUpdateServicingDto stationDto) {
		if (stationDto == null) {
			return;
		}
		if (StringUtils.isEmpty(stationDto.getName())) {
			throw new AugeServiceException(StationExceptionEnum.STATION_NAME_IS_NULL);
		}
		Address address = stationDto.getAddress();
		if (address == null) {
			throw new AugeServiceException(StationExceptionEnum.STATION_ADDRESS_IS_NULL);
		}
		String stationName = "";
		if (StringUtils.isNotBlank(address.getCountyDetail())) {
			stationName += address.getCountyDetail();
		}
		stationName += stationDto.getName();
		try {
			if (stationName.getBytes("UTF-8").length > 64) {
				throw new AugeServiceException(StationExceptionEnum.CAINIAO_STATION_NAME_TOO_LENGTH);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("validate:", e);
		}

		String stationNum = stationDto.getStationNum();
		if (StringUtils.isEmpty(stationNum)) {

			throw new AugeServiceException(StationExceptionEnum.STATION_NUM_IS_NULL);
		}
		stationNum = stationNum.toUpperCase();
		if (stationNum.length() > 16) {
			throw new AugeServiceException(StationExceptionEnum.STATION_NUM_TOO_LENGTH);
		}

		if (isSpecialStr(stationNum)) {
			throw new AugeServiceException(StationExceptionEnum.STATION_NUM_ILLEGAL);
		}
	}
	
	public static void validateStationInfo(StationDto stationDto) {
		if (StringUtils.isBlank(stationDto.getName())) {
			throw new AugeServiceException(StationExceptionEnum.STATION_NAME_IS_NULL);
		}
		Address address = stationDto.getAddress();
		if (address == null) {
			throw new AugeServiceException(StationExceptionEnum.STATION_ADDRESS_IS_NULL);
		}
		String stationName = "";
		if (StringUtils.isNotBlank(address.getCountyDetail())) {
			stationName += address.getCountyDetail();
		}
		stationName += stationDto.getName();
		try {
			if (stationName.getBytes("UTF-8").length > 64) {
				throw new AugeServiceException(StationExceptionEnum.CAINIAO_STATION_NAME_TOO_LENGTH);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("validate:", e);
		}

		String stationNum = stationDto.getStationNum();
		if (StringUtils.isEmpty(stationNum)) {

			throw new AugeServiceException(StationExceptionEnum.STATION_NUM_IS_NULL);
		}
		stationNum = stationNum.toUpperCase();
		if (stationNum.length() > 16) {
			throw new AugeServiceException(StationExceptionEnum.STATION_NUM_TOO_LENGTH);
		}

		if (isSpecialStr(stationNum)) {
			throw new AugeServiceException(StationExceptionEnum.STATION_NUM_ILLEGAL);
		}
	}
	
	private static boolean isSpecialStr(String str) {
		Pattern pat = Pattern.compile(RULE_REGEX);
		Matcher mat = pat.matcher(str);
		if (mat.find()) {
			return false;
		} else {
			return true;
		}
	}

}
