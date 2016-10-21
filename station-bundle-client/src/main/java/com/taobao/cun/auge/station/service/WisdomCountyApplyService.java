package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.WisdomCountyApplyCondition;
import com.taobao.cun.auge.station.dto.WisdomCountyApplyAuditDto;
import com.taobao.cun.auge.station.dto.WisdomCountyApplyDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

import java.util.List;
import java.util.Map;

/**
 * Created by xiao on 16/10/17.
 */
public interface WisdomCountyApplyService {

    public WisdomCountyApplyDto getWisdomCountyApplyByCountyId(Long countyId) throws AugeServiceException;

    public Long addWisdomCountyApply(WisdomCountyApplyDto wisdomCountyApplyDto) throws AugeServiceException;

    public WisdomCountyApplyDto getWisdomCountyApplyById(Long id) throws AugeServiceException;

    public PageDto<WisdomCountyApplyDto> queryByPage(WisdomCountyApplyCondition condition) throws AugeServiceException;

    public Map<Long, WisdomCountyApplyDto> getWisdomCountyApplyByCountyIds(List<Long> ids) throws AugeServiceException;

    public void updateWisdomCountyApply(WisdomCountyApplyDto wisdomCountyApplyDto) throws AugeServiceException;

    public boolean audit(WisdomCountyApplyAuditDto dto) throws AugeServiceException;

}
