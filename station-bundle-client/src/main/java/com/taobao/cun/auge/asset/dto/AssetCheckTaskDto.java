package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 资产任务dto
 * @author quanzhu.wangqz
 *
 */
public class AssetCheckTaskDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7771666556620868874L;

	private Long id;


	private Date gmtCreate;


	private Date gmtModified;

	private String creator;


	private String modifier;


	private String isDeleted;


	private String taskType;


	private String taskCode;


	private String taskStatus;

	private String checkerName;

	private String checkerId;

	private String checkerType;

	private Long stationId;

	private Long orgId;

	private String stationName;

	private String orgName;

	private String otherReason;

	private String stationExtInfo;

	private String lostAsset;

	private String waitBackAsset;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getCheckerName() {
		return checkerName;
	}

	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}

	public String getCheckerId() {
		return checkerId;
	}

	public void setCheckerId(String checkerId) {
		this.checkerId = checkerId;
	}

	public String getCheckerType() {
		return checkerType;
	}

	public void setCheckerType(String checkerType) {
		this.checkerType = checkerType;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOtherReason() {
		return otherReason;
	}

	public void setOtherReason(String otherReason) {
		this.otherReason = otherReason;
	}

	public String getStationExtInfo() {
		return stationExtInfo;
	}

	public void setStationExtInfo(String stationExtInfo) {
		this.stationExtInfo = stationExtInfo;
	}

	public String getLostAsset() {
		return lostAsset;
	}

	public void setLostAsset(String lostAsset) {
		this.lostAsset = lostAsset;
	}

	public String getWaitBackAsset() {
		return waitBackAsset;
	}

	public void setWaitBackAsset(String waitBackAsset) {
		this.waitBackAsset = waitBackAsset;
	}

	@Override
	public String toString() {
		return "AssetCheckTaskDto{" +
				"id=" + id +
				", gmtCreate=" + gmtCreate +
				", gmtModified=" + gmtModified +
				", creator='" + creator + '\'' +
				", modifier='" + modifier + '\'' +
				", isDeleted='" + isDeleted + '\'' +
				", taskType='" + taskType + '\'' +
				", taskCode='" + taskCode + '\'' +
				", taskStatus='" + taskStatus + '\'' +
				", checkerName='" + checkerName + '\'' +
				", checkerId='" + checkerId + '\'' +
				", checkerType='" + checkerType + '\'' +
				", stationId=" + stationId +
				", orgId=" + orgId +
				", stationName='" + stationName + '\'' +
				", orgName='" + orgName + '\'' +
				", otherReason='" + otherReason + '\'' +
				", stationExtInfo='" + stationExtInfo + '\'' +
				", lostAsset='" + lostAsset + '\'' +
				", waitBackAsset='" + waitBackAsset + '\'' +
				'}';
	}
}
