package com.taobao.cun.auge.lifecycle.validator.impl;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.PhaseExtensionSupport;
import com.taobao.cun.auge.lifecycle.RuntimeMetaInfoCollector.PhaseInfo;
import com.taobao.cun.auge.lifecycle.RuntimeMetaInfoCollector.StepInfo;
import com.taobao.cun.auge.lifecycle.validator.LifeCyclePhaseValidator;

@Component
@PhaseExtensionSupport(validationExtCode=LifeCyclePhaseValidator.ALIPAY_ACCOUNT_VALIDATION)
public class AlipayAccountValidator implements LifeCyclePhaseValidator{

	@Override
	public boolean support(PhaseInfo phaseInfo) {
		 
		for(StepInfo step : phaseInfo.getStepInfos()){
			
		}
		return false;
	}

	@Override
	public void validate(LifeCyclePhaseContext context) {
		// TODO Auto-generated method stub
		
	}

}
