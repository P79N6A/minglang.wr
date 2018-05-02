package com.taobao.cun.auge.transition.transition;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.CuntaoLifecycleTransition;

/**
 * 合伙人生命周期主状态变更记录处理器
 * @author zhenhuan.zhangzh
 *
 */
@Component
public class PartnerInstanceStateTransition extends MainStateTransitionProcessor{

	@Override
	public Boolean isMatched(String action, String tableName) {
		return ("INSERT".equals(action)||"UPDATE".equals(action)) && "partner_station_rel".equals(tableName);
	}

	@Override
	public String bizType() {
		return "PARTNER_INSTNCE_LIFECYCLE";
	}

	@Override
	public BiConsumer<StateTransitionTuple, CuntaoLifecycleTransition> getStateComsumer() {
		return (tuple,transition) ->{
			if(tuple.isInsert()){
				String newState = (String)tuple.getValue("state");
				transition.setNewState(newState);
				transition.setOldState("NEW");
			}else if(tuple.isUpdate()){
				String oldState = (String)tuple.getValue("state");
				String newState = (String)tuple.getNewValue("state");
				transition.setNewState(newState);
				transition.setOldState(oldState);
			}
		};
	}

	@Override
	public BiPredicate<Map<String, Serializable>,Map<String, Serializable>> getStatePredicate() {
		return (row,modifiedRow)->{
			if(modifiedRow != null){
				return modifiedRow.containsKey("state");
			}else{
				return row.containsKey("state");
			}
		};
	}

	@Override
	public Function<StateTransitionTuple, BaseTransitionInfo> getBaseTransitionInfoProvider() {
		return this.providerByPartnerInstanceIdKey("id");
	}

	
	
}
