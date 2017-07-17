package com.taobao.cun.auge.fuwu.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ali.dowjones.service.dto.PriceDto;
import com.ali.dowjones.service.dto.ProductDto;
import com.ali.dowjones.service.dto.ProductQueryDto;
import com.ali.dowjones.service.portal.ProductService;
import com.ali.dowjones.service.result.ResultModel;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.fuwu.FuwuProductService;
import com.taobao.cun.auge.fuwu.dto.FuwuProductDto;
import com.taobao.cun.auge.fuwu.dto.FuwuProductPackageDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeSystemException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("fuwuProductService")
@HSFProvider(serviceInterface = FuwuProductService.class)
public class FuwuProductServiceImpl implements FuwuProductService{

	private static final Logger logger = LoggerFactory.getLogger(FuwuProductServiceImpl.class);

	
	@Autowired
	ProductService productService;

	@Override
	public FuwuProductPackageDto queryProductListByPolicyId(Integer policyId) {
		Assert.notNull(policyId);
		ProductQueryDto param=new ProductQueryDto();
		Set<Integer> policyIds=new HashSet<Integer>();
		policyIds.add(policyId);
		param.setPromotionPackageKeys(policyIds);
			ResultModel<ProductDto> result=productService.getProduct(param);
			if(result.isSuccessed()){
				return convertToProductPackageDto(result.getReturnValue());
			}else{
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,result.getExceptionDesc());
			}
	}

	@Override
	public FuwuProductDto queryProductByCode(String productCode) {
		Assert.notNull(productCode);
		ProductQueryDto param=new ProductQueryDto();
		Set<String> productCodes=new HashSet<String>();
		productCodes.add(productCode);
		param.setProductKeys(productCodes);
			ResultModel<ProductDto> result=productService.getProduct(param);
			if(result.isSuccessed()){
				return convertToProductDto(result.getReturnValue());
			}else{
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,result.getExceptionDesc());
			}
	}

	
	private FuwuProductDto  convertToProductDto(ProductDto dto){
		if(dto==null){
			return null;
		}
		FuwuProductDto result=new FuwuProductDto();
		result.setCode(dto.getKey());
		result.setGroupKey(dto.getGroupKey());
		result.setIcon(dto.getIcon());
		result.setIconLarge(dto.getIconLarge());
		result.setIconMiddle(dto.getIconMiddle());
		result.setIconSmall(dto.getIconSmall());
		result.setMkey(dto.getMkey());
		result.setName(dto.getName());
		PriceDto price=((ArrayList<PriceDto>)dto.getPrices()).get(0);
		result.setBasePrice(price.getBasePrice());
		result.setPrice(price.getPrice());
		return result;
	}
	
	private FuwuProductPackageDto convertToProductPackageDto(ProductDto dto){
		if(dto==null){
			return null;
		}
		FuwuProductPackageDto result=new FuwuProductPackageDto();
		result.setName(dto.getName());
		PriceDto price=((ArrayList<PriceDto>)dto.getPrices()).get(0);
		result.setBasePrice(price.getBasePrice());
		result.setPrice(price.getPrice());
		result.setKey(dto.getKey());
		result.setName(dto.getName());
		result.setIcon(dto.getIcon());
		result.setIconLarge(dto.getIconLarge());
		result.setIconMiddle(dto.getIconMiddle());
		result.setIconSmall(dto.getIconSmall());
		result.setDescription(dto.getDescription());
		List<FuwuProductDto> pr=new ArrayList<FuwuProductDto>();
		for(ProductDto pd:dto.getMandatoryProducts()){
			pr.add(convertToProductDto(pd));
		}
		result.setProducts(pr);
		return result;
	}
}
