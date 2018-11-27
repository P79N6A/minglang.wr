package com.taobao.cun.auge.station.bo;

import java.util.Date;

import com.taobao.cun.auge.dal.domain.StationTransInfo;
import com.taobao.cun.auge.station.dto.StationTransInfoDto;

/**
 * 
 * @author quanzhu.wangqz
 *
 */
public interface StationTransInfoBO {
	/**
	 * 新增
	 * @param dto
	 * @return
	 */
	public Long addTransInfo(StationTransInfoDto dto);
	/**
	 *根据存点ID查最后一条转型记录
	 * @param stationId
	 * @return
	 */
    public StationTransInfo getLastTransInfoByStationId(Long stationId);
    
    /**
	 *根据存点ID查最后一条转型记录
	 * @param stationId
	 * @return
	 */
    public StationTransInfoDto getLastTransInfoDtoByStationId(Long stationId);
   
    /**
     * 改成转型中
     */
    public void changeTransing(Long stationId,String operator);
    
    /**
     * 完成转型
     */
    public void finishTrans(Long stationId,Date newBizDate,String operator);
    
    
}
