package com.taobao.cun.auge.station.condition;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.StationStatusEnum;

/**
 * 
 * @author quanzhu.wangqz
 *
 */
public class StationCondition extends OperatorDto implements Serializable {

	private static final long serialVersionUID = -5161645049551081998L;

	/**
	 * 村服务站id
	 */
	private Long id;
	
	/**
	 * 是否需要村点附件信息
	 */
	private Boolean needAttachementInfo = Boolean.TRUE;

	private String name;
	
	private Long orgId;

	private String orgIdPath;

	private StationStatusEnum stationStatusEnum;

	private Integer pageStart; // 分页参数

	private Integer pageSize;

	public Integer getPageStart() {
		return pageStart;
	}

	public void setPageStart(Integer pageStart) {
		this.pageStart = pageStart;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgIdPath() {
		return orgIdPath;
	}

	public void setOrgIdPath(String orgIdPath) {
		this.orgIdPath = orgIdPath;
	}

	public StationStatusEnum getStationStatusEnum() {
		return stationStatusEnum;
	}

	public void setStationStatusEnum(StationStatusEnum stationStatusEnum) {
		this.stationStatusEnum = stationStatusEnum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getNeedAttachementInfo() {
		return needAttachementInfo;
	}

	public void setNeedAttachementInfo(Boolean needAttachementInfo) {
		this.needAttachementInfo = needAttachementInfo;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
}
