package com.taobao.cun.auge.fuwu;

import java.util.List;
import java.util.Set;

import com.taobao.cun.auge.fuwu.dto.FuwuOrderDto;

/**
 * 服务市场订单服务
 * @author yi.shaoy
 *
 */
public interface FuwuOrderService {

	//根据userid和产品code和状态查询订单
	public List<FuwuOrderDto> queryOrdersByUserIdAndCode(Long userId,List<String> productCode,Set<String> statuses);
		
	//根据套餐id创建订单, 客户id
	public List<FuwuOrderDto> createOrderByPolicyId(Long userId,String mkey,String userIp);
	
	//服务完成，关闭crm订单
	public void closeOrder(String orderNum);
}
