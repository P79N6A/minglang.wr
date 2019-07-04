package com.taobao.cun.auge.cuncounty.tag;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyTagEnum;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 标签工具类
 */
public class CountyTagUtils {
    public static List<CuntaoCountyTagEnum> convert(String tags){
        if(Strings.isNullOrEmpty(tags)){
            return Lists.newArrayList();
        }

        return Splitter.on(",").omitEmptyStrings().splitToList(tags).stream().map(t->{
            CuntaoCountyTagEnum e = CuntaoCountyTagEnum.valueof(t);
            if(e == null){
                e = new CuntaoCountyTagEnum(t, t);
            }
            return e;
        }).collect(Collectors.toList());
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
