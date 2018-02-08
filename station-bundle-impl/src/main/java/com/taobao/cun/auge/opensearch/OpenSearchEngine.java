package com.taobao.cun.auge.opensearch;

import com.opensearch.javasdk.CloudsearchSearch;
import org.apache.log4j.Logger;

public class OpenSearchEngine {
    private static final Logger log                          = Logger.getLogger(OpenSearchEngine.class);
    private OpenSearchParser parser;

    public void setParser(OpenSearchParser parser) {
        this.parser = parser;
    }

    public OpenSearchSearchResult  doQuery(CloudsearchSearch cloudsearchSearch)  {
        try {
            return parser.parse(cloudsearchSearch.search());
        } catch (Exception e) {
            log.error("OpenSearchEngine-doQuery-exception", e);
            throw new RuntimeException(e);
        }

    }

}
