package com.taobao.cun.auge.fence.service;

import java.util.List;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.fence.bo.FenceEntityBO;
import com.taobao.cun.auge.fence.bo.FenceTemplateBO;
import com.taobao.cun.auge.fence.dto.FenceTemplateDetailDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateListDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateQueryCondition;
import com.taobao.cun.auge.fence.dto.FenceTemplateStation;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xiao on 18/6/15.
 */
@HSFProvider(serviceInterface = FenceTemplateService.class)
public class FenceTemplateServiceImpl implements FenceTemplateService {

    @Autowired
    private FenceTemplateBO fenceTemplateBO;

    @Autowired
    private FenceEntityBO fenceEntityBO;

    @Override
    public Long addFenceTemplate(FenceTemplateDetailDto detailDto) {
        return fenceTemplateBO.addFenceTemplate(detailDto);
    }

    @Override
    public void updateFenceTemplate(FenceTemplateDetailDto detailDto) {
        fenceTemplateBO.updateFenceTemplate(detailDto);
    }

    @Override
    public PageDto<FenceTemplateListDto> getFenceTemplateList(FenceTemplateQueryCondition condition) {
        return fenceTemplateBO.getFenceTemplateList(condition);
    }

    @Override
    public FenceTemplateDetailDto getTemplateDetail(Long id) {
        return fenceTemplateBO.getFenceTemplateById(id);
    }

    @Override
    public PageDto<FenceTemplateStation> getFenceTemplateStationList(FenceTemplateQueryCondition condition) {
        return fenceTemplateBO.getFenceTemplateStationList(condition);
    }

    @Override
    public void enableFenceTemplate(Long id, String operator) {
        fenceTemplateBO.enableFenceTemplate(id, operator);
        fenceEntityBO.enableEntityListByTemplateId(id, operator);
    }

    @Override
    public void disableFenceTemplate(Long id, String operator) {
        fenceTemplateBO.disableFenceTemplate(id, operator);
        fenceEntityBO.disableEntityListByTemplateId(id, operator);
    }

    @Override
    public List<FenceTemplateDto> getFenceTemplateListByStationId(Long stationId) {
        List<Long> templateIdList = fenceEntityBO.getTemplateIdListByStationId(stationId);
        return fenceTemplateBO.getFenceTemplateListByIdList(templateIdList);
    }

}
