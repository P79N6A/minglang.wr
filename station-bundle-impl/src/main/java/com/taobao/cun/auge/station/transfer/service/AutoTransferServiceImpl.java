package com.taobao.cun.auge.station.transfer.service;

import java.util.concurrent.ExecutorService;

import javax.annotation.Resource;

import com.taobao.cun.auge.common.concurrent.Executors;
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

	private ExecutorService executorService = null;
	
	public AutoTransferServiceImpl() {
		executorService = Executors.newFixedThreadPool(1, "auto-transfer-job-");
	}
	
	@Override
	public void transfer() {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				autoUltimateTransferFacade.transfer();
			}
		});
	}
}
