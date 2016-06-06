package com.taobao.cun.auge.station.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.CloseStationApply;
import com.taobao.cun.auge.dal.domain.CloseStationApplyExample;
import com.taobao.cun.auge.dal.mapper.CloseStationApplyMapper;
import com.taobao.cun.auge.station.bo.CloseStationApplyBO;
import com.taobao.cun.auge.station.convert.CloseStationApplyConverter;
import com.taobao.cun.auge.station.dto.CloseStationApplyDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceCloseTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.validator.BeanValidator;

@Component("closeStationApplyBO")
public class CloseStationApplyBOImpl implements CloseStationApplyBO {
	
	@Autowired
	CloseStationApplyMapper closeStationApplyMapper;

	@Override
	public Long addCloseStationApply(CloseStationApplyDto closeStationApplyDto)
			throws AugeServiceException {
		// 参数校验
		BeanValidator.validateWithThrowable(closeStationApplyDto);
		CloseStationApplyDto applydto = getCloseStationApply(closeStationApplyDto.getPartnerInstanceId());
		if (applydto != null) {
			throw new AugeServiceException(CommonExceptionEnum.RECORD_EXISTS);
		}
		CloseStationApply closeStationApply = CloseStationApplyConverter.toCloseStationApply(closeStationApplyDto);
		DomainUtils.beforeInsert(closeStationApply, closeStationApplyDto.getOperator());
		closeStationApplyMapper.insert(closeStationApply);
		return closeStationApply.getId();
	}

	@Override
	public void deleteCloseStationApply(Long partnerInstanceId, String operator)
			throws AugeServiceException {
		ValidateUtils.notNull(partnerInstanceId);
		ValidateUtils.notNull(operator);
		CloseStationApply closeStationApply =new CloseStationApply();
		DomainUtils.beforeDelete(closeStationApply, operator);
		
		CloseStationApplyExample example = new CloseStationApplyExample();
		example.createCriteria().andIsDeletedEqualTo("n").andPartnerInstanceIdEqualTo(partnerInstanceId);
		
		closeStationApplyMapper.updateByExampleSelective(closeStationApply, example);
	}

	@Override
	public CloseStationApplyDto getCloseStationApply(Long partnerInstanceId) throws AugeServiceException {
		ValidateUtils.notNull(partnerInstanceId);
		
		CloseStationApplyExample example = new CloseStationApplyExample();
		example.createCriteria().andIsDeletedEqualTo("n").andPartnerInstanceIdEqualTo(partnerInstanceId);
		return  CloseStationApplyConverter.toCloseStationApplyDto(ResultUtils.selectOne(closeStationApplyMapper.selectByExample(example)));
	}

}
