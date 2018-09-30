package com.taobao.cun.auge.log.bo;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.BizActionLog;
import com.taobao.cun.auge.dal.domain.BizActionLogExample;
import com.taobao.cun.auge.dal.mapper.BizActionLogMapper;
import com.taobao.cun.auge.log.BizActionEnum;
import com.taobao.cun.auge.log.BizActionLogDto;
import com.taobao.cun.common.util.BeanCopy;

@Component
public class BizActionLogBo {
	@Resource
	private BizActionLogMapper bizActionLogMapper;
	
	public void addLog(BizActionLogDto bizActionLogAddDto) {
		BizActionLog record = new BizActionLog();
		record.setActionDesc(bizActionLogAddDto.getBizActionEnum().desc);
		record.setActionName(bizActionLogAddDto.getBizActionEnum().name());
		record.setGmtCreate(new Date());
		record.setOpOrgId(bizActionLogAddDto.getOpOrgId());
		record.setOpWorkId(bizActionLogAddDto.getOpWorkId());
		record.setObjectId(bizActionLogAddDto.getObjectId());
		record.setObjectType(bizActionLogAddDto.getObjectType());
		record.setRoleName(bizActionLogAddDto.getRoleName());
		record.setValue1(bizActionLogAddDto.getValue1());
		record.setValue2(bizActionLogAddDto.getValue2());
		bizActionLogMapper.insert(record);
	}
	
	public List<BizActionLogDto> getBizActionLogDtos(Long objectId, String objectType, BizActionEnum bizActionEnum){
		BizActionLogExample example = new BizActionLogExample();
		example.createCriteria().andObjectIdEqualTo(objectId)
		.andObjectTypeEqualTo(objectType)
		.andActionNameEqualTo(bizActionEnum.name());
		List<BizActionLog> list = bizActionLogMapper.selectByExample(example);
		
		List<BizActionLogDto> bizActionLogDtos = Lists.newArrayList();
		for(BizActionLog bizActionLog : list) {
			BizActionLogDto bizActionLogDto = BeanCopy.copy(BizActionLogDto.class, bizActionLog);
			bizActionLogDto.setBizActionEnum(BizActionEnum.valueOf(bizActionLog.getActionName()));
			bizActionLogDtos.add(bizActionLogDto);
		}
		return bizActionLogDtos;
	}
}
