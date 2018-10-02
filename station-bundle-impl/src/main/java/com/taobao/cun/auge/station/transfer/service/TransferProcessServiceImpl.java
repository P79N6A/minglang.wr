package com.taobao.cun.auge.station.transfer.service;

import javax.annotation.Resource;

import com.taobao.cun.auge.station.transfer.TransferException;
import com.taobao.cun.auge.station.transfer.TransferJobBo;
import com.taobao.cun.auge.station.transfer.dto.CountyStationTransferCondition;
import com.taobao.cun.auge.station.transfer.dto.CountyStationTransferDetail;
import com.taobao.cun.auge.station.transfer.dto.TransferJob;
import com.taobao.cun.auge.station.transfer.process.TransferAuditBo;
import com.taobao.cun.auge.station.transfer.process.TransferProcessFacade;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = TransferProcessService.class)
public class TransferProcessServiceImpl implements TransferProcessService {
	@Resource
	private TransferProcessFacade transferProcessFacade;
	@Resource
	private TransferAuditBo transferAuditBo;
	@Resource
	private TransferJobBo transferJobBo;
	
	
	public void startTransferProcess(TransferJob transferJob) throws TransferException{
		transferProcessFacade.startTransferProcess(transferJob);
	}
	
	public CountyStationTransferCondition prepare(Long countyStationId) {
		return transferProcessFacade.prepare(countyStationId);
	}

	@Override
	public void agree(String applyId, String operator, long orgId) {
		transferAuditBo.agree(applyId, operator, orgId);
	}

	@Override
	public void disagree(String applyId, String operator) {
		transferAuditBo.disagree(applyId, operator);
	}

	@Override
	public CountyStationTransferDetail getCountyStationTransferDetail(Long id) {
		return transferJobBo.getCountyStationTransferDetail(id);
	}
	
}
