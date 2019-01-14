package com.taobao.cun.auge.station.validate;

import java.io.IOException;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.dto.StationUpdateServicingDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.diamond.client.Diamond;

public final class StationValidator {
	
	public static final String RULE_REGEX_STATIONMUN = "^[0-9A-Z]+$";
	
	public static final String RULE_REGEX_ADDRESS = "[`~!@#$%^&*+=|{}':;',\\[\\].<>/?~！@#￥%……&*——+|{}【】《》‘；：”“’。，、？]";

    /**
     * 比上述特殊字符少了一个^字符,该字符是菜鸟用来分隔的
     */
	public static final String CAINIAO_RULE_REGEX_ADDRESS = "[`~!@#$%&*+=|{}':;',\\[\\].<>/?~！@#￥%……&*——+|{}【】《》‘；：”“’。，、？]";

	static final String OTHER_VILLAGE = "-1";
	
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
    
    public static HashSet<String> addressInvalidWord = new HashSet<>();
    
    public static HashSet<String> nameInvalidWord = new HashSet<>();
	
    public static HashSet<String> villageDeatilValidEndWord = new HashSet<>();
        
	private static final Logger logger = LoggerFactory.getLogger(StationValidator.class);

    
    static {
        generalInvalidWord.add("www");
        generalInvalidWord.add("http");
        generalInvalidWord.add("com");
        generalInvalidWord.add(".cn");
        generalInvalidWord.add(".net");
        generalInvalidWord.add(".org");
        addressInvalidWord.addAll(generalInvalidWord);
        
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
        
        villageDeatilValidEndWord.add("村");
        villageDeatilValidEndWord.add("区");
        villageDeatilValidEndWord.add("社区");
        villageDeatilValidEndWord.add("组");
        villageDeatilValidEndWord.add("队");   
        villageDeatilValidEndWord.add("连"); 
        villageDeatilValidEndWord.add("场");   
        villageDeatilValidEndWord.add("屯");   
        villageDeatilValidEndWord.add("会");  
        villageDeatilValidEndWord.add("部"); 
        villageDeatilValidEndWord.add("站"); 
        villageDeatilValidEndWord.add("路");    
        villageDeatilValidEndWord.add("委"); 
        villageDeatilValidEndWord.add("园");    
        villageDeatilValidEndWord.add("所");     
        villageDeatilValidEndWord.add("沟");  
        villageDeatilValidEndWord.add("大道");  

        
    }
    
    public static HashSet<String> getVillageEndWordFromDiamond(){
        
        HashSet<String> villageDeatilValidEndWordDiamond = new HashSet<>();

        try {
			String villageEndWordDiamond = Diamond.getConfig("com.taobao.cun:auge.villageendwords","DEFAULT_GROUP" , 5000);
			String[] villageEndWords = StringUtils.split(villageEndWordDiamond,",");
			for(String endWord : villageEndWords) {
				villageDeatilValidEndWordDiamond.add(endWord);
			}
			logger.info("villageDeatilValidEndWordDiamond loaded");
		} catch (IOException e) {
			logger.warn("villageDeatilValidEndWordDiamond load failed",e);

		}
        
        return villageDeatilValidEndWordDiamond;
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
	
	public static boolean isSpecialStr(String str,String rule) {
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
        if (name.length() > 20 || name.length() < 2) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"服务点名称长度2-20");
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

    public static boolean umStationNameCheck(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"优盟店名称不能为空");
        }
        if (name.length() > 10 || name.length() < 2) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"优盟店名称长度2-10");
        }
        for (String vw : nameInvalidWord) {
            if (name.indexOf(vw) >= 0) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"优盟店名称不能含有特殊关键字:"+vw);
            }
        }
        if(!isAllChinese(name)){
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"优盟店名称只允许中文汉字");
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
        //校验详细地址内容规范
    	addressDetailFormatCheck(address);
    	
        //校验行政地址内容规范
    	villageFormatCheck(address);

        return true;
    }
    
    /**
     * 行政村地址格式校验
     * 1.需要以特定关键词结尾 { 村、区、社区、组、队、连、场、屯、会、部、站、路、委、园、所、沟 }
     * 2.不能与特殊关键词相等
     * 
     * @param address 传入Address预留一定扩展性
     * @return
     */
    public static boolean villageFormatCheck(Address address){
        if (address == null) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"地址不能为空");
        }
       
        if(StringUtils.isEmpty(address.getVillageDetail())) {
        	return true;
        }
        
        //特殊符号校验
        if (address.getVillageDetail().length() > 20) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"行政村名称不能超过20");
        }
        
        //特殊符号校验
        if ( !isSpecialStr(address.getVillageDetail(),RULE_REGEX_ADDRESS)) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"所属行政村不可含有特殊字符");
        }
        
        boolean isOk = false;
        //行政村后缀和全称验证
        HashSet<String> endWordSet = getVillageEndWordFromDiamond();
        if(CollectionUtils.isEmpty(endWordSet)) {
        	endWordSet = villageDeatilValidEndWord;
        }
        
        for(String endWord : endWordSet) {
        	
        	if(StringUtils.endsWith(address.getVillageDetail(), endWord) && !villageDeatilValidEndWord.contains(address.getVillageDetail())) {
        		isOk = true;
        		break;
        	}
        }
    	
        if(!isOk) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"行政村名称不符合行政标准");
        }
        
        return isOk;
    }
    
    /**
     * 详细村地址格式校验
     * 
     * @param address 传入Address预留一定扩展性
     * @return
     */
    public static boolean addressDetailFormatCheck(Address address){
        if (address == null || StringUtils.isEmpty(address.getAddressDetail())) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"地址不能为空");
        }
        if (address.getAddressDetail().length() > 30 || address.getAddressDetail().length() < 3) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"详细地址长度控制在3-30位");
        }
        if (!isSpecialStr(address.getAddressDetail(),RULE_REGEX_ADDRESS) || !isContainChinese(address.getAddressDetail())) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"详细地址不可含有特殊字符,并且最少一个汉字");
        }
        String loweraddr = address.getAddressDetail();
        for(String vm : addressInvalidWord){
            if(loweraddr.contains(vm)){
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
