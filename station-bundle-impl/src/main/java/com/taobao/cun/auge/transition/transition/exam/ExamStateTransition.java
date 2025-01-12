package com.taobao.cun.auge.transition.transition.exam;

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
 * 申请考试状态变更处理器
 * @author zhenhuan.zhangzh
 *
 */
@Component
public class ExamStateTransition extends SubStateTransitionProcessor{

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
				String newState = (String)tuple.getValue("apply_exam_status");
				transition.setNewState(newState);
				transition.setOldState("NEW");
			}else if(tuple.isUpdate()){
				String oldState = (String)tuple.getValue("apply_exam_status");
				String newState = (String)tuple.getNewValue("apply_exam_status");
				transition.setNewState(newState);
				transition.setOldState(oldState);
			}
			transition.setParentState("STATE_APPLY_INTERVIEW");
			transition.setSubBizType("PARTNER_APPLY_EXAM");
		};
	}

	@Override
	public BiPredicate<Map<String, Serializable>, Map<String, Serializable>> getStatePredicate() {
		return (row,modifiedRow)->{
			if(modifiedRow != null){
				return modifiedRow.containsKey("apply_exam_status");
			}else{
				return row.containsKey("apply_exam_status");
			}
		};
	}

	@Override
	public Function<StateTransitionTuple, BaseTransitionInfo> getBaseTransitionInfoProvider() {
		return this.providerByTaobaoUserIdKey("taobao_user_id");
	}

}
