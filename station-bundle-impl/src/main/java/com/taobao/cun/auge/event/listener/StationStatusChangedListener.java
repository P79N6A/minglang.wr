package com.taobao.cun.auge.event.listener;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.station.bo.Emp360BO;
import com.taobao.cun.crius.bpm.dto.CuntaoProcessInstance;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;
import com.taobao.cun.dto.flow.enums.CuntaoFlowOperateOpinionEnum;
import com.taobao.cun.dto.flow.enums.CuntaoFlowTargetTypeEnum;
import com.taobao.cun.dto.permission.enums.PermissionNameEnum;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;

@EventSub("station-status-changed-event")
public class StationStatusChangedListener implements EventListener {

    @Autowired
    private CuntaoWorkFlowService  cuntaoWorkFlowService;
    
    @Autowired
    private Emp360BO                          emp360BO;
    
	@Override
	public void onMessage(Event arg0) {
		// TODO Auto-generated method stub

	}
	
	private void createQuitingTask(){
		
		String businessCode = "station-quit";
		String stationApplyId ;
		String operatorId ;
		
		String orgId;
		String remarks;
		
//      // 创建退出村点任务流程
		Map<String,String> initData = new HashMap<String, String>();
		initData.put("orgId", orgId);
		initData.put("remarks", remarks);
		com.taobao.cun.crius.common.resultmodel.ResultModel<CuntaoProcessInstance> rm = cuntaoWorkFlowService
				.startProcessInstance(businessCode,stationApplyId, operatorId, initData);
		String procId = null;
		if(rm.isSuccess()){
			procId = rm.getResult().getProcessInstanceId();
			//村点停业和退出的日志，触达
			stationQuitFlowDto.setOperateOpinion(CuntaoFlowOperateOpinionEnum.COMMIT.getCode());
			if(stationQuitFlowDto.getType().equals(CuntaoFlowTargetTypeEnum.STATION_FORCED_CLOSURE.getCode())){
				sendForcedClosureEvent(stationQuitFlowDto,context,applyId,"QUIT_APPLYING");
				noticeNextOperator(PermissionNameEnum.BOPS_FORCE_QUIT_PROVINCE_AUDIT,applyId,context);
			}else if(stationQuitFlowDto.getType().equals(CuntaoFlowTargetTypeEnum.STATION_QUIT.getCode())){
				sendRecordEvent(stationQuitFlowDto,context,applyId,"提交");
				noticeNextOperator(PermissionNameEnum.BOPS_STATION_PROVINCE_AUDIT,applyId,context);
			}
		}
		return procId;
	}

}
