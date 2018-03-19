package com.taobao.cun.auge.inspection.condition;

import java.io.Serializable;
/**
 * 
 * @author zhenhuan.zhangzh
 *
 */

import com.taobao.cun.auge.common.PageQuery;
public class PartnerInstanceInspectionPagedCondition  extends PageQuery implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6045390093424191902L;

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
	

	public String getOrgIdPath() {
		return orgIdPath;
	}

	public void setOrgIdPath(String orgIdPath) {
		this.orgIdPath = orgIdPath;
	}

	public String getType() {
		return PartnerInstanceInspectionTypes.getType(this.type);
	}

	public void setType(String type) {
		this.type = type;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
	public String getMode(){
		return PartnerInstanceInspectionTypes.getVersion(this.getType());
	}
}
