package com.taobao.cun.auge.fence.convert;

import java.util.Collections;
import java.util.List;

import com.taobao.cun.auge.fence.dto.FenceBatchOpDto;
import com.taobao.cun.auge.fence.dto.job.ConditionCreateFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.ConditionDeleteFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.FenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.StationDeleteFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.TemplateCloseFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.TemplateOpenFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.TemplateUpdateFenceInstanceJob;

/**
 * Created by xiao on 18/7/3.
 */
public class JobConvertor {

    public final static String TEMPLATE_UPDATE = "TEMPLATE_UPDATE";

    public final static String TEMPLATE_OPEN = "TEMPLATE_OPEN";

    public final static String TEMPLATE_CLOSE = "TEMPLATE_CLOSE";

    public final static String STATION_DELETE = "STATION_DELETE";

    public static FenceInstanceJob convertToFenceTemplateJob (Long templateId, String type, String creator) {
        if (TEMPLATE_UPDATE.equals(type)) {
            TemplateUpdateFenceInstanceJob updateJob = new TemplateUpdateFenceInstanceJob();
            updateJob.setTemplateId(templateId);
            updateJob.setCreator(creator);
            return updateJob;
        } else if (TEMPLATE_OPEN.equals(type)) {
            TemplateOpenFenceInstanceJob openJob = new TemplateOpenFenceInstanceJob();
            openJob.setTemplateIds(Collections.singletonList(templateId));
            openJob.setCreator(creator);
            return openJob;
        } else if (TEMPLATE_CLOSE.equals(type)) {
            TemplateCloseFenceInstanceJob closeJob = new TemplateCloseFenceInstanceJob();
            closeJob.setTemplateIds(Collections.singletonList(templateId));
            closeJob.setCreator(creator);
            return closeJob;
        }
        return null;
    }

    public static FenceInstanceJob convertToFenceStationJob (Long templateId, Long stationId, String type, String creator) {
        if (STATION_DELETE.equals(type)) {
            StationDeleteFenceInstanceJob job = new StationDeleteFenceInstanceJob();
            job.setStationId(stationId);
            job.setTemplateIds(Collections.singletonList(templateId));
            job.setCreator(creator);
            return job;
        }
        return null;
    }

    public static FenceInstanceJob convertToConditionFenceInstanceJob(List<Long> templateIds, String condition, String opType, String creator) {
        if (FenceBatchOpDto.BATCH_OVERRIDE.equals(opType)) {
            ConditionCreateFenceInstanceJob createJob = new ConditionCreateFenceInstanceJob();
            createJob.setTemplateIds(templateIds);
            createJob.setCondition(condition);
            createJob.setCreateRule(FenceInstanceJob.CREATE_RULE_OVERRIDE);
            createJob.setCreator(creator);
            return createJob;
        } else if (FenceBatchOpDto.BATCH_NEW.equals(opType)) {
            ConditionCreateFenceInstanceJob createJob = new ConditionCreateFenceInstanceJob();
            createJob.setTemplateIds(templateIds);
            createJob.setCondition(condition);
            createJob.setCreateRule(FenceInstanceJob.CREATE_RULE_NEW);
            createJob.setCreator(creator);
            return createJob;
        } else if (FenceBatchOpDto.BATCH_DELETE.equals(opType)) {
            ConditionDeleteFenceInstanceJob deleteJob = new ConditionDeleteFenceInstanceJob();
            deleteJob.setTemplateIds(templateIds);
            deleteJob.setCondition(condition);
            deleteJob.setCreator(creator);
            return deleteJob;
        }
        return null;
    }

}
