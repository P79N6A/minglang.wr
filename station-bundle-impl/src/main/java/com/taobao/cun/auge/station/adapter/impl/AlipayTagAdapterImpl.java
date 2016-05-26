package com.taobao.cun.auge.station.adapter.impl;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.station.adapter.AlipayTagAdapter;
import com.taobao.cun.auge.station.dto.AlipayTagDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.common.resultmodel.ResultModel;
import com.taobao.cun.dto.alipay.AlipayAccountTagDto;
import com.taobao.cun.service.alipay.AlipayAccountTagService;

@Component
public class AlipayTagAdapterImpl implements AlipayTagAdapter {

	private static final Logger logger = LoggerFactory.getLogger(AlipayTagAdapterImpl.class);

	private AlipayAccountTagService alipayAccountTagService;

	@Override
	public boolean dealTag(AlipayTagDto alipayTagDto) throws AugeServiceException {
		try {
			logger.info("start alipay tag : " + JSON.toJSONString(alipayTagDto));
			AlipayAccountTagDto alipayAccountTagDto = new AlipayAccountTagDto();
			BeanUtils.copyProperties(alipayAccountTagDto, alipayTagDto);
			ResultModel<AlipayAccountTagDto> resultModel = alipayAccountTagService.dealTag(alipayAccountTagDto);
			if (!resultModel.isSuccess()) {
				if (null != resultModel.getException()) {
					logger.error("AlipayAccountTagService.dealTag error", resultModel.getException());
				} else {
					logger.error("AlipayAccountTagService.dealTag error" + JSON.toJSONString(resultModel));
				}
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.error("AlipayTagAdapter.dealTag error", e);
			throw new AugeServiceException("AlipayTagAdapter.dealTag error" + e.getMessage());
		}
	}

}
