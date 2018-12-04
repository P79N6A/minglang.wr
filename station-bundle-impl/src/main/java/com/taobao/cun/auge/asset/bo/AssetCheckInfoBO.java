package com.taobao.cun.auge.asset.bo;

import com.taobao.cun.auge.asset.dto.AssetCheckInfoAddDto;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoDto;
import com.taobao.cun.auge.asset.dto.CountyCheckCountDto;
import com.taobao.cun.auge.asset.dto.CountyFollowCheckCountDto;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.dal.domain.AssetCheckInfo;

/**
 * 盘点明细 基础服务
 * @author quanzhu.wangqz
 *
 */
public interface AssetCheckInfoBO {
	/**
	 * 新增盘点信息
	 * @param addDto
	 */
	public Boolean addCheckInfo(AssetCheckInfoAddDto addDto);
	
	/**
	 * 删除盘点信息
	 * @param infoId
	 * @param operator
	 */
	public Boolean delCheckInfo(Long infoId,OperatorDto operator);
	/**
	 * 总部人员确认盘点信息
	 * @param infoId
	 * @param operator
	 */
	public Boolean confrimCheckInfo(Long infoId,String aliNo,OperatorDto operator);
	
	/**
	 * 盘点信息列表
	 * @param param
	 * @return
	 */
	public PageDto<AssetCheckInfoDto> listInfoForOrg(AssetCheckInfoCondition  param);
	/**
	 * 盘点信息列表
	 * @param param
	 * @return
	 */
	public PageDto<AssetCheckInfoDto> listInfo(AssetCheckInfoCondition  param);
	
	/**
	 * 盘点信息
	 * @param infoId
	 * @return
	 */
	public AssetCheckInfo  getCheckInfoById(Long infoId);
	
	/**
	 * 县盘点总数
	 * @param countyOrgId
	 * @return
	 */
	public CountyCheckCountDto getCountyCheckCount(Long countyOrgId);
	/**
	 * 县跟踪盘点总数
	 * @param countyOrgId
	 * @return
	 */
	public CountyFollowCheckCountDto getCountyFollowCheckCount(Long countyOrgId);
	
	/**
	 * 盘点信息
	 * @param infoId
	 * @return
	 */
	public AssetCheckInfo  getCheckInfoBySerialNo(String serialNo);
}
