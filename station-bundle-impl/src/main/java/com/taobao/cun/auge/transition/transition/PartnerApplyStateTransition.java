package com.taobao.cun.auge.transition.transition;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.CuntaoLifecycleTransition;

/**
 * 合伙人招募状态变更
 * @author zhenhuan.zhangzh
 *
 */
@Component
public class PartnerApplyStateTransition extends MainStateTransitionProcessor{

	@Override
	public Boolean isMatched(String action, String tableName) {
		return ("INSERT".equals(action)||"UPDATE".equals(action)) && "partner_apply".equals(tableName);
	}

	@Override
	public String bizType() {
		return "PARTNER_APPLY_LIFECYCLE";
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
	public BiPredicate<Map<String, Serializable>, Map<String, Serializable>> getStatePredicate() {
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
		return this.providerByPartnerApplyIdKey("id");
	}

}
