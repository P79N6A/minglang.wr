package com.taobao.cun.auge.asset.bo.impl;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.asset.bo.AssetCheckInfoBO;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoAddDto;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoDto;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;

@Component
public class AssetCheckInfoBOImpl implements AssetCheckInfoBO {

	@Override
	public void addCheckInfo(AssetCheckInfoAddDto addDto) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delCheckInfo(Long infoId, OperatorDto operator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void confrimCheckInfo(Long infoId, OperatorDto operator) {
		// TODO Auto-generated method stub

	}

	@Override
	public PageDto<AssetCheckInfoDto> listInfoForOrg(AssetCheckInfoCondition param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDto<AssetCheckInfoDto> listInfo(AssetCheckInfoCondition param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssetCheckInfoDto getCheckInfoById(Long infoId) {
		// TODO Auto-generated method stub
		return null;
	}

}
