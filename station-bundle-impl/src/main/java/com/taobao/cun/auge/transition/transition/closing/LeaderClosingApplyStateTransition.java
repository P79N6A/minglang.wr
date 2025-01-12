package com.taobao.cun.auge.transition.transition.closing;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.CuntaoLifecycleTransition;
import com.taobao.cun.auge.transition.transition.BaseTransitionInfo;
import com.taobao.cun.auge.transition.transition.StateTransitionTuple;
import com.taobao.cun.auge.transition.transition.SubStateTransitionProcessor;

/**
 * 县小二强制停业状态变更
 * @author zhenhuan.zhangzh
 *
 */
@Component
public class LeaderClosingApplyStateTransition extends SubStateTransitionProcessor{

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
			transition.setParentState("CLOSING");
			transition.setSubBizType("PARTNER_CLOSE_AUDIT");
		};
	}

	@Override
	public BiPredicate<Map<String, Serializable>, Map<String, Serializable>> getStatePredicate() {
		return (row,modifiedRow) -> {
			if(modifiedRow != null){
				return "CLOSING".equals(row.get("business_type")) && modifiedRow.containsKey("role_approve");
			}else{
				return "CLOSING".equals(row.get("business_type")) && StringUtils.isNotEmpty((String)row.get("role_approve"));
			}
		};
	}

	@Override
	public Function<StateTransitionTuple, BaseTransitionInfo> getBaseTransitionInfoProvider() {
		return this.providerByPartnerInstanceIdKey("partner_instance_id");
	}

}
