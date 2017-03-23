package com.taobao.cun.auge.evaluate.dto;


import java.io.Serializable;
import java.util.List;

/**
 * 晋升答辩材料DTO
 * Created by xujianhui on 16/12/8.
 */
public class ReplyMaterialDTO implements Serializable {

    private static final long serialVersionUID = -1771781726742389239L;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 答辩材料针对哪个任务节点的.
     */
    private Long levelTaskNodeId;

    /**
     * 状态:已上传,未上传,逾期上传等
      */
    private String status;

    /**
     * 答辩事件描述:比如作为S7晋升答辩
     */
    private String replayEventDesc;

    /**
     * 附件材料
     */
    private List<Attachment> attachmentDtoList;

    public Long getLevelTaskNodeId() {
        return levelTaskNodeId;
    }

    public void setLevelTaskNodeId(Long levelTaskNodeId) {
        this.levelTaskNodeId = levelTaskNodeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Attachment> getAttachmentDtoList() {
        return attachmentDtoList;
    }

    public void setAttachmentDtoList(List<Attachment> attachmentDtoList) {
        this.attachmentDtoList = attachmentDtoList;
    }

    public String getReplayEventDesc() {
        return replayEventDesc;
    }

    public void setReplayEventDesc(String replayEventDesc) {
        this.replayEventDesc = replayEventDesc;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public static class Attachment {
        private String fileName;
        private String fileType;
        /**
         * oss存储的话 是Identifier
         */
        private String url;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static enum UploadStatus {
        NOT_UPLOAD,
        UPLOADED,
        CANT_UPLOAD
        ;
    }
}
