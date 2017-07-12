package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.CuntaoFlowRecord;
import com.taobao.cun.auge.flowRecord.condition.CuntaoFlowRecordCondition;

public interface CuntaoFlowRecordBO {

	public void addRecord(CuntaoFlowRecord record);

	public CuntaoFlowRecord getRecordByTargetIdAndType(CuntaoFlowRecordCondition condition);
}
