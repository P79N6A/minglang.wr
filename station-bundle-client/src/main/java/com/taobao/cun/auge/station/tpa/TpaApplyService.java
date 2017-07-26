package com.taobao.cun.auge.station.tpa;

import com.taobao.cun.auge.station.request.CheckTpaApplyRequest;
import com.taobao.cun.auge.station.request.TpaApplyRequest;
import com.taobao.cun.auge.station.response.CheckTpaApplyResponse;
import com.taobao.cun.auge.station.response.TpaApplyResponse;
/**
 * 淘帮手申请服务
 * @author zhenhuan.zhangzh
 *
 */
public interface TpaApplyService {

	/**
	 * 检查淘帮手申请请求
	 * @param request
	 * @return
	 */
	public CheckTpaApplyResponse checkTpaApply(CheckTpaApplyRequest request);
	
	/**
	 * 合伙人申请淘帮手请求
	 * @param request
	 * @return
	 */
	public TpaApplyResponse applyTpa(TpaApplyRequest request);
	
	/**
	 * 修改淘帮手
	 * @param request
	 * @return
	 */
	public TpaApplyResponse updateTpa(TpaApplyRequest request);
}
