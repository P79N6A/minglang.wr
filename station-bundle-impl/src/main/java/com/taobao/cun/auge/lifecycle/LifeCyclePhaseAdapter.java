package com.taobao.cun.auge.lifecycle;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

public abstract class LifeCyclePhaseAdapter implements LifeCyclePhase {

	@Override
	public PhaseKey getPhaseKey() {
		Phase phase = AnnotationUtils.getAnnotation(this.getClass(),Phase.class);
		Assert.notNull(phase);
		Assert.notNull(phase.event());
		Assert.notNull(phase.type());
		return new PhaseKey(phase.type(),phase.event().getEvent());
	}

	@Override
	public void createOrUpdateStation(LifeCyclePhaseContext context) {

	}

	@Override
	public void createOrUpdatePartner(LifeCyclePhaseContext context) {

	}

	@Override
	public void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {

	}

	@Override
	public void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context) {

	}

	@Override
	public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {

	}

	@Override
	public void triggerStateChangeEvent(LifeCyclePhaseContext context) {

	}

	@Override
	public void syncStationApply(LifeCyclePhaseContext context) {

	}

}
