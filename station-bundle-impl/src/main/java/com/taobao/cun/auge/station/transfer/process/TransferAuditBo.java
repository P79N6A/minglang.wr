package com.taobao.cun.auge.station.transfer.process;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.taobao.cun.auge.org.bo.CuntaoOrgBO;
import com.taobao.cun.auge.station.transfer.CountyStationTransferBo;
import com.taobao.cun.auge.station.transfer.StationTransferBo;
import com.taobao.cun.auge.station.transfer.TransferItemBo;
import com.taobao.cun.auge.station.transfer.TransferJobBo;
import com.taobao.cun.auge.station.transfer.dto.CountyStationTransferDetail;

@Component
public class TransferAuditBo {
	@Resource
	private CountyStationTransferBo countyStationTransferBo;
	@Resource
	private StationTransferBo stationTransferBo;
	@Resource
	private CuntaoOrgBO cuntaoOrgBO;
	@Resource
	private TransferJobBo transferJobBo;
	@Resource
	private TransferItemBo transferItemBo;
	
	private Map<String, TransferHandler> transferHandlers = Maps.newHashMap();
	
	public TransferAuditBo() {
		transferHandlers.put("county", new CountyTransferHandler());
		transferHandlers.put("station", new StationTransferHandler());
	}
	
	@Transactional
	public void agree(String applyId, String operator, Long orgId) {
		CountyStationTransferDetail detail = transferJobBo.getCountyStationTransferDetail(Long.parseLong(applyId));
		transferHandlers.get(detail.getType()).agree(detail, operator, orgId);
		transferJobBo.updateTransferJobState(Long.parseLong(applyId), "AGREE");
		transferItemBo.updateStateByJobId(Long.parseLong(applyId), "AGREE");
	}
	
	@Transactional
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
	
	class CountyTransferHandler implements TransferHandler{

		@Override
		public void agree(CountyStationTransferDetail detail, String userId, Long orgId) {
			//更新站点的转交状态为FINISHED
			stationTransferBo.endTransfer(detail.getStationIds());
			//更新县点的转交状态为FINISHED
			countyStationTransferBo.endTransfer(detail.getCountyStation().getId());
			//将县在运营部的组织挂到指定的特战队
			cuntaoOrgBO.updateParent(orgId, detail.getTargetTeamOrgId());
		}

		@Override
		public void disagree(CountyStationTransferDetail detail) {
			//更新站点的转交状态为FINISHED
			countyStationTransferBo.cancelTransfer(detail.getCountyStation().getId());
			//更新站点的转交状态为WAITING
			stationTransferBo.cancelTransfer(detail.getStationIds());
		}
	}
	
	class StationTransferHandler implements TransferHandler{

		@Override
		public void agree(CountyStationTransferDetail detail, String userId, Long orgId) {
			//更新站点的转交状态为FINISHED
			stationTransferBo.endTransfer(detail.getStationIds());
		}

		@Override
		public void disagree(CountyStationTransferDetail detail) {
			//更新站点的转交状态为WAITING
			stationTransferBo.cancelTransfer(detail.getStationIds());
		}
	}
}
