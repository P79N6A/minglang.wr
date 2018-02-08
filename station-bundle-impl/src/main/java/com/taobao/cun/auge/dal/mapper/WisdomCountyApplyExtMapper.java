package com.taobao.cun.auge.dal.mapper;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.dal.domain.WisdomCountyApply;
import com.taobao.cun.auge.dal.domain.WisdomCountyApplyExtExample;

/**
 * Created by xiao on 16/10/18.
 */
public interface WisdomCountyApplyExtMapper {

    Page<WisdomCountyApply> getPageWisdomCountyApply(WisdomCountyApplyExtExample wisdomCountyApplyExtExample);

}
