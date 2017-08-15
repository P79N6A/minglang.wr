package com.taobao.cun.auge.station.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.ceres.commonservice.po.PoQueryService;
import com.alibaba.ceres.commonservice.po.model.PoResultDto;
import com.alibaba.fastjson.JSON;
import com.taobao.cun.appResource.dto.AppResourceDto;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PeixunPurchaseBO;
import com.taobao.cun.auge.station.condition.PeixunPuchaseQueryCondition;
import com.taobao.cun.auge.station.dto.PartnerPeixunSupplierDto;
import com.taobao.cun.auge.station.dto.PeixunPurchaseDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
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
	
	@Autowired
	PoQueryService poQueryService;

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

	@Override
	public List<PartnerPeixunSupplierDto> getSupplierListByProvince(
			Long provinceOrgId) {
		Assert.notNull(provinceOrgId);
		List<PartnerPeixunSupplierDto> returnList=new ArrayList<PartnerPeixunSupplierDto>();
		Map<String,String> provinceMap=configuredProperties.getPurchaseProvinceMap();
		for(String key:provinceMap.keySet()){
			if(key.equals(String.valueOf(provinceOrgId))){
				//返回该省配置的供应商列表
				List<PartnerPeixunSupplierDto> supplierDtos=getSupplierList();
				l1:for(String supplierIdStr:provinceMap.get(key).split(",")){
					for(PartnerPeixunSupplierDto dto:supplierDtos){
						if(supplierIdStr.equals(dto.getValue())){
							returnList.add(dto);
							continue l1;
						}
					}
				}
				break;
			}
		}
		return returnList;
	}

	@Override
	public Boolean judgePurchaseProcessEnd(Long id) {
		Assert.notNull(id);
		PeixunPurchaseDto dto=queryById(id);
		String poNo=dto.getPrNo();
		try{
			PoResultDto poDto=poQueryService.queryPoInfo(poNo);
			if(poDto!=null&&"已生效".equals(poDto.getStatus())){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			throw new AugeBusinessException(AugeErrorCodes.PURCHASE_BUSINESS_CHECK_ERROR_CODE,e.getMessage());
		}
	}
	
}
