package com.taobao.cun.auge.fence.bo.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.dal.domain.FenceTemplate;
import com.taobao.cun.auge.dal.domain.FenceTemplateExample;
import com.taobao.cun.auge.dal.domain.FenceTemplateExample.Criteria;
import com.taobao.cun.auge.dal.mapper.FenceTemplateMapper;
import com.taobao.cun.auge.dal.mapper.StationExtMapper;
import com.taobao.cun.auge.fence.bo.FenceTemplateBO;
import com.taobao.cun.auge.fence.constant.FenceConstants;
import com.taobao.cun.auge.fence.dto.FenceTemplateDetailDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateListDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateQueryCondition;
import com.taobao.cun.auge.fence.dto.FenceTemplateStation;
import com.taobao.cun.auge.fence.enums.FenceTypeEnum;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.github.pagehelper.Page;

/**
 * Created by xiao on 18/6/15.
 */
@Component
public class FenceTemplateBOImpl implements FenceTemplateBO {

    @Autowired
    private FenceTemplateMapper templateMapper;

    @Autowired
    private Emp360Adapter emp360Adapter;

    @Autowired
    private StationExtMapper stationExtMapper;

    @Override
    public Long addFenceTemplate(FenceTemplateDetailDto detailDto) {
        FenceTemplate fenceTemplate = new FenceTemplate();
        BeanUtils.copyProperties(detailDto, fenceTemplate);
        DomainUtils.beforeInsert(fenceTemplate, detailDto.getOperator());
        templateMapper.insert(fenceTemplate);
        return fenceTemplate.getId();
    }

    @Override
    public void updateFenceTemplate(FenceTemplateDetailDto detailDto) {
        FenceTemplate fenceTemplate = new FenceTemplate();
        BeanUtils.copyProperties(detailDto, fenceTemplate);
        DomainUtils.beforeUpdate(fenceTemplate, detailDto.getOperator());
        templateMapper.updateByPrimaryKeySelective(fenceTemplate);
    }

    @Override
    public PageDto<FenceTemplateListDto> getFenceTemplateList(FenceTemplateQueryCondition condition) {
        FenceTemplateExample example = new FenceTemplateExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        if (!StringUtils.isEmpty(condition.getName())) {
            criteria.andNameLike("%" + condition.getName() + "%");
        }
        if (!StringUtils.isEmpty(condition.getType())) {
            criteria.andTypeEqualTo(condition.getType());
        }
        if (!StringUtils.isEmpty(condition.getLimitCommodity())) {
            criteria.andLimitCommodityEqualTo(condition.getLimitCommodity());
        }
        if (!StringUtils.isEmpty(condition.getState())) {
            criteria.andStateEqualTo(condition.getState());
        }
        PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
        List<FenceTemplate> templateList = templateMapper.selectByExample(example);
        List<FenceTemplateListDto> dtoList = templateList.stream().map(template -> {
            FenceTemplateListDto listDto = new FenceTemplateListDto();
            BeanUtils.copyProperties(template, listDto);
            listDto.setTypeEnum(FenceTypeEnum.valueOf(template.getType()));
            listDto.setModifierName(emp360Adapter.getName(template.getModifier()));
            return listDto;
        }).collect(Collectors.toList());
        return PageDtoUtil.success((Page<FenceTemplate>)templateList, dtoList);
    }

    @Override
    public FenceTemplateDetailDto getFenceTemplateById(Long id) {
        FenceTemplate fenceTemplate = templateMapper.selectByPrimaryKey(id);
        return Optional.ofNullable(fenceTemplate).map(template -> {
            FenceTemplateDetailDto detailDto = new FenceTemplateDetailDto();
            BeanUtils.copyProperties(fenceTemplate, detailDto);
            return detailDto;
        }).orElse(null);
    }

    @Override
    public void enableFenceTemplate(Long id, String operator) {
        FenceTemplate fenceTemplate = templateMapper.selectByPrimaryKey(id);
        fenceTemplate.setState(FenceConstants.ENABLE);
        DomainUtils.beforeUpdate(fenceTemplate, operator);
        templateMapper.updateByPrimaryKey(fenceTemplate);
    }

    @Override
    public void disableFenceTemplate(Long id, String operator) {
        FenceTemplate fenceTemplate = templateMapper.selectByPrimaryKey(id);
        fenceTemplate.setState(FenceConstants.DISABLE);
        DomainUtils.beforeUpdate(fenceTemplate, operator);
        templateMapper.updateByPrimaryKey(fenceTemplate);
    }

    @Override
    public PageDto<FenceTemplateStation> getFenceTemplateStationList(FenceTemplateQueryCondition condition) {
        PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
        Page<FenceTemplateStation> templateStationList = (Page<FenceTemplateStation>)stationExtMapper.getFenceTemplateStation(
            condition.getTemplateId());
        return PageDtoUtil.success(templateStationList, templateStationList);
    }

}
