package com.taobao.cun.auge.fuwu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ali.dowjones.service.dto.OrderDto;
import com.ali.dowjones.service.portal.OrderPortalService;
import com.ali.dowjones.service.result.ResultModel;
import com.taobao.cun.auge.fuwu.FuwuOrderService;
import com.taobao.cun.auge.fuwu.dto.FuwuOrderDto;
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
			logger.error("orderPortalService error,",e);
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
	
}
