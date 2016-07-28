package com.taobao.cun.auge.station.adapter;

import com.taobao.cun.auge.station.dto.AlipayTagDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface AlipayTagAdapter {
	
	/**
	 * 处理打标和解标的方法
	 * @param alipayAccountTagDto
	 * @return
	 */
	public boolean dealTag(AlipayTagDto alipayTagDto) throws AugeServiceException;
}
