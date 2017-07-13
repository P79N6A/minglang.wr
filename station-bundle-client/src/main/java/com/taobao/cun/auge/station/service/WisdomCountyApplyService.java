package com.taobao.cun.auge.station.service;

import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.WisdomCountyApplyCondition;
import com.taobao.cun.auge.station.dto.WisdomCountyApplyAuditDto;
import com.taobao.cun.auge.station.dto.WisdomCountyApplyDto;

/**
 * Created by xiao on 16/10/17.
 */
public interface WisdomCountyApplyService {

    public WisdomCountyApplyDto getWisdomCountyApplyByCountyId(Long countyId);

    public Long addWisdomCountyApply(WisdomCountyApplyDto wisdomCountyApplyDto);

    public WisdomCountyApplyDto getWisdomCountyApplyById(Long id);

    public PageDto<WisdomCountyApplyDto> queryByPage(WisdomCountyApplyCondition condition);

    public Map<Long, WisdomCountyApplyDto> getWisdomCountyApplyByCountyIds(List<Long> ids);

    public void updateWisdomCountyApply(WisdomCountyApplyDto wisdomCountyApplyDto);

    public void deleteWisdomCountyApplyByCountyId(Long countyId, String operator);

    public boolean audit(WisdomCountyApplyAuditDto dto);

    public void apply(WisdomCountyApplyDto wisdomCountyApplyDto);

}
