package com.taobao.cun.auge.cuncounty.mobile;

import com.taobao.cun.auge.flow.FlowContent;

/**
 * 
 * 县服务中心流程（移动端）
 * 
 * @author chengyu.zhoucy
 *
 */
public interface CuntaoCountyProcessService {
	/**
	 * 获取县点详情
	 * @param taskCode
	 * @param id
	 * @return
	 */
	FlowContent getCuntaoCountyDetail(String taskCode, Long id);
}
