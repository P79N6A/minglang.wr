package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.station.condition.WisdomCountyApplyCondition;
import com.taobao.cun.auge.station.dto.WisdomCountyApplyDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

import java.util.List;
import java.util.Map;

/**
 * Created by xiao on 16/10/17.
 */
public interface WisdomCountyApplyBO {

    public WisdomCountyApplyDto getWisdomCountyApplyByCountyId(Long countyId) throws AugeServiceException;

    public Long addWisdomCountyApply(WisdomCountyApplyDto dto) throws AugeServiceException;

    public WisdomCountyApplyDto  getWisdomCountyApplyById(Long countyId) throws AugeServiceException;

    public List<WisdomCountyApplyDto> getPageWisdomCountyApply(WisdomCountyApplyCondition condition) throws AugeServiceException;

    public Map<Long, WisdomCountyApplyDto> getWisdomCountyApplyByCountyIds(List<Long> ids) throws AugeServiceException;
}
