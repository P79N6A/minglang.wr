package com.taobao.cun.auge.station.dto;

import java.math.BigDecimal;
import java.util.List;

import com.taobao.cun.attachment.dto.AttachmentDto;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.StationDecorateIsValidEnum;
import com.taobao.cun.auge.station.enums.StationDecoratePaymentTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecorateStatusEnum;
import com.taobao.cun.auge.station.enums.StationDecorateTypeEnum;

/**
 * 服务站装修记录dto
 * @author quanzhu.wangqz
 *
 */
public class StationDecorateDto  extends OperatorDto {

	private static final long serialVersionUID = -237618830587383870L;

	/**
	 * 主键id
	 */
    private Long id;

    /**
     * 服务站id
     */
    private Long stationId;

    /**
     * 合伙人淘宝userid
     */
    private Long partnerUserId;

    /**
     * 卖家淘宝userid
     */
    private String sellerTaobaoUserId;

    /**
     * 淘宝订单号
     */
    private String taobaoOrderNum;

    /**
     * 状态
     */
    private StationDecorateStatusEnum status;

    /**
     * 装修反馈号
     */
    private String reflectOrderNum;

    /**
     * 门头面积
     */
    private String doorArea;

    /**
     * 墙贴面积
     */
    private String wallArea;

    /**
     * 玻璃贴面积
     */
    private String glassArea;

    /**
     * 室内围贴面积
     */
    private String insideArea;

    /**
     * 室内装修面积
     */
    private String carpetArea;

	/**
	 * 装修反馈新增是否满足标准化
	 */
	private String reflectSatisfySolid;

    /**
     * 附件
     */
    private List<AttachmentDto> attachments;

    /**
     * 装修反馈人
     */
    private Long reflectUserId;

    /**
     * 审计意见
     */
    private String auditOpinion;

    /**
     * 反馈人nick
     */
    private String reflectNick;

    private StationDecorateIsValidEnum  isValid;

    /**
     * 服务站基础信息
     */
    private StationDto stationDto;

    /**
     * 装修订单信息
     */
    private StationDecorateOrderDto stationDecorateOrderDto;

    /**
	 * 淘宝订单详情
	 */
	private String taobaoOrderDetailUrl;

	/**
	 * 淘宝商品
	 */
	private String taobaoItemUrl;
	
	private StationDecoratePaymentTypeEnum paymentType;
	
	private StationDecorateTypeEnum decorateType;

	
	/**
	 * 卖场面积
	 */
	private String mallArea;
	
	/**
	 * 仓库面积
	 */
	private String repoArea;
	
	/**
	 * 租金
	 */
	private BigDecimal rentMoney;
	
	/**
	 * 预算
	 */
	private BigDecimal budgetMoney;
	
	/**
	 * 设计门头附件
	 */
	private List<AttachmentDto> doorAttachments;
	
	/**
	 * 设计背景墙附件
	 */
	private List<AttachmentDto> wallAttachments;
	
	/**
	 * 设计室内图附件
	 */
	private List<AttachmentDto> insideAttachments;
	
	
	private List<AttachmentDto> checkDoorAttachments;
	
	/**
	 * 反馈背景墙附件
	 */
	private List<AttachmentDto> checkWallAttachments;
	
	/**
	 * 反馈室外全景图附件
	 */
	private List<AttachmentDto> checkOutsideAttachments;
	
	
	/**
	 * 反馈前台桌附件
	 */
	private List<AttachmentDto> checkDeskAttachments;
	
	
	/**
	 * 反馈室外视频附件
	 */
	private List<AttachmentDto> checkOutsideVideoAttachments;
	
	
	/**
	 * 反馈室内视频附件
	 */
	private List<AttachmentDto> checkInsideVideoAttachments;
	
	private String designAuditOpinion;
	
	private String checkAuditOpinion;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getPartnerUserId() {
		return partnerUserId;
	}

	public void setPartnerUserId(Long partnerUserId) {
		this.partnerUserId = partnerUserId;
	}

	public String getTaobaoOrderNum() {
		return taobaoOrderNum;
	}

	public void setTaobaoOrderNum(String taobaoOrderNum) {
		this.taobaoOrderNum = taobaoOrderNum;
	}

	public StationDecorateStatusEnum getStatus() {
		return status;
	}

	public void setStatus(StationDecorateStatusEnum status) {
		this.status = status;
	}

	public String getReflectOrderNum() {
		return reflectOrderNum;
	}

	public void setReflectOrderNum(String reflectOrderNum) {
		this.reflectOrderNum = reflectOrderNum;
	}

	public String getDoorArea() {
		return doorArea;
	}

	public void setDoorArea(String doorArea) {
		this.doorArea = doorArea;
	}

	public String getWallArea() {
		return wallArea;
	}

	public void setWallArea(String wallArea) {
		this.wallArea = wallArea;
	}

	public String getGlassArea() {
		return glassArea;
	}

	public void setGlassArea(String glassArea) {
		this.glassArea = glassArea;
	}

	public String getInsideArea() {
		return insideArea;
	}

	public void setInsideArea(String insideArea) {
		this.insideArea = insideArea;
	}

	public Long getReflectUserId() {
		return reflectUserId;
	}

	public void setReflectUserId(Long reflectUserId) {
		this.reflectUserId = reflectUserId;
	}

	public String getAuditOpinion() {
		return auditOpinion;
	}

	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}

	public String getReflectNick() {
		return reflectNick;
	}

	public void setReflectNick(String reflectNick) {
		this.reflectNick = reflectNick;
	}

	public StationDecorateIsValidEnum getIsValid() {
		return isValid;
	}

	public void setIsValid(StationDecorateIsValidEnum isValid) {
		this.isValid = isValid;
	}

	public String getSellerTaobaoUserId() {
		return sellerTaobaoUserId;
	}

	public void setSellerTaobaoUserId(String sellerTaobaoUserId) {
		this.sellerTaobaoUserId = sellerTaobaoUserId;
	}

	public StationDto getStationDto() {
		return stationDto;
	}

	public void setStationDto(StationDto stationDto) {
		this.stationDto = stationDto;
	}

	public StationDecorateOrderDto getStationDecorateOrderDto() {
		return stationDecorateOrderDto;
	}

	public void setStationDecorateOrderDto(
			StationDecorateOrderDto stationDecorateOrderDto) {
		this.stationDecorateOrderDto = stationDecorateOrderDto;
	}

	public String getTaobaoOrderDetailUrl() {
		return taobaoOrderDetailUrl;
	}

	public void setTaobaoOrderDetailUrl(String taobaoOrderDetailUrl) {
		this.taobaoOrderDetailUrl = taobaoOrderDetailUrl;
	}

	public String getTaobaoItemUrl() {
		return taobaoItemUrl;
	}

	public void setTaobaoItemUrl(String taobaoItemUrl) {
		this.taobaoItemUrl = taobaoItemUrl;
	}

	public String getCarpetArea() {
		return carpetArea;
	}

	public void setCarpetArea(String carpetArea) {
		this.carpetArea = carpetArea;
	}

	public String getReflectSatisfySolid() {
		return reflectSatisfySolid;
	}

	public void setReflectSatisfySolid(String reflectSatisfySolid) {
		this.reflectSatisfySolid = reflectSatisfySolid;
	}

	public StationDecoratePaymentTypeEnum getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(StationDecoratePaymentTypeEnum paymentType) {
		this.paymentType = paymentType;
	}

	public StationDecorateTypeEnum getDecorateType() {
		return decorateType;
	}

	public void setDecorateType(StationDecorateTypeEnum decorateType) {
		this.decorateType = decorateType;
	}

	public List<AttachmentDto> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentDto> attachments) {
		this.attachments = attachments;
	}

	public String getMallArea() {
		return mallArea;
	}

	public void setMallArea(String mallArea) {
		this.mallArea = mallArea;
	}

	public String getRepoArea() {
		return repoArea;
	}

	public void setRepoArea(String repoArea) {
		this.repoArea = repoArea;
	}

	public BigDecimal getRentMoney() {
		return rentMoney;
	}

	public void setRentMoney(BigDecimal rentMoney) {
		this.rentMoney = rentMoney;
	}

	public BigDecimal getBudgetMoney() {
		return budgetMoney;
	}

	public void setBudgetMoney(BigDecimal budgetMoney) {
		this.budgetMoney = budgetMoney;
	}

	public List<AttachmentDto> getDoorAttachments() {
		return doorAttachments;
	}

	public void setDoorAttachments(List<AttachmentDto> doorAttachments) {
		this.doorAttachments = doorAttachments;
	}

	public List<AttachmentDto> getWallAttachments() {
		return wallAttachments;
	}

	public void setWallAttachments(List<AttachmentDto> wallAttachments) {
		this.wallAttachments = wallAttachments;
	}

	public List<AttachmentDto> getInsideAttachments() {
		return insideAttachments;
	}

	public void setInsideAttachments(List<AttachmentDto> insideAttachments) {
		this.insideAttachments = insideAttachments;
	}

	public List<AttachmentDto> getCheckDoorAttachments() {
		return checkDoorAttachments;
	}

	public void setCheckDoorAttachments(List<AttachmentDto> checkDoorAttachments) {
		this.checkDoorAttachments = checkDoorAttachments;
	}

	public List<AttachmentDto> getCheckWallAttachments() {
		return checkWallAttachments;
	}

	public void setCheckWallAttachments(List<AttachmentDto> checkWallAttachments) {
		this.checkWallAttachments = checkWallAttachments;
	}

	public List<AttachmentDto> getCheckOutsideAttachments() {
		return checkOutsideAttachments;
	}

	public void setCheckOutsideAttachments(List<AttachmentDto> checkOutsideAttachments) {
		this.checkOutsideAttachments = checkOutsideAttachments;
	}

	public List<AttachmentDto> getCheckDeskAttachments() {
		return checkDeskAttachments;
	}

	public void setCheckDeskAttachments(List<AttachmentDto> checkDeskAttachments) {
		this.checkDeskAttachments = checkDeskAttachments;
	}

	public List<AttachmentDto> getCheckOutsideVideoAttachments() {
		return checkOutsideVideoAttachments;
	}

	public void setCheckOutsideVideoAttachments(List<AttachmentDto> checkOutsideVideoAttachments) {
		this.checkOutsideVideoAttachments = checkOutsideVideoAttachments;
	}

	public List<AttachmentDto> getCheckInsideVideoAttachments() {
		return checkInsideVideoAttachments;
	}

	public void setCheckInsideVideoAttachments(List<AttachmentDto> checkInsideVideoAttachments) {
		this.checkInsideVideoAttachments = checkInsideVideoAttachments;
	}

	public String getDesignAuditOpinion() {
		return designAuditOpinion;
	}

	public void setDesignAuditOpinion(String designAuditOpinion) {
		this.designAuditOpinion = designAuditOpinion;
	}

	public String getCheckAuditOpinion() {
		return checkAuditOpinion;
	}

	public void setCheckAuditOpinion(String checkAuditOpinion) {
		this.checkAuditOpinion = checkAuditOpinion;
	}
}
