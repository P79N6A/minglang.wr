package com.taobao.cun.auge.transition.transition.quiting;

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
 * 县小二申请退出审批流状态变更
 * @author zhenhuan.zhangzh
 *
 */
@Component
public class LeaderQuitingApplyStateTransition extends SubStateTransitionProcessor{

	@Override
	public Boolean isMatched(String action, String tableName) {
		return ("INSERT".equals(action)||"UPDATE".equals(action)) && "partner_lifecycle_items".equals(tableName);
	}

	@Override
	public String bizType() {
		return "PARTNER_INSTNCE_LIFECYCLE";
	}

	@Override
	public BiConsumer<StateTransitionTuple, CuntaoLifecycleTransition> getStateComsumer() {
		return (tuple,transition) ->{
			if(tuple.isInsert()){
				String newState = (String)tuple.getValue("role_approve");
				transition.setNewState(newState);
				transition.setOldState("NEW");
			}else if(tuple.isUpdate()){
				String oldState = (String)tuple.getValue("role_approve");
				String newState = (String)tuple.getNewValue("role_approve");
				transition.setNewState(newState);
				transition.setOldState(oldState);
			}
			transition.setParentState("QUITING");
			transition.setSubBizType("QUIT_AUDIT");
		};
	}

	@Override
	public BiPredicate<Map<String, Serializable>, Map<String, Serializable>> getStatePredicate() {
		return (row,modifiedRow) -> {
			if(modifiedRow != null){
				return  "QUITING".equals(row.get("business_type")) && modifiedRow.containsKey("role_approve");
			}else{
				return "QUITING".equals(row.get("business_type")) &&  row.containsKey("role_approve");
			}
		};
	}

	@Override
	public Function<StateTransitionTuple, BaseTransitionInfo> getBaseTransitionInfoProvider() {
		return this.providerByPartnerInstanceIdKey("partner_instance_id");
	}

}
