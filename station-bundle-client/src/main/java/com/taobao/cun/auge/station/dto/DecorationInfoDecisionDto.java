
package com.taobao.cun.auge.station.dto;

import java.math.BigDecimal;
import java.util.List;

import com.taobao.cun.attachment.dto.AttachmentDto;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.DecorationInfoDecisionStatusEnum;

/**
 * @author alibaba-54766
 *
 */
public class DecorationInfoDecisionDto extends OperatorDto{

    private static final long serialVersionUID = 871789291046860825L;

    private Long id;
    
    private Long stationId;
    
    private DecorationInfoDecisionStatusEnum status;
    
    private Long orgId;

    private String shopArea;

    private String warehouseArea;

    private BigDecimal decorateMoney;
    
    /**
     * 是否同意
     */
    private Boolean isAgree;
    
    /**
     * 审计意见
     */
    private String auditOpinion;
    
    /**
     * 附件
     */
    private List<AttachmentDto> attachments;
    
    private StationDto stationDto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public DecorationInfoDecisionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(DecorationInfoDecisionStatusEnum status) {
        this.status = status;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getShopArea() {
        return shopArea;
    }

    public void setShopArea(String shopArea) {
        this.shopArea = shopArea;
    }

    public String getWarehouseArea() {
        return warehouseArea;
    }

    public void setWarehouseArea(String warehouseArea) {
        this.warehouseArea = warehouseArea;
    }

    public BigDecimal getDecorateMoney() {
        return decorateMoney;
    }

    public void setDecorateMoney(BigDecimal decorateMoney) {
        this.decorateMoney = decorateMoney;
    }

    public Boolean getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(Boolean isAgree) {
        this.isAgree = isAgree;
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
    }

    public List<AttachmentDto> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDto> attachments) {
        this.attachments = attachments;
    }

    public StationDto getStationDto() {
        return stationDto;
    }

    public void setStationDto(StationDto stationDto) {
        this.stationDto = stationDto;
    }
    
}
