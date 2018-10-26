package com.taobao.cun.auge.log.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.taobao.cun.auge.log.BizActionEnum;
import com.taobao.cun.auge.log.BizActionLogDto;
import com.taobao.cun.auge.log.bo.BizActionLogBo;
import com.taobao.cun.auge.log.service.BizActionLogService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * 关键动作记录
 * 
 * @author chengyu.zhoucy
 *
 */
@HSFProvider(serviceInterface = BizActionLogService.class)
public class BizActionLogServiceImpl implements BizActionLogService {
	@Resource
	private BizActionLogBo bizActionLogBo;
	
	@Override
	public void addLog(BizActionLogDto bizActionLogAddDto) {
		bizActionLogBo.addLog(bizActionLogAddDto);
	}

	@Override
	public List<BizActionLogDto> getBizActionLogs(Long objectId, String objectType, BizActionEnum bizActionEnum) {
		return bizActionLogBo.getBizActionLogDtos(objectId, objectType, bizActionEnum);
	}

}
