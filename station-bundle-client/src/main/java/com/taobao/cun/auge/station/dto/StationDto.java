package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.taobao.cun.attachment.dto.AttachmentDto;
import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceIsOnTown;
import com.taobao.cun.auge.station.enums.StationAreaTypeEnum;
import com.taobao.cun.auge.station.enums.StationFixedTypeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.enums.StationType;
import com.taobao.cun.auge.station.enums.StationlLogisticsStateEnum;
import com.taobao.cun.auge.store.dto.StoreDto;

public class StationDto extends OperatorDto implements Serializable{

	private static final long serialVersionUID = 5611747398839067505L;
	
	/**
     * 主键
     */
    private Long id;

    /**
     * 服务站名称
     */
    private String name;

    /**
     * 服务站简介
     */
    private String description;


    /**
     * 服务点状态
     */
    private StationStateEnum state;

    /**
     * 县服务中心orgId
     */
    private Long applyOrg;
    
    /**
     * 县服务中心名称
     */
    private String countyStationName;

    /**
     * 村点编号
     */
    private String stationNum;


    /**
     * 覆盖人口
     */
    private String covered;

    /**
     * 特色农副产品
     */
    private String products;

    /**
     * 物流状态
     */
    private StationlLogisticsStateEnum logisticsState;

    /**
     * 目前业态
     */
    private String format;

    /**
     * 固点，或者不固点
     */
    private StationAreaTypeEnum areaType;

    /**
     * 管理员user_id
     */
    private String managerId;

    /**
     * 服务商id
     */
    private Long providerId;

    /**
     * 其他特性，用于扩展服务站属性
     */
    private Map<String,String> feature;

    /**
     * 新的服务站状态
     */
    private StationStatusEnum status;

    /**
     * 场地固点类型 GOV_FIXED 政府固点
	 *	TRIPARTITE_FIXED 三方固点
     */
    private StationFixedTypeEnum fixedType;
    
    /**
     * 服务站相关附件
     */
    private List<AttachmentDto> attachments;
    
    /**
     * 服务站地址相关信息
     */
    private Address  address;
    
    private PartnerProtocolRelDto fixedProtocols;
    
    
    private String alipayAccount;
    
    private String taobaoNick;
    
    private Long taobaoUserId;

    /**
	 * 是否在镇上
	 */
	private PartnerInstanceIsOnTown partnerInstanceIsOnTown;

	private Integer stationType;
	
	/**
	 * 门店信息
	 */
	private StoreDto storeDto;
	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getApplyOrg() {
		return applyOrg;
	}

	public void setApplyOrg(Long applyOrg) {
		this.applyOrg = applyOrg;
	}

	public String getStationNum() {
		return stationNum;
	}

	public void setStationNum(String stationNum) {
		this.stationNum = stationNum;
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

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

	public Map<String, String> getFeature() {
		return feature;
	}

	public void setFeature(Map<String, String> feature) {
		this.feature = feature;
	}

	public StationStatusEnum getStatus() {
		return status;
	}

	public void setStatus(StationStatusEnum status) {
		this.status = status;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public StationFixedTypeEnum getFixedType() {
		return fixedType;
	}

	public void setFixedType(StationFixedTypeEnum fixedType) {
		this.fixedType = fixedType;
	}

	public StationStateEnum getState() {
		return state;
	}

	public void setState(StationStateEnum state) {
		this.state = state;
	}

	public StationAreaTypeEnum getAreaType() {
		return areaType;
	}

	public void setAreaType(StationAreaTypeEnum areaType) {
		this.areaType = areaType;
	}

	public PartnerProtocolRelDto getFixedProtocols() {
		return fixedProtocols;
	}

	public void setFixedProtocols(PartnerProtocolRelDto fixedProtocols) {
		this.fixedProtocols = fixedProtocols;
	}

	public StationlLogisticsStateEnum getLogisticsState() {
		return logisticsState;
	}

	public void setLogisticsState(StationlLogisticsStateEnum logisticsState) {
		this.logisticsState = logisticsState;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public String getTaobaoNick() {
		return taobaoNick;
	}

	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public String getCountyStationName() {
		return countyStationName;
	}

	public void setCountyStationName(String countyStationName) {
		this.countyStationName = countyStationName;
	}

	public PartnerInstanceIsOnTown getPartnerInstanceIsOnTown() {
		return partnerInstanceIsOnTown;
	}

	public void setPartnerInstanceIsOnTown(PartnerInstanceIsOnTown partnerInstanceIsOnTown) {
		this.partnerInstanceIsOnTown = partnerInstanceIsOnTown;
	}

	public List<AttachmentDto> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentDto> attachments) {
		this.attachments = attachments;
	}
	
	public boolean isStore(){
		return isStationType(StationType.STORE);
	}
	
	public boolean isStationType(StationType type){
		if(this.stationType == null) return false;
		return StationType.hasType(stationType,type);
	}
	
	public boolean isStation(){
		return isStationType(StationType.STATION);
	}

	public Integer getStationType() {
		return stationType;
	}

	public void setStationType(Integer stationType) {
		this.stationType = stationType;
	}

	public StoreDto getStoreDto() {
		return storeDto;
	}

	public void setStoreDto(StoreDto storeDto) {
		this.storeDto = storeDto;
	}
	
	
}
