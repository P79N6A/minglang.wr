package com.taobao.cun.auge.fence.bo;

import java.util.List;
import java.util.Map;

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

}
