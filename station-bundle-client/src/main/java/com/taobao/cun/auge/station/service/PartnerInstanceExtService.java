package com.taobao.cun.auge.station.service;

public interface PartnerInstanceExtService {

	/**
	 * 校验下一级是否超过名额限制，没有超过返回false，超过，返回true
	 * 
	 * @return
	 */
	public Boolean validateChildNum(Long parentStationId);
}
