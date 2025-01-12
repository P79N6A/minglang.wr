package com.taobao.cun.auge.lifecycle.common;

import static io.advantageous.reakt.promise.Promises.invokablePromise;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import io.advantageous.reakt.promise.Promise;
import io.advantageous.reakt.promise.Promises;


public class LifeCyclePhaseDSL {

    private List<Consumer<LifeCyclePhaseContext>> consumers= Lists.newArrayList();

    Promise<LifeCyclePhaseContext> createPromise(LifeCyclePhaseContext context) {
        return invokablePromise(promise -> {
            if (context == null) {
                promise.reject("LifeCyclePhaseContext was null");
            } else {
                promise.resolve(context);
            }
        });
    }

    public LifeCyclePhaseDSL then(Consumer<LifeCyclePhaseContext> consumer){
        consumers.add(consumer);
        return this;
    }

    public void invoke(){
        List<Promise<LifeCyclePhaseContext>> promises = consumers.stream().map(consumer -> createPromise(LifeCyclePhaseContextHolder.getContext()).then(consumer).asPromise())
                .collect(Collectors.toList());
        Promises.all(promises).invoke();
    }
}
