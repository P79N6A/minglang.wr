package com.taobao.cun.auge.statemachine;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.Application;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseEvent;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.TpaApplyInfoDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.tpa.TestTpaApplyService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@EnableAutoConfiguration
public class TestStateMachineService {
	
	@Autowired
	private StateMachineService stateMachineService;
	
	@Test
	public void testTPSettlingWithStateMachine() throws IOException{
		System.err.println("start testTPSettlingWithStateMachine");
		String requestJSON = IOUtils.toString(TestTpaApplyService.class.getClassLoader().getResourceAsStream("tpSettleRequest.json"));
		PartnerInstanceDto request = JSON.parseObject(requestJSON, PartnerInstanceDto.class);
		LifeCyclePhaseEvent phaseEvent = new LifeCyclePhaseEvent("TpStateMachine",StateMachineEvent.SETTLING_EVENT,request);
		stateMachineService.executePhase(phaseEvent);
		System.err.println("end testTPSettlingWithStateMachine");
	}
	
	
	@Test
	public void testTPDecoratingWithStateMachine(){
		System.err.println("start testTPDecoratingWithStateMachine");
		PartnerInstanceDto partnerInstanceDto = new PartnerInstanceDto();
		partnerInstanceDto.setType(PartnerInstanceTypeEnum.TP);
		LifeCyclePhaseEvent phaseEvent = new LifeCyclePhaseEvent("TpStateMachine",StateMachineEvent.DECORATING_EVENT,partnerInstanceDto);
		phaseEvent.setCurrentState("SETTLING");
		stateMachineService.executePhase(phaseEvent);
		System.err.println("end testTPDecoratingWithStateMachine");
	}
	
	
	@Test
	public void testTPASettlingWithStateMachine(){
		System.err.println("start testTPASettlingWithStateMachine");
		PartnerInstanceDto partnerInstanceDto = new PartnerInstanceDto();
		partnerInstanceDto.setType(PartnerInstanceTypeEnum.TPA);
		LifeCyclePhaseEvent phaseEvent = new LifeCyclePhaseEvent("TpaStateMachine",StateMachineEvent.SETTLING_EVENT,partnerInstanceDto);
		stateMachineService.executePhase(phaseEvent);
		System.err.println("end testTPASettlingWithStateMachine");
	}
	
	@Test
	public void testTPAServicingWithStateMachine(){
		System.err.println("start testTPAServicingWithStateMachine");
		PartnerInstanceDto partnerInstanceDto = new PartnerInstanceDto();
		partnerInstanceDto.setType(PartnerInstanceTypeEnum.TPA);
		LifeCyclePhaseEvent phaseEvent = new LifeCyclePhaseEvent("TpaStateMachine",StateMachineEvent.SERVICING_EVENT,partnerInstanceDto);
		phaseEvent.setCurrentState("SETTLING");
		stateMachineService.executePhase(phaseEvent);
		System.err.println("end testTPAServicingWithStateMachine");
	}
}
