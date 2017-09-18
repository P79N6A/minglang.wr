package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRelExample;
import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRelExample.Criteria;
import com.taobao.cun.auge.dal.mapper.CuntaoCainiaoStationRelMapper;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.dto.CuntaoCainiaoStationRelDto;
import com.taobao.cun.auge.station.enums.CuntaoCainiaoStationRelTypeEnum;

@Component("cuntaoCainiaoStationRelBO")
public class CuntaoCainiaoStationRelBOImpl implements CuntaoCainiaoStationRelBO {

	@Autowired
	CuntaoCainiaoStationRelMapper cuntaoCainiaoStationRelMapper;
	
	@Override
	public CuntaoCainiaoStationRel queryCuntaoCainiaoStationRel(
			Long objectId, CuntaoCainiaoStationRelTypeEnum type)  {
		ValidateUtils.notNull(objectId);
		ValidateUtils.notNull(type);
		CuntaoCainiaoStationRelExample example = new CuntaoCainiaoStationRelExample();
		Criteria criteria = example.createCriteria();
		criteria.andObjectIdEqualTo(objectId);
		criteria.andIsDeletedEqualTo("n");
		criteria.andTypeEqualTo(type.getCode());
		return ResultUtils.selectOne(cuntaoCainiaoStationRelMapper.selectByExample(example));
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Integer deleteCuntaoCainiaoStationRel(Long objectId, CuntaoCainiaoStationRelTypeEnum type)
			 {
		CuntaoCainiaoStationRel  param = new CuntaoCainiaoStationRel();
		DomainUtils.beforeDelete(param, DomainUtils.DEFAULT_OPERATOR);
		CuntaoCainiaoStationRelExample example = new CuntaoCainiaoStationRelExample();
		Criteria criteria = example.createCriteria();
		criteria.andObjectIdEqualTo(objectId);
		criteria.andIsDeletedEqualTo("n");
		criteria.andTypeEqualTo(type.getCode());
		return cuntaoCainiaoStationRelMapper.updateByExampleSelective(param, example);
		
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Long insertCuntaoCainiaoStationRel(CuntaoCainiaoStationRelDto relDto)
			 {
		CuntaoCainiaoStationRel relDO = convertToDomain(relDto);
		DomainUtils.beforeInsert(relDO, relDto.getOperator());
		cuntaoCainiaoStationRelMapper.insert(relDO);
		return relDO.getId();
	}
	
	private CuntaoCainiaoStationRel convertToDomain(CuntaoCainiaoStationRelDto relDto){
		CuntaoCainiaoStationRel relDO = new CuntaoCainiaoStationRel();
		relDO.setCainiaoStationId(relDto.getCainiaoStationId());
		relDO.setIsOwn(relDto.getIsOwn());
		relDO.setLogisticsStationId(relDto.getLogisticsStationId());
		relDO.setObjectId(relDto.getObjectId());
		relDO.setType(relDto.getType().getCode());
		return relDO;
	}

	@Override
	public Long getCainiaoStationId(Long stationId)
			 {
		CuntaoCainiaoStationRel rel = queryCuntaoCainiaoStationRel(stationId,CuntaoCainiaoStationRelTypeEnum.STATION);
		if (rel ==null) {
			return null;
		}
		return rel.getCainiaoStationId();
	}
	
	@Override
	public List<CuntaoCainiaoStationRel> findCainiaoStationRels(List<Long> stationIds) {
		CuntaoCainiaoStationRelExample example = new CuntaoCainiaoStationRelExample();
		example.createCriteria().andIsDeletedEqualTo("n").andObjectIdIn(stationIds).andTypeEqualTo("STATION");
		List<CuntaoCainiaoStationRel> rels = cuntaoCainiaoStationRelMapper.selectByExample(example);
		return rels;
	}
	
	@Override
    public Boolean updateCainiaoStationRel(CuntaoCainiaoStationRel stationRel, String operator) {
		DomainUtils.beforeUpdate(stationRel, operator);
		cuntaoCainiaoStationRelMapper.updateByPrimaryKeySelective(stationRel);
		return true;
	}
}
