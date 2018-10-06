package com.taobao.cun.auge.station.transfer.process;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.taobao.cun.auge.log.BizActionEnum;
import com.taobao.cun.auge.log.bo.BizActionLogBo;
import com.taobao.cun.auge.org.bo.CuntaoOrgBO;
import com.taobao.cun.auge.org.dto.OrgDeptType;
import com.taobao.cun.auge.station.transfer.CountyStationTransferBo;
import com.taobao.cun.auge.station.transfer.StationTransferBo;
import com.taobao.cun.auge.station.transfer.TransferItemBo;
import com.taobao.cun.auge.station.transfer.TransferJobBo;
import com.taobao.cun.auge.station.transfer.dto.CountyStationTransferDetail;
import com.taobao.cun.auge.station.transfer.state.CountyTransferStateMgrBo;
import com.taobao.cun.auge.station.transfer.state.StationTransferStateMgrBo;

/**
 * 转交审批
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class TransferAuditBo {
	@Resource
	private CountyStationTransferBo countyStationTransferBo;
	@Resource
	private StationTransferBo stationTransferBo;
	@Resource
	private CountyTransferStateMgrBo countyTransferStateMgrBo;
	@Resource
	private StationTransferStateMgrBo stationTransferStateMgrBo;
	@Resource
	private CuntaoOrgBO cuntaoOrgBO;
	@Resource
	private TransferJobBo transferJobBo;
	@Resource
	private TransferItemBo transferItemBo;
	@Resource
	private BizActionLogBo bizActionLogBo;
	
	private Map<String, TransferHandler> transferHandlers = Maps.newHashMap();
	
	public TransferAuditBo() {
		transferHandlers.put("county", new CountyTransferHandler());
		transferHandlers.put("station", new StationTransferHandler());
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void agree(String applyId, String operator, Long orgId) {
		CountyStationTransferDetail detail = transferJobBo.getCountyStationTransferDetail(Long.parseLong(applyId));
		transferHandlers.get(detail.getType()).agree(detail, operator, orgId);
		transferJobBo.updateTransferJobState(Long.parseLong(applyId), "AGREE");
		transferItemBo.updateStateByJobId(Long.parseLong(applyId), "AGREE");
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void disagree(String applyId, String operator) {
		CountyStationTransferDetail detail = transferJobBo.getCountyStationTransferDetail(Long.parseLong(applyId));
		transferHandlers.get(detail.getType()).disagree(detail);
		transferJobBo.updateTransferJobState(Long.parseLong(applyId), "DISAGREE");
		transferItemBo.updateStateByJobId(Long.parseLong(applyId), "DISAGREE");
	}
	
	interface TransferHandler{
		void agree(CountyStationTransferDetail detail, String userId, Long orgId);
		void disagree(CountyStationTransferDetail detail);
	}
	
	abstract class AbstractTransferHandler implements TransferHandler{
		void addStationBizActionLog(List<Long> stationIds, String userId, Long orgId) {
			for(Long stationId : stationIds) {
				bizActionLogBo.addLog(stationId, "station", userId, orgId, OrgDeptType.extdept.name(), BizActionEnum.station_transfer_finished);
			}
		}
	}
	
	class CountyTransferHandler extends AbstractTransferHandler{

		@Override
		public void agree(CountyStationTransferDetail detail, String userId, Long orgId) {
			//更新站点的转交状态为FINISHED
			stationTransferStateMgrBo.endTransfer(detail.getStationIds());
			//更新县点的转交状态为FINISHED
			countyTransferStateMgrBo.endTransfer(detail.getCountyStation().getId());
			//将县在运营部的组织挂到指定的特战队
			cuntaoOrgBO.updateParent(orgId, detail.getTargetTeamOrgId());
			//记录日志
			bizActionLogBo.addLog(detail.getCountyStation().getId(), "county", userId, orgId, OrgDeptType.extdept.name(), BizActionEnum.countystation_transfer_finished);
			addStationBizActionLog(detail.getStationIds(), userId, orgId);
		}

		@Override
		public void disagree(CountyStationTransferDetail detail) {
			//更新站点的转交状态为FINISHED
			countyTransferStateMgrBo.cancelTransfer(detail.getCountyStation().getId());
			//更新站点的转交状态为WAITING
			stationTransferStateMgrBo.cancelTransfer(detail.getStationIds());
		}
	}
	
	class StationTransferHandler extends AbstractTransferHandler{

		@Override
		public void agree(CountyStationTransferDetail detail, String userId, Long orgId) {
			//更新站点的转交状态为FINISHED
			stationTransferStateMgrBo.endTransfer(detail.getStationIds());
			addStationBizActionLog(detail.getStationIds(), userId, orgId);
		}

		@Override
		public void disagree(CountyStationTransferDetail detail) {
			//更新站点的转交状态为WAITING
			stationTransferStateMgrBo.cancelTransfer(detail.getStationIds());
		}
	}
}
