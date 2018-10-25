package com.taobao.cun.auge.station.transfer.service;

import javax.annotation.Resource;

import com.taobao.cun.auge.station.transfer.ultimate.AutoUltimateTransferFacade;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * 自动交接服务
 * 
 * @author chengyu.zhoucy
 *
 */
@HSFProvider(serviceInterface = AutoTransferService.class)
public class AutoTransferServiceImpl implements AutoTransferService {
	@Resource
	private AutoUltimateTransferFacade autoUltimateTransferFacade;

	@Override
	public void transfer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				autoUltimateTransferFacade.transfer();
			}}).start();
	}
}
