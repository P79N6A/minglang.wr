package com.taobao.cun.auge.fence.service;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateListDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateQueryCondition;

/**
 * Created by xiao on 18/6/15.
 */
public interface FenceTemplateService {

    PageDto<FenceTemplateListDto> getFenceTemplateList(FenceTemplateQueryCondition condition);

}
