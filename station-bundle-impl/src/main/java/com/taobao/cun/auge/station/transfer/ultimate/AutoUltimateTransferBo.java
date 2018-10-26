package com.taobao.cun.auge.station.transfer.ultimate;

import com.taobao.cun.auge.station.transfer.ultimate.handle.HandlerGroup;
import org.springframework.stereotype.Component;

/**
 * N + 75这天会自动转交
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class AutoUltimateTransferBo extends BaseUltimateTransferBo{

	@Override
	protected String getHandlerGroup() {
		return HandlerGroup.AUTO;
	}
}
