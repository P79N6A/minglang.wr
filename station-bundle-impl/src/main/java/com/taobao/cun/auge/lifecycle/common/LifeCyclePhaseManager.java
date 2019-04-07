package com.taobao.cun.auge.lifecycle.common;

import com.google.common.collect.Maps;
import com.taobao.cun.auge.lifecycle.annotation.PhaseStepMeta;
import net.sf.cglib.proxy.*;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 生命周期管理容器
 * 各渠道类型每一个生命周期节点的注册管理和执行路由
 */
@Component
public class LifeCyclePhaseManager implements InitializingBean {

    Map<String, LifeCyclePhase> lifeCycleComponents = Maps.newConcurrentMap();

    @Autowired
    private List<LifeCyclePhase> lifeCyclePhases;

    public LifeCyclePhase getLifeCyclePhase(String componentIndentity) {
        Assert.notNull(lifeCycleComponents.get(componentIndentity), "componentIndentity can not be null");
        return lifeCycleComponents.get(componentIndentity);
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void execute(LifeCyclePhaseContext context) {
        LifeCyclePhase phase = getLifeCyclePhase(context.getPhaseKey().getKey());
        try {
            LifeCyclePhaseContextHolder.setContext(context);
            executeDSL(phase.createPhaseDSL());
        } finally {
            LifeCyclePhaseContextHolder.clean();
        }

    }

    private void executeDSL(LifeCyclePhaseDSL dsl) {
        dsl.invoke();
    }


    public void registerLifeCyclePhase(LifeCyclePhase phase) {
        collectRuntimMetaInfo(phase);
        this.lifeCycleComponents.putIfAbsent(phase.getPhaseKey().getKey(), phase);
    }

    @Override
    public void afterPropertiesSet() {
        if (this.lifeCyclePhases != null && !this.lifeCyclePhases.isEmpty()) {
            this.lifeCyclePhases.stream().forEach(this::registerLifeCyclePhase);
        }
    }

    /**
     * 增强模拟调用，用于收集元数据
     * @param phase
     */
    private void collectRuntimMetaInfo(LifeCyclePhase phase) {
        Enhancer enhancer = new Enhancer();
        CallbackFilter callbackFilter = new LifeCyclePhaseManager.TargetMethodCallbackFilter();
        Callback[] cbarray = new Callback[]{NoOp.INSTANCE, new LifeCyclePhaseManager.LifeCyclePhaseInterceptor()};
        enhancer.setCallbackFilter(callbackFilter);
        enhancer.setSuperclass(AopUtils.getTargetClass(phase));
        enhancer.setCallbacks(cbarray);
        LifeCyclePhase proxyPhase = (LifeCyclePhase) enhancer.create();
        LifeCyclePhaseContextHolder.setContext(new LifeCyclePhaseContext());
        proxyPhase.createPhaseDSL().invoke();
        LifeCyclePhaseContextHolder.clean();
    }


    static class LifeCyclePhaseInterceptor implements MethodInterceptor {
        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            RuntimeMetaInfoCollector.collectMetaInfo(method.getDeclaringClass(), method);
            return null;
        }

    }

    static class TargetMethodCallbackFilter implements CallbackFilter {

        @Override
        public int accept(Method method) {
            PhaseStepMeta meta = AnnotationUtils.getAnnotation(method, PhaseStepMeta.class);
            if (meta == null) {
                return 0;
            } else {
                return 1;
            }
        }

    }
}
