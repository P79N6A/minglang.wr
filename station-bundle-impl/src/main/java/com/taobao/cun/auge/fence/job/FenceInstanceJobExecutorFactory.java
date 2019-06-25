package com.taobao.cun.auge.fence.job;

import javax.annotation.Resource;

import com.taobao.cun.auge.fence.dto.job.*;
import org.springframework.stereotype.Component;

@Component
public class FenceInstanceJobExecutorFactory {
	@Resource
	private ConditionCreateFenceInstanceJobExecutor conditionCreateFenceInstanceJobExecutor;
	
	@Resource
	private ConditionDeleteFenceInstanceJobExecutor conditionDeleteFenceInstanceJobExecutor;
	
	@Resource
	private StationCreateFenceInstanceJobExecutor stationCreateFenceInstanceJobExecutor;
	
	@Resource
	private StationDeleteFenceInstanceJobExecutor stationDeleteFenceInstanceJobExecutor;
	
	@Resource
	private StationUpdateFenceInstanceJobExecutor stationUpdateFenceInstanceJobExecutor;
	
	@Resource
	private TemplateCloseFenceInstanceJobExecutor templateCloseFenceInstanceJobExecutor;
	
	@Resource
	private TemplateOpenFenceInstanceJobExecutor templateOpenFenceInstanceJobExecutor;
	
	@Resource
	private TemplateUpdateFenceInstanceJobExecutor templateUpdateFenceInstanceJobExecutor;
	
	@Resource
	private BatchStationQuitFenceInstanceJobExecutor batchStationQuitFenceInstanceJobExecutor;
	
	@Resource
	private BatchStationInitFenceInstanceJobExecutor batchStationInitFenceInstanceJobExecutor;
	
	@Resource
	private TemplateDeleteFenceInstanceJobExecutor templateDeleteFenceInstanceJobExecutor;

	@Resource
	private CainiaoStationCloseFenceInstanceJobExecutor cainiaoStationCloseFenceInstanceJobExecutor;
	
	@SuppressWarnings("unchecked")
	<F extends FenceInstanceJob> FenceInstanceJobExecutor<F> getFenceInstanceJobExecutor(F fenceInstanceJob){
		if(fenceInstanceJob instanceof ConditionCreateFenceInstanceJob) {
			return (FenceInstanceJobExecutor<F>) conditionCreateFenceInstanceJobExecutor;
		}
		
		if(fenceInstanceJob instanceof ConditionDeleteFenceInstanceJob) {
			return (FenceInstanceJobExecutor<F>) conditionDeleteFenceInstanceJobExecutor;
		}
		
		if(fenceInstanceJob instanceof StationCreateFenceInstanceJob) {
			return (FenceInstanceJobExecutor<F>) stationCreateFenceInstanceJobExecutor;
		}
		
		if(fenceInstanceJob instanceof StationDeleteFenceInstanceJob) {
			return (FenceInstanceJobExecutor<F>) stationDeleteFenceInstanceJobExecutor;
		}
		
		if(fenceInstanceJob instanceof StationUpdateFenceInstanceJob) {
			return (FenceInstanceJobExecutor<F>) stationUpdateFenceInstanceJobExecutor;
		}
		
		if(fenceInstanceJob instanceof TemplateCloseFenceInstanceJob) {
			return (FenceInstanceJobExecutor<F>) templateCloseFenceInstanceJobExecutor;
		}
		
		if(fenceInstanceJob instanceof TemplateOpenFenceInstanceJob) {
			return (FenceInstanceJobExecutor<F>) templateOpenFenceInstanceJobExecutor;
		}
		
		if(fenceInstanceJob instanceof TemplateUpdateFenceInstanceJob) {
			return (FenceInstanceJobExecutor<F>) templateUpdateFenceInstanceJobExecutor;
		}
		
		if(fenceInstanceJob instanceof BatchStationQuitFenceInstanceJob) {
			return (FenceInstanceJobExecutor<F>) batchStationQuitFenceInstanceJobExecutor;
		}
		
		if(fenceInstanceJob instanceof BatchStationInitFenceInstanceJob) {
			return (FenceInstanceJobExecutor<F>) batchStationInitFenceInstanceJobExecutor;
		}
		
		if(fenceInstanceJob instanceof TemplateDeleteFenceInstanceJob) {
			return (FenceInstanceJobExecutor<F>) templateDeleteFenceInstanceJobExecutor;
		}

		if(fenceInstanceJob instanceof CainiaoStationCloseFenceInstanceJob){
			return (FenceInstanceJobExecutor<F>)cainiaoStationCloseFenceInstanceJobExecutor;
		}
		
		return null;
	}
}
