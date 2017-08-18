package com.taobao.cun.auge.station.service.impl;

import com.taobao.cun.auge.dal.domain.ProcessedStationStatus;
import com.taobao.cun.auge.station.condition.StationStatisticsCondition;

import java.util.List;

/**
 * Created by linjianke on 2017/8/17.
 */
public interface ProcessedStationStatusExecutor {

    public List<ProcessedStationStatus> execute(StationStatisticsCondition condition);
}