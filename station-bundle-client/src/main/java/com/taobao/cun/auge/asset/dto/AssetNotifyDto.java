package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by xiao on 17/5/24.
 */
public class AssetNotifyDto implements Serializable{

    private static final long serialVersionUID = -7835446498198910715L;

    private String appId;

    //默认为0 系统发送
    private Long sender = 0L;

    private List<Long> receivers;

    private String receiverType;

    private String msgType;

    private String msgTypeDetail;

    private String action;

    private Content content;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public List<Long> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<Long> receivers) {
        this.receivers = receivers;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgTypeDetail() {
        return msgTypeDetail;
    }

    public void setMsgTypeDetail(String msgTypeDetail) {
        this.msgTypeDetail = msgTypeDetail;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public class Content implements Serializable{

        private static final long serialVersionUID = 6655145712877609691L;

        private Long bizId;

        private Date publishTime;

        private String title;

        private String content;

        private String routeUrl;

        public Long getBizId() {
            return bizId;
        }

        public void setBizId(Long bizId) {
            this.bizId = bizId;
        }

        public Date getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(Date publishTime) {
            this.publishTime = publishTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getRouteUrl() {
            return routeUrl;
        }

        public void setRouteUrl(String routeUrl) {
            this.routeUrl = routeUrl;
        }
    }
}

