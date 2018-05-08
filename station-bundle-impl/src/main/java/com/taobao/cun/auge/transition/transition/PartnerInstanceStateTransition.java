package com.taobao.cun.auge.transition.transition;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.CuntaoLifecycleTransition;
import com.taobao.cun.auge.dal.domain.CuntaoLifecycleTransitionExample;

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

	@Override
	public void calcSpendTime(StateTransitionTuple tuple, CuntaoLifecycleTransition transition) {
		if(tuple.isInsert()){
			//如果招募环节最后一个主状态时间作为上一个状态的变更时间
			CuntaoLifecycleTransitionExample example = new CuntaoLifecycleTransitionExample();
			example.setOrderByClause("change_time desc");
			example.createCriteria().andIsDeletedEqualTo("n").andTaobaoUserIdEqualTo(transition.getTaobaoUserId()).andBizTypeEqualTo("PARTNER_APPLY_LIFECYCLE").andIsMainStateTransitionEqualTo("y");
			List<CuntaoLifecycleTransition> lastTranstion = cuntaoLifecycleTransitionMapper.selectByExample(example);
			if(lastTranstion != null && !lastTranstion.isEmpty()){
				Date date = new Date();
				Long spendTime = date.getTime()-lastTranstion.iterator().next().getChangeTime().getTime();
				transition.setSpendTime(spendTime/1000);
				return;
			}
			transition.setSpendTime(0l);
		}else{
			super.calcSpendTime(tuple, transition);
		}
		
	}

	
	
}
