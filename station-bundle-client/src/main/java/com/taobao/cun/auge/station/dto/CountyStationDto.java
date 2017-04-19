package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.taobao.cun.auge.station.enums.CountyStationLeaseTypeEnum;
import com.taobao.cun.auge.station.enums.CountyStationManageModelEnum;
import com.taobao.cun.auge.station.enums.CountyStationManageStatusEnum;

/**
 * 县服务中心dto
 * @author quanzhu.wangqz
 *
 */
public class CountyStationDto implements Serializable{

	private static final long serialVersionUID = -2917747436881893641L;

	/**
	 * 主键id
	 */
    private Long id;
   
    /**
     * 县服务中心名称
     */
    private String name;
    /**
     * 省
     */
    private String province;
    /**
     * 省详情
     */
    private String provinceDetail;
    /**
     * 市
     */
    private String city;

    /**
     * 市详情
     */
    private String cityDetail;

    /**
     * 县
     */
    private String county;

    /**
     * 县详情
     */
    private String countyDetail;

    /**
     * 乡
     */
    private String town;

    /**
     * 乡详情
     */
    private String townDetail;

    /**
     * 详细地址
     */
    private String addressDetail;

    /**
     * 组织id
     */
    private Long orgId;

    /**
     * 工号
     */
    private String employeeId;

    /**
     * taobaoUserId
     */
    private Long taobaoUserId;

    /**
     * 经营状态
     */
    private CountyStationManageStatusEnum manageStatus;

    /**
     * 服务中心办公面积
     */
    private String acreage;

    /**
     * 仓库数
     */
    private Integer warehouseNum;

    /**
     * 服务中心租赁模式
     */
    private String leasingModel;

    /**
     * 办公地情况
     */
    private String officeDetail;

    /**
     * 免租期限
     */
    private String freeDeadline;

    /**
     * 其他自理费用
     */
    private String selfCosts;

    /**
     * 备注
     */
    private String remark;

    /**
     * 经营模式：SELF（自营）、THIRD_PARTY（第三方）
     */
    private CountyStationManageModelEnum manageModel;

    /**
     * 淘宝昵称
     */
    private String taobaoNick;

    /**
     * 县运营中心的父组织ID，即大区组织id
     */
    private Long parentId;

    /**
     * 联系电话
     */
    private String employeePhone;

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 物流联系电话
     */
    private String logisticsPhone;

    /**
     * 物流操作人
     */
    private String logisticsOperator;

    /**
     * 开始运营时间
     */
    private Date gmtStartOperation;

    /**
     * 其他特性
     */
    private Map<String,String> featureMap;

    /**
     * 租赁协议开始时间
     */
    private Date leaseProtocolBeginTime;

    /**
     * 租赁协议结束时间
     */
    private Date leaseProtocolEndTime;

    /**
     * 租赁类型
     */
    private CountyStationLeaseTypeEnum leaseType;

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

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
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

	public CountyStationManageModelEnum getManageModel() {
		return manageModel;
	}

	public void setManageModel(CountyStationManageModelEnum manageModel) {
		this.manageModel = manageModel;
	}

	public String getTaobaoNick() {
		return taobaoNick;
	}

	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getEmployeePhone() {
		return employeePhone;
	}

	public void setEmployeePhone(String employeePhone) {
		this.employeePhone = employeePhone;
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

	public String getLogisticsPhone() {
		return logisticsPhone;
	}

	public void setLogisticsPhone(String logisticsPhone) {
		this.logisticsPhone = logisticsPhone;
	}

	public String getLogisticsOperator() {
		return logisticsOperator;
	}

	public void setLogisticsOperator(String logisticsOperator) {
		this.logisticsOperator = logisticsOperator;
	}

	public Date getGmtStartOperation() {
		return gmtStartOperation;
	}

	public void setGmtStartOperation(Date gmtStartOperation) {
		this.gmtStartOperation = gmtStartOperation;
	}

	public Map<String, String> getFeatureMap() {
		return featureMap;
	}

	public void setFeatureMap(Map<String, String> featureMap) {
		this.featureMap = featureMap;
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

	public CountyStationLeaseTypeEnum getLeaseType() {
		return leaseType;
	}

	public void setLeaseType(CountyStationLeaseTypeEnum leaseType) {
		this.leaseType = leaseType;
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
}
