package com.aliexpress.boot.mybatis;

import com.aliexpress.boot.endpoints.AbstractAliExpressEndpoint;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Mybatis endpoint
 *
 * @author leijuan
 */
public class MybatisEndpoint extends AbstractAliExpressEndpoint {
    private SqlSessionFactory sqlSessionFactory;
    private MetricsInterceptor interceptor;
    private MybatisProperties properties;
    private List<Map<String, Object>> statements = null;

    public MybatisEndpoint(MybatisProperties properties, SqlSessionFactory sqlSessionFactory, MetricsInterceptor interceptor) {
        super("mybatis");
        this.properties = properties;
        this.sqlSessionFactory = sqlSessionFactory;
        this.interceptor = interceptor;
    }

    public String getName() {
        return "Mybatis";
    }

    public String getVersion() {
        return "1.0.0";
    }

    public List<String> getAuthors() {
        return Collections.singletonList("leijuan <jacky.chenlb@alibaba-inc.com>");
    }

    public String getDocs() {
        return "http://blog.mybatis.org/p/products.html";
    }

    public String getScm() {
        return "http://gitlab.alibaba-inc.com/spring-boot/spring-boot-starter-mybatis";
    }

    @Override
    public Optional<Object> getConfig() {
        return Optional.of(properties);
    }

    @Override
    public Optional<Map<String, Object>> getMetrics() {
        if (interceptor.getSentences().isEmpty()) {
            return Optional.empty();
        }
        Map<String, Object> metrics = new HashMap<>();
        interceptor.getSentences().forEach((key, atomicLong) -> metrics.put(key, atomicLong.get()));
        return Optional.of(metrics);
    }

    @Override
    public Optional<Map<String, Object>> getRuntime() {
        Map<String, Object> runtime = new HashMap<>();
        //initStatements();
        runtime.put("statements", statements);
        if (!interceptor.getLongQueries().isEmpty()) {
            runtime.put("longQueries", interceptor.getLongQueries());
        }
        return Optional.of(runtime);
    }

    @PostConstruct
    public void initStatements() {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        List<MappedStatement> mappedStatements = new ArrayList<>();
        Object[] objects = configuration.getMappedStatements().toArray();
        for (Object object : objects) {
            if (object instanceof MappedStatement) {
                mappedStatements.add((MappedStatement) object);
            }
        }
        this.statements = new ArrayList<>();
        Set<String> statementIds = new HashSet<>();
        for (MappedStatement mappedStatement : mappedStatements) {
            String statementId = mappedStatement.getId();
            if (!statementIds.contains(statementId) && !statementId.contains("!")) {
                Map<String, Object> statement = new HashMap<>();
                statement.put("id", statementId);
                ParameterMap parameterMap = mappedStatement.getParameterMap();
                if (parameterMap != null && parameterMap.getType() != null) {
                    statement.put("parameterMap", parameterMap.getType().getSimpleName());
                }
                List<ResultMap> resultMaps = mappedStatement.getResultMaps();
                if (!resultMaps.isEmpty()) {
                    List<String> resultMapNames = new ArrayList<>();
                    resultMaps.stream().filter(t -> !t.getType().getSimpleName().equalsIgnoreCase("void")).forEach(t -> resultMapNames.add(t.getType().getSimpleName()));
                    if (!resultMapNames.isEmpty()) {
                        statement.put("resultMaps", resultMapNames);
                    }
                }
                String sqlCommandType = mappedStatement.getSqlCommandType().toString();
                if (sqlCommandType != null) {
                    statement.put("commandType", sqlCommandType);
                    if (sqlCommandType.equalsIgnoreCase("INSERT")) {
                        KeyGenerator keygenerator = mappedStatement.getKeyGenerator();
                        if (!(keygenerator instanceof NoKeyGenerator)) {
                            statement.put("keyGenerator", keygenerator.getClass().getSimpleName());
                        }
                    }
                }
                try {
                    statement.put("sql", mappedStatement.getBoundSql(new HashMap<>()).getSql());
                } catch (Exception ignore) {
                }
                if (mappedStatement.getSqlSource() instanceof DynamicSqlSource) {
                    statement.put("dynamic", true);
                }
                statements.add(statement);
                statementIds.add(statementId);
            }
        }
    }
}
