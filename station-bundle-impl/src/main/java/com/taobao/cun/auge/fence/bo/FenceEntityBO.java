package com.taobao.cun.auge.fence.bo;

import java.util.List;

import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.fence.dto.FenceTemplateStation;

/**
 * Created by xiao on 18/6/20.
 */
public interface FenceEntityBO {

    void enableEntityListByTemplateId(Long templateId, String operator);

    void disableEntityListByTemplateId(Long templateId, String operator);

    /**
     * 获得模板应用的实体个数
     * @param templateId
     * @return
     */
    Integer getFenceEntityCountByTemplateId(Long templateId);

    /**
     * 获得站点关联的模板id列表
     */
    List<Long> getTemplateIdListByStationId(Long stationId);

    /**
     * 删除单个站点关联的电子围栏
     * @param fenceTemplateStation
     */
    void deleteFenceTemplateStation(FenceTemplateStation fenceTemplateStation);

    /**
     * 获取模板关联的围栏实例
     * @param templateId
     * @return
     */
    List<FenceEntity> getFenceEntityByTemplateId(Long templateId);
    
    /**
     * 添加
     * @param fenceEntity
     */
    void addFenceEntity(FenceEntity fenceEntity);

    /**
     * 更新
     * @param fenceEntity
     */
	void updateFenceEntity(FenceEntity fenceEntity);
}
