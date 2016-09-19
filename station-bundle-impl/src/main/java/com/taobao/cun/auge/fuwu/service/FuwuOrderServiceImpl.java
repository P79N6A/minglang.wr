package com.taobao.cun.auge.fuwu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ali.dowjones.service.constants.OrderItemSiteType;
import com.ali.dowjones.service.constants.ProductType;
import com.ali.dowjones.service.dto.CustomerDto;
import com.ali.dowjones.service.dto.OrderDto;
import com.ali.dowjones.service.dto.ProductDto;
import com.ali.dowjones.service.dto.ShoppingCartDto;
import com.ali.dowjones.service.portal.OrderPortalService;
import com.ali.dowjones.service.portal.ShoppingCartPortalService;
import com.ali.dowjones.service.result.ResultModel;
import com.taobao.cun.auge.fuwu.FuwuOrderService;
import com.taobao.cun.auge.fuwu.dto.FuwuOrderDto;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
/**
 * 
 * @author yi.shaoy
 *
 */
@Service("fuwuOrderService")
@HSFProvider(serviceInterface = FuwuOrderService.class)
public class FuwuOrderServiceImpl implements FuwuOrderService{

	private static final Logger logger = LoggerFactory.getLogger(FuwuOrderServiceImpl.class);

	@Autowired
	OrderPortalService orderPortalService;
	@Autowired
	ShoppingCartPortalService shoppingCartPortalService;
	@Autowired
	AppResourceBO appResourceBO;
	
	private static String customerIdentity="cuntao";
	
	@Override
	public  List<FuwuOrderDto>  queryOrdersByUserIdAndCode(Long userId,
			List<String> productCode, Set<String> statuses) {
		Assert.notNull(userId);
		Assert.notEmpty(productCode, "productCode is null");
		try {
			ResultModel<ArrayList<OrderDto>> result = orderPortalService
					.getOrdersFromProductCodes(userId, new ArrayList<String>(
							productCode), statuses, customerIdentity);
			if(result.isSuccessed()){
				return convertOrderDtoToFuwuDto(result.getReturnValue());
			}else{
				throw new AugeServiceException(result.getExceptionDesc());
			}
		} catch (Exception e) {
			logger.error("orderPortalService error,userId:"+String.valueOf(userId)+",productCode:"+productCode,e);
			throw new AugeServiceException("query dowjones error ,"+"userId:"+String.valueOf(userId),e);
		}
	}
	
	
	private List<FuwuOrderDto> convertOrderDtoToFuwuDto(ArrayList<OrderDto> dtos){
		List<FuwuOrderDto> results=new ArrayList<FuwuOrderDto>();
		if(dtos==null||dtos.size()==0){
			return results;
		}
		for(OrderDto dto:dtos){
			FuwuOrderDto fuwu=new FuwuOrderDto();
			fuwu.setId(dto.getId());
			fuwu.setBasePrice(dto.getBasePrice());
			fuwu.setExecutePrice(dto.getExecutePrice());
			fuwu.setOrderNo(dto.getOrderNo());
			fuwu.setOrderTitle(dto.getOrderTitle());
			fuwu.setPaymentAmount(dto.getPaymentAmount());
			fuwu.setUserId(dto.getAliId());
			fuwu.setComments(dto.getComments());
			results.add(fuwu);
		}
		return results;
		
	}


	@Override
	public List<FuwuOrderDto> createOrderByPolicyId(Long userId,
			String mkey,String userIp) {
		Assert.notNull(userId);
		Assert.notNull(mkey);
		Assert.notNull(userIp);
		CustomerDto customer = new CustomerDto();
		customer.setSite(OrderItemSiteType.B2BCN.getValue());
		customer.setUserIp(userIp);
		customer.setAliId(userId);
		customer.setCustomerIdentity(customerIdentity);
		ProductDto product = new ProductDto();
		product.setMkey(mkey);
		try {
			ResultModel<ArrayList<ShoppingCartDto>> result = shoppingCartPortalService
					.addCartItemsQuick(customer, product);
			if(result.isSuccessed()){
				List<FuwuOrderDto> returnResult=new ArrayList<FuwuOrderDto>();
				for(ShoppingCartDto cartDto:result.getReturnValue()){
					returnResult.add(convertToFuwuOrderDto(cartDto));
				}
				return returnResult;
			}else{
				throw new AugeServiceException(result.getExceptionDesc());
			}
		} catch (Exception e) {
			logger.error("createOrder error,userId:"+String.valueOf(userId)+",mkey:"+mkey,e);
			throw new AugeServiceException("createOrder error,userId ,"+"userId:"+String.valueOf(userId),e);
		}
	}
	
	
	private FuwuOrderDto convertToFuwuOrderDto(ShoppingCartDto cart){
		FuwuOrderDto dto=new FuwuOrderDto();
//		dto.setUserId(cart.get);
		dto.setExecutePrice(cart.getExecutePrice());
        dto.setOrderNo(cart.getOrderNo());
        return dto;
	}
	
}
