package com.taobao.cun.auge.station.service;

import java.util.List;

import com.taobao.cun.auge.station.dto.PartnerPeixunDto;

/**
 * 合伙人培训服务
 * @author yi.shaoy
 *
 */
public interface PartnerPeixunService {

	public PartnerPeixunDto queryPartnerPeixunProcess(Long userId);
	
	public List<PartnerPeixunDto> queryBatchPeixunPocess(List<Long> userIds);
}
