package com.taobao.cun.auge.station.sync;

import com.taobao.cun.auge.station.exception.AugeServiceException;



public interface SyncStationApplyBO {
	
	
	/**
	 * @param stationApplyId
	 * @return
	 */
	public Long syncTemp(Long partnerInstanceId) throws AugeServiceException;
	
	
	/**
	 * 淘帮手审批待新增
	 * @param stationApplyId
	 */
	public void syncTPATemp(Long partnerInstanceId) throws AugeServiceException;
	
	/**
	 * 淘帮手 入驻审核 失败
	 * @param stationApplyId
	 */
	public void syncTPATempAduitNotPass(Long partnerInstanceId) throws AugeServiceException;
	
	/**
	 * 合伙人降级
	 * @param stationApplyId
	 */
	public void syncTPDegrade(Long partnerInstanceId) throws AugeServiceException;
	/**
	 * 合伙人，淘帮手新增提交
	 * @param stationApplyId 
	 */
	public void syncSumitted(Long partnerInstanceId) throws AugeServiceException;

	/**
	 * 淘帮手，合伙人 暂存后提交
	 * @param stationApplyId
	 */
	public void synctempSubmit(Long partnerInstanceId)throws AugeServiceException ;
	/**
	 * 确认协议
	 * @param stationApplyId
	 */
	public void syncConfirm(Long partnerInstanceId)throws AugeServiceException;
	/**
	 * 冻结保证金
	 * @param stationApplyId
	 */
	public void syncfreeze(Long partnerInstanceId)throws AugeServiceException;
	
	/**
	 * 合伙人，淘帮手  除 暂存外修改
	 */
	public void syncModify(Long partnerInstanceId)throws AugeServiceException;
	
	/**
	 * 合伙人，淘帮手 小二强制停业
	 * @param stationApplyId
	 */
	public void syncForcedClosure(Long partnerInstanceId)throws AugeServiceException;
	
	/**
	 * 合伙人 淘帮手 主动申请停业
	 * @param stationApplyId
	 */
	public  void syncClosing(Long partnerInstanceId)throws AugeServiceException;
	
	/**
	 * 合伙人，淘帮手 小二强制停业 审核
	 * @param stationApplyId
	 */
	public void syncForcedClosureAudit(Long partnerInstanceId)throws AugeServiceException;
	/**
	 * 合伙人 淘帮手 主动申请停业  小二确认，取消  走到服务中
	 * @param stationApplyId
	 */
	public void syncCloseConfirm(Long partnerInstanceId)throws AugeServiceException;
	
	/**
	 * 小二申请退出 
	 * @param stationApplyId
	 */
	public void syncQuiting(Long partnerInstanceId) throws AugeServiceException;
	
	/**
	 * 小二申请退出 审核  审核失败 走到已停业  审核通过 走到已退出
	 * @param stationApplyId
	 */
	public void syncQuitingAudit(Long partnerInstanceId) throws AugeServiceException;
	
	/**
	 * 合伙人退出已解冻 
	 * @param stationApplyId
	 */
	public void syncHasthaw(Long partnerInstanceId) throws AugeServiceException;
	
	/**
	 * 合伙人装修中定时任务同步
	 * @param stationApplyId
	 */
	public void syncDecorating(Long partnerInstanceId) throws AugeServiceException;
	
	/**
	 * 合伙人 开业同步
	 * @param stationApplyId
	 */
	public void syncServicing(Long partnerInstanceId) throws AugeServiceException;
	
	/**
	 * 合伙人 淘帮手删除功能
	 * @param stationApplyId
	 */
	public void syncDelete(Long partnerInstanceId) throws AugeServiceException;
}
