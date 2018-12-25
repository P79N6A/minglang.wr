package com.taobao.cun.auge.level.service;

import javax.annotation.Resource;

import com.taobao.cun.auge.level.bo.TownLevelUpdateApplyBo;
import com.taobao.cun.auge.level.dto.TownLevelUpdateApplyDto;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface= TownLevelUpdateApplyService.class)
public class TownLevelUpdateApplyServiceImpl implements TownLevelUpdateApplyService {
	@Resource
	private TownLevelUpdateApplyBo townLevelUpdateApplyBo;
	
	@Override
	public void apply(TownLevelUpdateApplyDto townLevelUpdateApplyDto) {
		BeanValidator.validateWithThrowable(townLevelUpdateApplyDto);
		townLevelUpdateApplyBo.apply(townLevelUpdateApplyDto);
	}

	@Override
	public void agree(String applyId) {
		townLevelUpdateApplyBo.agree(applyId);
	}

	@Override
	public void disagree(String applyId) {
		townLevelUpdateApplyBo.disagree(applyId);
	}

}
