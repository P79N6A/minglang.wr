package com.taobao.cun.auge.fence.service;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.fence.bo.FenceTemplateBO;
import com.taobao.cun.auge.fence.dto.FenceTemplateListDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateQueryCondition;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xiao on 18/6/15.
 */
@HSFProvider(serviceInterface = FenceTemplateService.class)
    public class FenceTemplateServiceImpl implements FenceTemplateService {

    @Autowired
    private FenceTemplateBO fenceTemplateBO;

    @Override
    public PageDto<FenceTemplateListDto> getFenceTemplateList(FenceTemplateQueryCondition condition) {
        return fenceTemplateBO.getFenceTemplateList(condition);
    }

}
