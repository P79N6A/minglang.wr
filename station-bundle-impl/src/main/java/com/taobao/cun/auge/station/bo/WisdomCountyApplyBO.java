package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.WisdomCountyApply;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * Created by xiao on 16/10/17.
 */
public interface WisdomCountyApplyBO {

    public WisdomCountyApply getWisdomCountyApplyByCountyId(Long countyId) throws AugeServiceException;

}
