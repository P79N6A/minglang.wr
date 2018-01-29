package com.taobao.cun.auge.station.validate;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.dto.StationUpdateServicingDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import org.apache.commons.lang.StringUtils;

public final class StationValidator {
	
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
        generalInvalidWord.add("www");
        generalInvalidWord.add("http");
        generalInvalidWord.add("com");
        generalInvalidWord.add(".cn");
        generalInvalidWord.add(".net");
        generalInvalidWord.add(".org");
        nameInvalidWord.addAll(generalInvalidWord);
        nameInvalidWord.add("附近");
        nameInvalidWord.add("旁边");
        nameInvalidWord.add("优品");
        nameInvalidWord.add("服务站");
        nameInvalidWord.add("农村淘宝");
        nameInvalidWord.add("村淘");
        nameInvalidWord.add("天猫");
        nameInvalidWord.add("体验店");
        nameInvalidWord.add("电器");
        nameInvalidWord.add("母婴");
        nameInvalidWord.add("招商中");
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
		addressFormatCheck(address);
	}
	
	public static void validateStationCanUpdateInfo(StationUpdateServicingDto stationDto) {
		if (stationDto == null) {
			return;
		}
		Address address = stationDto.getAddress();
		//村地址基础校验
		addressFormatCheck(address);

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
		Address address = stationDto.getAddress();
		//村地址基础校验
        addressFormatCheck(address);
        
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
     * 服务站名称校验
     * 1.不含有：{ 附近 旁边 套餐}
     * 2.全是中文汉字
     * @param name 必填
     * @return true 通过
     */
    public static boolean nameFormatCheck(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"服务点名称不能为空");
        }
        if (name.length() > 20 || name.length() < 4) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"服务点名称长度4-20");
        }
        for (String vw : nameInvalidWord) {
            if (name.indexOf(vw) >= 0) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"名称不能含有特殊关键字:"+vw);
            }
        }
        if(!isAllChinese(name)){
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"名称只允许中文汉字");
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
    public static boolean addressFormatCheck(Address address){
        if (address == null) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"服务站地址不能为空");
        }
        if (address.getAddressDetail().length() > 30 || address.getAddressDetail().length() < 4) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"村服务站地址长度控制在4-30位");
        }
        if (!isSpecialStr(address.getAddressDetail(),RULE_REGEX_ADDRESS) || !isContainChinese(address.getAddressDetail())) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"村服务站地址不可含有特殊字符,并且最少一个汉字");
        }
        String loweraddr = address.getAddressDetail();
        for(String vm : addressInvalidWord){
            if(loweraddr.indexOf(vm) >= 0){
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"地址不能含有特殊关键字:"+vm);
            }
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
     * 全为中文
     * 
     * @param name
     * @return
     */
    public static boolean isAllChinese(String name) {
        char[] cs = name.toCharArray();
        for (char c : cs) {
            int num = (int) c;
            if ((mix <= num && num <= max)) {
            }else{
                return false;
            }
        }
        return true;
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
