package com.taobao.cun.auge.security;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import com.taobao.cun.auge.platform.service.KfcTextService;
import org.apache.commons.collections.CollectionUtils;
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
    private KfcTextService kfcTextService;
    
    public Optional<String> getProhibitedWord(String str) {
        List<String> words = null;
        try {
            words = kfcTextService.getProhibitedWords(str);
        } catch(Exception e){
            return Optional.empty();
        }

        if(CollectionUtils.isNotEmpty(words)) {
            return Optional.of(words.get(0));
        }else{
            return Optional.empty();
        }
    }

	public boolean hasProhibitedWord(String word) {
		return getProhibitedWord(word).isPresent();
	}
}
