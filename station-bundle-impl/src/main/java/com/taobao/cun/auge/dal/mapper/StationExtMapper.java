package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.example.StationExtExample;

import java.util.List;

/**
 * Created by xiao on 16/8/26.
 */
public interface StationExtMapper {

    /**
     * 根据村点名、状态查询村点
     * @param stationExtExample
     * @return
     */
    List<Station> getStationsByName(StationExtExample stationExtExample);
}
