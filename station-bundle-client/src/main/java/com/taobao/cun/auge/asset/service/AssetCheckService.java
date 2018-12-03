package com.taobao.cun.auge.asset.service;

import java.util.List;

import com.taobao.cun.auge.asset.dto.AssetCheckInfoCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoDto;
import com.taobao.cun.auge.asset.dto.AssetCheckTaskCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckTaskDto;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;

/**
 * 资产盘点服务
 * @author quanzhu.wangqz
 *
 */
public interface AssetCheckService {

	/**
	 * 盘点任务列表
	 * @param param
	 * @return
	 */
    public PageDto<AssetCheckTaskDto> listTasks(AssetCheckTaskCondition  param);
    
    /**
	 * 盘点信息列表
	 * @param param
	 * @return
	 */
	public PageDto<AssetCheckInfoDto> listInfoForOrg(AssetCheckInfoCondition  param);
	
	/**
	 * 盘点信息
	 * @param infoId
	 * @return
	 */
	public AssetCheckInfoDto  getCheckInfoById(Long infoId); 
	
	/**
	 * 批量盘点
	 * @param ids
	 */
	public void check(List<Long> ids,OperatorDto ope);
	
	public void modifyAsset();
	
}
