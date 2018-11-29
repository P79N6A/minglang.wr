package com.taobao.cun.auge.asset.bo;

import com.taobao.cun.auge.asset.dto.AssetCheckInfoAddDto;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoDto;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;

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
	public void addCheckInfo(AssetCheckInfoAddDto addDto);
	
	/**
	 * 删除盘点信息
	 * @param infoId
	 * @param operator
	 */
	public void delCheckInfo(Long infoId,OperatorDto operator);
	/**
	 * 总部人员确认盘点信息
	 * @param infoId
	 * @param operator
	 */
	public void confrimCheckInfo(Long infoId,OperatorDto operator);
	
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
}
