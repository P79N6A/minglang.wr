package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceCloseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceIsCurrentEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTransStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.StationApplyStateEnum;
import com.taobao.cun.auge.station.enums.StationDecoratePaymentTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecorateTypeEnum;

public class PartnerInstanceDto extends OperatorDto implements Serializable {

	private static final long serialVersionUID = -3494792623384321459L;

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 申请时间
	 */
	private Date applyTime;

	/**
	 * 服务开始时间
	 */
	private Date serviceBeginTime;

	/**
	 * 服务结束时间
	 */
	private Date serviceEndTime;

	/**
	 * 淘帮手所属合伙人的村服务站id
	 */
	private Long parentStationId;

	/**
	 * 状态
	 */
	private PartnerInstanceStateEnum state;

	/**
	 * 申请人
	 */
	private String applierId;

	/**
	 * 位：可以用来标示淘帮手是否是由合伙人降级而来
	 */
	private Integer bit;

	/**
	 * 合伙人or村拍档
	 */
	private PartnerInstanceTypeEnum type;

	/**
	 * 开业时间
	 */
	private Date openDate;

	/**
	 * 是否是当前人
	 */
	private PartnerInstanceIsCurrentEnum isCurrent;

	/**
	 * 申请人类型，buc，还是havana
	 */
	private String applierType;

	/**
	 * 停业类型：合伙人主动退出，还是小二主动清退
	 */
	private PartnerInstanceCloseTypeEnum closeType;

	/**
	 * station_apply表的state 兼容老模型
	 */
	private StationApplyStateEnum stationApplyState;

	/**
	 * 淘宝id
	 */
	private Long taobaoUserId;

	/**
	 * 村点id
	 */
	private Long stationId;

	/**
	 * 合伙人主键id
	 */
	private Long partnerId;

	/**
	 * 村点dto
	 */
	private StationDto stationDto;

	/**
	 * 合伙人dto
	 */
	private PartnerDto partnerDto;

	/**
	 * 生命周期dto
	 */
	private PartnerLifecycleDto partnerLifecycleDto;

	/**
	 * 站点装修类型
	 */
	private StationDecorateTypeEnum stationDecorateTypeEnum;

	/**
	 * 站点装修出资类型
	 */
	private StationDecoratePaymentTypeEnum stationDecoratePaymentTypeEnum;

	/**
	 * 乐观锁版本
	 */
	private Long version;

	/**
	 * 合伙人分层dto
	 */
	private PartnerInstanceLevelDto partnerInstanceLevel;

	private String mode;
	
	private Long sellerId;
	
	private Long shopId;
	
	private Long distributionChannelId;
	
	private String distributorCode;
	
	private PartnerInstanceTransStatusEnum transStatusEnum;

	private String incomeMode;

	private Date incomeModeBeginTime;

	/**实际运营人*/
	private String operateName;
	/**运营人电话*/
	private String operatePhone;
	/**是否本人经营*/
	private String operatePersonal;
	/**经营模式*/
	private String operateMethod;
	
	/**
	 * 是否有巡检记录
	 */
	private boolean hasInspectionRecord;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOperateName() {
		return operateName;
	}

	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}

	public String getOperatePhone() {
		return operatePhone;
	}

	public void setOperatePhone(String operatePhone) {
		this.operatePhone = operatePhone;
	}

	public String getOperatePersonal() {
		return operatePersonal;
	}

	public void setOperatePersonal(String operatePersonal) {
		this.operatePersonal = operatePersonal;
	}

	public String getOperateMethod() {
		return operateMethod;
	}

	public void setOperateMethod(String operateMethod) {
		this.operateMethod = operateMethod;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getServiceBeginTime() {
		return serviceBeginTime;
	}

	public void setServiceBeginTime(Date serviceBeginTime) {
		this.serviceBeginTime = serviceBeginTime;
	}

	public Date getServiceEndTime() {
		return serviceEndTime;
	}

	public void setServiceEndTime(Date serviceEndTime) {
		this.serviceEndTime = serviceEndTime;
	}

	public Long getParentStationId() {
		return parentStationId;
	}

	public void setParentStationId(Long parentStationId) {
		this.parentStationId = parentStationId;
	}

	public PartnerInstanceStateEnum getState() {
		return state;
	}

	public void setState(PartnerInstanceStateEnum state) {
		this.state = state;
	}

	public String getApplierId() {
		return applierId;
	}

	public void setApplierId(String applierId) {
		this.applierId = applierId;
	}

	public Integer getBit() {
		return bit;
	}  

	public void setBit(Integer bit) {
		this.bit = bit;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public String getApplierType() {
		return applierType;
	}

	public void setApplierType(String applierType) {
		this.applierType = applierType;
	}

	public StationDto getStationDto() {
		return stationDto;
	}

	public void setStationDto(StationDto stationDto) {
		this.stationDto = stationDto;
	}

	public PartnerDto getPartnerDto() {
		return partnerDto;
	}

	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
	}

	public PartnerLifecycleDto getPartnerLifecycleDto() {
		return partnerLifecycleDto;
	}

	public void setPartnerLifecycleDto(PartnerLifecycleDto partnerLifecycleDto) {
		this.partnerLifecycleDto = partnerLifecycleDto;
	}

	public PartnerInstanceTypeEnum getType() {
		return type;
	}

	public void setType(PartnerInstanceTypeEnum type) {
		this.type = type;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public PartnerInstanceIsCurrentEnum getIsCurrent() {
		return isCurrent;
	}

	public void setIsCurrent(PartnerInstanceIsCurrentEnum isCurrent) {
		this.isCurrent = isCurrent;
	}

	public PartnerInstanceCloseTypeEnum getCloseType() {
		return closeType;
	}

	public void setCloseType(PartnerInstanceCloseTypeEnum closeType) {
		this.closeType = closeType;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public StationDecorateTypeEnum getStationDecorateTypeEnum() {
		return stationDecorateTypeEnum;
	}

	public void setStationDecorateTypeEnum(StationDecorateTypeEnum stationDecorateTypeEnum) {
		this.stationDecorateTypeEnum = stationDecorateTypeEnum;
	}

	public StationDecoratePaymentTypeEnum getStationDecoratePaymentTypeEnum() {
		return stationDecoratePaymentTypeEnum;
	}

	public void setStationDecoratePaymentTypeEnum(StationDecoratePaymentTypeEnum stationDecoratePaymentTypeEnum) {
		this.stationDecoratePaymentTypeEnum = stationDecoratePaymentTypeEnum;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public StationApplyStateEnum getStationApplyState() {
		return stationApplyState;
	}

	public void setStationApplyState(StationApplyStateEnum stationApplyState) {
		this.stationApplyState = stationApplyState;
	}

	public PartnerInstanceLevelDto getPartnerInstanceLevel() {
		return partnerInstanceLevel;
	}

	public void setPartnerInstanceLevel(PartnerInstanceLevelDto partnerInstanceLevel) {
		this.partnerInstanceLevel = partnerInstanceLevel;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

	public PartnerInstanceTransStatusEnum getTransStatusEnum() {
		return transStatusEnum;
	}

	public void setTransStatusEnum(PartnerInstanceTransStatusEnum transStatusEnum) {
		this.transStatusEnum = transStatusEnum;
	}

	public boolean isHasInspectionRecord() {
		return hasInspectionRecord;
	}

	public void setHasInspectionRecord(boolean hasInspectionRecord) {
		this.hasInspectionRecord = hasInspectionRecord;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getDistributionChannelId() {
		return distributionChannelId;
	}

	public void setDistributionChannelId(Long distributionChannelId) {
		this.distributionChannelId = distributionChannelId;
	}

	public String getDistributorCode() {
		return distributorCode;
	}

	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}

	public String getIncomeMode() {
		return incomeMode;
	}

	public void setIncomeMode(String incomeMode) {
		this.incomeMode = incomeMode;
	}

	public Date getIncomeModeBeginTime() {
		return incomeModeBeginTime;
	}

	public void setIncomeModeBeginTime(Date incomeModeBeginTime) {
		this.incomeModeBeginTime = incomeModeBeginTime;
	}
}
