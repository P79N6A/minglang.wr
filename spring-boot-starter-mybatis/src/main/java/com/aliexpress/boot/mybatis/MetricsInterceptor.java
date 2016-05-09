package com.aliexpress.boot.mybatis;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * mybatis metrics interceptor
 *
 * @author linux_china
 */
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}),
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class MetricsInterceptor implements Interceptor, PublicMetrics {
    public final Map<String, AtomicLong> sentences = new ConcurrentHashMap<String, AtomicLong>();
    public final Map<String, Date> longQueries = new ConcurrentHashMap<>();
    private MybatisProperties properties;

    public MetricsInterceptor(MybatisProperties properties) {
        this.properties = properties;
    }

    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        wrap((mappedStatement.getId())).incrementAndGet();
        long start = System.currentTimeMillis();
        Object proceed = invocation.proceed();
        long end = System.currentTimeMillis();
        if (properties.isLongQuery() && (end - start > properties.getLongQueryTime())) {
            longQueries.put(mappedStatement.getId(), new Date());
        }
        return proceed;
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {

    }

    private AtomicLong wrap(String metricName) {
        if (this.sentences.containsKey(metricName)) {
            return this.sentences.get(metricName);
        }
        AtomicLong atomic = new AtomicLong(0);
        this.sentences.put(metricName, atomic);
        return atomic;
    }

    public Map<String, AtomicLong> getSentences() {
        return sentences;
    }

    public Map<String, Date> getLongQueries() {
        return longQueries;
    }

    public Collection<Metric<?>> metrics() {
        return sentences.entrySet().stream().map(entry -> new Metric<>("mybatis." + entry.getKey(), entry.getValue().get())).collect(Collectors.toCollection(ArrayList::new));
    }
}
