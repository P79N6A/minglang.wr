package com.taobao.cun.auge.opensearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensearch.javasdk.CloudsearchSearch;

public class OpenSearchEngine {
    private static final Logger logger  = LoggerFactory.getLogger(OpenSearchEngine.class);
    private OpenSearchParser parser;

    public void setParser(OpenSearchParser parser) {
        this.parser = parser;
    }

    public OpenSearchSearchResult  doQuery(CloudsearchSearch cloudsearchSearch)  {
        try {
            return parser.parse(cloudsearchSearch.search());
        } catch (Exception e) {
        	logger.error("OpenSearchEngine-doQuery-exception:{}", cloudsearchSearch.getQuery(), e);
        	return null;
        }

    }

}
