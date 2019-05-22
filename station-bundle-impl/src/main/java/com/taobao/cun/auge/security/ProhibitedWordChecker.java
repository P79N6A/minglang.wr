package com.taobao.cun.auge.security;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.common.lang.diagnostic.Profiler;
import com.taobao.kfc.client.lite.bean.KeywordMatchResult;
import com.taobao.kfc.client.lite.bean.KeywordMatchResult.MatchItem;
import com.taobao.kfc.client.lite.service.LiteMergeSearchService;

/**
 * 违禁词检查
 * @author chengyu.zhoucy
 *
 */
@Component
public class ProhibitedWordChecker {
	@Resource
    private LiteMergeSearchService liteMergeSearchService;
    
    private static String firstApply = "xinxianquanpingbi";
    private static String secondApply = "pingbiciku";

    public Optional<String> getProhibitedWord(String str) {
        try {
            Profiler.enter("kfcCheck:" + str);
            KeywordMatchResult result = liteMergeSearchService.search(str, firstApply, new String[] {secondApply});
            if(result.isMatch()) {
	            List<MatchItem> matchItems = result.getMatchedKeywords();
	            if (matchItems.size() > 0) {
	            	return Optional.of(matchItems.get(0).getKeyword());
	            }
            }
        } finally {
            Profiler.release();
        }
        return Optional.empty();
    }

	public boolean isProhibitedWord(String word) {
		return getProhibitedWord(word).isPresent();
	}
}
