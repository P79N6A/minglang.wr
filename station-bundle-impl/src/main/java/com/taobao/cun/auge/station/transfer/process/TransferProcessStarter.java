package com.taobao.cun.auge.station.transfer.process;

import com.taobao.cun.auge.station.transfer.TransferException;
import com.taobao.cun.auge.station.transfer.dto.TransferJob;

/**
 * 交接流程
 * @author chengyu.zhoucy
 *
 */
public interface TransferProcessStarter {
	/**
	 * 发起交接流程
	 * @param transferJob
	 */
	void startTransferProcess(TransferJob transferJob) throws TransferException;
}
