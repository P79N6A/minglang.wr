package com.taobao.cun.auge.dal.mapper;

import java.util.List;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.example.StationExtExample;
import com.taobao.cun.auge.fence.dto.FenceTemplateStation;
import org.apache.ibatis.annotations.Param;

public interface StationExtMapper {

    /**
     * 根据村点名、状态查询村点
     * @param stationExtExample
     * @return
     */
    List<Station> getTpStationsByName(StationExtExample stationExtExample);
    
    
    List<Station> selectByExample(StationExtExample stationExtExample);

    List<FenceTemplateStation> getFenceTemplateStation(@Param("templateId") Long templateId, @Param("stationName") String stationName);
    
}
