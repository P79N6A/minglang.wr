package com.taobao.cun.auge.statemachine;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.Application;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseEvent;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseEventBuilder;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.ConfirmCloseDto;
import com.taobao.cun.auge.station.dto.OpenStationDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
import com.taobao.cun.auge.tpa.TestTpaApplyService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@EnableAutoConfiguration
public class TestStateMachineService {
	
	@Autowired
	private StateMachineService stateMachineService;
	
	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	private PartnerInstanceService partnerInstanceService;
	
	@Test
	public void testTPSettlingWithStateMachine() throws IOException{
		System.err.println("start testTPSettlingWithStateMachine");
		String requestJSON = IOUtils.toString(TestTpaApplyService.class.getClassLoader().getResourceAsStream("tpSettleRequest.json"));
		PartnerInstanceDto request = JSON.parseObject(requestJSON, PartnerInstanceDto.class);
		LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(request,StateMachineEvent.SETTLING_EVENT);
		stateMachineService.executePhase(phaseEvent);
		System.err.println("end testTPSettlingWithStateMachine");
	}
	
	@Test
	public void testTPServicingWithStateMachine() throws IOException{
		System.err.println("start testTPServicingWithStateMachine");
		OpenStationDto openStationDto = new OpenStationDto();
		openStationDto.setImme(true);
		openStationDto.setOpenDate(new Date());
		openStationDto.setOperator("62333");
		openStationDto.setOperatorType(OperatorTypeEnum.BUC);
		openStationDto.setPartnerInstanceId(3648734374l);
		partnerInstanceService.openStation(openStationDto);
		System.err.println("end testTPServicingWithStateMachine");
	}
	
	@Test
	public void testTPClosingWithStateMachine() throws IOException{
		System.err.println("start testTPClosingWithStateMachine");
		partnerInstanceService.applyCloseByPartner(3709109723l);
		System.err.println("end testTPClosingWithStateMachine");
	}
	
	@Test
	public void testTPClosedWithStateMachine() throws IOException{
		System.err.println("start testTPClosedWithStateMachine");
		ConfirmCloseDto confirmCloseDto = new ConfirmCloseDto();
		confirmCloseDto.setAgree(true);
		confirmCloseDto.setPartnerInstanceId(3648734374l);
		confirmCloseDto.setOperator("62333");
		confirmCloseDto.setOperatorType(OperatorTypeEnum.BUC);
		confirmCloseDto.setOperatorOrgId(1l);
		partnerInstanceService.confirmClose(confirmCloseDto);
		System.err.println("end testTPClosedWithStateMachine");
	}
	
	@Test
	public void testTPreServiceWithStateMachine() throws IOException{
		System.err.println("start testTPreServiceWithStateMachine");
		partnerInstanceService.reService(3648734374l, "62333");
		System.err.println("end testTPreServiceWithStateMachine");
	}
	
	
	@Test
	public void testTPASettlingWithStateMachine() throws IOException{
		System.err.println("start testTPSettlingWithStateMachine");
		String requestJSON = IOUtils.toString(TestTpaApplyService.class.getClassLoader().getResourceAsStream("tpaSettleRequest.json"));
		PartnerInstanceDto request = JSON.parseObject(requestJSON, PartnerInstanceDto.class);
		LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(request,StateMachineEvent.SETTLING_EVENT);
		stateMachineService.executePhase(phaseEvent);
		System.err.println("end testTPSettlingWithStateMachine");
	}
	
	
	@Test
	public void testTPDecoratingWithStateMachine() throws InterruptedException{
		System.err.println("start testTPDecoratingWithStateMachine");
		PartnerInstanceSettleSuccessDto settleSuccessDto = new PartnerInstanceSettleSuccessDto();
		settleSuccessDto.setInstanceId(3648734374l);
		settleSuccessDto.setOperator("3709109723");
		settleSuccessDto.setOperatorType(OperatorTypeEnum.HAVANA);
    	PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(settleSuccessDto.getInstanceId());
    	PartnerInstanceDto partnerInstanceDto = new PartnerInstanceDto();
    	partnerInstanceDto.setType(PartnerInstanceTypeEnum.valueof(rel.getType()));
    	partnerInstanceDto.setId(settleSuccessDto.getInstanceId());
    	partnerInstanceDto.copyOperatorDto(settleSuccessDto);
    	partnerInstanceDto.setStationId(rel.getStationId());
    	partnerInstanceDto.setPartnerId(rel.getPartnerId());
    	partnerInstanceDto.setTaobaoUserId(rel.getTaobaoUserId());
    	partnerInstanceDto.setVersion(rel.getVersion());
    	partnerInstanceDto.setState(PartnerInstanceStateEnum.valueof(rel.getState()));
    	StateMachineEvent sme = null;
    	if("TP".equals(partnerInstanceDto.getType().getCode())||"TPT".equals(partnerInstanceDto.getType().getCode())){
    		sme = sme.DECORATING_EVENT;
    	}else{
    		sme = sme.SERVICING_EVENT;
    	}
    	LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(partnerInstanceDto,sme);
		stateMachineService.executePhase(phaseEvent);
		System.err.println("end testTPDecoratingWithStateMachine");
	}
	
	

	@Test
	public void testTPAServicingWithStateMachine(){
		System.err.println("start testTPAServicingWithStateMachine");
		PartnerInstanceDto partnerInstanceDto = new PartnerInstanceDto();
		partnerInstanceDto.setType(PartnerInstanceTypeEnum.TPA);
		LifeCyclePhaseEvent phaseEvent = new LifeCyclePhaseEvent("TPAStateMachine",StateMachineEvent.SERVICING_EVENT,partnerInstanceDto);
		phaseEvent.setCurrentState("SETTLING");
		stateMachineService.executePhase(phaseEvent);
		System.err.println("end testTPAServicingWithStateMachine");
	}
}
