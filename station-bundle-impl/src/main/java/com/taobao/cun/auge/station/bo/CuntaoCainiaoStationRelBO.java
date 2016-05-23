package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface CuntaoCainiaoStationRelBO {

	public CuntaoCainiaoStationRel queryCuntaoCainiaoStationRel(Long objectId,String type) throws AugeServiceException;;
}
