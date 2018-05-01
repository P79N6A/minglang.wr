package com.taobao.cun.auge.transition.transition.decorating;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.CuntaoLifecycleTransition;
import com.taobao.cun.auge.transition.transition.BaseTransitionInfo;
import com.taobao.cun.auge.transition.transition.StateTransitionTuple;
import com.taobao.cun.auge.transition.transition.SubStateTransitionProcessor;

/**
 * 装修子状态变更
 * @author zhenhuan.zhangzh
 *
 */
@Component
public class DecorateStateTransition extends SubStateTransitionProcessor{

	@Override
	public Boolean isMatched(String action, String tableName) {
		return "station_decorate".equals(tableName);
	}

	@Override
	public String bizType() {
		return "PARTNER_INSTNCE_LIFECYCLE";
	}

	@Override
	public BiPredicate<Map<String, Serializable>, Map<String, Serializable>> getStatePredicate() {
		return (row, modifiedRow) -> {
			if (modifiedRow != null) {
				return modifiedRow.containsKey("status");
			} else {
				return row.containsKey("status");
			}
		};
	}

	@Override
	public BiConsumer<StateTransitionTuple, CuntaoLifecycleTransition> getStateComsumer() {
		return (tuple,transition) ->{
			if(tuple.isInsert()){
				String newState = (String)tuple.getValue("status");
				transition.setNewState(newState);
				transition.setOldState("NEW");
			}else if(tuple.isUpdate()){
				String oldState = (String)tuple.getValue("status");
				String newState = (String)tuple.getNewValue("status");
				transition.setNewState(newState);
				transition.setOldState(oldState);
			}
			transition.setParentState("DECORATING");
			transition.setSubBizType("DECORATE");
		};
	}

	@Override
	public Function<StateTransitionTuple, BaseTransitionInfo> getBaseTransitionInfoProvider() {
		return this.providerByStationIdKey("station_id");
	}

}
