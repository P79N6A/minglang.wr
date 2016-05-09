package com.aliexpress.boot.mybatis;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Mybatis
 *
 * @author leijuan
 */
@ConfigurationProperties(prefix = "spring.mybatis")
public class MybatisProperties {

    /**
     * Config file path.
     */
    private String config;
    /**
     * long query record
     */
    private boolean longQuery = true;
    /**
     * long query time: default is 3000 Millis
     */
    private long longQueryTime = 3000;
    /**
     * scan base package
     */
    private String scanBasePackage;
    
    private String mapperLocations;
    
    private String typeAliasesPackage;

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public boolean isLongQuery() {
        return longQuery;
    }

    public void setLongQuery(boolean longQuery) {
        this.longQuery = longQuery;
    }

    public long getLongQueryTime() {
        return longQueryTime;
    }

    public void setLongQueryTime(long longQueryTime) {
        this.longQueryTime = longQueryTime;
    }

    public String getScanBasePackage() {
        return scanBasePackage;
    }

    public void setScanBasePackage(String scanBasePackage) {
        this.scanBasePackage = scanBasePackage;
    }

	public String getMapperLocations() {
		return mapperLocations;
	}

	public void setMapperLocations(String mapperLocations) {
		this.mapperLocations = mapperLocations;
	}

	public String getTypeAliasesPackage() {
		return typeAliasesPackage;
	}

	public void setTypeAliasesPackage(String typeAliasesPackage) {
		this.typeAliasesPackage = typeAliasesPackage;
	}
}
