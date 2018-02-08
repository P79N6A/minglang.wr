package com.taobao.cun.auge.partner.service.impl;

import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.partner.service.PartnerAssetService;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.dto.ContextDto;
import com.taobao.cun.dto.SystemTypeEnum;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("partnerAssetService")
@HSFProvider(serviceInterface = PartnerAssetService.class)
public class PartnerAssetServiceImpl implements PartnerAssetService {

    @Autowired
    PartnerInstanceBO partnerInstanceBO;

    @Autowired
    AssetBO assetBO;

    @Override
    public boolean isBackAsset(Long instanceId) {
        PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
        if (rel == null) {
            throw new AugeServiceException("查询不到村小二信息！");
        }
        assetBO.validateAssetForQuiting(rel.getStationId(), rel.getTaobaoUserId());
        return true;
    }

    private ContextDto buildSystemContextDto() {
        ContextDto contextDto = new ContextDto();
        contextDto.setSystemType(SystemTypeEnum.CUNTAO_ADMIN);
        contextDto.setAppId("1");
        return contextDto;
    }

    private String getErrorMessage(String methodName, String param, String error) {
        StringBuilder sb = new StringBuilder();
        sb.append("PartnerAssetService-Error|").append(methodName).append("(.param=").append(param).append(").")
            .append("errorMessage:").append(error);
        return sb.toString();
    }

}
