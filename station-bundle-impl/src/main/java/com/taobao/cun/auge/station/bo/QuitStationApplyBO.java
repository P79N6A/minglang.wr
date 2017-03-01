package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface QuitStationApplyBO {

	/**
	 * 保存退出申请单
	 * 
	 * @param quitStationApply
	 * @param operator
	 * @return
	 */
	public void saveQuitStationApply(QuitStationApply quitStationApply, String operator);

	/**
	 * 根据合伙人实例id，查询退出申请单
	 * 
	 * @param instanceId
	 * @return
	 */
	public QuitStationApply findQuitStationApply(Long instanceId);

	/**
	 * 删除退出申请单
	 * 
	 * @param instanceId
	 */
	public void deleteQuitStationApply(Long instanceId, String operator);

	/**
	 * 根据每次申请单id，查询退出申请单
	 * 
	 * @param applyId
	 * @return
	 * @throws AugeServiceException
	 */
	public QuitStationApply getQuitStationApplyById(Long applyId);
	
}
