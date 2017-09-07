package com.taobao.cun.auge.alipay.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.taobao.cun.auge.alipay.dto.AlipayRiskScanResult;
import com.taobao.cun.auge.alipay.service.AlipayRiskScanService;
import com.taobao.cun.auge.alipay.service.util.AlipayRiskScanSignGenerator;
import com.taobao.cun.auge.alipay.service.util.HttpClientUtil;
import com.taobao.cun.auge.client.result.ResultModel;
import com.taobao.cun.common.exceptions.ServiceException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.uic.common.domain.BaseUserDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicReadServiceClient;

@Service("alipayRiskScanService")
@HSFProvider(serviceInterface= AlipayRiskScanService.class,serviceVersion="1.0.0.daily.fhh")
public class AlipayRiskScanServiceImpl implements AlipayRiskScanService {
	private static final Logger logger = LoggerFactory.getLogger(AlipayRiskScanServiceImpl.class);

	@Autowired
	private UicReadServiceClient uicReadServiceClient;

	private static final String EVENT_TYPE = "CUN_TAO";
	private static final String PATH = "riskScan.json";
	@Value("${alipay.risk.scan.appKey}")
	private String appKey;
	@Value("${alipay.risk.scan.secretKey}")
	private String secretKey;
	@Value("${alipay.risk.scan.url}")
	private String url;

	@Override
	public ResultModel<AlipayRiskScanResult> checkEntryRisk(Long taobaoUserId) {
		return checkEntryRisk(taobaoUserId, null);
	}
	

	@Override
	public ResultModel<AlipayRiskScanResult> checkEntryRisk(String taobaoNick) {
		return checkEntryRisk(null, taobaoNick);
	}


	private ResultModel<AlipayRiskScanResult> checkEntryRisk(Long taobaoUserId, String taobaoNick) {
		if ((null == taobaoUserId || 0 == taobaoUserId) && StringUtils.isBlank(taobaoNick)) {
			return unSucceed("input parameter is null");
		}
		try {
			ResultDO<BaseUserDO> baseUserDOresult = null;
			if (taobaoUserId != null && taobaoUserId != 0) {
				baseUserDOresult = uicReadServiceClient.getBaseUserByUserId(taobaoUserId);
			} else {
				baseUserDOresult = uicReadServiceClient.getBaseUserByNick(taobaoNick);
			}
			if (null == baseUserDOresult || !baseUserDOresult.isSuccess()) {
				throw new ServiceException("UicReadServiceClient error" + JSON.toJSONString(baseUserDOresult));
			}
			BaseUserDO userDO = baseUserDOresult.getModule();
			if (null == userDO || StringUtils.isBlank(userDO.getIdCardNumber())) {
				return success(AlipayRiskScanResult.success(true, "因为没有找到身份证信息，入职校验存在风险"));
			}
			Map<String, Object> params = Maps.newHashMap();
			params.put("eventType", EVENT_TYPE);
			params.put("idCardNo", userDO.getIdCardNumber()); // 必填参数
			params.put("name", userDO.getFullname()); // 选填
			params.put("taobaoId", String.valueOf(userDO.getUserId()));// 选填
			params.put("appKey", appKey);
			// 生成签名
			String sign = AlipayRiskScanSignGenerator.sign(PATH, params, secretKey);
			params.put("sign", sign);
			Map<String, String> map = Maps.newHashMap();
			for (String key : params.keySet()) {
				map.put(key, params.get(key) + "");
			}
			String response = HttpClientUtil.get(url, map, null);
			if (StringUtils.isBlank(response)){
				return unSucceed("支付宝接口调用失败");
			}
			AlipayRiskScanResult result = JSON.parseObject(response, AlipayRiskScanResult.class);
			if (result.isSuccess()) {
				return success(result);
			} else {
				return unSucceed(result.getErrorMsg());
			}
		} catch (Exception e) {
			logger.error("checkEntryRisk error", e);
			return unSucceed(e.getMessage());
		}
	}
	
	public static <T> ResultModel<T> success(T data){
		ResultModel<T> result = new ResultModel<T>();
		
		result.setSuccess(true);
		result.setResult(data);
		
		return result;
	}
	
	public static <T> ResultModel<T> unSucceed(String message){
		ResultModel<T> result = new ResultModel<T>();
		
		result.setSuccess(false);
		result.setErrorMessage(message);
		
		return result;
	}

}
