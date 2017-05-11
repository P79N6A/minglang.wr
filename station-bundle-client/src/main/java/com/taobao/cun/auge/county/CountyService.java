package com.taobao.cun.auge.county;

import java.util.List;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.county.dto.CountyDto;
import com.taobao.cun.auge.county.dto.CountyQueryCondition;
import com.taobao.cun.auge.county.dto.CountyStationQueryCondition;
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
	 * @param context
	 * @param countyStationDto
	 * @return
	 */
	public CountyDto saveCountyStation(String operator,CountyDto countyDto);
	
	/**
	 * 查询大区列表下的省编码与名称列表
	 * 
	 * @param areaIds
	 * @return
	 */
	public List<CountyDto> getProvinceList(List<Long> areaOrgIds);
	
	/**
	 * 查询省下的县服务站列表
	 * 
	 * @param areaIds
	 * @return
	 */
	public List<CountyDto> getCountyStationByProvince(String provinceCode);

	/**
	 * 查询大区列表下的县运营中心列表
	 * 
	 * @param context
	 * @param areaIds
	 * @return
	 */
	public List<CountyDto> getCountyStationList(List<Long> areaIds);
	
	/**
	 * 查询县运营中心的信息
	 * 
	 * @param context
	 * @param id
	 *            县运营中心id
	 * @return
	 */
	public CountyDto getCountyStation(Long id,Boolean isMobile);

	/**
	 * 查询县运营中心的信息
	 *
	 * @param context
	 * @param id
	 *            组织id
	 * @return
	 */
	public CountyDto getCountyStationByOrgId(Long id);
	
	/**
	 * 查询大区下的县运营中心列表
	 * 
	 * @param context
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
	 * @param in
	 * @return
	 */
	public PageDto<CountyDto> queryCountyStation(CountyQueryCondition queryCondition);
	
	public CountyDto startOperate(String operator,CountyDto countyDto);
}
