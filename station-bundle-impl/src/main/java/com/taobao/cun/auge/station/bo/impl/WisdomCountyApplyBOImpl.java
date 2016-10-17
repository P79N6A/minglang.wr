package com.taobao.cun.auge.station.bo.impl;

import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.WisdomCountyApply;
import com.taobao.cun.auge.dal.domain.WisdomCountyApplyExample;
import com.taobao.cun.auge.dal.mapper.WisdomCountyApplyMapper;
import com.taobao.cun.auge.station.bo.WisdomCountyApplyBO;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xiao on 16/10/17.
 */
@Component("wisdomCountyApplyBO")
public class WisdomCountyApplyBOImpl implements WisdomCountyApplyBO{

    @Autowired
    WisdomCountyApplyMapper wisdomCountyApplyMapper;

    @Override
    public WisdomCountyApply getWisdomCountyApplyByCountyId(Long countyId) throws AugeServiceException {
        ValidateUtils.notNull(countyId);
        WisdomCountyApplyExample example = new WisdomCountyApplyExample();
        WisdomCountyApplyExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andCountyIdEqualTo(countyId);
        return ResultUtils.selectOne(wisdomCountyApplyMapper.selectByExample(example));
    }
}
