package com.taobao.cun.auge.station.service.internal.flows;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.PublishSubscribeSpec;
import org.springframework.integration.dsl.support.Consumer;
import org.springframework.integration.dsl.support.GenericHandler;
import org.springframework.messaging.MessageChannel;

import com.taobao.cun.auge.station.dto.SampleInstance;
import com.taobao.cun.auge.station.service.SampleInternalService;

@Configuration
public class SampleFlowConfigurer {

	@Bean
	@Description("simple example")
	public IntegrationFlow saveSampleFlow(){
		return IntegrationFlows.from(SampleInternalService.ADD_SAMPLE_CHANNEL)
				//.transform("@sampleTransformer.transformStationInstance(payload)")
				.handle("sampleTransformer","transformStationInstance")
				.handle("beanName","method")
				.handle("sampleRepository","save")
				//.split().aggregate().filter(genericSelector)
				//.<Station,Long>transform(station -> station.getId())
				.get();
	}
	
	@Bean
	@Description("rounte example")
	public IntegrationFlow routeExampleFlow(){
		return IntegrationFlows
				.from(SampleInternalService.ROUTE_SAMPLE_CHANNEL)
				.route("payload.type", m -> m.suffix("Channel"))
				.get();
	}
	
	@Bean
	public IntegrationFlow tpExampleFlow(){
		return IntegrationFlows
				.from("tpChannel").handle(m -> {
					System.out.println(((SampleInstance)m.getPayload()).getType());
				})
				.get();
	}
	
	
	@Bean
	public IntegrationFlow tpaExampleFlow(){
		return IntegrationFlows
				.from("tpaChannel").handle( new GenericHandler<SampleInstance>(){

					@Override
					public Object handle(SampleInstance payload, Map<String, Object> headers) {
						System.out.println(payload.getType());
						return null;
					}
					
				},e -> e.requiresReply(false))
				.get();
	}
	
	@Bean
	public IntegrationFlow tpvExampleFlow(){
		return IntegrationFlows
				.from("tpvChannel")
				.handle(tpvChannelHandler(),e -> e.requiresReply(false))
				.get();
	}
	
	@Bean
	GenericHandler<SampleInstance> tpvChannelHandler(){
		return  new GenericHandler<SampleInstance>(){

			public Object handle(SampleInstance payload, Map<String, Object> headers){
				System.out.println(payload.getType());
				return null;
			}
			
		};
	}
	
	@Bean 
	MessageChannel publishSubscribeChannel() {
	   return new PublishSubscribeChannel();
	}
	
	@Bean
	@Description("publishSubscribeSample")
	public IntegrationFlow publishSubscribeSampleFlow(){
		/*return IntegrationFlows
				.from(SampleInternalService.PUBLISH_SUBSCRIBE_SAMPLE_CHANNEL)
				.publishSubscribeChannel(c -> c
						.subscribe(s -> s.handle(m -> System.out.println("sub1")))
						.subscribe(s -> s.handle(m -> System.out.println("sub2"))))
				.get();*/
		
		return IntegrationFlows.from(SampleInternalService.PUBLISH_SUBSCRIBE_SAMPLE_CHANNEL).handle(m -> System.out.println("main") ).channel("AAA").get();
	}
	
	
	@Bean
	IntegrationFlow flowB() {
	   return IntegrationFlows.from("AAA").handle(m -> System.out.println("sub1") ).get();
	}

	@Bean
	IntegrationFlow flowC() {
	   return IntegrationFlows.from("AAA").handle(m -> System.out.println("sub2")).get();
	}
}
