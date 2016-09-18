package com.taobao.cun.auge.fuwu.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ali.dowjones.service.dto.ProductDto;
import com.ali.dowjones.service.dto.ProductQueryDto;
import com.ali.dowjones.service.portal.ProductService;
import com.ali.dowjones.service.result.ResultModel;
import com.taobao.cun.auge.fuwu.FuwuProductService;
import com.taobao.cun.auge.fuwu.dto.FuwuProductDto;
import com.taobao.cun.auge.station.bo.StationDecorateBO;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public class FuwuProductServiceImpl implements FuwuProductService{

	private static final Logger logger = LoggerFactory.getLogger(FuwuProductServiceImpl.class);

	
	@Autowired
	ProductService productService;

	@Override
	public List<FuwuProductDto> queryProductListByPolicyId(Integer policyId) {
		ProductQueryDto param=new ProductQueryDto();
		Set<Integer> policyIds=new HashSet<Integer>();
		policyIds.add(policyId);
		param.setPromotionPackageKeys(policyIds);
		try{
			FuwuProductDto dto=new FuwuProductDto();
			ResultModel<ProductDto> result=productService.getProduct(param);
			if(result.isSuccessed()){
				BeanUtils.copyProperties(result.getReturnValue(), dto);
				return null;
			}else{
				throw new AugeServiceException(result.getExceptionDesc());
			}
		}catch(Exception e){
			logger.error("queryProduct From dowjones error,",e);
			throw new AugeServiceException(e);
		}
	}

	@Override
	public FuwuProductDto queryProductByCode(String productCode) {
		ProductQueryDto param=new ProductQueryDto();
		Set<String> productCodes=new HashSet<String>();
		productCodes.add(productCode);
		param.setProductKeys(productCodes);
		try{
			FuwuProductDto dto=new FuwuProductDto();
			ResultModel<ProductDto> result=productService.getProduct(param);
			if(result.isSuccessed()){
				BeanUtils.copyProperties(result.getReturnValue(), dto);
				return dto;
			}else{
				throw new AugeServiceException(result.getExceptionDesc());
			}
		}catch(Exception e){
			logger.error("queryProduct From dowjones error,",e);
			throw new AugeServiceException(e);
		}
	}

}
