package com.taobao.cun.auge.station.dto;

import java.util.List;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.StationDecorateIsValidEnum;
import com.taobao.cun.auge.station.enums.StationDecorateStatusEnum;

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
    private List<AttachementDto> attachements;

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

	public List<AttachementDto> getAttachements() {
		return attachements;
	}

	public void setAttachements(List<AttachementDto> attachements) {
		this.attachements = attachements;
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
}
