package com.taobao.cun.auge.goods;

import com.taobao.cun.auge.common.result.Result;

/**
 * 
 * @author zhenhuan.zhangzh
 *
 */
public interface CuntaoGoodsService {

	/**
	 * 确认拍样协议
	 * @param taobaoUserId
	 * @return
	 */
	public Result<Boolean> confirmSampleGoodsProtocol(Long taobaoUserId);
	
	/**
	 * 是否确认拍样协议
	 * @param taobaoUserId
	 * @return
	 */
	public Result<Boolean> isConfirmSampleGoodsProtocol(Long taobaoUserId);
	
	/**
	 * 确认补货协议
	 * @param taobaoUserId
	 * @return
	 */
	public Result<Boolean> confirmSupplyGoodsProtocol(Long taobaoUserId);
	
	/**
	 * 是否确认补货协议
	 * @param taobaoUserId
	 * @return
	 */
	public Result<Boolean> isConfirmSupplyGoodsProtocol(Long taobaoUserId);
	
	/**
	 * 确认门店补货协议
	 * @param taobaoUserId
	 * @return
	 */
	public Result<Boolean> confirmStoreSupplyGoodsProtocol(Long taobaoUserId);
	
	/**
	 * 是否确认门店补货协议
	 * @param taobaoUserId
	 * @return
	 */
	public Result<Boolean> isConfirmStoreSupplyGoodsProtocol(Long taobaoUserId);
	
	/**
	 * 村点开业包协议
	 * @param taobaoUserId
	 * @return
	 */
	public Result<Boolean> confirmStationOpeningProtocol(Long taobaoUserId);
	
	/**
	 * 是否确认开业包协议
	 * @param taobaoUserId
	 * @return
	 */
	public Result<Boolean> isConfirmStationOpeningProtocol(Long taobaoUserId);
}
