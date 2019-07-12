package com.taobao.cun.auge.cuncounty.tag;

import com.alibaba.fastjson.JSONArray;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyTagEnum;

import java.util.List;
import java.util.Optional;

/**
 * 标签工具类
 */
public class CountyTagUtils {
    public static List<CuntaoCountyTagEnum> convert(String tags){
        return JSONArray.parseArray(tags, CuntaoCountyTagEnum.class);
    }

    public static boolean containAlarmTags(List<CuntaoCountyTagEnum> tags){
        return containProtocolTags(tags);
    }

    public static boolean containProtocolTags(List<CuntaoCountyTagEnum> tags){
        return tags != null && tags.stream().filter(CountyTagUtils::isProtocolTag).count() > 0;
    }

    public static boolean isProtocolTag(CuntaoCountyTagEnum tag){
        return tag != null && (tag.getCode().equals(CuntaoCountyTagEnum.protocolNotExists.getCode()) ||
                tag.getCode().equals(CuntaoCountyTagEnum.protocolMaybeNotExists.getCode()) ||
                tag.getCode().equals(CuntaoCountyTagEnum.protocolWillExpire.getCode()));
    }

    public static Optional<CuntaoCountyTagEnum> getProtocolTag(List<CuntaoCountyTagEnum> tags){
        if(tags != null){
            return tags.stream().filter(CountyTagUtils::isProtocolTag).findFirst();
        }else{
            return Optional.empty();
        }
    }
}
