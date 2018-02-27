package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.StationModifyApply;
import com.taobao.cun.auge.station.dto.StationModifyApplyDto;
import com.taobao.cun.auge.station.enums.StationModifyApplyBusitypeEnum;
import com.taobao.cun.auge.station.enums.StationModifyApplyStatusEnum;

/**
 * 村点信息修改申请表
 * @author quanzhu.wangqz
 *
 */
public interface StationModifyApplyBO {

    /**
     * 新增
     */
  public Long addStationModifyApply(StationModifyApplyDto dto);
  /**
   * 审批,监听工作流使用
   */
  public void auditForNameAndAddress(Long id,StationModifyApplyStatusEnum status);
  
  
  /**
   * 查询
   * @param type
   * @param stationId
   * @return
   */
  public StationModifyApplyDto getApplyInfoByStationId(StationModifyApplyBusitypeEnum type,Long stationId,StationModifyApplyStatusEnum status);
  /**
   * 查询
   * @return
   */
  public StationModifyApplyDto getApplyInfoById(Long id);
  
  /**
   * 查询
   * @return
   */
  public StationModifyApply getStationModifyApplyById(Long id);
  
  
  public void updateName(StationModifyApplyDto dto);
}
