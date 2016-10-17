package com.taobao.cun.auge.station.service.impl;

import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.WisdomCountyApply;
import com.taobao.cun.auge.station.bo.WisdomCountyApplyBO;
import com.taobao.cun.auge.station.convert.WisdomCountyApplyConverter;
import com.taobao.cun.auge.station.dto.WisdomCountyApplyDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.WisdomCountyApplyService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xiao on 16/10/17.
 */
@Service("wisdomCountyApplyService")
@HSFProvider(serviceInterface = WisdomCountyApplyService.class)
public class WisdomCountyApplyServiceImpl implements WisdomCountyApplyService{

    @Autowired
    WisdomCountyApplyBO wisdomCountyApplyBO;

    @Override
    public WisdomCountyApplyDto getWisdomCountyApplyByCountyId(Long countyId) throws AugeServiceException {
        ValidateUtils.notNull(countyId);
        WisdomCountyApply wisdomCountyApply = wisdomCountyApplyBO.getWisdomCountyApplyByCountyId(countyId);
        return WisdomCountyApplyConverter.toDto(wisdomCountyApply);
    }
}
