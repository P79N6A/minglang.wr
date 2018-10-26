package com.taobao.cun.auge.station.transfer.ultimate;

import com.taobao.cun.auge.station.transfer.ultimate.handle.HandlerGroup;
import org.springframework.stereotype.Component;

/**
 * 提前发起交接
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class AdvanceUltimateTransferBo extends BaseUltimateTransferBo{
	@Override
	protected String getHandlerGroup() {
		return HandlerGroup.ADVANCE;
	}
}
