package com.taobao.cun.auge.event.listener;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.event.domain.EventConstant;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

/**
 * 合伙人实例状态转换监听器
 * @author quanzhu.wangqz
 *
 */

@Component("partnerInstanceStateChangeListener")
@EventSub(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT)
public class PartnerInstanceStateChangeListener implements EventListener {

	@Override
	public void onMessage(Event event) {
		Map<String,Object> content = event.getContent();
		String curState = String.valueOf(content.get("partnerInstanceState"));
		String preState = String.valueOf(content.get("prePartnerInstanceState"));
		if (PartnerInstanceStateEnum.SETTLING.equals(curState)) {
			
		}else if (PartnerInstanceStateEnum.DECORATING.equals(curState)) {
			
		}else if (PartnerInstanceStateEnum.SERVICING.equals(curState)) {
			if (PartnerInstanceStateEnum.DECORATING.equals(preState)) {//开业
				// 短信推送
				//记录日志
			}else {//取消停业申请
				
			}
			
		}else if (PartnerInstanceStateEnum.CLOSING.equals(curState)) {
			if( PartnerInstanceStateEnum.SERVICING.equals(preState)) {//申请停业
				
			}
			
		}else if (PartnerInstanceStateEnum.QUITING.equals(curState)) {
			if (PartnerInstanceStateEnum.CLOSED.equals(preState)) {//申请退出
				
			}
		}
	}

}
