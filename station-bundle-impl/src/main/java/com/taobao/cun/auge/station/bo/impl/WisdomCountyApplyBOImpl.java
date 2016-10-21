package com.taobao.cun.auge.station.bo.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
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
import com.taobao.cun.auge.station.dto.WisdomCountyApplyAuditDto;
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
    public PageDto<WisdomCountyApplyDto> queryByPage(WisdomCountyApplyCondition condition) throws AugeServiceException {
        ValidateUtils.notNull(condition);
        WisdomCountyApplyExtExample extExample = WisdomCountyApplyConverter.conditionToExtExample(condition);
        PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
        Page<WisdomCountyApply> page = wisdomCountyApplyExtMapper.getPageWisdomCountyApply(extExample);
        return PageDtoUtil.success(page, page.stream().map(WisdomCountyApplyConverter::toDto).collect(Collectors.toList()));
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

    @Override
    public void updateWisdomCountyApply(WisdomCountyApplyDto wisdomCountyApplyDto) throws AugeServiceException {
        ValidateUtils.notNull(wisdomCountyApplyDto.getId());
        WisdomCountyApply apply = WisdomCountyApplyConverter.dtoTo(wisdomCountyApplyDto);
        DomainUtils.beforeUpdate(apply, wisdomCountyApplyDto.getOperator());
        wisdomCountyApplyMapper.updateByPrimaryKeySelective(apply);
    }

    @Override
    public boolean audit(WisdomCountyApplyAuditDto auditDto) throws AugeServiceException {
        ValidateUtils.notNull(auditDto.getId());
        ValidateUtils.notNull(auditDto.getState());
        WisdomCountyApply wisdomCountyApply = wisdomCountyApplyMapper.selectByPrimaryKey(auditDto.getId());
        if (!"APPLY".equals(wisdomCountyApply.getState())){
            throw new AugeServiceException("该县点不处于报名状态中");
        }
        wisdomCountyApply.setState(auditDto.getState().getCode());
        DomainUtils.beforeUpdate(wisdomCountyApply, auditDto.getOperator());
        return wisdomCountyApplyMapper.updateByPrimaryKeySelective(wisdomCountyApply) == 1;
    }
}
