package com.taobao.cun.auge.dal.example;

import java.io.Serializable;

/**
 * 合伙人巡检查询Example
 * @author zhenhuan.zhangzh
 *
 */
public class InspectionStationExample implements Serializable {

	private static final long serialVersionUID = 1232528817432543217L;

	/**
	 * 组织ID路径
	 */
    private String orgIdPath;
	
    /**
     * 服务站类型
     */
	private String type;
	
	/**
	 * 服务站入驻状态
	 */
	private String state;
	
	/**
	 * 村点名称
	 */
	private String stationName;
	
	/**
	 * 门店类型
	 */
	private String storeCategory;

	/**
	 * 服务站模式
	 */
	private String mode;
	
	/**
	 * 巡检状态
	 */
	private String inspectionState;
	
	
	private String  level;
	
	public String getOrgIdPath() {
		return orgIdPath;
	}

	public void setOrgIdPath(String orgIdPath) {
		this.orgIdPath = orgIdPath;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStoreCategory() {
		return storeCategory;
	}

	public void setStoreCategory(String storeCategory) {
		this.storeCategory = storeCategory;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getInspectionState() {
		return inspectionState;
	}

	public void setInspectionState(String inspectionState) {
		this.inspectionState = inspectionState;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

}
