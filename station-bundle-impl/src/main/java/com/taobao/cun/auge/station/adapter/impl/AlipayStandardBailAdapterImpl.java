package com.taobao.cun.auge.station.adapter.impl;

import com.alibaba.fastjson.JSON;

import com.taobao.cun.auge.bail.BailService;
import com.taobao.cun.auge.bail.dto.BaiDtoBuilder;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.adapter.AlipayStandardBailAdapter;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.AlipayStandardBailDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.settle.common.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("alipayStandardBailAdapter")
public class AlipayStandardBailAdapterImpl implements AlipayStandardBailAdapter {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PartnerInstanceBO partnerInstanceBO;

	@Autowired
	private BailService bailService;

	@Override
	public boolean dealStandardBail(AlipayStandardBailDto alipayStandardBailDto) {
			logger.info("start dealStandardBail : " + JSON.toJSONString(alipayStandardBailDto));
			String outOrderNo = alipayStandardBailDto.getOutOrderNo();
			if (StringUtils.isEmpty(outOrderNo)) {
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"outOrderNo is empty!");
			}
			ResultModel<Boolean> resultModel = null;
			PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(BaiDtoBuilder.parseInstanceIdFromOutOrderNo(outOrderNo));
			if (AlipayStandardBailDto.ALIPAY_OP_TYPE_FREEZE.equals(alipayStandardBailDto.getOpType())) {
				resultModel = bailService.freezeUserBail(BaiDtoBuilder.buildFreezeBailDtoFrom(rel.getTaobaoUserId(), alipayStandardBailDto));
			}else if(AlipayStandardBailDto.ALIPAY_OP_TYPE_UNFREEZE.equals(alipayStandardBailDto.getOpType())) {
				resultModel = bailService.unfreezeUserBail(BaiDtoBuilder.buildFrom(rel.getTaobaoUserId(), alipayStandardBailDto));
			}else if(AlipayStandardBailDto.ALIPAY_OP_TYPE_UNFREEZE_REPLENISH.equals(alipayStandardBailDto.getOpType())) {
				resultModel = bailService.unfreezeUserReplenishBail(rel.getId());
			}else if (AlipayStandardBailDto.ALIPAY_OP_TYPE_TRANSFER.equals(alipayStandardBailDto.getOpType())) {
				throw new AugeBusinessException(AugeErrorCodes.ALIPAY_BUSINESS_CHECK_ERROR_CODE,"Not Support Operation Exception!");
			}
			if (resultModel != null && resultModel.isSuccess() && resultModel.getResult() != null) {
				return resultModel.getResult();
			}
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"alipayStandardBailService.dealStandardBail false, outOrderNo is:" + alipayStandardBailDto.getOutOrderNo());
	}
}
