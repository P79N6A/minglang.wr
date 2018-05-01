package com.taobao.cun.auge.transition.transition.settling;

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
 * 保证金冻结状态变更
 * @author zhenhuan.zhangzh
 *
 */
@Component
public class BondFrozenStateTransition extends SubStateTransitionProcessor{
	
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
				return "SETTLING".equals(row.get("business_type")) && modifiedRow.containsKey("bond");
			}else{
				return "SETTLING".equals(row.get("business_type")) && row.containsKey("bond");
			}
		};
	}
	
	@Override
	public BiConsumer<StateTransitionTuple, CuntaoLifecycleTransition> getStateComsumer() {
		return (tuple,transition) ->{
			if(tuple.isInsert()){
				String state = (String)tuple.getValue("bond");
				transition.setNewState(state);
				transition.setOldState("NEW");
				transition.setParentState("SETTLING");
			}else if(tuple.isUpdate()){
				String newSubState = (String)tuple.getNewValue("bond");
				String oldSubState = (String)tuple.getValue("bond");
				transition.setNewState(newSubState);
				transition.setOldState(oldSubState);
				transition.setParentState("SETTLING");
			}
			transition.setSubBizType("BOND_FROZEN");
		};
	}

	@Override
	public Function<StateTransitionTuple, BaseTransitionInfo> getBaseTransitionInfoProvider() {
		return this.providerByPartnerInstanceIdKey("partner_instance_id");
	}
}
