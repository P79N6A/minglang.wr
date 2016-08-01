package com.taobao.cun.auge.transforms;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Transformer;

import com.taobao.cun.auge.dal.domain.Sample;
import com.taobao.cun.auge.station.dto.SampleInstance;

@MessageEndpoint
public class SampleTransformer {

	@Transformer
	public Sample transformStationInstance(SampleInstance instance){
		Sample sample = new Sample();
		return sample;
	}
}
