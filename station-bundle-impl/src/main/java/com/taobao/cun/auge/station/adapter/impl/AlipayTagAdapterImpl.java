package com.taobao.cun.auge.station.adapter.impl;

import com.taobao.cun.auge.station.adapter.AlipayTagAdapter;
import com.taobao.cun.auge.station.dto.AlipayTagDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("alipayTagAdapter")
public class AlipayTagAdapterImpl implements AlipayTagAdapter {

	private static final Logger logger = LoggerFactory.getLogger(AlipayTagAdapterImpl.class);
	public static final String ALIPAY_TAG_ERROR_MSG = "ALIPAY_TAG_ERROR";
    //服务已下线
//	@Autowired
//	private AlipayAccountTagService alipayAccountTagService;

	@Override
	public boolean dealTag(AlipayTagDto alipayTagDto){
//		try {
//			logger.info("start alipay tag : " + JSON.toJSONString(alipayTagDto));
//			AlipayAccountTagDto alipayAccountTagDto = new AlipayAccountTagDto();
//			BeanUtils.copyProperties(alipayAccountTagDto, alipayTagDto);
//			ResultModel<AlipayAccountTagDto> resultModel = alipayAccountTagService.dealTag(alipayAccountTagDto);
//			if (!resultModel.isSuccess()) {
//				if (null != resultModel.getException()) {
//					logger.error("AlipayAccountTagService.dealTag error", resultModel.getException());
//				} else {
//					logger.error("AlipayAccountTagService.dealTag error" + JSON.toJSONString(resultModel));
//				}
//				return false;
//			}
//			return true;
//		} catch (Exception e) {
//			logger.error(ALIPAY_TAG_ERROR_MSG + " parameter = {},{}", JSON.toJSONString(alipayTagDto), e);
//			throw new AugeServiceException("AlipayTagAdapter.dealTag error" + e.getMessage());
//		}
		return true;
	}

}
