package com.taobao.cun.auge.station.transfer.auto;

import javax.annotation.Priority;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.log.BizActionEnum;
import com.taobao.cun.auge.log.bo.BizActionLogBo;
import com.taobao.cun.auge.org.dto.OrgDeptType;
import com.taobao.cun.auge.station.transfer.state.CountyTransferStateMgrBo;

/**
 * 处理县点：县点OWN_DEPT变更成OrgDeptType.OP_DEPT
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
@Priority(100)
public class CountyAutoTransferHandler implements AutoTransferHandler {
	@Resource
	private CountyTransferStateMgrBo countyTransferStateMgrBo;
	@Resource
	private BizActionLogBo bizActionLogBo;
	
	@Override
	public void transfer(CountyStation countyStation, String operator, Long opOrgId) {
		countyTransferStateMgrBo.autoTransfer(countyStation.getId());
		bizActionLogBo.addLog(countyStation.getId(), "county", operator, opOrgId, OrgDeptType.extdept.name(), BizActionEnum.countystation_auto_transfer_finished);
	}
}
