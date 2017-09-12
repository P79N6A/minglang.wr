package com.taobao.cun.auge.asset.dto;

import java.util.List;

/**
 * Created by xiao on 17/9/12.
 */
public class AssetAppMessageDto {

    private String AppId = "cuntaoCRM";

    private String receiverType = "EMPIDS";

    private String msgType = "cuntaoCRMAsset";

    private String action = "all";

    private String msgTypeDetail;

    private Long bizId;

    private String title;

    private String content;

    private List<Long> receiverList;

    public String getAppId() {
        return AppId;
    }

    public void setAppId(String appId) {
        AppId = appId;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMsgTypeDetail() {
        return msgTypeDetail;
    }

    public void setMsgTypeDetail(String msgTypeDetail) {
        this.msgTypeDetail = msgTypeDetail;
    }

    public Long getBizId() {
        return bizId;
    }

    public void setBizId(Long bizId) {
        this.bizId = bizId;
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

    public List<Long> getReceiverList() {
        return receiverList;
    }

    public void setReceiverList(List<Long> receiverList) {
        this.receiverList = receiverList;
    }


}
