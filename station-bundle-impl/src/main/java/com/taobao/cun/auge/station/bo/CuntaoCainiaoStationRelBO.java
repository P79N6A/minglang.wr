package com.taobao.cun.auge.station.bo;

import java.util.List;

import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.station.dto.CuntaoCainiaoStationRelDto;
import com.taobao.cun.auge.station.enums.CuntaoCainiaoStationRelTypeEnum;

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
	 * @
	 */
	public CuntaoCainiaoStationRel queryCuntaoCainiaoStationRel(Long objectId,CuntaoCainiaoStationRelTypeEnum type) ;
	
	/**
	 * 删除菜鸟物流关系
	 * @param objectId
	 * @param type
	 * @return
	 * @
	 */
	public Integer deleteCuntaoCainiaoStationRel(Long objectId,CuntaoCainiaoStationRelTypeEnum type) ;
	/**
	 * 新增菜鸟物流关系
	 * @param relDto
	 * @return
	 * @
	 */
	public Long insertCuntaoCainiaoStationRel(CuntaoCainiaoStationRelDto relDto) ;
	
	
	/**
	 * 查询菜鸟物流关系
	 * @param objectId
	 * @param type
	 * @return
	 * @
	 */
	public Long getCainiaoStationId(Long stationId) ;
	
	
	public List<CuntaoCainiaoStationRel> findCainiaoStationRels(List<Long> stationId);
	
	public Boolean updateCainiaoStationRel(CuntaoCainiaoStationRel stationRel,String operator);
}
