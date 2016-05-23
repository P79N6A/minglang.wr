package com.taobao.cun.auge.station.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tk.mybatis.mapper.entity.Example;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.dal.mapper.CuntaoCainiaoStationRelMapper;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.dto.CuntaoCainiaoStationRelDto;
import com.taobao.cun.auge.station.enums.CuntaoCainiaoStationRelTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

@Component("cuntaoCainiaoStationRelBO")
public class CuntaoCainiaoStationRelBOImpl implements CuntaoCainiaoStationRelBO {

	@Autowired
	CuntaoCainiaoStationRelMapper cuntaoCainiaoStationRelMapper;
	
	@Override
	public CuntaoCainiaoStationRel queryCuntaoCainiaoStationRel(
			Long objectId, CuntaoCainiaoStationRelTypeEnum type) throws AugeServiceException {
		CuntaoCainiaoStationRel  param = new CuntaoCainiaoStationRel();
		param.setObjectId(objectId);
		param.setType(type.getCode());
		param.setIsDeleted("n");
		return cuntaoCainiaoStationRelMapper.selectOne(param);
	}

	@Override
	public Integer deleteCuntaoCainiaoStationRel(Long objectId, CuntaoCainiaoStationRelTypeEnum type)
			throws AugeServiceException {
		CuntaoCainiaoStationRel  param = new CuntaoCainiaoStationRel();
		DomainUtils.beforeDelete(param, DomainUtils.DEFAULT_OPERATOR);
		Example example = new Example(CuntaoCainiaoStationRel.class);
		example.createCriteria().andCondition("objectId", objectId).andCondition("type",type.getCode()).andCondition("isDeleted","n");
		return cuntaoCainiaoStationRelMapper.updateByExampleSelective(param, example);
	}

	@Override
	public void insertCuntaoCainiaoStationRel(CuntaoCainiaoStationRelDto relDto)
			throws AugeServiceException {
		CuntaoCainiaoStationRel relDO = convertToDomain(relDto);
		DomainUtils.beforeInsert(relDO, relDto.getOperator());
		cuntaoCainiaoStationRelMapper.insert(relDO);
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
}
