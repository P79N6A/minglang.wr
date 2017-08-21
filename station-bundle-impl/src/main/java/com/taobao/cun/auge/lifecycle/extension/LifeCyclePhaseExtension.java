package com.taobao.cun.auge.lifecycle.extension;

import com.taobao.cun.auge.lifecycle.RuntimeMetaInfoCollector.PhaseInfo;

public interface LifeCyclePhaseExtension {

	public boolean support(PhaseInfo phaseInfo); 
}
