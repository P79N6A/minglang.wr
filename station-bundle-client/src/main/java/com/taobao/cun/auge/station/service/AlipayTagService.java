package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.AlipayTagDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface AlipayTagService {
	public boolean dealTag(AlipayTagDto alipayTagDto) throws AugeServiceException;
}
