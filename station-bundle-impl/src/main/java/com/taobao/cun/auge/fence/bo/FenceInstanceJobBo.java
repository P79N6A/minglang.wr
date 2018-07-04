package com.taobao.cun.auge.fence.bo;

import java.util.List;

import com.taobao.cun.auge.fence.dto.FenceInstanceJobUpdateDto;
import com.taobao.cun.auge.fence.dto.job.FenceInstanceJob;

/**
 * 模板实例任务业务类
 * 
 * @author chengyu.zhoucy
 *
 */
public interface FenceInstanceJobBo {
	/**
	 * 插入FenceInstanceJob
	 * 
	 * @param fenceInstanceJob
	 */
	void insertJob(FenceInstanceJob fenceInstanceJob);
	
	/**
	 * 更新任务信息
	 * 
	 * @param fenceInstanceJobUpdateDto
	 */
	void updateJob(FenceInstanceJobUpdateDto fenceInstanceJobUpdateDto);
	
	/**
	 * 获取状态为NEW的FenceInstanceJob
	 * @return
	 */
	List<FenceInstanceJob> getNewFenceInstanceJobs();
}
