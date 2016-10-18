package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.WisdomCountyApply;
import com.taobao.cun.auge.dal.domain.WisdomCountyApplyExtExample;

import java.util.List;

/**
 * Created by xiao on 16/10/18.
 */
public interface WisdomCountyApplyExtMapper {

    List<WisdomCountyApply> getPageWisdomCountyApply(WisdomCountyApplyExtExample wisdomCountyApplyExtExample);

}
