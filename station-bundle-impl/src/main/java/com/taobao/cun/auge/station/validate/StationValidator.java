package com.taobao.cun.auge.station.validate;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
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
	
	public static final String RULE_REGEX_STATIONMUN = "^[0-9A-Z]+$";
	
	public static final String RULE_REGEX_ADDRESS = "[`~!@#$%^&*+=|{}':;',\\[\\].<>/?~！@#￥%……&*——+|{}【】‘；：”“’。，、？]";
	
	/**
     * 全角对应于ASCII表的可见字符从！开始，偏移值为65281
     */
    static final char SBC_CHAR_START = 65281; // 全角！
    /**
     * 全角对应于ASCII表的可见字符到～结束，偏移值为65374
     */
    static final char SBC_CHAR_END = 65374; // 全角～
    /**
     * ASCII表中除空格外的可见字符与对应的全角字符的相对偏移
     */
    static final int CONVERT_STEP = 65248; // 全角半角转换间隔
    /**
     * 全角空格的值，它没有遵从与ASCII的相对偏移，必须单独处理
     */
    static final char SBC_SPACE = 12288; // 全角空格 12288
    static final char SBC_LEFT_BRACKET = 12304; //全角左中括号
    static final char SBC_RIGHT_BRACKET = 12305; //全角右中括号
    static final char SBC_LJANE_BRACKET = 12298;//全角左简括号
    static final char SBC_RJANE_BRACKET = 12299;//全角右简括号
    /**
     * 半角空格的值，在ASCII中为32(Decimal)
     */
    static final char DBC_SPACE = ' '; // 半角空格
    static final char DBC_LEFT_BRACKET ='[';
    static final char DBC_RIGHT_BRACKET =']';
    
    static final int mix = Integer.parseInt("4e00", 16);
    static final int max = Integer.parseInt("9fa5", 16);
    
    public static HashSet<String> generalInvalidWord = new HashSet<String>();
    
    public static HashSet<String> addressInvalidWord = new HashSet<String>();
    
    public static HashSet<String> nameInvalidWord = new HashSet<String>();
	
    static {
        generalInvalidWord.add("www.");
        generalInvalidWord.add("http:");
        generalInvalidWord.add(".com");
        generalInvalidWord.add(".cn");
        generalInvalidWord.add(".net");
        generalInvalidWord.add(".org");
        nameInvalidWord.addAll(generalInvalidWord);
        nameInvalidWord.add("?");
        nameInvalidWord.add("附近");
        nameInvalidWord.add("旁边");
        nameInvalidWord.add("套餐");
        addressInvalidWord.addAll(generalInvalidWord);
        addressInvalidWord.add("地址");
    }
    
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
		if (address.getAddressDetail().length() > 25 || address.getAddressDetail().length() < 5) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"村服务站地址长度控制在5-25位");
        }
		if (!isSpecialStr(address.getAddressDetail(),RULE_REGEX_ADDRESS) || !addressFormatCheck(address.getAddressDetail())) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"村服务站地址不可含有特殊字符,并且最少一个汉字");
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

		if (isSpecialStr(stationNum,RULE_REGEX_STATIONMUN)) {
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
	
        if (address.getAddressDetail().length() > 25 || address.getAddressDetail().length() < 5) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"村服务站地址长度控制在5-25位");
        }
        
        if (!isSpecialStr(address.getAddressDetail(),RULE_REGEX_ADDRESS) || !addressFormatCheck(address.getAddressDetail())) {
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

		if (isSpecialStr(stationNum,RULE_REGEX_STATIONMUN)) {
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
	
	/**
     * 通用店名格式校验
     * 1.不含有：{www. .com .cn .org. net ? 附近 旁边 套餐}
     * 2.不能全是特殊字符
     * 3.首尾特殊字符，不包括《》
     * 
     * @param name 必填
     * @return true 通过
     */
    public static boolean nameFormatCheck(String name) {
        String lowername = name.toLowerCase();
        for (String vw : nameInvalidWord) {
            if (lowername.indexOf(vw) >= 0) {
                return false;
            }
        }
        if (isFullSpecial(name)) {
            return false;
        }
        if (lowername.startsWith("《") && lowername.endsWith("》")) {
            return false;
        }
        return true;
    }
    
    /**
     * 地址格式校验
     * 1.不能包含 { www. .com .cn .org. net “地址” }
     * 2.必须至少包含一个汉字
     * 
     * @param address 必填
     * @return
     */
    public static boolean addressFormatCheck(String address){
        String loweraddr = address.toLowerCase();
        for(String vm : addressInvalidWord){
            if(loweraddr.indexOf(vm) >= 0){
                return false;
            }
        }
        if (!isContainChinese(address)) { // 至少包含一个汉字
            return false;
        }
        return true;
    }
    
    /**
     * 包含中文
     * 
     * @param name
     * @return
     */
    public static boolean isContainChinese(String name) {
        char[] cs = name.toCharArray();
        for (char c : cs) {
            int num = (int) c;
            if ((mix <= num && num <= max)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 全部是特殊字符
     * 
     * @param name
     * @return
     */
    public static boolean isFullSpecial(String name) {
        for (char c : name.toCharArray()) {
            int num = (int) c;
            if ((mix <= num && num <= max)) {
                return false;
            } else if ((48 <= num && num <= 57) || ((65 <= num && num <= 90))
                    || (97 <= num && num <= 122)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 预处理
     * 全角字符->半角字符转换   
     * 只处理全角的空格，全角！到全角～之间的字符，忽略其他
     */
    public static String doublebyte2singlebyte(String src){
        if (StringUtils.isBlank(src)) {
            return src;
        }
        StringBuilder buf = new StringBuilder(src.length());
        char[] ca = src.toCharArray();
        for (int i = 0; i < src.length(); i++) {
            if (ca[i] >= SBC_CHAR_START && ca[i] <= SBC_CHAR_END) { // 如果位于全角！到全角～区间内
                buf.append((char) (ca[i] - CONVERT_STEP));
            } else if (ca[i] == SBC_SPACE) { // 如果是全角空格
                buf.append(DBC_SPACE);
            }else if(ca[i]==SBC_LEFT_BRACKET){
                buf.append(DBC_LEFT_BRACKET);
            } else if(ca[i]==SBC_RIGHT_BRACKET){
                buf.append(DBC_RIGHT_BRACKET);
            }else { // 不处理全角空格，全角！到全角～区间外的字符
                buf.append(ca[i]);
            }
        }
        return buf.toString();
    }

}
