package com.taobao.cun.auge.station.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.appResource.dto.AppResourceDto;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.station.bo.PeixunPurchaseBO;
import com.taobao.cun.auge.station.condition.PeixunPuchaseQueryCondition;
import com.taobao.cun.auge.station.dto.PartnerPeixunSupplierDto;
import com.taobao.cun.auge.station.dto.PeixunPurchaseDto;
import com.taobao.cun.auge.station.service.PeixunPurchaseService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("peixunPurchaseService")
@HSFProvider(serviceInterface = PeixunPurchaseService.class)
public class PeixunPurchaseServiceImpl implements PeixunPurchaseService {

    @Autowired
    PeixunPurchaseBO peixunPurchaseBO;

    @Autowired
    DiamondConfiguredProperties configuredProperties;

	@Autowired
	AppResourceService appResourceService;

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
        return configuredProperties.getSupplierMap().entrySet().stream().map(entry -> {
            PartnerPeixunSupplierDto supplierDto = new PartnerPeixunSupplierDto();
            supplierDto.setName(entry.getValue());
            supplierDto.setValue(entry.getKey());
            return supplierDto;
        }).collect(Collectors.toList());
    }

    @Override
    public String getPurchaseJson() {
        List<AppResourceDto> resourceList = appResourceService.queryAppResourceList("PEIXUN_PURCHASE");
        if (!CollectionUtils.isEmpty(resourceList)) {
            List<PartnerPeixunSupplierDto> dtoList = resourceList.stream().map(appResource -> {
                PartnerPeixunSupplierDto supplierDto = new PartnerPeixunSupplierDto();
                supplierDto.setName(appResource.getName());
                supplierDto.setValue(appResource.getValue());
                return supplierDto;
            }).collect(Collectors.toList());
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            for (PartnerPeixunSupplierDto dto : dtoList) {
                map.put(dto.getName(), dto.getValue());
            }
            return JSON.toJSONString(map);
        }
        return null;
    }

}
