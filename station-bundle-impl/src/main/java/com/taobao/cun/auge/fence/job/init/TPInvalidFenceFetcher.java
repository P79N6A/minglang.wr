package com.taobao.cun.auge.fence.job.init;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.dal.mapper.ext.ExtFenceEntityMapper;

/**
 * 不再是TP了，需要将默认围栏删除
 * @author chengyu.zhoucy
 *
 */
@Component
public class TPInvalidFenceFetcher implements InvalidFenceFetcher {

	@Resource
	private ExtFenceEntityMapper extFenceEntityMapper;
	@Resource
	private FenceInitTemplateConfig fenceInitTemplateConfig;
	
	@Override
	public List<FenceEntity> getFenceEntities() {
		return extFenceEntityMapper.selectTypeChangedDefaultFenceEntities("TP", fenceInitTemplateConfig.getTPTemplates());
	}

}
