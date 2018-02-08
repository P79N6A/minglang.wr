package com.taobao.cun.auge.station.service.impl;

import java.util.List;

import com.taobao.cun.auge.dal.domain.ProcessedStationStatus;
import com.taobao.cun.auge.station.condition.StationStatisticsCondition;

/**
 * Created by linjianke on 2017/8/17.
 */
public interface ProcessedStationStatusExecutor {

    public List<ProcessedStationStatus> execute(StationStatisticsCondition condition);
}