package com.taobao.cun.auge.station.adapter.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.common.utils.BeanCopyUtils;
import com.taobao.cun.auge.station.adapter.AlipayStandardBailAdapter;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.common.resultmodel.ResultModel;
import com.taobao.cun.service.alipay.AlipayStandardBailService;

@Component("alipayStandardBailAdapter")
public class AlipayStandardBailAdapterImpl implements AlipayStandardBailAdapter {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AlipayStandardBailService alipayStandardBailService;

	@Override
	public boolean dealStandardBail(com.taobao.cun.auge.station.dto.AlipayStandardBailDto alipayStandardBailDto) {
		try {
			logger.info("start dealStandardBail : " + JSON.toJSONString(alipayStandardBailDto));
			com.taobao.cun.dto.alipay.AlipayStandardBailDto dto = new com.taobao.cun.dto.alipay.AlipayStandardBailDto();
			BeanCopyUtils.copyNotNullProperties(alipayStandardBailDto,dto);
			ResultModel<com.taobao.cun.dto.alipay.AlipayStandardBailDto> resultModel = alipayStandardBailService.dealStandardBail(dto);
			if (!resultModel.isSuccess()) {
				if (null != resultModel.getException()) {
					logger.error("alipayStandardBailService.dealStandardBail error", resultModel.getException());
				} else {
					logger.error("alipayStandardBailService.dealStandardBail error" + JSON.toJSONString(resultModel));
				}
				throw resultModel.getException() == null ? new RuntimeException("dealStandardBail error") : resultModel.getException();
			}
			return true;
		} catch (Exception e) {
			logger.error("alipayStandardBailService.dealStandardBail error", e);
			throw new AugeServiceException("alipayStandardBailService.dealStandardBail error" + e.getMessage());
		}
	}

}
