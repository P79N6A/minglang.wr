package com.taobao.cun.auge.fence.bo.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.dal.domain.FenceEntityExample;
import com.taobao.cun.auge.dal.mapper.FenceEntityMapper;
import com.taobao.cun.auge.dal.mapper.ext.ExtFenceEntityMapper;
import com.taobao.cun.auge.fence.bo.FenceEntityBO;
import com.taobao.cun.auge.fence.constant.FenceConstants;
import com.taobao.cun.auge.fence.dto.FenceTemplateStation;

/**
 * Created by xiao on 18/6/20.
 */
@Component
public class FenceEntityBOImpl implements FenceEntityBO {

    @Autowired
    private FenceEntityMapper entityMapper;
    
    @Autowired
    private ExtFenceEntityMapper extFenceEntityMapper;

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
    public void deleteFenceTemplateStation(FenceTemplateStation fenceTemplateStation) {
        FenceEntityExample example = new FenceEntityExample();
        example.createCriteria().andIsDeletedEqualTo("n").andStationIdEqualTo(fenceTemplateStation.getStationId())
            .andTemplateIdEqualTo(fenceTemplateStation.getTemplateId());
        FenceEntity fenceEntity = new FenceEntity();
        fenceEntity.setIsDeleted("y");
        DomainUtils.beforeUpdate(fenceEntity, fenceTemplateStation.getOperator());
        entityMapper.updateByExampleSelective(fenceEntity, example);
    }

	@Override
	public List<FenceEntity> getFenceEntityByTemplateId(Long templateId) {
		FenceEntityExample example = new FenceEntityExample();
        example.createCriteria().andIsDeletedEqualTo("n").andTemplateIdEqualTo(templateId);
		return entityMapper.selectByExample(example);
	}

	@Override
	public void addFenceEntity(FenceEntity fenceEntity) {
		entityMapper.insert(fenceEntity);
	}

	@Override
	public void updateFenceEntity(FenceEntity fenceEntity) {
		entityMapper.updateByPrimaryKeyWithBLOBs(fenceEntity);
	}

	@Override
	public void deleteFences(Long stationId, String fenceType) {
		 FenceEntityExample example = new FenceEntityExample();
	        example.createCriteria().andIsDeletedEqualTo("n").andStationIdEqualTo(stationId)
	            .andTypeEqualTo(fenceType);
	        FenceEntity fenceEntity = new FenceEntity();
	        fenceEntity.setIsDeleted("y");
	        fenceEntity.setGmtModified(new Date());
	        entityMapper.updateByExampleSelective(fenceEntity, example);
	}

	@Override
	public List<FenceEntity> getStationFenceEntitiesByFenceType(Long stationId, String fenceType) {
		FenceEntityExample example = new FenceEntityExample();
        example.createCriteria().andIsDeletedEqualTo("n").andStationIdEqualTo(stationId)
            .andTypeEqualTo(fenceType);
		return entityMapper.selectByExample(example);
	}

	@Override
	public FenceEntity getStationFenceEntityByTemplateId(Long stationId, Long templateId) {
		FenceEntityExample example = new FenceEntityExample();
        example.createCriteria().andIsDeletedEqualTo("n").andStationIdEqualTo(stationId)
            .andTemplateIdEqualTo(templateId);
        List<FenceEntity> fenceEntities = entityMapper.selectByExample(example);
		if(CollectionUtils.isNotEmpty(fenceEntities)) {
			return fenceEntities.get(0);
		}else {
			return null;
		}
	}

	@Override
	public void deleteById(Long id, String operator) {
		FenceEntityExample example = new FenceEntityExample();
        example.createCriteria().andIsDeletedEqualTo("n").andIdEqualTo(id);
        FenceEntity fenceEntity = new FenceEntity();
        fenceEntity.setIsDeleted("y");
        fenceEntity.setModifier(operator);
        fenceEntity.setGmtModified(new Date());
        entityMapper.updateByExampleSelective(fenceEntity, example);
	}

	@Override
	public List<FenceEntity> getFenceEntitiesByStationId(Long stationId) {
		FenceEntityExample example = new FenceEntityExample();
        example.createCriteria().andIsDeletedEqualTo("n").andStationIdEqualTo(stationId);
		return entityMapper.selectByExample(example);
	}

	@Override
	public List<FenceEntity> getStationQuitedFenceEntities() {
		return extFenceEntityMapper.selectStationQuitedFenceEntities();
	}
    
}
