package com.taobao.cun.auge.station.bo.impl;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.WisdomCountyApply;
import com.taobao.cun.auge.dal.domain.WisdomCountyApplyExample;
import com.taobao.cun.auge.dal.domain.WisdomCountyApplyExtExample;
import com.taobao.cun.auge.dal.mapper.WisdomCountyApplyExtMapper;
import com.taobao.cun.auge.dal.mapper.WisdomCountyApplyMapper;
import com.taobao.cun.auge.station.bo.WisdomCountyApplyBO;
import com.taobao.cun.auge.station.condition.WisdomCountyApplyCondition;
import com.taobao.cun.auge.station.convert.WisdomCountyApplyConverter;
import com.taobao.cun.auge.station.dto.WisdomCountyApplyDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xiao on 16/10/17.
 */
@Component("wisdomCountyApplyBO")
public class WisdomCountyApplyBOImpl implements WisdomCountyApplyBO{

    @Autowired
    WisdomCountyApplyMapper wisdomCountyApplyMapper;

    @Autowired
    WisdomCountyApplyExtMapper wisdomCountyApplyExtMapper;

    @Override
    public WisdomCountyApplyDto getWisdomCountyApplyByCountyId(Long countyId) throws AugeServiceException {
        ValidateUtils.notNull(countyId);
        WisdomCountyApplyExample example = new WisdomCountyApplyExample();
        WisdomCountyApplyExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andCountyIdEqualTo(countyId);
        WisdomCountyApply wisdomCountyApply = ResultUtils.selectOne(wisdomCountyApplyMapper.selectByExample(example));
        return WisdomCountyApplyConverter.toDto(wisdomCountyApply);
    }

    @Override
    public Long addWisdomCountyApply(WisdomCountyApplyDto dto) throws AugeServiceException {
        ValidateUtils.notNull(dto);
        WisdomCountyApply wisdomCountyApply = WisdomCountyApplyConverter.dtoTo(dto);
        DomainUtils.beforeInsert(wisdomCountyApply, dto.getOperator());
        wisdomCountyApplyMapper.insert(wisdomCountyApply);
        return wisdomCountyApply.getId();
    }

    @Override
    public WisdomCountyApplyDto getWisdomCountyApplyById(Long id) throws AugeServiceException {
        ValidateUtils.notNull(id);
        WisdomCountyApply wisdomCountyApply = wisdomCountyApplyMapper.selectByPrimaryKey(id);
        return WisdomCountyApplyConverter.toDto(wisdomCountyApply);
    }

    @Override
    public List<WisdomCountyApplyDto> getPageWisdomCountyApply(WisdomCountyApplyCondition condition) throws AugeServiceException {
        ValidateUtils.notNull(condition);
        WisdomCountyApplyExtExample extExample = WisdomCountyApplyConverter.conditonToExtExample(condition);
        List<WisdomCountyApply> wisdomCountyApply = wisdomCountyApplyExtMapper.getPageWisdomCountyApply(extExample);
        return wisdomCountyApply.stream().map(WisdomCountyApplyConverter::toDto).collect(Collectors.toList());
    }

    @Override
    public Map<Long, WisdomCountyApplyDto> getWisdomCountyApplyByCountyIds(List<Long> ids) throws AugeServiceException {
        ValidateUtils.notEmpty(ids);
        WisdomCountyApplyExample example = new WisdomCountyApplyExample();
        WisdomCountyApplyExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andCountyIdIn(ids);
        List<WisdomCountyApply> wisdomCountyApplies = wisdomCountyApplyMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(wisdomCountyApplies)){
            return wisdomCountyApplies.stream().collect(Collectors.toMap(WisdomCountyApply::getCountyId, WisdomCountyApplyConverter::toDto));
        }
        return null;
    }
}
