package com.taobao.cun.auge.station.transfer.state;

/**
 * 转交状态管理，管理县点、村点状态和状态变化
 * 
 * @author chengyu.zhoucy
 *
 */
public interface TransferStateMgrBo<S> {
	/**
	 * 获取交接阶段
	 * @param id
	 * @return
	 */
	S getTransferPhase(Long id);
}
