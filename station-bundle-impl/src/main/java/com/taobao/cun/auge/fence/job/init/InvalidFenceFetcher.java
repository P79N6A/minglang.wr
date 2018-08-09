package com.taobao.cun.auge.fence.job.init;

import java.util.List;

import com.taobao.cun.auge.dal.domain.FenceEntity;

/**
 * 获取已经失效的默认围栏
 * @author chengyu.zhoucy
 *
 */
public interface InvalidFenceFetcher {
	/**
	 * 获取围栏实例
	 * @return
	 */
	List<FenceEntity> getFenceEntities();
}
