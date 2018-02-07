package com.taobao.cun.auge.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.common.lang.diagnostic.Profiler;

import com.taobao.kfc.core.match.Match;
import com.taobao.kfc.core.search.SearchResult;
import com.taobao.kfc.core.search.SearcherService;
import com.taobao.kfc.core.search.query.FuzzyIndexOf;
import com.taobao.kfc.core.search.query.Query;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author alibaba lengs
 *
 */
@Component
public class KFCServiceConfig {
    private final static Logger logger = Logger.getLogger(KFCServiceConfig.class);
    
//    @Autowired
//    private Searcher kfcSearchService;
    
    @Resource
    private SearcherService searcher;
    
    private static String firstApply = "xinxianquanpingbi";
    private static String secondApply = "pingbiciku";

    public Map<String, String> kfcCheck(String str) {
        Map<String, String> kfcErrorMap = null;
        try {
            Profiler.enter("kfcCheck:" + str);
            Query query = new FuzzyIndexOf();
            query.setFirsApply(firstApply);
            query.setContent(str);
            query.setSecondApply(new String[] { secondApply });
            List<String> words = null;
            try {
                SearchResult searchResult = searcher.search(query);
                logger.info("kfc.query.param:" + query+":result is:"+searchResult);
                if (null != searchResult && searchResult.getMatch() != null) {
                    words = new ArrayList<String>();
                    List<Match> matches = searchResult.getMatch();
                    for (Match match : matches) {
                        words.add(match.getKeyword());
                    }
                }
            } catch (Exception e) {
                // 捕获所有异常
                logger.error("check the str from KFC service error!!!str :" + str, e);
                return null;
            }
            if (null != words && words.size() > 0) {
                kfcErrorMap = new HashMap<String, String>();
                // 多个关键词列表只返回第1个
                kfcErrorMap.put("word", words.get(0).toString());
            }
        } finally {
            Profiler.release();
        }
        return kfcErrorMap;
    }

    public boolean isProhibitedWord(String key) {
        Map<String, String> tempMap = kfcCheck(key);
        if (tempMap == null || tempMap.isEmpty()){
            return false;
        }
        return true;
    }

    public boolean isProhibitedWord_byMulti(String... params) {
        if (params == null || params.length <= 0){
            return false;
        }
        for (String param : params) {
            Map<String, String> tempMap = kfcCheck(param);
            if (tempMap != null && !tempMap.isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
