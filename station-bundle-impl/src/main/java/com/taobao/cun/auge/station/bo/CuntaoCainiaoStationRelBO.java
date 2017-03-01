package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.station.dto.CuntaoCainiaoStationRelDto;
import com.taobao.cun.auge.station.enums.CuntaoCainiaoStationRelTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 菜鸟物流关系基础服务接口
 * @author quanzhu.wangqz
 *
 */
public interface CuntaoCainiaoStationRelBO {
	/**
	 * 查询菜鸟物流关系
	 * @param objectId
	 * @param type
	 * @return
	 * @throws AugeServiceException
	 */
	public CuntaoCainiaoStationRel queryCuntaoCainiaoStationRel(Long objectId,CuntaoCainiaoStationRelTypeEnum type) throws AugeServiceException;
	
	/**
	 * 删除菜鸟物流关系
	 * @param objectId
	 * @param type
	 * @return
	 * @throws AugeServiceException
	 */
	public Integer deleteCuntaoCainiaoStationRel(Long objectId,CuntaoCainiaoStationRelTypeEnum type) throws AugeServiceException;
	/**
	 * 新增菜鸟物流关系
	 * @param relDto
	 * @return
	 * @throws AugeServiceException
	 */
	public void insertCuntaoCainiaoStationRel(CuntaoCainiaoStationRelDto relDto) throws AugeServiceException;
	
	
	/**
	 * 查询菜鸟物流关系
	 * @param objectId
	 * @param type
	 * @return
	 * @throws AugeServiceException
	 */
	public Long getCainiaoStationId(Long stationId) throws AugeServiceException;
}
