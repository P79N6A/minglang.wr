package com.taobao.cun.auge.county;

import java.util.List;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.county.dto.CountyDto;
import com.taobao.cun.auge.county.dto.CountyQueryCondition;
import com.taobao.cun.auge.county.dto.CountyStationQueryCondition;
import com.taobao.cun.auge.county.dto.CountyPOI;
/**
 * 从center迁移至auge
 * 供org、motion、无线端调用
 * @author yi.shaoy
 *
 */
public interface CountyService {

	/**
	 * 新增或更新县运营中心
	 * 
	 * @param operator
	 * @param countyDto
	 * @return
	 */
	public CountyDto saveCountyStation(String operator,CountyDto countyDto);
	
	/**
	 * 查询大区列表下的省编码与名称列表
	 * 
	 * @param areaOrgIds
	 * @return
	 */
	public List<CountyDto> getProvinceList(List<Long> areaOrgIds);
	
	/**
	 * 查询省下的县服务站列表
	 * 
	 * @param provinceCode
	 * @return
	 */
	public List<CountyDto> getCountyStationByProvince(String provinceCode);

	/**
	 * 查询大区列表下的县运营中心列表
	 * 
	 * @param areaIds
	 * @return
	 */
	public List<CountyDto> getCountyStationList(List<Long> areaIds);
	
	/**
	 * 查询县运营中心的信息
	 * 
	 * @param isMobile
	 * @param id
	 *            县运营中心id
	 * @return
	 */
	public CountyDto getCountyStation(Long id,Boolean isMobile);

	/**
	 * 查询县运营中心的信息
	 *
	 * @param id
	 *            组织id
	 * @return
	 */
	public CountyDto getCountyStationByOrgId(Long id);
	
	/**
	 * 根据组织id，查询行政地址county code
	 * @param orgIds
	 * @return
	 */
	public List<Long> getCountiesByOrgId(List<Long> orgIds);

	/**
	 * 根据orgId获取县列表
	 * @param orgIds
	 * @return
	 */
	public List<CountyDto> getCountyListByOrgIds(List<Long> orgIds);

    /**
     * 根据市行政code获取县列表
     * @param cityCode
     * @return
     */
    public List<CountyDto> getCountyStationByCity(String cityCode);
	
	/**
	 * 查询大区下的县运营中心列表
	 * 
	 * @param queryCondition
	 * @return
	 */
	public PageDto<CountyDto> getCountyStationList(CountyStationQueryCondition queryCondition);
	
	/**
	 * 县点分页查询  原crius服务 迁移至auge
	 * 查询参数：countyName 			- 县点名称
	 * 			state				- 经营状态
	 * 			countyOfficial		- 县负责人
	 * 			teamLeader			- 特战队长
	 * 			fullIdPath			- 组织的全路径
	 * @return
	 */
	public PageDto<CountyDto> queryCountyStation(CountyQueryCondition queryCondition);
	
	public CountyDto startOperate(String operator,CountyDto countyDto);
	
	/**
	 * 
	 * @param countyAreaId 县点淘标ID
	 * @return
	 */
	public CountyPOI queryCountyPOI(Long countyAreaId);
}
