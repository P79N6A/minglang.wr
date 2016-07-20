package com.taobao.cun.auge.station.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.OperatorDto;

public class StationDecorateReflectDto extends OperatorDto {

	private static final long serialVersionUID = -3820265156480664238L;

	/**
	 * 主键id
	 */
	@NotNull(message="id not null")
    private Long id;
    
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
     * 附件
     */
    private List<AttachementDto> attachements;
    
    /**
     * 装修反馈人
     */
    private Long reflectUserId;
    
    /**
     * 反馈人nick
     */
    private String reflectNick;

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

	public String getReflectNick() {
		return reflectNick;
	}

	public void setReflectNick(String reflectNick) {
		this.reflectNick = reflectNick;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCarpetArea() {
		return carpetArea;
	}

	public void setCarpetArea(String carpetArea) {
		this.carpetArea = carpetArea;
	}
	
	
}
