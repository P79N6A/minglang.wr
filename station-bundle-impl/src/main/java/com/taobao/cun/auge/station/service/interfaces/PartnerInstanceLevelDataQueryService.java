package com.taobao.cun.auge.station.service.interfaces;

import java.util.List;

import com.taobao.cun.auge.station.dto.PartnerInstanceLevelGrowthDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelGrowthDtoV2;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelGrowthTrendDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelGrowthTrendDtoV2;

public interface PartnerInstanceLevelDataQueryService {

    /**
     * 获取合伙人层级成长信息
     * @param taobaoUserId
     * @return
     */
    public PartnerInstanceLevelGrowthDto getPartnerInstanceLevelGrowthData(Long taobaoUserId);
    
    /**
     * 获取合伙人成长趋势指标数据
     * @param taobaoUserId
     * @param statDate
     * @return
     */
    public List<PartnerInstanceLevelGrowthTrendDto> getPartnerInstanceLevelGrowthTrendData(Long taobaoUserId, String statDate);
    
    
    /**
     * 获取合伙人层级成长信息
     * @param taobaoUserId
     * @return
     */
    public PartnerInstanceLevelGrowthDtoV2 getPartnerInstanceLevelGrowthDataV2(Long taobaoUserId);
    
    /**
     * 获取合伙人成长趋势指标数据
     * @param taobaoUserId
     * @param statDate
     * @return
     */
    public List<PartnerInstanceLevelGrowthTrendDtoV2> getPartnerInstanceLevelGrowthTrendDataV2(Long taobaoUserId, String statDate);
}
