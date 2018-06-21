package com.taobao.cun.auge.fence.bo;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateDetailDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateListDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateQueryCondition;
import com.taobao.cun.auge.fence.dto.FenceTemplateStation;

/**
 * Created by xiao on 18/6/15.
 */
public interface FenceTemplateBO {

    Long addFenceTemplate(FenceTemplateDetailDto detailDto);

    void updateFenceTemplate(FenceTemplateDetailDto detailDto);

    PageDto<FenceTemplateListDto> getFenceTemplateList(FenceTemplateQueryCondition condition);

    FenceTemplateDetailDto getFenceTemplateById(Long id);

    void enableFenceTemplate(Long id, String operator);

    void disableFenceTemplate(Long id, String operator);

    PageDto<FenceTemplateStation> getFenceTemplateStationList(FenceTemplateQueryCondition condition);

}
