package com.taobao.cun.auge.station.condition;


import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.PageQuery;
import com.taobao.cun.auge.station.enums.*;

public class PartnerInstancePageCondition extends PageQuery{

	private static final long serialVersionUID = 2555611748293416838L;
	
	//老的组织idpath，只有手机端传，待周老师，组织改造完成后，再使用orgIdPath
	private String fullIdPath;

	// 村服务站所属村淘组织
	private String orgIdPath;

	// 村服务站编号
	private String stationNum;

	// 村服务站名称
	private String stationName;

	// 村服务站管理员
	private String managerId;

	// 村服务站地址
	private Address address;

	// 合伙人taobao昵称
	private String taobaoNick;

	// 合伙人姓名
	private String partnerName;

	// 老的状态
	private StationApplyStateEnum stationApplyState;

	// 这个状态和上面老的状态，只能传一个
	// 合伙人状态
	private PartnerInstanceStateEnum instanceState;

	// 合伙人类型
//	@NotNull(message = "partnerType is null")
	private PartnerInstanceTypeEnum partnerType;
	
	//合伙人的级别
	private PartnerInstanceLevelEnum partnerInstanceLevel;

	// 所属TP商id
	private Long providerId;
	
	// 父站点id
	private Long parentStationId;
	
	//是否是当前人
	private Boolean isCurrent;

	private TPCategoryEnum tpCategoryEnum;
	
	private String ownDept;

	/**
	 * instance mode
	 */
	private  String mode;

	private StationBizTypeEnum  bizTypeEnum;

    public StationBizTypeEnum getBizTypeEnum() {
        return bizTypeEnum;
    }

    public void setBizTypeEnum(StationBizTypeEnum bizTypeEnum) {
        this.bizTypeEnum = bizTypeEnum;
    }

    public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getOwnDept() {
		return ownDept;
	}

	public void setOwnDept(String ownDept) {
		this.ownDept = ownDept;
	}
	
	public String getOrgIdPath() {
		return orgIdPath;
	}

	public void setOrgIdPath(String orgIdPath) {
		this.orgIdPath = orgIdPath;
	}

	public String getStationNum() {
		return stationNum;
	}

	public void setStationNum(String stationNum) {
		this.stationNum = stationNum;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getTaobaoNick() {
		return taobaoNick;
	}

	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public StationApplyStateEnum getStationApplyState() {
		return stationApplyState;
	}

	public void setStationApplyState(StationApplyStateEnum stationApplyState) {
		this.stationApplyState = stationApplyState;
	}

	public PartnerInstanceTypeEnum getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(PartnerInstanceTypeEnum partnerType) {
		this.partnerType = partnerType;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

	public Long getParentStationId() {
		return parentStationId;
	}

	public void setParentStationId(Long parentStationId) {
		this.parentStationId = parentStationId;
	}

	public PartnerInstanceLevelEnum getPartnerInstanceLevel() {
		return partnerInstanceLevel;
	}

	public void setPartnerInstanceLevel(PartnerInstanceLevelEnum partnerInstanceLevel) {
		this.partnerInstanceLevel = partnerInstanceLevel;
	}

	public Boolean getIsCurrent() {
		return isCurrent;
	}

	public void setIsCurrent(Boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	public PartnerInstanceStateEnum getInstanceState() {
		return instanceState;
	}

	public void setInstanceState(PartnerInstanceStateEnum instanceState) {
		this.instanceState = instanceState;
	}

	public String getFullIdPath() {
		return fullIdPath;
	}

	public void setFullIdPath(String fullIdPath) {
		this.fullIdPath = fullIdPath;
	}

	public TPCategoryEnum getTpCategoryEnum() {
		return tpCategoryEnum;
	}

	public void setTpCategoryEnum(TPCategoryEnum tpCategoryEnum) {
		this.tpCategoryEnum = tpCategoryEnum;
	}
}
