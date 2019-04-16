package com.taobao.cun.auge.fence.job.init;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.bo.FenceTemplateBO;
import com.taobao.cun.auge.fence.dto.FenceTemplateDto;
import com.taobao.cun.auge.fence.enums.FenceTypeEnum;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.service.CaiNiaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 站点初始化抽像实现
 *
 * @author chengyu.zhoucy
 */
public abstract class AbstractInitingStationFetcher implements InitingStationFetcher {
    @Resource
    protected StationBO stationBO;
    @Resource
    protected FenceInitTemplateConfig fenceInitTemplateConfig;
    @Resource
    protected FenceTemplateBO fenceTemplateBO;
    @Resource
    protected CaiNiaoService caiNiaoService;

    protected Logger logger = LoggerFactory.getLogger(AbstractInitingStationFetcher.class);

    @Override
    public List<InitingStation> getInitingStations() {
        List<InitingStation> initingStations = Lists.newArrayList();

        for (Long templateId : getTemplateIds()) {
            List<Station> fenceInitingStations = getFenceInitingStations(templateId).stream().filter(
                it -> matchCondition(templateId, it)).collect(
                Collectors.toList());
            initingStations.add(new InitingStation(fenceInitingStations, templateId));
        }

        return initingStations;
    }

    /**
     * 获取模板ID列表
     *
     * @return
     */
    protected abstract List<Long> getTemplateIds();

    /**
     * 查询站点
     *
     * @param templateId
     * @return
     */
    protected abstract List<Station> getFenceInitingStations(Long templateId);

    /**
     * 检查站点是否符合围栏开启附加条件
     *
     * @param templateId
     * @param station
     * @return
     */
    protected boolean matchCondition(Long templateId, Station station) {
        FenceTemplateDto fenceTemplate = fenceTemplateBO.getFenceTemplateById(templateId);
        if (fenceTemplate != null) {
            try {
                if (FenceTypeEnum.SELL.equals(fenceTemplate.getTypeEnum())) {
                    return matchSellCondition(station);
                } else if (FenceTypeEnum.CHARGE.equals(fenceTemplate.getTypeEnum())) {
                    return matchChargeCondition(station);
                } else if (FenceTypeEnum.LOGISTICS.equals(fenceTemplate.getTypeEnum())) {
                    return matchLogisticsCondition(station);
                }
            } catch (Exception e) {
                logger.error("station {stationId} init fence {templateId} error", station.getId(), templateId, e);
                return false;
            }
        }
        return true;
    }

    /**
     * 检查站点是否符合售卖围栏开启附加条件
     * @param station
     * @return
     */
    protected abstract boolean matchSellCondition(Station station);

    /**
     * 检查站点是否符合收费围栏开启附加条件
     * @param station
     * @return
     */
    protected abstract boolean matchChargeCondition(Station station);

    /**
     * 检查站点是否符合物流围栏开启附加条件
     * @param station
     * @return
     */
    protected abstract boolean matchLogisticsCondition(Station station);

}
