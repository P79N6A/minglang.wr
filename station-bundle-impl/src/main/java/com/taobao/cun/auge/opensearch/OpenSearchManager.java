package com.taobao.cun.auge.opensearch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.common.lang.StringUtil;
import com.opensearch.javasdk.CloudsearchClient;
import com.opensearch.javasdk.CloudsearchSearch;
import com.opensearch.javasdk.object.KeyTypeEnum;

public class OpenSearchManager {
    private CloudsearchClient cloudsearchClient;
    private String accesskey;
    private String secret;
    private String host;
    private List<String> indexs;
    private OpenSearchParser parser;
    private OpenSearchEngine openSearchEngine;

    public void setCloudsearchClient(CloudsearchClient cloudsearchClient) {
        this.cloudsearchClient = cloudsearchClient;
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setIndexs(List<String> indexs) {
        this.indexs = indexs;
    }

    public void setParser(OpenSearchParser parser) {
        this.parser = parser;
    }

    public void setOpenSearchEngine(OpenSearchEngine openSearchEngine) {
        this.openSearchEngine = openSearchEngine;
    }

    public void init() {
        if(StringUtil.isEmpty(accesskey)||StringUtil.isEmpty(secret)||indexs==null||indexs.size()==0){
            throw new IllegalArgumentException("accesskey="+accesskey+",secret="+secret+"indexs="+indexs);
        }
        Map<String, Object> opts = new HashMap<String, Object>();
        if(!StringUtil.isEmpty(host)){
            opts.put("host", host);
        }
        cloudsearchClient = new CloudsearchClient(accesskey, secret, opts, KeyTypeEnum.ALIYUN);
        openSearchEngine= new OpenSearchEngine();
        openSearchEngine.setParser(parser);
    }

    public OpenSearchSearchResult  doQuery(CloudsearchSearch cloudsearchSearch){
       return openSearchEngine.doQuery(cloudsearchSearch);
    }

    public CloudsearchSearch getCloudsearchSearch(){
        CloudsearchSearch cloudsearchSearch = new  CloudsearchSearch(cloudsearchClient);
        cloudsearchSearch.addIndex(indexs);
        cloudsearchSearch.setFormat("fulljson");
        return cloudsearchSearch;
    }
}
