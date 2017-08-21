package com.taobao.cun.auge.station.tpa;

import com.taobao.cun.auge.station.dto.TpaApplyInfoDto;
import com.taobao.cun.auge.station.request.CheckTpaApplyRequest;
import com.taobao.cun.auge.station.response.CheckTpaApplyResponse;
import com.taobao.cun.auge.station.response.TpaApplyResponse;
import com.taobao.cun.auge.station.response.TpaListQueryResponse;
/**
 * 淘帮手申请服务
 * @author zhenhuan.zhangzh
 *
 */
public interface TpaApplyService {

	/**
	 * 检查淘帮手申请请求
	 * @param request
	 * @return
	 */
	public CheckTpaApplyResponse checkTpaApply(CheckTpaApplyRequest request);
	
	/**
	 * 合伙人申请淘帮手请求
	 * @param request
	 * @return
	 */
	public TpaApplyResponse applyTpa(TpaApplyInfoDto tpaApplyInfo);
	
	/**
	 * 修改淘帮手
	 * @param request
	 * @return
	 */
	public TpaApplyResponse updateTpa(TpaApplyInfoDto tpaApplyInfo);
	
	/**
	 * 获取淘帮手信息
	 * @param stationId
	 * @return
	 */
	public TpaApplyInfoDto getTpaInfo(Long stationId);
	
	/**
	 * 获取申请信息
	 * @param tpaTaobaoNick
	 * @param partnerStationId
	 * @return
	 */
	public TpaApplyInfoDto getTpaApplyInfo(String tpaTaobaoNick, Long partnerStationId);
	
	/**
	 * 获取合伙人的淘帮手列表
	 * @param 合伙人stationId
	 * @return
	 */
	public TpaListQueryResponse queryTpaStations(Long parentStationId);
	
	/**
	 * 是否具有经营场所
	 * 
	 * @param stationId
	 * @return
	 */
	public Boolean hasPlace(Long stationId);
}
