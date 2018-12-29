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
	/**
     * 主键ID
     */
	private Long id;
	/**
     * 任务类型
     */
	private String taskType;
	
	/**
     * 任务code
     */
	private String taskCode;

	/**
     * 任务状态
     */
	private String taskStatus;
	/**
     * 盘点人名字
     */
	private String checkerName;
	/**
     * 盘点人ID
     */
	private String checkerId;
	/**
     * 盘点人类型
     */
	private String checkerType;
	/**
     * 村ID
     */
	private Long stationId;
	/**
     * 县组织ID
     */
	private Long orgId;
	/**
     * 县ID
     */
	private String stationName;
	/**
     * 县名称
     */
	private String orgName;
	/**
     * 其他原因
     */
	private String otherReason;
	/**
     * 遗失资产
     */
	private String lostAsset;
	/**
     * 待回收资产
     */
	private String waitBackAsset;
	
	/**
	 * 总资产数
	 */
	private Long assetCount;
	/**
	 * 已盘资产数
	 */
	private Long  doneCount;
	
	/**
     * 村点信息
     */
    private AssetCheckStationExtInfo stationExtInfo;
    

	public Long getAssetCount() {
		return assetCount;
	}

	public void setAssetCount(Long assetCount) {
		this.assetCount = assetCount;
	}

	public Long getDoneCount() {
		return doneCount;
	}

	public void setDoneCount(Long doneCount) {
		this.doneCount = doneCount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AssetCheckStationExtInfo getStationExtInfo() {
		return stationExtInfo;
	}

	public void setStationExtInfo(AssetCheckStationExtInfo stationExtInfo) {
		this.stationExtInfo = stationExtInfo;
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
