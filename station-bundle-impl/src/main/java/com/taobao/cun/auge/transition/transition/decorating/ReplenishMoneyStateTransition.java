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
 * 铺货金状态
 * @author zhenhuan.zhangzh
 *
 */
@Component
public class ReplenishMoneyStateTransition extends SubStateTransitionProcessor{
	
	public Boolean isMatched(String action,String tableName) {
		return ("INSERT".equals(action)||"UPDATE".equals(action)) && "partner_lifecycle_items".equals(tableName);
	}

	@Override
	public String bizType() {
		return "PARTNER_INSTNCE_LIFECYCLE";
	}


	@Override
	public BiPredicate<Map<String, Serializable>,Map<String, Serializable>> getStatePredicate() {
		return (row,modifiedRow) -> {
			if(modifiedRow != null){
				return modifiedRow.containsKey("replenish_money");
			}else{
				return "DECORATING".equals(row.get("business_type"));
			}
		};
	}
	
	
	@Override
	public BiConsumer<StateTransitionTuple, CuntaoLifecycleTransition> getStateComsumer() {
		return (tuple,transition) ->{
			if(tuple.isInsert()){
				String state = (String)tuple.getValue("replenish_money");
				transition.setNewState(state);
				transition.setOldState("NEW");
				transition.setParentState("DECORATING");
			}else if(tuple.isUpdate()){
				String newSubState = (String)tuple.getNewValue("replenish_money");
				String oldSubState = (String)tuple.getValue("replenish_money");
				transition.setNewState(newSubState);
				transition.setOldState(oldSubState);
				transition.setParentState("DECORATING");
			}
			transition.setSubBizType("replenishMoney");
		};
	}

	@Override
	public Function<StateTransitionTuple, BaseTransitionInfo> getBaseTransitionInfoProvider() {
		return this.providerByPartnerInstanceIdKey("partner_instance_id");
	}
}
