package com.taobao.cun.auge.station.bo;

import java.util.List;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.condition.StationCondition;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.github.pagehelper.Page;
/**
 *服务站表基础服务
 * @author quanzhu.wangqz
 *
 */
public interface StationBO {
	/**
	 * 根据服务站id查询村点
	 * 
	 * @param stationId
	 * @return
	 * @
	 */
	public Station getStationById(Long stationId) ;
	
	/**
	 * 根据服务站id查询村点
	 * 
	 * @param stationId
	 * @return
	 * @
	 */
	public List<Station> getStationById(List<Long> stationIds) ;

	/**
	 * 根据服务站编号查询村点
	 * 
	 * @param stationNum
	 * @return
	 * @
	 */
	public Station getStationByStationNum(String stationNum) ;
   
	/**
     * 变更村点状态
     * @param stationId
     * @param preStatus
     * @param postStatus
     * @param operator
     * @
     */
	public void changeState(Long stationId, StationStatusEnum preStatus, StationStatusEnum postStatus, String operator);

	/**
	 * 新增服务站
	 * @param stationDto
	 * @return 主键
	 * @
	 */
	public Long addStation(StationDto stationDto) ;
	
	/**
	 * 修改服务站
	 * @param stationDto
	 * @return
	 * @
	 */
	public void updateStation(StationDto stationDto) ;
	
	/**
	 * 检查服务站编号使用已经使用
	 * 
	 * @param stationNum
	 * @return
	 * @
	 */
	public int getStationCountByStationNum(String stationNum) ;
	
	/**
	 * 删除服务站
	 * @param stationId
	 * @param operator
	 * @
	 */
	public void deleteStation(Long stationId, String operator) ;

	/**
	 * 根据name、orgIdPath、stationStatusEnum查询station  为搜索tp服务站使用  只返回名字，id,村点编号
	 * @param stationCondition
	 * @return
	 * @
	 */
	public List<Station> getTpStationsByName(StationCondition stationCondition) ;
	
	/**
	 * 根据name、orgId、stationStatusEnum查询station
	 * 
	 * @param stationCondition
	 * @return
	 * @
	 */
	public Page<Station> getStations(StationCondition stationCondition);
	
	/**
     * 同一个省村站名称不能相同
     * 
     * @param stationName、province
     * @return
     * @
     */
    public int getSameNameInProvinceCnt(String stationName,String province);
}
