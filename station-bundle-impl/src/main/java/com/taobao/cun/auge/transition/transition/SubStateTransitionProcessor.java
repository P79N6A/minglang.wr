package com.taobao.cun.auge.transition.transition;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.middleware.jingwei.client.custom.EventMessage;
import com.alibaba.middleware.jingwei.client.custom.InsertEvent;
import com.alibaba.middleware.jingwei.client.custom.UpdateEvent;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.CuntaoLifecycleTransition;
import com.taobao.cun.auge.dal.domain.CuntaoLifecycleTransitionExample;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.mapper.CuntaoLifecycleTransitionMapper;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.recruit.partner.dto.PartnerApplyDto;
import com.taobao.cun.recruit.partner.service.PartnerApplyService;

/**
 * 子节点状态变更转换器
 * @author zhenhuan.zhangzh
 *
 */
public abstract class SubStateTransitionProcessor implements StateTransitionEventProcessor,StateTransition {

	@Autowired
	protected PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	protected CuntaoLifecycleTransitionMapper cuntaoLifecycleTransitionMapper;
	
	@Autowired
	protected PartnerApplyService partnerApplyService;
	
	/**
	 * 设置基本信息
	 * @param partnerInstanceDto
	 * @param transition
	 */
	protected void setTransitionInfo(BaseTransitionInfo baseTransitionInfo,CuntaoLifecycleTransition transition){
		transition.setBizPrimaryKey(baseTransitionInfo.getBizPrimaryKey());
		transition.setTaobaoUserId(baseTransitionInfo.getTaobaoUserId());
		transition.setChangeTime(new Date());
		transition.setStationId(baseTransitionInfo.getStationId());
		transition.setUserType(baseTransitionInfo.getUserType());
		transition.setBizType(bizType());
		transition.setIsMainStateTransition("n");
		transition.setVersion("1");
	}
	
	
	
	public List<StateTransitionTuple> mapTuple(EventMessage event) {
		List<StateTransitionTuple> tuples = Lists.newArrayList();
		if (!isMatched(event.getAction().name(), event.getTableName())) {
			return tuples;
		}
		collectEventTuple(event, tuples,getStatePredicate());
		return tuples;
	}

	private void collectEventTuple(EventMessage event, List<StateTransitionTuple> tuples,BiPredicate<Map<String, Serializable>,Map<String, Serializable>> predicate) {
		if (event instanceof InsertEvent) {
			doCollectInsertEventTuple((InsertEvent)event, tuples,predicate);
		}
		if (event instanceof UpdateEvent) {
			doCollectUpdateEventTuple((UpdateEvent)event, tuples,predicate);
		}
	}

	
	public void doCollectUpdateEventTuple(EventMessage event, List<StateTransitionTuple> tuples,BiPredicate<Map<String, Serializable>,Map<String, Serializable>> predicate) {
		UpdateEvent updateEvent = (UpdateEvent) event;
		List<Map<String, Serializable>> modifiedRows = updateEvent.getModifyRowDataMaps();
		for (int index = 0; index < modifiedRows.size(); index++) {
			Map<String, Serializable> modifiedRow = modifiedRows.get(index);
			Map<String, Serializable> row = updateEvent.getRowDataMaps().get(index);
			if (predicate.test(row,modifiedRow)) {
				StateTransitionTuple tuple = new StateTransitionTuple(row, modifiedRow);
				tuples.add(tuple);
			}
		}
	}
	
	/**
	 * 
	 * @param event
	 * @param tuples
	 * @param concernKey
	 * @param concernValue
	 */
	public void doCollectInsertEventTuple(EventMessage event, List<StateTransitionTuple> tuples,BiPredicate<Map<String, Serializable>,Map<String, Serializable>> predicate) {
		InsertEvent insert = (InsertEvent) event;
		List<Map<String, Serializable>> rows = insert.getRowDataMaps();
		for (int index = 0; index < rows.size(); index++) {
			Map<String, Serializable> row = rows.get(index);
			if (predicate.test(row,null)) {
				StateTransitionTuple tuple = new StateTransitionTuple(row, null);
				tuples.add(tuple);
			}
		}
	}
	

	/**
	 * 设置子状态变更
	 * @param oldSubState
	 * @param newSubState
	 * @param transition
	 */
	public void setStateTransition(StateTransitionTuple tuple, CuntaoLifecycleTransition transition){
		this.getStateComsumer().accept(tuple, transition);
	};
	
	@Override
	public List<CuntaoLifecycleTransition> mapTransition(List<StateTransitionTuple> tuples) {
		List<CuntaoLifecycleTransition> transitions = Lists.newArrayList();
		for(StateTransitionTuple tuple : tuples){
			CuntaoLifecycleTransition transition = new CuntaoLifecycleTransition();
			Function<StateTransitionTuple,BaseTransitionInfo> provoider = getBaseTransitionInfoProvider();
			String modifier  = (String)tuple.getValue("modifier");
			DomainUtils.beforeInsert(transition, StringUtils.defaultIfEmpty(modifier, "system"));
			BaseTransitionInfo baseTransitionInfo = provoider.apply(tuple);
			if(baseTransitionInfo != null){
				this.setTransitionInfo(baseTransitionInfo, transition);
				this.setStateTransition(tuple,transition);
				this.calcSpendTime(tuple, transition);
				transitions.add(transition);
			}
		}
		return transitions;
	}
	
	
	protected Function<StateTransitionTuple, BaseTransitionInfo> providerByPartnerInstanceIdKey(String bizIdKey) {
		return (tuple) ->{
			BaseTransitionInfo baseInfo = new BaseTransitionInfo();
			Long partnerInstanceId = (Long)tuple.getValue(bizIdKey);
			PartnerInstanceDto partnerInstance = this.partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
			baseInfo.setBizPrimaryKey(partnerInstance.getId());
			baseInfo.setTaobaoUserId(partnerInstance.getTaobaoUserId());
			baseInfo.setStationId(partnerInstance.getStationId());
			baseInfo.setUserType(partnerInstance.getType().getCode());
			return baseInfo;
		};
	}

	protected Function<StateTransitionTuple, BaseTransitionInfo> providerByStationIdKey(String stationIdKey) {
		return (tuple) ->{
			BaseTransitionInfo baseInfo = new BaseTransitionInfo();
			Long stationId = (Long)tuple.getValue(stationIdKey);
			PartnerStationRel partnerInstance = this.partnerInstanceBO.findPartnerInstanceByStationId(stationId);
			baseInfo.setBizPrimaryKey(partnerInstance.getId());
			baseInfo.setTaobaoUserId(partnerInstance.getTaobaoUserId());
			baseInfo.setStationId(partnerInstance.getStationId());
			baseInfo.setUserType(partnerInstance.getType());
			return baseInfo;
		};
	}
	
	
	protected Function<StateTransitionTuple, BaseTransitionInfo> providerByTaobaoUserIdKey(String taobaoUserIdKey) {
		return (tuple) ->{
			BaseTransitionInfo baseInfo = new BaseTransitionInfo();
			Long taobaoUserId = (Long)tuple.getValue(taobaoUserIdKey);
			PartnerApplyDto partnerApply  = this.partnerApplyService.getPartnerApplyByTaobaoUserId(taobaoUserId);
			baseInfo.setBizPrimaryKey(partnerApply.getId());
			baseInfo.setTaobaoUserId(partnerApply.getTaobaoUserId());
			if(partnerApply.getPartnerType() !=null){
				baseInfo.setUserType(partnerApply.getPartnerType().toUpperCase());
			}
			return baseInfo;
		};
	}
	
	public void calcSpendTime(StateTransitionTuple tuple,CuntaoLifecycleTransition transition){
		//如果是插入说明是初始状态返回耗时0
		if(tuple.isInsert()){
			transition.setSpendTime(0l);
			return;
		}else if(tuple.isUpdate()){
			String oldState = transition.getOldState();
			if(StringUtils.isEmpty(oldState)){
				transition.setSpendTime(0l);
				return;
			}
			CuntaoLifecycleTransitionExample example = new CuntaoLifecycleTransitionExample();
			example.setOrderByClause("id desc");
			example.createCriteria().andIsDeletedEqualTo("n").andBizPrimaryKeyEqualTo(transition.getBizPrimaryKey()).andBizTypeEqualTo(bizType())
			.andNewStateEqualTo(oldState).andSubBizTypeEqualTo(transition.getSubBizType());
			List<CuntaoLifecycleTransition> lastTranstion = cuntaoLifecycleTransitionMapper.selectByExample(example);
			if(lastTranstion != null && !lastTranstion.isEmpty()){
				Date date = new Date();
				Long spendTime = date.getTime()-lastTranstion.iterator().next().getChangeTime().getTime();
				transition.setSpendTime(spendTime/1000);
				return;
			}
			transition.setSpendTime(0l);
		}
	}
	
}
