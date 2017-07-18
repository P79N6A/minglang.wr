package com.taobao.cun.auge.station.adapter.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.adapter.WangwangAdapter;
import com.taobao.cun.auge.station.exception.AugeBusinessException;

@Component("wangwangAdapter")
public class WangwangAdapterImpl implements WangwangAdapter {

	public static final Logger logger = LoggerFactory.getLogger(WangwangAdapterImpl.class);

	public static final String ADD_TAG_VALUE = "1";
	public static final String REMOVE_TAG_VALUE = "0";

	public static final Long SUCCESS_CODE = 10000l;

	@Value("${wangwang.strCaller:cntaobaoCuntao}")
	private String strCaller;
	@Value("${wangwang.strServiceType:cntaobaoCuntaoType}")
	private String strServiceType;
	@Value("${wangwang.strUserPreffix:cntaobao}")
	private String strUserPreffix;
	@Value("${wangwang.strProperty:security}")
	private String strProperty;

	@Autowired
	private com.taobao.wws.hsf2icesrv hsf2icesrv;

	public void addWangWangTagByNick(String taobaoNick) {
		updateWangWangTag(taobaoNick, ADD_TAG_VALUE);
	}

	public void removeWangWangTagByNick(String taobaoNick) {
		updateWangWangTag(taobaoNick, REMOVE_TAG_VALUE);
	}

	private void updateWangWangTag(String taobaoNick, String tag) {
		if (StringUtil.isEmpty(taobaoNick)) {
			throw new IllegalArgumentException("taobaoNick is null!");
		}
		logger.info("hsf2icesrv.wwPropertyAgentId_SetUserProperty param:{}, tag:{}.", taobaoNick.toLowerCase(), tag);
		List<Object> res = hsf2icesrv.wwPropertyAgentId_SetUserProperty(strCaller, strServiceType, strUserPreffix + taobaoNick, strProperty,
				tag);
		logger.info("hsf2icesrv.wwPropertyAgentId_SetUserProperty result : {}", JSON.toJSONString(res));

		if (res == null || res.size() == 0) {
			throw new AugeBusinessException(AugeErrorCodes.WANGWANG_BUSINESS_CHECK_ERROR_CODE,"hsf2icesrv.wwPropertyAgentId_SetUserProperty result is null!");
		}
		Long code = (Long) res.get(0);
		if (!code.equals(SUCCESS_CODE)) {
			throw new AugeBusinessException(AugeErrorCodes.WANGWANG_BUSINESS_CHECK_ERROR_CODE,"hsf2icesrv.wwPropertyAgentId_SetUserProperty result code error!");
		}
	}
}
