package com.taobao.cun.auge.station.service;

import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.station.dto.DecorationInfoDecisionDto;
import com.taobao.cun.auge.station.dto.StationDecorateAuditDto;
import com.taobao.cun.auge.station.dto.StationDecorateDto;
import com.taobao.cun.auge.station.dto.StationDecorateReflectDto;
import com.taobao.cun.auge.station.enums.StationDecorateStatusEnum;

/**
 * 服务站装修记录 服务接口
 * @author quanzhu.wangqz
 *
 */
public interface StationDecorateService {

	/**
	 * 装修记录审核
	 * @param stationDecorateAuditDto
	 * @return
	 * @
	 */
	public void audit(StationDecorateAuditDto stationDecorateAuditDto);
	
	/**
	 * 查询装修记录
	 * @param taobaoUserId
	 * @return
	 * @
	 */
	public StationDecorateDto getInfoByTaobaoUserId(Long taobaoUserId);
	
	/**
	 * 查询装修记录
	 * @param stationId
	 * @return
	 * @
	 */
	public StationDecorateDto getInfoByStationId(Long stationId);
	
	/**
	 * 反馈装修记录
	 * @param stationDecorateDto
	 * @
	 */
	public void reflectStationDecorate(StationDecorateReflectDto stationDecorateReflectDto); 
	
	/**
	 * 返回装修记录
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @
	 */
	public List<StationDecorateDto> getStationDecorateListForSchedule(int pageNum,int pageSize); 
	
	/**
	 * 返回装修记录总数
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @
	 */
	public int getStationDecorateListCountForSchedule();
	
	/**
	 * 更新装修记录
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @
	 */
	public void updateStationDecorate(StationDecorateDto stationDecorateDto);
	
	
	/**
	 * 定时同步订单状态
	 * @param stationDecorateDto
	 * @
	 */
	public void syncStationDecorateFromTaobao(StationDecorateDto stationDecorateDto);
	
	/**
	 * 根据stationId 获得装修状态
	 * @param stationIds
	 * @return
	 * @
	 */
	public Map<Long,StationDecorateStatusEnum> getStatusByStationId(List<Long> stationIds);
	
	
	/**
	 * 只有在装修中，装修审核待反馈的时候，才返回url,否则返回null
	 * @param taobaoUserId
	 * @return
	 * @
	 */
	public String getReflectUrl(Long taobaoUserId);
	
	/**
	 * 合伙人确认进入装修中
	 */
	public void confirmAcessDecorating(Long id);
	
	/**
	 * 判断装修装状态是否允许合伙人退出
	 */
	public void judgeDecorateQuit(Long stationId);
	
	/**
	 * 开通1688采购商城权限
	 */
	public void openAccessCbuMarket(Long taobaoUserId);
	
	/**
     * 查询装修记录
     * @param Id
     * @return
     * @
     */
    public StationDecorateDto getInfoById(Long Id);
    
    /**
     * 查询装修图纸
     */
    public DecorationInfoDecisionDto getDecorationDecisionById(Long id);
    
    /**
     * 装修图纸信息审核
     */
    public void auditDecorationDecision(DecorationInfoDecisionDto decorationInfoDecisionDto);
    
    /**
     * 更新装修图纸信息
     */
    public void updateDecorationDecision(DecorationInfoDecisionDto decorationInfoDecisionDto);
    
    /**
     *提交装修图纸信息
     */
    public void submitDecorationDecision(DecorationInfoDecisionDto decorationInfoDecisionDto);
    
    /**
     * 查询装修图纸信息
     * @param stationId
     * @return
     * @
     */
    public DecorationInfoDecisionDto getDecorationDecisionByStationId(Long stationId);
}
