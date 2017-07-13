package com.taobao.cun.auge.station.adapter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.cainiao.cuntaonetwork.dto.warehouse.WarehouseDTO;
import com.taobao.cun.auge.station.dto.CaiNiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncModifyLngLatDto;
import com.taobao.cun.common.exception.ServiceException;

public interface CaiNiaoAdapter {
	/**
	 * 当前站点对应的村淘合伙人淘宝用户ID（合伙人自己或淘帮手对应的合伙人）
	 */
	public static final String CTP_TB_UID = "ctpTbUid";

	/**
	 * 当前站点对应的合伙人村淘ORG站点ID（合伙人自己或淘帮手对应的合伙人）
	 */
	public static final String CTP_ORG_STA_ID = "ctpOrgStaId";

	/**
	 * 当前站点合伙人类型（合伙人／1.0降级合伙人）
	 */
	public static final String CTP_TYPE = "ctpType";

	/** 村淘站点的code **/
	public static final String CUNTAO_CODE = "CTCODE";

	/**
	 * 给用户关系表打标记，如果是合伙人，值是自己的uid,如果是淘帮手，值是所属合伙人的uid
	 */
	public static final String PARTNER_ID = "partnerId";

	/**
	 * 新增县点
	 * 
	 * @param station
	 * @return
	 * @
	 */
	public Long addCounty(CaiNiaoStationDto station);

	/**
	 * 在某县域下新增村站
	 * 
	 * @param station
	 * @return
	 * @
	 */
	public Long addStation(CaiNiaoStationDto station) ;

	/**
	 * 在某村站下新增买家
	 * 
	 * @param station
	 * @return
	 * @
	 */
	public boolean addStationUserRel(CaiNiaoStationDto station, String userType) ;

	/**
	 * 更新村站基本信息
	 * 
	 * @param dto
	 */
	public boolean modifyStation(CaiNiaoStationDto station) ;

	/**
	 * 更新买家基本信息
	 * 
	 * @param station
	 * @return
	 * @
	 */
	public boolean updateStationUserRel(CaiNiaoStationDto station) ;

	/**
	 * 删除村站(先删用户站点关系，再删站点)
	 * 
	 * @param cainiaoStationId
	 * @return
	 * @
	 */
	public boolean removeStationById(Long cainiaoStationId, Long userId) ;
	
	
	/**
	 * 删除不可用村站( 没有人的情况调用 ，直接删除站点)
	 * 
	 * @param cainiaoStationId
	 * @return
	 * @
	 */
	public boolean removeNotUserdStationById(Long cainiaoStationId) ;
	

	/**
	 * 删除买家
	 * 
	 * @param userId
	 * @return
	 * @
	 */
	public boolean removeStationUserRel(Long userId) ;

	/**
	 * 给村站打标
	 * 
	 * @param stationId
	 * @param featureMap
	 * @return
	 * @
	 */
	public boolean updateStationFeatures(Long stationId, LinkedHashMap<String, String> features)
			;

	/**
	 * 给村站和用户关系表打标
	 * 
	 * @param userId
	 * @param featureMap
	 * @return
	 * @
	 */
	public boolean updateStationUserRelFeature(Long userId, Map<String, String> featureMap) ;
	
	/**
	 * 村站解绑合伙人  目前仅支持合伙人使用
	 * 文档：http://gitlab.alibaba-inc.com/cainiao-cuntao/cuntaonetwork/wikis/station_unbind_admin
	 * @return
	 * @
	 */
	public boolean unBindAdmin(Long cainiaoStationId) ;
	
	/**
	 * 村站绑定合伙人 目前仅支持合伙人使用 
	 * 文档：http://gitlab.alibaba-inc.com/cainiao-cuntao/cuntaonetwork/wikis/station_bind_admin
	 * @param station
	 * @return
	 * @
	 */
	public boolean bindAdmin(CaiNiaoStationDto station) ;
	
	/**
	 * 村站修改合伙人
	 * @param station
	 * @return
	 * @
	 */
	public boolean updateAdmin(CaiNiaoStationDto station) ;
	
	/**
	 * 删除 村站feature字段的值
	 * @param stationId
	 * @param keys
	 * @return
	 * @
	 */
	public boolean removeStationFeatures(Long stationId,Set<String> keys) ;
	
	public List<WarehouseDTO> queryWarehouseById(Long id) ; 
	
	public Long addCountyByOrg(CaiNiaoStationDto stationDto) throws ServiceException ;
	
	/**
	 * 停业通知菜鸟服务站停业
	 * 
	 * @param cainiaoStationId
	 * @return
	 * @
	 */
	public boolean closeToCainiaoStation(Long cainiaoStationId) ;
	
	/**
	 * 经纬度同步菜鸟
	 * 
	 * @param syncModifyLngLatDto
	 * @return
	 * @
	 */
	public boolean modifyLngLatToCainiao(SyncModifyLngLatDto dto) ;
}
