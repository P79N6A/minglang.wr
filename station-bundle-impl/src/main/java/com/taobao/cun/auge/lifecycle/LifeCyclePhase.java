package com.taobao.cun.auge.lifecycle;

/**
 * 生命周期阶段模板类，定义了每个生命周期内
 *  新增或修改村点
 *  新增或修改合伙人
 *  新增或修改合伙人关系
 *  新增或修改生命周期
 *  新增或修扩展业务
 *  发送状态变更事件
 *  同步老模型
 *
 */
public interface LifeCyclePhase extends LifeCyclePhaseComponent{

	/**
	 * 新增或修改村点
	 */
	void createOrUpdateStation(LifeCyclePhaseContext context);
	
	/**
	 *  新增或修改合伙人
	 */
	void createOrUpdatePartner(LifeCyclePhaseContext context);
	
	/**
	 * 新增或修改合伙人关系
	 */
	void createOrUpdatePartnerInstance(LifeCyclePhaseContext context);
	
	/**
	 * 新增或修改生命周期
	 */
	void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context);
	
	/**
	 * 新增或修扩展业务
	 */
	void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context);
	
	/**
	 * 发送状态变更事件
	 */
	void triggerStateChangeEvent(LifeCyclePhaseContext context);
	
	/**
	 * 同步历史模型，业务迁移完毕后下线
	 */
	void syncStationApply(LifeCyclePhaseContext context);

	
	/**
	 * 创建生命周期极业务流程
	 * @return
	 */
	default LifeCyclePhaseDSL createPhaseDSL(){
			LifeCyclePhaseDSL dsl = new LifeCyclePhaseDSL();
			 dsl.then(this::createOrUpdateStation);
			 dsl.then(this::createOrUpdatePartner);
			 dsl.then(this::createOrUpdatePartnerInstance);
			 dsl.then(this::createOrUpdateLifeCycleItems);
			 dsl.then(this::createOrUpdateExtensionBusiness);
			 dsl.then(this::triggerStateChangeEvent);
			 dsl.then(this::syncStationApply);
			 return dsl;
	}
	
	
}
