package com.taobao.cun.auge.fence.bo;

import java.util.List;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateEditDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateListDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateQueryCondition;
import com.taobao.cun.auge.fence.dto.FenceTemplateStation;

/**
 * Created by xiao on 18/6/15.
 */
public interface FenceTemplateBO {

    Long addFenceTemplate(FenceTemplateEditDto detailDto);

    void updateFenceTemplate(FenceTemplateEditDto detailDto);

    PageDto<FenceTemplateListDto> getFenceTemplateList(FenceTemplateQueryCondition condition);

    FenceTemplateDto getFenceTemplateById(Long id);

    void enableFenceTemplate(Long id, String operator);

    void disableFenceTemplate(Long id, String operator);

    PageDto<FenceTemplateStation> getFenceTemplateStationList(FenceTemplateQueryCondition condition);

    List<FenceTemplateDto> getFenceTemplateListByIdList(List<Long> idList);

}
