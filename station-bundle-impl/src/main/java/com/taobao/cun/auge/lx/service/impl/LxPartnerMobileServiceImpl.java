package com.taobao.cun.auge.lx.service.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.common.result.ErrorInfo;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.lx.bo.LxPartnerBO;
import com.taobao.cun.auge.lx.dto.LxPartnerAddDto;
import com.taobao.cun.auge.lx.dto.LxPartnerListDto;
import com.taobao.cun.auge.lx.service.LxPartnerMobileService;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.diamond.client.Diamond;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.mtee.fmac.IdentifyRisk;

@HSFProvider(serviceInterface = LxPartnerMobileService.class)
@Service("lxPartnerMobileService")
public class LxPartnerMobileServiceImpl implements LxPartnerMobileService {

	private static final Logger logger = LoggerFactory.getLogger(LxPartnerMobileService.class);

	@Autowired
	private LxPartnerBO lxPartnerBO;
	@Autowired
	private IdentifyRisk identifyRisk;

	@Override
	public Result<Boolean> addLxPartner(LxPartnerAddDto param) {
		try {
			return Result.of(lxPartnerBO.addLxPartner(param));
		} 
		catch (NullPointerException e2) {
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.PARTNER_BUSINESS_CHECK_ERROR_CODE, null, e2.getMessage());
			return Result.of(errorInfo);
		}
		catch (AugeBusinessException e1) {
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.PARTNER_BUSINESS_CHECK_ERROR_CODE, null, e1.getMessage());
			return Result.of(errorInfo);
		} catch (Exception e) {
			logger.error("LxPartnerMobileService.addLxPartner error! param:" + JSON.toJSONString(param), e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

	@Override
	public Result<LxPartnerListDto> listLxPartner(Long taobaoUserId) {
		try {
			return Result.of(lxPartnerBO.listLxPartner(taobaoUserId));
		} catch (AugeBusinessException e1) {
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.PARTNER_BUSINESS_CHECK_ERROR_CODE, null, e1.getMessage());
			return Result.of(errorInfo);
		} catch (Exception e) {
			logger.error("LxPartnerMobileService.addLxPartner error! param:" + taobaoUserId, e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

	@Override
	public  Result<Boolean> checkFkByTaobaoUserId(Long taobaoUserId) {
		com.taobao.mtee.rmb.RmbParameter p = new com.taobao.mtee.rmb.RmbParameter();
		p.setUserId(String.valueOf(taobaoUserId));
		String asac="";
		try {
			asac= Diamond.getConfig("auge.lx.identifyrisk.asac", "DEFAULT_GROUP", 3000);
		} catch (IOException e) {
			logger.error("LxPartnerMobileService.checkFkByTaobaoUserId error! param:" + taobaoUserId, e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
		p.setAsac(asac);
		com.taobao.mtee.rmb.RmbTokenResult  r = identifyRisk.identifyRisk(p);
		if (r.getRmbResult().isSafe()) {
			return Result.of(Boolean.TRUE);
		}else {
			logger.error("LxPartnerMobileService.checkFkByTaobaoUserId error! param:" + taobaoUserId,r.getRmbResult().getErrorMsg());
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, r.getRmbResult().getErrorMsg());
			return Result.of(errorInfo);
		}
	}
}
