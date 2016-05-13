package com.taobao.cun.auge.station.condition;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.station.enums.LogisticsStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;

public class StationCondition implements Serializable {

	private static final long serialVersionUID = -5161645049551081998L;

	// 村服务站id
	private Long id;

	// 所属组织id
	private Long parentOrgId;

	// 所属服务商id
	private Long providerId;

	// 新的状态
	private StationStatusEnum status;

	// 村编号
	private String stationNum;

	// 村服务站名称
	private String name;

	// 经营地址
	private AddressCondition address;

	// 目前业态
	private String format;

	// 覆盖人口
	protected String covered;

	// 特色农产品
	protected String products;

	// 物流情况
	protected LogisticsStateEnum logisticsState;

	// 现场照片附件
	protected List<AttachementCondition> attachements;

	// 描述
	private String description;

	// 其他特性
	private Map<String, String> featureMap;

	// 是否提交物流变更
	private boolean submitLogisticsChange;

	// 管理员id 小二工号，或者TP商 taoboa_user_id
	private Long managerId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentOrgId() {
		return parentOrgId;
	}

	public void setParentOrgId(Long parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

	public StationStatusEnum getStatus() {
		return status;
	}

	public void setStatus(StationStatusEnum status) {
		this.status = status;
	}

	public String getStationNum() {
		return stationNum;
	}

	public void setStationNum(String stationNum) {
		this.stationNum = stationNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AddressCondition getAddress() {
		return address;
	}

	public void setAddress(AddressCondition address) {
		this.address = address;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getCovered() {
		return covered;
	}

	public void setCovered(String covered) {
		this.covered = covered;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public LogisticsStateEnum getLogisticsState() {
		return logisticsState;
	}

	public void setLogisticsState(LogisticsStateEnum logisticsState) {
		this.logisticsState = logisticsState;
	}

	public List<AttachementCondition> getAttachements() {
		return attachements;
	}

	public void setAttachements(List<AttachementCondition> attachements) {
		this.attachements = attachements;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, String> getFeatureMap() {
		return featureMap;
	}

	public void setFeatureMap(Map<String, String> featureMap) {
		this.featureMap = featureMap;
	}

	public boolean isSubmitLogisticsChange() {
		return submitLogisticsChange;
	}

	public void setSubmitLogisticsChange(boolean submitLogisticsChange) {
		this.submitLogisticsChange = submitLogisticsChange;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

}
