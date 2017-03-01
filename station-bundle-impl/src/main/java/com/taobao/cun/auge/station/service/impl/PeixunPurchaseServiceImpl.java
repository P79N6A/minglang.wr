package com.taobao.cun.auge.station.service.impl;

import com.taobao.cun.auge.dal.domain.AppResource;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.dto.PartnerPeixunSupplierDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.bo.PeixunPurchaseBO;
import com.taobao.cun.auge.station.condition.PeixunPuchaseQueryCondition;
import com.taobao.cun.auge.station.dto.PeixunPurchaseDto;
import com.taobao.cun.auge.station.service.PeixunPurchaseService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service("peixunPurchaseService")
@HSFProvider(serviceInterface = PeixunPurchaseService.class)
public class PeixunPurchaseServiceImpl implements PeixunPurchaseService {

    @Autowired
    PeixunPurchaseBO peixunPurchaseBO;

    @Autowired
    private AppResourceBO appResourceBO;

    @Override
    public Long createOrUpdatePeixunPurchase(PeixunPurchaseDto dto) {
        return peixunPurchaseBO.createOrUpdatePeixunPurchase(dto);
    }

    @Override
    public boolean rollback(Long id, String operate) {
        return peixunPurchaseBO.rollback(id, operate);
    }

    @Override
    public boolean createOrder(Long id, String operate) {
        return peixunPurchaseBO.createOrder(id, operate);
    }

    @Override
    public PageDto<PeixunPurchaseDto> queryPeixunPurchaseList(
            PeixunPuchaseQueryCondition condition) {
        return peixunPurchaseBO.queryPeixunPurchaseList(condition);
    }

    @Override
    public PeixunPurchaseDto queryById(Long id) {
        return peixunPurchaseBO.queryById(id);
    }

    @Override
    public List<PartnerPeixunSupplierDto> getSupplierList() {
        List<AppResource> resourceList = appResourceBO.queryAppResourceList("PARTNER_PEIXUN_SUPPLIER");
        if (!CollectionUtils.isEmpty(resourceList)) {
            return resourceList.stream().map(appResource -> {
                PartnerPeixunSupplierDto supplierDto = new PartnerPeixunSupplierDto();
                supplierDto.setName(appResource.getName());
                supplierDto.setValue(appResource.getValue());
                return supplierDto;
            }).collect(Collectors.toList());
        }
        return null;
    }

}
