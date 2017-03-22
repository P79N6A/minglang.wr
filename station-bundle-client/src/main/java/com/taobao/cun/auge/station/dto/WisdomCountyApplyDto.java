package com.taobao.cun.auge.station.dto;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.WisdomCountyStateEnum;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by xiao on 16/10/17.
 */
public class WisdomCountyApplyDto extends OperatorDto implements Serializable {

    private static final long serialVersionUID = -2909250274438543672L;

    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private String isDeleted;

    private String leaderName;

    private String job;

    private String mail;

    private Long countyId;

    private String remark;

    private String leaderPhone;

    private Long applyAttachmentId;

    private Long functionAttachmentId;

    private WisdomCountyStateEnum state;

    private List<AttachementDto> attachementDtos;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public List<AttachementDto> getAttachementDtos() {
        return attachementDtos;
    }

    public void setAttachementDtos(List<AttachementDto> attachementDtos) {
        this.attachementDtos = attachementDtos;
    }

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

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Long getCountyId() {
        return countyId;
    }

    public void setCountyId(Long countyId) {
        this.countyId = countyId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLeaderPhone() {
        return leaderPhone;
    }

    public void setLeaderPhone(String leaderPhone) {
        this.leaderPhone = leaderPhone;
    }

    public Long getApplyAttachmentId() {
        return applyAttachmentId;
    }

    public void setApplyAttachmentId(Long applyAttachmentId) {
        this.applyAttachmentId = applyAttachmentId;
    }

    public Long getFunctionAttachmentId() {
        return functionAttachmentId;
    }

    public void setFunctionAttachmentId(Long functionAttachmentId) {
        this.functionAttachmentId = functionAttachmentId;
    }

    public WisdomCountyStateEnum getState() {
        return state;
    }

    public void setState(WisdomCountyStateEnum state) {
        this.state = state;
    }
}
