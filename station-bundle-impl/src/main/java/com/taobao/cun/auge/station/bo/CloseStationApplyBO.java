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
	 * @
	 */
	public Long addCloseStationApply(CloseStationApplyDto closeStationApplyDto) ;
	
	/**
	 * 删除停业申请
	 * @param closeStationApplyDto
	 * @
	 */
	public void deleteCloseStationApply(Long partnerInstanceId, String operator) ;
	
	/**
	 * 查询停业申请
	 * @param partnerInstanceId
	 * @return
	 * @
	 */
	public CloseStationApplyDto getCloseStationApply(Long partnerInstanceId) ;

	/**
	 * 根据每次申请单id，查询停业申请单
	 * 
	 * @param applyId
	 * @return
	 * @
	 */
	public CloseStationApplyDto getCloseStationApplyById(Long applyId);
}
