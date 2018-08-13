package com.taobao.cun.auge.fence.job.init;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.dal.mapper.ext.ExtFenceEntityMapper;

/**
 * 不再是TPS了，需要将其TPS的默认围栏删除
 * @author chengyu.zhoucy
 *
 */
@Component
public class TPSInvalidFenceFetcher implements InvalidFenceFetcher {
	@Resource
	private ExtFenceEntityMapper extFenceEntityMapper;
	@Resource
	private FenceInitTemplateConfig fenceInitTemplateConfig;
	
	@Override
	public List<FenceEntity> getFenceEntities() {
		return extFenceEntityMapper.selectTypeChangedDefaultFenceEntities("TPS", fenceInitTemplateConfig.getStoreTemplates());
	}

}
