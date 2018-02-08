package com.taobao.cun.auge.fuwu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ali.dowjones.service.constants.OrderItemSiteType;
import com.ali.dowjones.service.dto.CustomerDto;
import com.ali.dowjones.service.dto.OrderDto;
import com.ali.dowjones.service.dto.OrderItemDto;
import com.ali.dowjones.service.dto.ProductDto;
import com.ali.dowjones.service.dto.ShoppingCartDto;
import com.ali.dowjones.service.portal.OrderPortalService;
import com.ali.dowjones.service.portal.ShoppingCartPortalService;
import com.ali.dowjones.service.result.ResultModel;
import com.ali.martini.biz.order.interfaces.orderitem.facade.OrderItemFacade;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.auge.common.utils.PayParam;
import com.taobao.cun.auge.common.utils.PayUtil;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.fuwu.FuwuOrderService;
import com.taobao.cun.auge.fuwu.dto.FuwuOrderDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
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
	AppResourceService appResourceService;
	@Autowired
	OrderItemFacade orderItemFacade;
	
	private static String customerIdentity="cun_tao";
	
	@Override
	public  List<FuwuOrderDto>  queryOrdersByUserIdAndCode(Long userId,
			List<String> productCode, Set<String> statuses) {
		Assert.notNull(userId);
		Assert.notEmpty(productCode, "productCode is null");
			ResultModel<ArrayList<OrderDto>> result = orderPortalService
					.getOrdersFromProductCodes(userId, new ArrayList<String>(
							productCode), statuses, customerIdentity);
			if(result.isSuccessed()){
				return convertOrderDtoToFuwuDto(result.getReturnValue(),userId);
			}else{
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,result.getExceptionDesc());
			}
	}
	
	
	private List<FuwuOrderDto> convertOrderDtoToFuwuDto(ArrayList<OrderDto> dtos,Long userId){
		List<FuwuOrderDto> results=new ArrayList<FuwuOrderDto>();
		if(dtos==null||dtos.size()==0){
			return results;
		}
		for(OrderDto dto:dtos){
			OrderItemDto itemDto=dto.getOrderItems().get(0);
			FuwuOrderDto fuwu=new FuwuOrderDto();
			fuwu.setId(itemDto.getId());
			fuwu.setBasePrice(itemDto.getBasePrice());
			fuwu.setExecutePrice(itemDto.getExecutePrice());
			fuwu.setOrderNo(itemDto.getOrderNo());
			fuwu.setOrderItemNo(itemDto.getItemNum());
			fuwu.setOrderTitle(itemDto.getProductName());
			fuwu.setPaymentAmount(itemDto.getPaymentAmount());
			fuwu.setComments(dto.getComments());
			fuwu.setStatus(dto.getStatus());
			fuwu.setUserId(userId);
			fuwu.setPayUrl(getPayUrl(String.valueOf(userId), itemDto.getOrderNo()));
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
			ResultModel<ArrayList<ShoppingCartDto>> result = shoppingCartPortalService
					.addCartItemsQuick(customer, product);
			if(result.isSuccessed()){
				List<FuwuOrderDto> returnResult=new ArrayList<FuwuOrderDto>();
				for(ShoppingCartDto cartDto:result.getReturnValue()){
					FuwuOrderDto fdto=convertToFuwuOrderDto(cartDto);
					fdto.setUserId(userId);
					fdto.setPayUrl(getPayUrl(String.valueOf(userId), fdto.getOrderNo()));
					returnResult.add(fdto);
				}
				return returnResult;
			}else{
				logger.warn("addCartItemsQuick fail, userId:{}, errorCode:{}", userId, result.getErrorCode());
				throw new RuntimeException(result.getErrorDesc());
			}
	}
	
	
	private FuwuOrderDto convertToFuwuOrderDto(ShoppingCartDto cart){
		FuwuOrderDto dto=new FuwuOrderDto();
		dto.setExecutePrice(cart.getExecutePrice());
        dto.setOrderNo(cart.getOrderNo());
        dto.setOrderItemNo(cart.getItemNo());
        dto.setOrderName(cart.getItemName());
        dto.setProductCode(cart.getProductCode());
        return dto;
	}
	
	private String getPayUrl(String aliId, String itemNum) {
		PayParam param = new PayParam();
		param.setAliId(aliId);
		param.setItemNum(itemNum);
		param.setSite(appResourceService.queryAppResourceValue("NASDAQ_PAY",
				"SYSTEM"));
		param.setKey(appResourceService.queryAppResourceValue("NASDAQ_PAY",
				"KEY"));
		param.setGetway(appResourceService.queryAppResourceValue("NASDAQ_PAY",
				"URL"));
		param.setSystem(appResourceService.queryAppResourceValue("NASDAQ_PAY",
				"SYSTEM"));
		param.setReturnUrl(appResourceService.queryAppResourceValue(
				"NASDAQ_PAY", "RETURN_URL"));
		return PayUtil.createUrl(param);
	}
	
	


	@Override
	public void closeOrder(String orderNum) {
		Assert.notNull(orderNum);
			orderItemFacade.serviceWasFinished(orderNum);
	}
	
}
