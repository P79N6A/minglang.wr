package com.taobao.cun.auge.station.adapter;

import java.util.LinkedHashMap;
import java.util.Map;

import com.taobao.cun.auge.station.dto.CaiNiaoStationDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

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
	 * @throws AugeServiceException
	 */
	public Long addCounty(CaiNiaoStationDto station) throws AugeServiceException;

	/**
	 * 在某县域下新增村站
	 * 
	 * @param station
	 * @return
	 * @throws AugeServiceException
	 */
	public Long addStation(CaiNiaoStationDto station) throws AugeServiceException;

	/**
	 * 在某村站下新增买家
	 * 
	 * @param station
	 * @return
	 * @throws AugeServiceException
	 */
	public boolean addStationUserRel(CaiNiaoStationDto station, String userType) throws AugeServiceException;

	/**
	 * 更新村站基本信息
	 * 
	 * @param dto
	 */
	public boolean modifyStation(CaiNiaoStationDto station) throws AugeServiceException;

	/**
	 * 更新买家基本信息
	 * 
	 * @param station
	 * @return
	 * @throws AugeServiceException
	 */
	public boolean updateStationUserRel(CaiNiaoStationDto station) throws AugeServiceException;

	/**
	 * 删除村站(先删用户站点关系，再删站点)
	 * 
	 * @param cainiaoStationId
	 * @return
	 * @throws AugeServiceException
	 */
	public boolean removeStationById(Long cainiaoStationId, Long userId) throws AugeServiceException;

	/**
	 * 删除买家
	 * 
	 * @param userId
	 * @return
	 * @throws AugeServiceException
	 */
	public boolean removeStationUserRel(Long userId) throws AugeServiceException;

	/**
	 * 给村站打标
	 * 
	 * @param stationId
	 * @param featureMap
	 * @return
	 * @throws AugeServiceException
	 */
	public boolean updateStationFeatures(Long stationId, LinkedHashMap<String, String> features)
			throws AugeServiceException;

	/**
	 * 给村站和用户关系表打标
	 * 
	 * @param userId
	 * @param featureMap
	 * @return
	 * @throws AugeServiceException
	 */
	public boolean updateStationUserRelFeature(Long userId, Map<String, String> featureMap) throws AugeServiceException;

}
