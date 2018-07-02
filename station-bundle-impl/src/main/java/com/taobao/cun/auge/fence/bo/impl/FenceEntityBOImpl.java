package com.taobao.cun.auge.fence.bo.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.dal.domain.FenceEntityExample;
import com.taobao.cun.auge.dal.mapper.FenceEntityMapper;
import com.taobao.cun.auge.fence.bo.FenceEntityBO;
import com.taobao.cun.auge.fence.constant.FenceConstants;
import com.taobao.cun.auge.fence.dto.FenceTemplateStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xiao on 18/6/20.
 */
@Component
public class FenceEntityBOImpl implements FenceEntityBO {

    @Autowired
    private FenceEntityMapper entityMapper;

    @Override
    public void enableEntityListByTemplateId(Long templateId, String operator) {
        FenceEntityExample example = new FenceEntityExample();
        example.createCriteria().andIsDeletedEqualTo("n").andTemplateIdEqualTo(templateId);
        FenceEntity fenceEntity = new FenceEntity();
        fenceEntity.setState(FenceConstants.ENABLE);
        DomainUtils.beforeUpdate(fenceEntity, operator);
        entityMapper.updateByExampleSelective(fenceEntity, example);
    }

    @Override
    public void disableEntityListByTemplateId(Long templateId, String operator) {
        FenceEntityExample example = new FenceEntityExample();
        example.createCriteria().andIsDeletedEqualTo("n").andTemplateIdEqualTo(templateId);
        FenceEntity fenceEntity = new FenceEntity();
        fenceEntity.setState(FenceConstants.DISABLE);
        DomainUtils.beforeUpdate(fenceEntity, operator);
        entityMapper.updateByExampleSelective(fenceEntity, example);
    }

    @Override
    public Integer getFenceEntityCountByTemplateId(Long templateId) {
        FenceEntityExample example = new FenceEntityExample();
        example.createCriteria().andIsDeletedEqualTo("n").andTemplateIdEqualTo(templateId);
        return entityMapper.countByExample(example);
    }

    @Override
    public List<Long> getTemplateIdListByStationId(Long stationId) {
        FenceEntityExample example = new FenceEntityExample();
        example.createCriteria().andIsDeletedEqualTo("n").andStationIdEqualTo(stationId);
        List<FenceEntity> entityList = entityMapper.selectByExample(example);
        return entityList.stream().map(FenceEntity::getTemplateId).collect(Collectors.toList());
    }

    @Override
    public void deleteStationFenceTemplate(FenceTemplateStation fenceTemplateStation) {
        FenceEntityExample example = new FenceEntityExample();
        example.createCriteria().andIsDeletedEqualTo("n").andStationIdEqualTo(fenceTemplateStation.getStationId())
            .andTemplateIdEqualTo(fenceTemplateStation.getTemplateId());
        FenceEntity fenceEntity = new FenceEntity();
        fenceEntity.setIsDeleted("y");
        DomainUtils.beforeUpdate(fenceEntity, fenceTemplateStation.getOperator());
        entityMapper.updateByExampleSelective(fenceEntity, example);
    }

}
