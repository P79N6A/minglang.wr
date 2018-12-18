package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

/**
 * @Description:
 * @author: qinguw.yhp
 * @Date: 2018-12-01 18:52
 */
public class FileUploadDto implements Serializable{


    private static final long serialVersionUID = -5568796554032860063L;

    private String title;

    private String fileType;

    private String fileUrl;

    //文件的补充url，比如视频的首帧，视频的尾帧
    private String addtionalUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getAddtionalUrl() {
        return addtionalUrl;
    }

    public void setAddtionalUrl(String addtionalUrl) {
        this.addtionalUrl = addtionalUrl;
    }

    @Override
    public String toString() {
        return "FileUploadDto{" +
                "title='" + title + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", addtionalUrl='" + addtionalUrl + '\'' +
                '}';
    }


}
