package com.taobao.cun.auge.station.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.dal.mapper.CuntaoCainiaoStationRelMapper;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.exception.AugeServiceException;

@Component("cuntaoCainiaoStationRelBO")
public class CuntaoCainiaoStationRelBOImpl implements CuntaoCainiaoStationRelBO {

	@Autowired
	CuntaoCainiaoStationRelMapper cuntaoCainiaoStationRelMapper;
	
	@Override
	public CuntaoCainiaoStationRel queryCuntaoCainiaoStationRel(
			Long objectId, String type) throws AugeServiceException {
		CuntaoCainiaoStationRel  param = new CuntaoCainiaoStationRel();
		param.setObjectId(objectId);
		param.setType(type);
		param.setIsDeleted("n");
		return cuntaoCainiaoStationRelMapper.selectOne(param);
	}
}
