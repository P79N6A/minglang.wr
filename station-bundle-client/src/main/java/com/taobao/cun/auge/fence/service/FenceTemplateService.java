package com.taobao.cun.auge.fence.service;

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
public interface FenceTemplateService {

    /**
     * 新增一个电子围栏模板
     * @param detailDto
     * @return
     */
    Long addFenceTemplate(FenceTemplateEditDto detailDto);

    /**
     * 更新模板
     * @param detailDto
     */
    void updateFenceTemplate(FenceTemplateEditDto detailDto);

    /**
     * 查询列表
     * @param condition
     * @return
     */
    PageDto<FenceTemplateListDto> getFenceTemplateList(FenceTemplateQueryCondition condition);

    /**
     * 查询详情
     * @param id
     * @return
     */
    FenceTemplateDto getTemplateDetail(Long id);

    /**
     * 查询模板应用的村点列表
     * @param condition
     * @return
     */
    PageDto<FenceTemplateStation> getFenceTemplateStationList(FenceTemplateQueryCondition condition);

    /**
     * 启用模板
     * @param id
     * @param operator
     */
    void enableFenceTemplate(Long id, String operator);

    /**
     * 禁用模板
     * @param id
     * @param operator
     */
    void disableFenceTemplate(Long id, String operator);

    /**
     * 获得站点关联的电子围栏模板列表
     * @param stationId
     * @return
     */
    List<FenceTemplateDto> getFenceTemplateListByStationId(Long stationId);

    /**
     * 删除单个站点关联的电子围栏
     * @param fenceTemplateStation
     */
    void deleteFenceTemplateStation(FenceTemplateStation fenceTemplateStation);

}
