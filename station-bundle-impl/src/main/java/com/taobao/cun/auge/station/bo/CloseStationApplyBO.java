package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.station.dto.CloseStationApplyDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 停业申请单
 * @author quanzhu.wangqz
 *
 */
public interface CloseStationApplyBO {
	
	/**
	 * 新增停业申请
	 * @param closeStationApplyDto
	 * @return
	 * @throws AugeServiceException
	 */
	public Long addCloseStationApply(CloseStationApplyDto closeStationApplyDto) throws AugeServiceException;
	
	/**
	 * 删除停业申请
	 * @param closeStationApplyDto
	 * @throws AugeServiceException
	 */
	public void deleteCloseStationApply(Long partnerInstanceId, String operator) throws AugeServiceException;
	
	/**
	 * 查询停业申请
	 * @param partnerInstanceId
	 * @return
	 * @throws AugeServiceException
	 */
	public CloseStationApplyDto getCloseStationApply(Long partnerInstanceId) throws AugeServiceException;
}
