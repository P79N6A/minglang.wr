package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author: qingwu.yhp
 * @Date: 2018-11-29 14:42
 */
public class StationDecorateFeedBackDto implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 站点ID
     */
    private Long stationId;

    /**
     * 站点名称
     */
    private String stationName;

    /**
     * 站点编号
     */
    private String stationNum;

    /**
     * 站点状态
     */
    private String status;

    /**
     * 审核意见
     */
    private String auditOption;

    /**
     * 反馈的室外全景图url
     */
    private List<FileUploadDto> feedbackOutsidePhoto;

    /**
     * 反馈的门头近景图url
     */
    private List<FileUploadDto> feedbackDoorPhoto;

    /**
     * 反馈的前台桌面及背景墙图url
     */
    private List<FileUploadDto> feedbackWallDeskPhoto;

    /**
     * 反馈的室内全景图url
     */
    private List<FileUploadDto> feedbackInsidePhoto;

    /**
     * 反馈的其他LOGO物料图url
     */
    private List<FileUploadDto> feedbackMaterielPhoto;

    /**
     * 反馈室内视频url
     */
    private List<FileUploadDto> feedbackInsideVideo;

    /**
     * 反馈室内视频url
     */
    private List<FileUploadDto>  feedbackOutsideVideo;

    /**
     * 操作人员
     */
    private String operator;

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationNum() {
        return stationNum;
    }

    public void setStationNum(String stationNum) {
        this.stationNum = stationNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuditOption() {
        return auditOption;
    }

    public void setAuditOption(String auditOption) {
        this.auditOption = auditOption;
    }

    public List<FileUploadDto> getFeedbackOutsidePhoto() {
        return feedbackOutsidePhoto;
    }

    public void setFeedbackOutsidePhoto(List<FileUploadDto> feedbackOutsidePhoto) {
        this.feedbackOutsidePhoto = feedbackOutsidePhoto;
    }

    public List<FileUploadDto> getFeedbackDoorPhoto() {
        return feedbackDoorPhoto;
    }

    public void setFeedbackDoorPhoto(List<FileUploadDto> feedbackDoorPhoto) {
        this.feedbackDoorPhoto = feedbackDoorPhoto;
    }

    public List<FileUploadDto> getFeedbackWallDeskPhoto() {
        return feedbackWallDeskPhoto;
    }

    public void setFeedbackWallDeskPhoto(List<FileUploadDto> feedbackWallDeskPhoto) {
        this.feedbackWallDeskPhoto = feedbackWallDeskPhoto;
    }

    public List<FileUploadDto> getFeedbackInsidePhoto() {
        return feedbackInsidePhoto;
    }

    public void setFeedbackInsidePhoto(List<FileUploadDto> feedbackInsidePhoto) {
        this.feedbackInsidePhoto = feedbackInsidePhoto;
    }

    public List<FileUploadDto> getFeedbackMaterielPhoto() {
        return feedbackMaterielPhoto;
    }

    public void setFeedbackMaterielPhoto(List<FileUploadDto> feedbackMaterielPhoto) {
        this.feedbackMaterielPhoto = feedbackMaterielPhoto;
    }

    public List<FileUploadDto> getFeedbackInsideVideo() {
        return feedbackInsideVideo;
    }

    public void setFeedbackInsideVideo(List<FileUploadDto> feedbackInsideVideo) {
        this.feedbackInsideVideo = feedbackInsideVideo;
    }

    public List<FileUploadDto> getFeedbackOutsideVideo() {
        return feedbackOutsideVideo;
    }

    public void setFeedbackOutsideVideo(List<FileUploadDto> feedbackOutsideVideo) {
        this.feedbackOutsideVideo = feedbackOutsideVideo;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }


}
