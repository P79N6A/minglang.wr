package com.taobao.cun.auge.station.transfer.ultimate.handle;

import javax.annotation.Priority;
import javax.annotation.Resource;

import com.taobao.cun.auge.annotation.Tag;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.log.BizActionEnum;
import com.taobao.cun.auge.log.bo.BizActionLogBo;
import com.taobao.cun.auge.org.dto.OrgDeptType;
import com.taobao.cun.auge.station.transfer.state.CountyTransferStateMgrBo;
import org.springframework.stereotype.Component;

/**
 * 处理县点：县点OWN_DEPT变更成OrgDeptType.OP_DEPT
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
@Priority(100)
@Tag({HandlerGroup.AUTO, HandlerGroup.ADVANCE})
public class CountyUltimateTransferHandler implements UltimateTransferHandler {
	@Resource
	private CountyTransferStateMgrBo countyTransferStateMgrBo;
	@Resource
	private BizActionLogBo bizActionLogBo;
	
	@Override
	public void transfer(CountyStation countyStation, String operator, Long opOrgId) {
		countyTransferStateMgrBo.autoTransfer(countyStation.getId());
		bizActionLogBo.addLog(countyStation.getId(), "county", operator, opOrgId, OrgDeptType.extdept.name(), BizActionEnum.countystation_final_transfer_finished);
	}
}