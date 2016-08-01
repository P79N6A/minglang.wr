package com.taobao.cun.auge.station.adapter.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.station.adapter.UicTagAdapter;
import com.taobao.cun.auge.station.dto.UserTagDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeUicTagException;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.uic.common.domain.ExtraUserDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicExtraReadServiceClient;
import com.taobao.uic.common.service.userinfo.client.UicTagWriteServiceClient;

@Component("uicTagAdapter")
public class UicTagAdapterImpl implements UicTagAdapter {

	public static final Logger logger = LoggerFactory.getLogger(UicTagAdapterImpl.class);
	public static final String UIC_TAG_ERROR_MSG = "UIC_TAG_ERROR";

	@Resource
	private UicTagWriteServiceClient uicTagWriteServiceClient;
	@Resource
	private UicExtraReadServiceClient uicExtraReadServiceClient;

	private Long DEFAULT_TAG = new Double(Math.pow(2, 12)).longValue();

	private Long TPA_TAG = new Double(Math.pow(2, 13)).longValue();

	private Long TPV_TAG = new Double(Math.pow(2, 25)).longValue();

	public void addUserTag(UserTagDto userTagDto) {
		BeanValidator.validateWithThrowable(userTagDto);
		try {
			Long taobaoUserId = userTagDto.getTaobaoUserId();
			PartnerInstanceTypeEnum.PartnerInstanceType type = userTagDto.getPartnerType().getType();
			switch (type) {
			case TP:
				addTpUserTag(taobaoUserId);
				break;
			case TPA:
				addTpUserTag(taobaoUserId);
				addTpaUserTag(taobaoUserId);
				break;
			case TPV:
				addTpUserTag(taobaoUserId);
				addTpvUserTag(taobaoUserId);
				break;
			}
		} catch (Exception e) {
			logger.error(UIC_TAG_ERROR_MSG + " [addUserTag]  parameter = {}, {}", JSON.toJSONString(userTagDto), e);
			throw new AugeUicTagException("addUserTag  error!", e);
		}
	}

	public void removeUserTag(UserTagDto userTagDto) {
		BeanValidator.validateWithThrowable(userTagDto);
		try {
			Long taobaoUserId = userTagDto.getTaobaoUserId();
			PartnerInstanceTypeEnum.PartnerInstanceType type = userTagDto.getPartnerType().getType();
			switch (type) {
			case TP:
				removeTpUserTag(taobaoUserId);
				break;
			case TPA:
				removeTpUserTag(taobaoUserId);
				removeTpaUserTag(taobaoUserId);
				break;
			case TPV:
				removeTpUserTag(taobaoUserId);
				removeTpvUserTag(taobaoUserId);
				break;
			}
		} catch (Exception e) {
			logger.error(UIC_TAG_ERROR_MSG + " [removeUserTag] parameter = {}, {}", JSON.toJSONString(userTagDto), e);
			throw new AugeUicTagException("addUserTag  error!", e);
		}
	}

	/**
	 * 打合伙人标
	 * 
	 * @param taobaoUserId
	 */
	private void addTpUserTag(long taobaoUserId) {
		long userTag = getTPTag(taobaoUserId);
		if (userTag == DEFAULT_TAG.longValue()) {
			logger.error("uicTagWriteServiceClient.addUserTag repeated! ");
			return;
		}

		ResultDO<Integer> result = uicTagWriteServiceClient.addUserTag11(taobaoUserId, DEFAULT_TAG);
		logger.info("uicTagWriteServiceClient.addUserTag11 , result : {}", JSON.toJSON(result));
		if (result == null || !result.isSuccess()) {
			logger.error("uicTagWriteServiceClient.addUserTag error,userId: {} tag:{}", taobaoUserId, userTag);
			throw new RuntimeException("uicTagWriteServiceClient.addUserTag error");
		}

	}

	/**
	 * 打淘帮手标
	 * 
	 * @param taobaoUserId
	 */
	private void addTpaUserTag(long taobaoUserId) {
		if (getTPATag(taobaoUserId) != TPA_TAG.longValue()) {
			ResultDO<Integer> result = uicTagWriteServiceClient.addUserTag17(taobaoUserId, TPA_TAG);
			logger.info("uicTagWriteServiceClient.addUserTag17 , result : {}", JSON.toJSON(result));
			if (result == null || !result.isSuccess()) {
				logger.error("uicTagWriteServiceClient.addUserTag error,userId: {} tag:{}", taobaoUserId);
				throw new RuntimeException("uicTagWriteServiceClient.addUserTag error");
			}
		}
	}

	/**
	 * 打村拍档标
	 * 
	 * @param taobaoUserId
	 */
	private void addTpvUserTag(long taobaoUserId) {
		long tpvTag = getTPVTag(taobaoUserId);
		if (tpvTag != TPV_TAG.longValue()) {
			ResultDO<Integer> result = uicTagWriteServiceClient.addUserTag19(taobaoUserId, TPV_TAG);
			logger.info("uicTagWriteServiceClient.addUserTag19 , result : {}", JSON.toJSONString(result));
			if (result == null || !result.isSuccess()) {
				logger.error("uicTagWriteServiceClient.addUserTag error,userId: {} tag:{}", taobaoUserId, TPV_TAG);
				throw new RuntimeException("uicTagWriteServiceClient.addUserTag error");
			}
		}
	}

	/**
	 * 去合伙人标
	 * 
	 * @param taobaoUserId
	 */
	private void removeTpUserTag(long taobaoUserId) {
		long userTag = getTPTag(taobaoUserId);
		if (userTag != DEFAULT_TAG.longValue()) {
			logger.warn("uicTagWriteServiceClient.removeUserTag tag has removed! ");
			return;
		}
		ResultDO<Integer> result = uicTagWriteServiceClient.removeUserTag11(taobaoUserId, DEFAULT_TAG);
		logger.info("uicTagWriteServiceClient.removeUserTag , taobaoUserId:{}, result:{}", taobaoUserId, JSON.toJSONString(result));
		if (result == null || !result.isSuccess()) {
			logger.error("uicTagWriteServiceClient.removeUserTag failed, taobaoUserId: {}, resultCode:{}, erroMsg:{}",
					new Object[] { taobaoUserId, result.getRetCode(), result.getErrMsg() });
			throw new RuntimeException("uicTagWriteServiceClient.removeUserTag error.");
		}
		logger.info("uicTagWriteServiceClient.removeUserTag success, taobaoUserId: {}.", taobaoUserId);

	}

	/**
	 * 去淘帮手标
	 * 
	 * @param taobaoUserId
	 */
	private void removeTpaUserTag(long taobaoUserId) {
		if (getTPATag(taobaoUserId) == TPA_TAG.longValue()) {
			ResultDO<Integer> result = uicTagWriteServiceClient.removeUserTag17(taobaoUserId, TPA_TAG);
			logger.info("uicTagWriteServiceClient.addUserTag17 , result : {}", JSON.toJSONString(result));
			if (result != null) {
				if (!result.isSuccess()) {
					logger.error("uicTagWriteServiceClient.removeUserTag17 error,userId: {} tag:{}", taobaoUserId);
					throw new RuntimeException("uicTagWriteServiceClient.removeUserTag17 error");
				}
			}
		}
	}

	/**
	 * 去村拍档标
	 * 
	 * @param taobaoUserId
	 */
	private void removeTpvUserTag(long taobaoUserId) {
		long tpvTag = getTPVTag(taobaoUserId);
		if (tpvTag == TPV_TAG.longValue()) {
			ResultDO<Integer> result = uicTagWriteServiceClient.removeUserTag19(taobaoUserId, TPV_TAG);
			logger.info("uicTagWriteServiceClient.removeUserTag , taobaoUserId:{}, result:{}", taobaoUserId, JSON.toJSONString(result));
			if (result != null) {
				if (result.isSuccess()) {
					logger.info("uicTagWriteServiceClient.removeUserTag success, taobaoUserId: {}.", taobaoUserId);
				} else {
					logger.error("uicTagWriteServiceClient.removeUserTag failed, taobaoUserId: {}, resultCode:{}, erroMsg:{}",
							new Object[] { taobaoUserId, result.getRetCode(), result.getErrMsg() });
					throw new RuntimeException("uicTagWriteServiceClient.removeUserTag error.");
				}
			}
		}
	}

	/**
	 * 获取合伙人标
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	private long getTPTag(long taobaoUserId) {
		ResultDO<ExtraUserDO> extraUserDOResultDO = uicExtraReadServiceClient.getExtraUserByUserId(taobaoUserId);
		if (extraUserDOResultDO == null || !extraUserDOResultDO.isSuccess() || extraUserDOResultDO.getModule() == null) {
			logger.error("uicExtraReadServiceClient.getExtraUserByUserId result null, param is userId: {}", taobaoUserId);
			throw new RuntimeException("uicExtraReadServiceClient.getExtraUserByUserId result null");
		}
		return extraUserDOResultDO.getModule().getUserTag11() & DEFAULT_TAG;
	}

	/**
	 * 获取淘帮手标
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	private long getTPATag(long taobaoUserId) {
		ResultDO<ExtraUserDO> extraUserDOResultDO = uicExtraReadServiceClient.getExtraUserByUserId(taobaoUserId);
		if (extraUserDOResultDO == null || !extraUserDOResultDO.isSuccess() || extraUserDOResultDO.getModule() == null) {
			logger.error("uicExtraReadServiceClient.getExtraUserByUserId result null, param is userId: {}", taobaoUserId);
			throw new RuntimeException("uicExtraReadServiceClient.getExtraUserByUserId result null");
		}
		return extraUserDOResultDO.getModule().getUserTag17() & TPA_TAG;
	}

	/**
	 * 获取村拍档标
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	private long getTPVTag(long taobaoUserId) {
		ResultDO<ExtraUserDO> extraUserDOResultDO = uicExtraReadServiceClient.getExtraUserByUserId(taobaoUserId);
		if (extraUserDOResultDO == null || !extraUserDOResultDO.isSuccess() || extraUserDOResultDO.getModule() == null) {
			logger.error("uicExtraReadServiceClient.getExtraUserByUserId result null, param is userId: {}", taobaoUserId);
			throw new RuntimeException("uicExtraReadServiceClient.getExtraUserByUserId result null");
		}
		return extraUserDOResultDO.getModule().getUserTag19() & TPV_TAG;
	}

}