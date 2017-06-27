package com.taobao.cun.auge.dal.mapper;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.dal.domain.Asset;
import com.taobao.cun.auge.dal.domain.AssetExtExample;

public interface AssetExtMapper {
    
	public Page<Asset> queryByPage(AssetExtExample extExample);

}