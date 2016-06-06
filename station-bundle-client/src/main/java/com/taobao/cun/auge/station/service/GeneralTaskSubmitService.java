package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface GeneralTaskSubmitService {

	public void submitFreezeBondTasks(PartnerInstanceDto instance) throws AugeServiceException;

}
