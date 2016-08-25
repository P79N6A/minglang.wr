package com.taobao.cun.auge.station.adapter;

public interface CuntaoAssetAdapter {
	/**
	 * 村点资产是否归还，如果已经归还，则返回true.
	 * 
	 * @param stationApplyId
	 * @return
	 */
	public boolean isBackAssets(Long stationApplyId);
}