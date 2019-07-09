package com.taobao.cun.auge.dal.example;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
	 * 服务站入驻状态
	 */
	private List<String> states;

	/**
	 * 村点名称
	 */
	private String stationName;

	/**
	 * 门店类型
	 */
	private String storeCategory;

	/**
	 * 巡检类型
	 */
	private String inspectionType;

	/**
	 * 巡检状态
	 */
	private String inspectionState;


	private String  level;

	/**
	 * 服务开始时间
	 */
	private Date serviceBeginDate;

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

	public Date getServiceBeginDate() {
		return serviceBeginDate;
	}

	public void setServiceBeginDate(Date serviceBeginDate) {
		this.serviceBeginDate = serviceBeginDate;
	}

	public List<String> getStates() {
		return states;
	}

	public void setStates(List<String> states) {
		this.states = states;
	}

	public String getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}
}
