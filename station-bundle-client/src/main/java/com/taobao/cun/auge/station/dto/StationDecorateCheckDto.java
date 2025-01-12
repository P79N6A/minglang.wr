package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.List;

import com.taobao.cun.attachment.dto.AttachmentDto;

public class StationDecorateCheckDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 站点ID
	 */
	private Long stationId;
	/**
	 * 反馈门头附件
	 */
	private List<AttachmentDto> checkDoorAttachments;
	
	/**
	 * 反馈背景墙附件
	 */
	private List<AttachmentDto> checkWallDeskAttachments;
	
	/**
	 * 反馈室外全景图附件
	 */
	private List<AttachmentDto> checkOutsideAttachments;
	
	/**
	 * 反馈室内全景图附件
	 */
	private List<AttachmentDto> checkInsideAttachments;

	/**
	 * 反馈前台桌附件
	 */
	private List<AttachmentDto> checkMaterielAttachments;
	
	
	/**
	 * 反馈室外视频附件
	 */
	private List<AttachmentDto> checkOutsideVideoAttachments;
	
	
	/**
	 * 反馈室内视频附件
	 */
	private List<AttachmentDto> checkInsideVideoAttachments;

	/**
	 * 操作人员
	 */
	private String operator;
	public List<AttachmentDto> getCheckDoorAttachments() {
		return checkDoorAttachments;
	}


	public void setCheckDoorAttachments(List<AttachmentDto> checkDoorAttachments) {
		this.checkDoorAttachments = checkDoorAttachments;
	}


	public List<AttachmentDto> getCheckOutsideAttachments() {
		return checkOutsideAttachments;
	}


	public void setCheckOutsideAttachments(List<AttachmentDto> checkOutsideAttachments) {
		this.checkOutsideAttachments = checkOutsideAttachments;
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


	public Long getStationId() {
		return stationId;
	}


	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}


	public String getOperator() {
		return operator;
	}


	public void setOperator(String operator) {
		this.operator = operator;
	}


	public List<AttachmentDto> getCheckWallDeskAttachments() {
		return checkWallDeskAttachments;
	}


	public void setCheckWallDeskAttachments(List<AttachmentDto> checkWallDeskAttachments) {
		this.checkWallDeskAttachments = checkWallDeskAttachments;
	}


	public List<AttachmentDto> getCheckInsideAttachments() {
		return checkInsideAttachments;
	}


	public void setCheckInsideAttachments(List<AttachmentDto> checkInsideAttachments) {
		this.checkInsideAttachments = checkInsideAttachments;
	}


	public List<AttachmentDto> getCheckMaterielAttachments() {
		return checkMaterielAttachments;
	}


	public void setCheckMaterielAttachments(List<AttachmentDto> checkMaterielAttachments) {
		this.checkMaterielAttachments = checkMaterielAttachments;
	}
	
}
