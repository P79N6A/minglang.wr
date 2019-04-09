package com.taobao.cun.auge.lifecycle.common;

/**
 * 生命周期阶段模板类，定义了每个生命周期内
 */
public interface LifeCyclePhase extends LifeCyclePhaseComponent {

    /**
     * 新增或修改村点
     */
    default void createOrUpdateStation(LifeCyclePhaseContext context) {
    }



    /**
     * 新增或修改合伙人
     */
    default void createOrUpdatePartner(LifeCyclePhaseContext context) {
    }



    /**
     * 新增或修改合伙人关系
     */
    default void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {
    }



    /**
     * 新增或修改生命周期
     */
    default void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context) {
    }



    /**
     * 新增或修扩展业务
     */
    default void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
    }



    /**
     * 发送状态变更事件
     */
    default void triggerStateChangeEvent(LifeCyclePhaseContext context) {
    }



    /**
     * 默认的生命周期业务流程
     *
     * @return
     */
    default LifeCyclePhaseDSL createPhaseDSL() {
        LifeCyclePhaseDSL dsl = new LifeCyclePhaseDSL();
        dsl.then(this::createOrUpdateStation);
        dsl.then(this::createOrUpdatePartner);
        dsl.then(this::createOrUpdatePartnerInstance);
        dsl.then(this::createOrUpdateLifeCycleItems);
        dsl.then(this::createOrUpdateExtensionBusiness);
        dsl.then(this::triggerStateChangeEvent);
        return dsl;
    }


}
