package com.taobao.cun.auge.county.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.station.dto.AttachementDto;
import com.taobao.cun.auge.station.enums.CountyStationLeaseTypeEnum;
import com.taobao.cun.auge.station.enums.CountyStationManageModelEnum;
import com.taobao.cun.auge.station.enums.CountyStationManageStatusEnum;

public class CountyDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String province;
	private String provinceDetail;
	private String city;
	private String cityDetail;
	private String county;
	private String countyDetail;
	private String town;
	private String townDetail;
	private String addressDetail;
	private Long parentId;
	private String parentName;
	private Long orgId;
	private String employeeId;
	private String employeeName;
	private String employeePhone;
	private Long taobaoUserId;
	private String taobaoNick;
	private CountyStationManageModelEnum manageModel;
	private CountyStationManageStatusEnum manageStatus;
	private String acreage;
	private Integer warehouseNum;
	private String leasingModel;
	private String officeDetail;
	private String freeDeadline;
	private String selfCosts;
	private String remark;
	private String lng;
	private String lat;
	private String logisticsOperator;
	private String logisticsPhone;
	private Long specialTeamId;
	private String operator;
	private Date gmtStartOperation;
	private List<StationManagerDto> managers;

	private Date createTime;

	private Date startOperationTime;

	private boolean viewCounty = false;
	private boolean manageCounty = false;

	// 资产申请
	private boolean assetPurchaseApply = false;

	protected List<AttachementDto> attachements;// 所有附件,这个是增加的所有附件

	private Map<String, String> featureMap;
	// 起航班
	// private CountyStationFeatureEnum qhb;

	private boolean manageCountyQhb = false;

	// 菜鸟县仓信息
	private List<CnWarehouseDto> warehouseDtos;

	/**
	 * 租赁协议开始时间
	 */
	private Date leaseProtocolBeginTime;
	/**
	 * 租赁协议结束时间
	 */
	private Date leaseProtocolEndTime;
	
	private String leaseProtocolBeginTimeFormat;
	private String leaseProtocolEndTimeFormat;
	/**
	 * 租赁类型
	 */
	private CountyStationLeaseTypeEnum leaseTypeEnum;
	/**
	 * 租赁付费金额
	 */
	private String leasePayment;

	/**
	 * 水费
	 */
	private String waterPayment;

	/**
	 * 电费
	 */
	private String electricPayment;

	/**
	 * 物业费
	 */
	private String propertyPayment;

	/**
	 * 仓储面积
	 */
	private String storageArea;

	private AddressDto addressDto;
	
	public AddressDto getAddressDto() {
		return addressDto;
	}

	public void setAddressDto(AddressDto addressDto) {
		this.addressDto = addressDto;
	}

	public Date getGmtStartOperation() {
		return gmtStartOperation;
	}

	public void setGmtStartOperation(Date gmtStartOperation) {
		this.gmtStartOperation = gmtStartOperation;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProvinceDetail() {
		return provinceDetail;
	}

	public void setProvinceDetail(String provinceDetail) {
		this.provinceDetail = provinceDetail;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityDetail() {
		return cityDetail;
	}

	public void setCityDetail(String cityDetail) {
		this.cityDetail = cityDetail;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCountyDetail() {
		return countyDetail;
	}

	public void setCountyDetail(String countyDetail) {
		this.countyDetail = countyDetail;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getTownDetail() {
		return townDetail;
	}

	public void setTownDetail(String townDetail) {
		this.townDetail = townDetail;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeePhone() {
		return employeePhone;
	}

	public void setEmployeePhone(String employeePhone) {
		this.employeePhone = employeePhone;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public String getTaobaoNick() {
		return taobaoNick;
	}

	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
	}

	public CountyStationManageModelEnum getManageModel() {
		return manageModel;
	}

	public void setManageModel(CountyStationManageModelEnum manageModel) {
		this.manageModel = manageModel;
	}

	public CountyStationManageStatusEnum getManageStatus() {
		return manageStatus;
	}

	public void setManageStatus(CountyStationManageStatusEnum manageStatus) {
		this.manageStatus = manageStatus;
	}

	public String getAcreage() {
		return acreage;
	}

	public void setAcreage(String acreage) {
		this.acreage = acreage;
	}

	public Integer getWarehouseNum() {
		return warehouseNum;
	}

	public void setWarehouseNum(Integer warehouseNum) {
		this.warehouseNum = warehouseNum;
	}

	public String getLeasingModel() {
		return leasingModel;
	}

	public void setLeasingModel(String leasingModel) {
		this.leasingModel = leasingModel;
	}

	public String getOfficeDetail() {
		return officeDetail;
	}

	public void setOfficeDetail(String officeDetail) {
		this.officeDetail = officeDetail;
	}

	public String getFreeDeadline() {
		return freeDeadline;
	}

	public void setFreeDeadline(String freeDeadline) {
		this.freeDeadline = freeDeadline;
	}

	public String getSelfCosts() {
		return selfCosts;
	}

	public void setSelfCosts(String selfCosts) {
		this.selfCosts = selfCosts;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLogisticsOperator() {
		return logisticsOperator;
	}

	public void setLogisticsOperator(String logisticsOperator) {
		this.logisticsOperator = logisticsOperator;
	}

	public String getLogisticsPhone() {
		return logisticsPhone;
	}

	public void setLogisticsPhone(String logisticsPhone) {
		this.logisticsPhone = logisticsPhone;
	}

	public Long getSpecialTeamId() {
		return specialTeamId;
	}

	public void setSpecialTeamId(Long specialTeamId) {
		this.specialTeamId = specialTeamId;
	}

	public List<StationManagerDto> getManagers() {
		return managers;
	}

	public void setManagers(List<StationManagerDto> managers) {
		this.managers = managers;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getStartOperationTime() {
		return startOperationTime;
	}

	public void setStartOperationTime(Date startOperationTime) {
		this.startOperationTime = startOperationTime;
	}

	public boolean isViewCounty() {
		return viewCounty;
	}

	public void setViewCounty(boolean viewCounty) {
		this.viewCounty = viewCounty;
	}

	public boolean isManageCounty() {
		return manageCounty;
	}

	public void setManageCounty(boolean manageCounty) {
		this.manageCounty = manageCounty;
	}

	public boolean isAssetPurchaseApply() {
		return assetPurchaseApply;
	}

	public void setAssetPurchaseApply(boolean assetPurchaseApply) {
		this.assetPurchaseApply = assetPurchaseApply;
	}

	public List<AttachementDto> getAttachements() {
		return attachements;
	}

	public void setAttachements(List<AttachementDto> attachements) {
		this.attachements = attachements;
	}

	public Map<String, String> getFeatureMap() {
		return featureMap;
	}

	public void setFeatureMap(Map<String, String> featureMap) {
		this.featureMap = featureMap;
	}

	public boolean isManageCountyQhb() {
		return manageCountyQhb;
	}

	public void setManageCountyQhb(boolean manageCountyQhb) {
		this.manageCountyQhb = manageCountyQhb;
	}

	public List<CnWarehouseDto> getWarehouseDtos() {
		return warehouseDtos;
	}

	public void setWarehouseDtos(List<CnWarehouseDto> warehouseDtos) {
		this.warehouseDtos = warehouseDtos;
	}

	public Date getLeaseProtocolBeginTime() {
		return leaseProtocolBeginTime;
	}

	public void setLeaseProtocolBeginTime(Date leaseProtocolBeginTime) {
		this.leaseProtocolBeginTime = leaseProtocolBeginTime;
	}

	public Date getLeaseProtocolEndTime() {
		return leaseProtocolEndTime;
	}

	public void setLeaseProtocolEndTime(Date leaseProtocolEndTime) {
		this.leaseProtocolEndTime = leaseProtocolEndTime;
	}

	public CountyStationLeaseTypeEnum getLeaseTypeEnum() {
		return leaseTypeEnum;
	}

	public void setLeaseTypeEnum(CountyStationLeaseTypeEnum leaseTypeEnum) {
		this.leaseTypeEnum = leaseTypeEnum;
	}

	public String getLeasePayment() {
		return leasePayment;
	}

	public void setLeasePayment(String leasePayment) {
		this.leasePayment = leasePayment;
	}

	public String getWaterPayment() {
		return waterPayment;
	}

	public void setWaterPayment(String waterPayment) {
		this.waterPayment = waterPayment;
	}

	public String getElectricPayment() {
		return electricPayment;
	}

	public void setElectricPayment(String electricPayment) {
		this.electricPayment = electricPayment;
	}

	public String getPropertyPayment() {
		return propertyPayment;
	}

	public void setPropertyPayment(String propertyPayment) {
		this.propertyPayment = propertyPayment;
	}

	public String getStorageArea() {
		return storageArea;
	}

	public void setStorageArea(String storageArea) {
		this.storageArea = storageArea;
	}

	public String getLeaseProtocolBeginTimeFormat() {
		return leaseProtocolBeginTimeFormat;
	}

	public void setLeaseProtocolBeginTimeFormat(String leaseProtocolBeginTimeFormat) {
		this.leaseProtocolBeginTimeFormat = leaseProtocolBeginTimeFormat;
	}

	public String getLeaseProtocolEndTimeFormat() {
		return leaseProtocolEndTimeFormat;
	}

	public void setLeaseProtocolEndTimeFormat(String leaseProtocolEndTimeFormat) {
		this.leaseProtocolEndTimeFormat = leaseProtocolEndTimeFormat;
	}


	
}
