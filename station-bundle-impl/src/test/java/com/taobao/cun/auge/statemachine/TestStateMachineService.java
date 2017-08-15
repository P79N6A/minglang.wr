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
import com.taobao.cun.auge.station.dto.ForcedCloseDto;
import com.taobao.cun.auge.station.dto.OpenStationDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.enums.AssertUseStateEnum;
import com.taobao.cun.auge.station.enums.CloseStationApplyCloseReasonEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.enums.RemoveBrandUserTypeEnum;
import com.taobao.cun.auge.station.notify.listener.ProcessProcessor;
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
	
	@Autowired
	private ProcessProcessor processProcessor;
	@Test
	public void testTPSettlingWithStateMachine() throws IOException{
		System.err.println("start testTPSettlingWithStateMachine");
		String requestJSON = IOUtils.toString(TestTpaApplyService.class.getClassLoader().getResourceAsStream("tpSettleRequest.json"));
		PartnerInstanceDto request = JSON.parseObject(requestJSON, PartnerInstanceDto.class);
		partnerInstanceService.applySettle(request);
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
	public void testTPClosingByPartnerWithStateMachine() throws IOException{
		System.err.println("start testTPClosingWithStateMachine");
		partnerInstanceService.applyCloseByPartner(3709109723l);
		System.err.println("end testTPClosingWithStateMachine");
	}
	
	@Test
	public void testTPClosingByManagerWithStateMachine() throws IOException{
		System.err.println("start testTPClosingWithStateMachine");
		ForcedCloseDto forceClosedDto = new ForcedCloseDto();
		forceClosedDto.setInstanceId(3648734374l);
		forceClosedDto.setOperator("62333");
		forceClosedDto.setOperatorType(OperatorTypeEnum.BUC);
		forceClosedDto.setOperatorOrgId(1l);
		forceClosedDto.setReason(CloseStationApplyCloseReasonEnum.ASSESS_FAIL);
		forceClosedDto.setRemarks("test");
		partnerInstanceService.applyCloseByManager(forceClosedDto);
		System.err.println("end testTPClosingWithStateMachine");
	}
	
	@Test
	public void testTPClosedByWorkFlowWithStateMachine() throws Exception{
		System.err.println("start testTPClosedWithStateMachine");
		processProcessor.closeApprove(3648734374l, ProcessApproveResultEnum.APPROVE_PASS);
		System.err.println("end testTPClosedWithStateMachine");
	}
	
	
	
	@Test
	public void testTPClosedRefusedWithStateMachine() throws IOException{
		System.err.println("start testTPClosedWithStateMachine");
		ConfirmCloseDto confirmCloseDto = new ConfirmCloseDto();
		confirmCloseDto.setAgree(false);
		confirmCloseDto.setPartnerInstanceId(3648734374l);
		confirmCloseDto.setOperator("62333");
		confirmCloseDto.setOperatorType(OperatorTypeEnum.BUC);
		confirmCloseDto.setOperatorOrgId(1l);
		partnerInstanceService.confirmClose(confirmCloseDto);
		System.err.println("end testTPClosedWithStateMachine");
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
	public void testTPQuitingWithStateMachine() throws IOException{
		System.err.println("start testTPQuitingWithStateMachine");
		QuitStationApplyDto quitDto = new QuitStationApplyDto();
		quitDto.setApprovalFileName("test");
		quitDto.setAssertUseState(AssertUseStateEnum.HAS_RETURN);
		quitDto.setInstanceId(3648734374l);
		quitDto.setIsQuitStation(true);
		quitDto.setOperator("62333");
		quitDto.setOperatorOrgId(1l);
		quitDto.setOperatorType(OperatorTypeEnum.BUC);
		quitDto.setIsRemoveBrand(true);
		quitDto.setLoanHasClose(true);
		quitDto.setLoanProveFileName("test");
		quitDto.setOtherDescription("test");
		quitDto.setRemoveBrandFileName("test");
		quitDto.setRemoveBrandUserType(RemoveBrandUserTypeEnum.PARTNER);
		partnerInstanceService.applyQuitByManager(quitDto);
		System.err.println("end testTPQuitingWithStateMachine");
	}
	
	
	@Test
	public void testTPQuitingKeepStationWithStateMachine() throws IOException{
		System.err.println("start testTPQuitingWithStateMachine");
		QuitStationApplyDto quitDto = new QuitStationApplyDto();
		quitDto.setApprovalFileName("test");
		quitDto.setAssertUseState(AssertUseStateEnum.HAS_RETURN);
		quitDto.setInstanceId(3648734374l);
		quitDto.setIsQuitStation(false);
		quitDto.setOperator("62333");
		quitDto.setOperatorOrgId(1l);
		quitDto.setOperatorType(OperatorTypeEnum.BUC);
		quitDto.setIsRemoveBrand(true);
		quitDto.setLoanHasClose(true);
		quitDto.setLoanProveFileName("test");
		quitDto.setOtherDescription("test");
		quitDto.setRemoveBrandFileName("test");
		quitDto.setRemoveBrandUserType(RemoveBrandUserTypeEnum.PARTNER);
		partnerInstanceService.applyQuitByManager(quitDto);
		System.err.println("end testTPQuitingWithStateMachine");
	}
	
	
	@Test
	public void testTPreServiceWithStateMachine() throws IOException{
		System.err.println("start testTPreServiceWithStateMachine");
		partnerInstanceService.reService(3648734374l, "62333");
		System.err.println("end testTPreServiceWithStateMachine");
	}
	
	@Test
	public void testTPQuitRefusedWithStateMachine() throws Exception{
		System.err.println("start testTPQuitRefusedWithStateMachine");
		processProcessor.quitApprove(3648734374l, ProcessApproveResultEnum.APPROVE_REFUSE);
		System.err.println("end testTPQuitRefusedWithStateMachine");
	}
	
	
	@Test
	public void testTPQuitByAuditFlowWithStateMachine() throws Exception{
		System.err.println("start testTPQuitByAuditFlowWithStateMachine");
		processProcessor.quitApprove(3648734374l, ProcessApproveResultEnum.APPROVE_PASS);
		System.err.println("end testTPQuitByAuditFlowWithStateMachine");
	}
	
	@Test
	public void testTPQuitByThawTaskWithStateMachine() throws Exception{
		System.err.println("start testTPQuitByThawTaskWithStateMachine");
		PartnerInstanceQuitDto partnerInstanceQuitDto = new PartnerInstanceQuitDto();
		partnerInstanceQuitDto.setInstanceId(3648734374l);
		partnerInstanceQuitDto.setOperator("system");
		partnerInstanceQuitDto.setOperatorType(OperatorTypeEnum.SYSTEM);
		partnerInstanceService.quitPartnerInstance(partnerInstanceQuitDto);
		System.err.println("end testTPQuitByThawTaskWithStateMachine");
	}
	
	
	
	@Test
	public void testTPASettlingWithStateMachine() throws IOException{
		System.err.println("start testTPASettlingWithStateMachine");
		String requestJSON = IOUtils.toString(TestTpaApplyService.class.getClassLoader().getResourceAsStream("tpaSettleRequest.json"));
		PartnerInstanceDto request = JSON.parseObject(requestJSON, PartnerInstanceDto.class);
		partnerInstanceService.applySettle(request);
		System.err.println("end testTPASettlingWithStateMachine");
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
		PartnerInstanceSettleSuccessDto settleSuccessDto = new PartnerInstanceSettleSuccessDto();
		settleSuccessDto.setInstanceId(3648734375l);
		settleSuccessDto.setOperator("3709109723");
		settleSuccessDto.setOperatorType(OperatorTypeEnum.HAVANA);
		partnerInstanceService.applySettleSuccess(settleSuccessDto);
		System.err.println("end testTPAServicingWithStateMachine");
	}
	
	
	@Test
	public void testTPAClosingByManagerWithStateMachine() throws IOException{
		System.err.println("start testTPClosingWithStateMachine");
		ForcedCloseDto forceClosedDto = new ForcedCloseDto();
		forceClosedDto.setInstanceId(3648734375l);
		forceClosedDto.setOperator("62333");
		forceClosedDto.setOperatorType(OperatorTypeEnum.BUC);
		forceClosedDto.setOperatorOrgId(1l);
		forceClosedDto.setReason(CloseStationApplyCloseReasonEnum.ASSESS_FAIL);
		forceClosedDto.setRemarks("test");
		partnerInstanceService.applyCloseByManager(forceClosedDto);
		System.err.println("end testTPClosingWithStateMachine");
	}
	
	
	@Test
	public void testTPAQuitingByManagerWithStateMachine() throws IOException{
		System.err.println("start testTPAQuitByManagerWithStateMachine");
		QuitStationApplyDto quitDto = new QuitStationApplyDto();
		quitDto.setApprovalFileName("test");
		quitDto.setAssertUseState(AssertUseStateEnum.HAS_RETURN);
		quitDto.setInstanceId(3648734375l);
		quitDto.setIsQuitStation(true);
		quitDto.setOperator("62333");
		quitDto.setOperatorOrgId(1l);
		quitDto.setOperatorType(OperatorTypeEnum.BUC);
		quitDto.setIsRemoveBrand(true);
		quitDto.setLoanHasClose(true);
		quitDto.setLoanProveFileName("test");
		quitDto.setOtherDescription("test");
		quitDto.setRemoveBrandFileName("test");
		quitDto.setRemoveBrandUserType(RemoveBrandUserTypeEnum.PARTNER);
		partnerInstanceService.applyQuitByManager(quitDto);
		System.err.println("end testTPAQuitByManagerWithStateMachine");
	}
	
	
	@Test
	public void testTPAQuitByThawTaskWithStateMachine() throws Exception{
		System.err.println("start testTPAQuitByThawTaskWithStateMachine");
		PartnerInstanceQuitDto partnerInstanceQuitDto = new PartnerInstanceQuitDto();
		partnerInstanceQuitDto.setInstanceId(3648734375l);
		partnerInstanceQuitDto.setOperator("system");
		partnerInstanceQuitDto.setOperatorType(OperatorTypeEnum.SYSTEM);
		partnerInstanceService.quitPartnerInstance(partnerInstanceQuitDto);
		System.err.println("end testTPAQuitByThawTaskWithStateMachine");
	}
	
}
